package file.util;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import instruction.util.InstrUtils;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/20 20:50
 */
public class FileUtils extends InstrUtils {
    private int highestIndex;
    public static FileUtils fileUtils = new FileUtils();
    private FileUtils(){}
    public static FileUtils getInstance(){
        if(fileUtils==null){
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }
    public int binaryToDec(String binaryString){
        highestIndex = binaryString.length()-1;
        int index = highestIndex;
        int result = 0;
        for(;index>=0;index--){
            result += Math.pow(2,(highestIndex-index))*(binaryString.charAt(index)-'0');
        }
        return result;
    }

    public String[] getSubContext(String[] source, int startIndex, int lastLength) {
        String[] tmp = new String[lastLength - startIndex];
        System.out.println(lastLength + " " + startIndex);
        for (int i = 0; i < lastLength - startIndex; i++) {
            tmp[i] = source[i];
        }
        return tmp;
    }

    public int getNextBlockIndex(DiskBlock[] fatTable, DiskBlock fileBlock) {
        int row = fileBlock.getIndex() / 128;
        int column = fileBlock.getIndex() % 128;
        return this.binaryToDec(fatTable[row].getBytes()[column].getDiskByte());
    }

    /**
     * @return
     */
    public ArrayList<Catalog> getFullCatalog(CatalogContainer tables,DiskBlock[] FATBlocks) {
        DiskService diskService = DiskService.getInstance();
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
     *
     * @param fileName
     * @param expandedName
     * @return
     */
    public CatalogEntry getTargetEntry(String fileName, String expandedName,CatalogContainer tables,DiskBlock[] FATBlocks) {
        ArrayList<Catalog> readingCatalog = fileUtils.getFullCatalog(tables,FATBlocks);
        CatalogEntry targetEntry = null;
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                if (formatName(entry.getName(),String.valueOf(entry.getExpandedName())).equals(formatName(fileName,expandedName))) {
                    targetEntry = entry;
                    break;
                }
                if(entry.isEmpty()){
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
     * @return
     */
    public String formatName(String name, String expandedName) {
        while (name.length()<3){
            name += (char) 0;
        }
        name = name+'.'+expandedName;
        return name;
    }

    /**
     * @param blockIndex
     * @param nextIndex
     */
    public void modifyFAT(int blockIndex, int nextIndex,DiskBlock[] FATBlocks){
        FATBlocks[blockIndex/128].getBytes()[blockIndex%128].setDiskByte(fileUtils.decToBinary(nextIndex,8));
    }

    /**
     *
     * @return
     */
    public int getEmptyBlockIndex(DiskService diskService) {
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

    public int getFileFatResult(DiskBlock[] fatTable, DiskBlock diskBlock) {
        int row = diskBlock.getIndex() / 128;
        int column = diskBlock.getIndex() % 128;
        return fileUtils.binaryToDec(fatTable[row].getBytes()[column].getDiskByte());
    }

    public void cleanBlock(DiskBlock targetDiskBlock) {
        for(int i=0;i<128;i++){
            targetDiskBlock.getBytes()[i].setDiskByte("00000000");
        }
    }
}
