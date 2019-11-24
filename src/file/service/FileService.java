package file.service;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import file.operation.CreateFile;
import file.util.FileUtils;
import org.jetbrains.annotations.NotNull;

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

    /**
     *
     * @param name
     * @return
     */
    private CatalogEntry getOpenCatalogEntry(String name) {
        ArrayList<Catalog> readingCatalog = getFullCatalog();
        CatalogEntry targetEntry = null;
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (entry.getName().equals(name)) {
                    targetEntry = entry;
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
    public boolean createFile(@NotNull String name, String expandedName, String attribute, int size) {
        ArrayList<Catalog> readingCatalog = getFullCatalog();
        CreateFile createFile = new CreateFile();
        while (name.length() < 3) {
            name += (char) 0;
        }
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (entry.getName().equals(name)) {
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
    public void saveFile(String fileName, String[] fileContext) {
        ArrayList<Catalog> readingCatalog = getFullCatalog();
        CreateFile createFile = new CreateFile();
        CatalogEntry targetEntry = null;
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (entry.getName().equals(fileName)) {
                    targetEntry = entry;
                    break;
                }
            }
        }
        if (targetEntry == null) {
            return;
        } else {
            targetEntry.setSize(fileContext.length);
            int startIndex = targetEntry.getStartedBlockIndex();
            int nextIndex = getEmptyBlockIndex();
        }
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
