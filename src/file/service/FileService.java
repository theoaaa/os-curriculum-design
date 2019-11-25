package file.service;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import file.operation.CreateFile;
import file.operation.DeleteFile;
import file.operation.OpenFile;
import file.util.FileUtils;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/11 12:37
 */
public class FileService {
    private DiskService diskService;
    private static FileService fileService = new FileService();
    private DiskBlock[] FATBlocks;
    private CatalogContainer tables;
    private FileUtils fileUtils = FileUtils.getInstance();
    private FileService(){
        diskService = DiskService.getInstance();
        Catalog rootCatalog = new Catalog(diskService.getDiskBlock(2));
        FATBlocks =  diskService.getFATBlocks();
        tables = CatalogContainer.getInstance(rootCatalog);
    }

    /**
     *
     */
    public void backward(){
        tables.backward();
    }

    /**
     *
     */
    public void forward(){
        tables.forward();
    }

    public String[] openFile(String fileName,String expandedName){
        CatalogEntry targetEntry =  fileUtils.getTargetEntry(fileName,expandedName,tables,FATBlocks);
        OpenFile openFile = new OpenFile();
        return openFile.openFile(FATBlocks,targetEntry);
    }
    /**
     *
     * @param fileName
     * @param expandedName
     * @param attribute
     * @param size
     * @return
     */
    public boolean createFile(String fileName, String expandedName, String attribute, int size) {
        if(tables.getTop().isFull()&&tables.getPresentIndex()!=1){
            fileUtils.modifyFAT(tables.getTop().getIndex(),fileUtils.getEmptyBlockIndex(diskService),FATBlocks);
        }
        CreateFile createFile = new CreateFile();
        boolean statement = false;
        CatalogEntry targetEntry = fileUtils.getTargetEntry(fileName,expandedName,tables,FATBlocks);
        if(targetEntry.isEmpty()){
            targetEntry.setContext(createFile.getEntryContext(fileName, expandedName, attribute, fileUtils.getEmptyBlockIndex(diskService), size));
            statement = true;
        }
        return statement;
    }

    /**
     *
     * @param fileName
     * @param fileContext
     */
    public void saveFile(String fileName, String expandedName,String[] fileContext) {
        CreateFile createFile = new CreateFile();
        CatalogEntry targetEntry = fileUtils.getTargetEntry(fileName,expandedName,tables,FATBlocks);
        if (!targetEntry.isEmpty()) {
            DiskBlock targetDiskBlock = diskService.getDiskBlock(targetEntry.getStartedBlockIndex());
            if(fileContext.length<=128){
                createFile.setFileContext(fileContext,targetDiskBlock);
                int nextIndex = fileUtils.getFileFatResult(FATBlocks,targetDiskBlock);
                if(nextIndex!=1){
                    while ((nextIndex = fileUtils.getFileFatResult(FATBlocks,targetDiskBlock))!=1){
                        fileUtils.cleanBlock(targetDiskBlock);
                        targetDiskBlock.setOccupy(false);
                        targetDiskBlock=diskService.getDiskBlock(nextIndex);
                    }
                    fileUtils.cleanBlock(targetDiskBlock);
                }
                fileUtils.modifyFAT(targetDiskBlock.getIndex(),1,FATBlocks);
            }else{
                for(int i = 0;i*128<fileContext.length;i++) {
                    boolean longer = false;
                    int contextLength;
                    if((i+1)*128>fileContext.length){
                        contextLength = 128;
                        longer = true;
                    }else{
                        contextLength = (i+1)*128-fileContext.length;
                    }
                    int nextBlockIndex;
                    if(fileUtils.getFileFatResult(FATBlocks,targetDiskBlock)!=0) {
                        nextBlockIndex = longer ? fileUtils.getFileFatResult(FATBlocks,targetDiskBlock):1;
                    }else{
                        nextBlockIndex = longer ? fileUtils.getEmptyBlockIndex(diskService) : 1;//新创建文件且length>128，后续的盘fatResult = 0
                    }
                    String[] subStr = fileUtils.getSubContext(fileContext,i*128,i*128+contextLength);
                    createFile.setFileContext(subStr,targetDiskBlock);
                    targetDiskBlock.setOccupy(true);
                    fileUtils.modifyFAT(targetDiskBlock.getIndex(),nextBlockIndex,FATBlocks);
                    targetDiskBlock = diskService.getDiskBlock(nextBlockIndex);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public static FileService getInstance(){
        if(fileService==null){
            fileService = new FileService();
        }
        return fileService;
    }
}
