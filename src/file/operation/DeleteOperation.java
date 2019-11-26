package file.operation;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/11 12:36
 */
public class DeleteOperation extends AbstractOperation{
    public boolean deleteFile(CatalogEntry targetEntry, ArrayList<Catalog> targetCatalog, DiskBlock[] FATBlocks){
        int startIndex = targetEntry.getStartedBlockIndex();
        boolean statement = modifyCatalog(targetEntry,targetCatalog);
        if(!targetEntry.isEmpty()){
            DiskService diskService = DiskService.getInstance();
            if(statement) {
                fileUtils.modifyNextBlock(FATBlocks, diskService.getDiskBlock(startIndex));
            }
        }
        return statement;
    }

    public boolean deleteDirectory(CatalogEntry targetEntry, ArrayList<Catalog> targetCatalog, DiskBlock[] FATBlocks){
        DiskService diskService = DiskService.getInstance();
        Catalog dir = new Catalog(diskService.getDiskBlock(targetEntry.getStartedBlockIndex()));//文件夹目录，即文件夹自己的内容
        ArrayList<Catalog> dirCatalog = fileUtils.getFullCatalog(dir,FATBlocks);//获取完整的目录，也就是文件夹
        for(Catalog catalog:dirCatalog){
            for(CatalogEntry entry:catalog.getEntries()){
                if(entry.getExpandedName().equals("D")){
                    Catalog tmp = new Catalog(diskService.getDiskBlock(entry.getStartedBlockIndex()));
                    ArrayList<Catalog> tmpCatalog = fileUtils.getFullCatalog(tmp,FATBlocks);
                    deleteDirectory(entry,tmpCatalog,FATBlocks);
                }else{
                    fileUtils.modifyNextBlock(FATBlocks,diskService.getDiskBlock(entry.getStartedBlockIndex()));//改变FAT
                }
            }
        }
        fileUtils.modifyNextBlock(FATBlocks,diskService.getDiskBlock(targetEntry.getStartedBlockIndex()));
        return modifyCatalog(targetEntry,targetCatalog);
    }

    public boolean modifyCatalog(CatalogEntry targetEntry,ArrayList<Catalog> targetCatalog){
        int targetIndex = -1;
        int endIndex = -1;
        boolean endFlag = false;
        for (int i = 0; i < targetCatalog.size(); i++) {
            for (int j = 0; j < targetCatalog.get(i).getEntries().length; j++) {
                String tmpName = targetCatalog.get(i).getEntries()[j].getName();
                String tmpExpandedName = targetCatalog.get(i).getEntries()[j].getExpandedName();
                if(fileUtils.formatName(tmpName,tmpExpandedName).equals(fileUtils.formatName(targetEntry.getName(),targetEntry.getExpandedName()))){
                    targetIndex = i*targetCatalog.get(i).getEntries().length+j;
                }
                if(targetIndex!=-1) {
                    if (targetCatalog.get(i).getEntries()[j].isEmpty() || ((i == targetCatalog.size() - 1) && (j == targetCatalog.get(i).getEntries().length - 1))) {
                        endIndex = i * targetCatalog.get(i).getEntries().length + j - 1;
                        endFlag = true;
                        break;
                    }
                }
            }
            if(endFlag){
                break;
            }
        }
        if(endFlag) {
            DiskByte[] lastContext = targetCatalog.get(endIndex / 16).getEntries()[endIndex % 16].getContext();
            targetCatalog.get(targetIndex / 16).getEntries()[targetIndex % 16].setContext(lastContext);
            targetCatalog.get(endIndex / 16).getEntries()[endIndex % 16].setEmpty();
        }
        return endFlag;
    }
}
