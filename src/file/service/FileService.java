package file.service;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import file.operation.CreateFile;
import file.util.FileUtils;

/**
 * @author Rorke
 * @date 2019/11/11 12:37
 */
public class FileService {
    private DiskService diskService;
    private static FileService fileService = new FileService();
    private DiskBlock[] FATBlocks;
    private Catalog rootCatalog;
    private CatalogContainer tables;
    private FileUtils fileUtils = FileUtils.getInstance();
    private FileService(){
        diskService = DiskService.getInstance();
        rootCatalog = (Catalog) diskService.getDiskBlock(2);
        FATBlocks =  diskService.getFATBlocks();
        tables = CatalogContainer.getInstance(rootCatalog);
    }

    public void backward(){
        tables.backward();
    }

    public void forward(){
        tables.forward();
    }

    public void openFile(){

    }

    public void createFile(String name, String expandedName, String attribute, int startedIndex, int size,String[] fileContext){
        CreateFile createFile = new CreateFile(name, expandedName, attribute, startedIndex, size);
        String[] context = createFile.getEntryContext();
        Catalog catalog = tables.getTop();
        for(CatalogEntry tmp:catalog.getEntries()) {
            if (tmp.isEmpty()) {
                tmp.setContext(context);
                break;
            }
        }
        int emptyIndex = 0;
        if(fileContext.length>128){
            emptyIndex = getEmptyBlockIndex();
            modifyFAT(startedIndex,emptyIndex);
        }
    }

    private void modifyFAT(int blockIndex,int nextIndex){
        FATBlocks[blockIndex/128].getBytes()[blockIndex%128].setDiskByte(fileUtils.decToBinary(nextIndex,8));
    }

    public int getEmptyBlockIndex(){
        int index = 3;
        for(;index<256;index++){
            if(diskService.getDiskBlock(index).getBlockStatus()==0){
                break;
            }
        }
        return index;
    }

    public static FileService getInstance(){
        if(fileService==null){
            fileService = new FileService();
        }
        return fileService;
    }
}
