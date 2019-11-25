package file.service;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import file.operation.CreateFile;
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

    private CatalogEntry getTargetEntry(String fileName,String expandedName) {
        ArrayList<Catalog> readingCatalog = getFullCatalog();
        CatalogEntry targetEntry = null;
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (formatName(entry.getName(),String.valueOf(entry.getExpandedName())).equals(formatName(fileName,expandedName))) {
                    targetEntry = entry;
                    break;
                }
            }
        }
        return targetEntry;
    }

    /**
     *
     * @param name
     * @param expandedName
     * @param attribute
     * @param size
     * @return
     */
    public boolean createFile(String name, String expandedName, String attribute, int size) {
        if(tables.getTop().isFull()&&tables.getPresentIndex()!=1){
            modifyFAT(tables.getTop().getIndex(),getEmptyBlockIndex());
        }
        ArrayList<Catalog> readingCatalog = getFullCatalog();
        CreateFile createFile = new CreateFile();
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (formatName(entry.getName(),String.valueOf(entry.getExpandedName())).equals(formatName(name,expandedName))) {
                    return false;
                }
                if (entry.isEmpty()) {
                    entry.setContext(createFile.getEntryContext(name, expandedName, attribute, getEmptyBlockIndex(), size));
                    break;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param fileName
     * @param fileContext
     */
    public void saveFile(String fileName, String expandedName,String[] fileContext) {
        CreateFile createFile = new CreateFile();
        CatalogEntry targetEntry = getTargetEntry(fileName,expandedName);
        if (targetEntry == null) {
            return;
        } else {
            DiskBlock targetDiskBlock = diskService.getDiskBlock(targetEntry.getStartedBlockIndex());
            if(fileContext.length<=128){
                createFile.setFileContext(fileContext,targetDiskBlock);
                modifyFAT(targetDiskBlock.getIndex(),1);
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
                    int nextBlockIndex = longer?getEmptyBlockIndex():1;
                    String[] subStr = fileUtils.getSubContext(fileContext,i*128,i*128+contextLength);
                    createFile.setFileContext(subStr,targetDiskBlock);
                    modifyFAT(targetDiskBlock.getIndex(),nextBlockIndex);
                    targetDiskBlock = diskService.getDiskBlock(nextBlockIndex);
                }
            }
        }
    }

    /**
     *
     * @param name
     * @param expandedName
     * @return
     */
    private String formatName(String name, String expandedName) {
        while (name.length()<3){
            name += (char) 0;
        }
        name = name+'.'+expandedName;
        return name;
    }
    /**
     * @return
     */
    private ArrayList<Catalog> getFullCatalog() {
        int nextFat;
        Catalog tmp;
        int presentIndex = tables.getTop().getIndex();
        ArrayList<Catalog> readingCatalog = new ArrayList<>();
        while ((nextFat = fileUtils.getNextBlockIndex(FATBlocks, tables.getTop())) != 1) {
            tmp = new Catalog(diskService.getDiskBlock(presentIndex));
            readingCatalog.add(tmp);
            presentIndex = nextFat;
        }
        tmp = new Catalog(diskService.getDiskBlock(presentIndex));
        readingCatalog.add(tmp);
        return readingCatalog;
    }

    /**
     * @param blockIndex
     * @param nextIndex
     */
    private void modifyFAT(int blockIndex, int nextIndex){
        FATBlocks[blockIndex/128].getBytes()[blockIndex%128].setDiskByte(fileUtils.decToBinary(nextIndex,8));
    }

    /**
     *
     * @return
     */
    private int getEmptyBlockIndex() {
        int index = 0;
        for (int i = 0; i < 256; i++) {
            if (!diskService.getDiskBlock(i).isOccupy()) {
                index = i;
                break;
            }
        }
        diskService.getDiskBlock(index).setOccupy(true);
        return index;
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
