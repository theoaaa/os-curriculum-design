package file.util;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import instruction.util.InstrUtils;
//import org.jetbrains.annotations.NotNull;

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

    /**
     * 二进制转十进制
     * @param binaryString 二进制串
     * @return  十进制数
     */
    public int binaryToDec(String binaryString){
        highestIndex = binaryString.length()-1;
        int index = highestIndex;
        int result = 0;
        for(;index>=0;index--){
            result += Math.pow(2,(highestIndex-index))*(binaryString.charAt(index)-'0');
        }
        return result;
    }

    /**
     * 获得小于等于一个磁盘块大小内容
     * @param source 原来的内容
     * @param startIndex 起始下标
     * @param lastIndex 终止下标
     * @return 串数组
     */
    public String[] getSubContext(String[] source, int startIndex, int lastIndex) {
        String[] tmp = new String[lastIndex - startIndex];
        for (int i = 0; i < lastIndex - startIndex; i++) {
            tmp[i] = decToBinary(source[i].charAt(0),8);
        }
        return tmp;
    }

    /**
     * 获得下一个磁盘块的编号
     * @param fatTable fat表
     * @param fileBlock 当前磁盘块
     * @return 下一磁盘块下标
     */
    public int getNextBlockIndex(DiskBlock[] fatTable, DiskBlock fileBlock) {
        int row = fileBlock.getIndex() / 128;
        int column = fileBlock.getIndex() % 128;
        return this.binaryToDec(fatTable[row].getBytes()[column].getDiskByte());
    }

    /**
     * 获得目录项
     * @param fileName 文件名
     * @param expandedName 文件扩展名
     * @return 对应的目录项
     */
    public CatalogEntry getTargetEntryByTables(String fileName, String expandedName, CatalogContainer tables, DiskBlock[] FATBlocks) {
        ArrayList<Catalog> readingCatalog = getFullCatalog(tables,FATBlocks);
        return getEntry(fileName,expandedName,readingCatalog);
    }
    private CatalogEntry getEntry(String fileName, String expandedName, ArrayList<Catalog> readingCatalog){
        CatalogEntry targetEntry = null;
        boolean breakFlag = false;
        for (Catalog catalog : readingCatalog) {
            for (CatalogEntry entry : catalog.getEntries()) {
                System.out.println(formatName(entry.getName(),entry.getExpandedName())+" "+(formatName(fileName,expandedName)));
                if (formatName(entry.getName(),entry.getExpandedName()).equals(formatName(fileName,expandedName))) {
                    targetEntry = entry;
                    breakFlag = true;
                    break;
                }
                if(entry.isEmpty()){
                    targetEntry = entry;
                    breakFlag = true;
                    break;
                }
            }
            if(breakFlag){
                break;
            }
        }
        return targetEntry;
    }
    public CatalogEntry getTargetEntryByCatalog(String fileName, String expandedName, ArrayList<Catalog> catalog){
        return getEntry(fileName,expandedName,catalog);
    }
    /**
     *
     * @param name
     * @param expandedName
     * @return
     */
    //public String formatName(@NotNull String name, String expandedName) {
    public String formatName(String name, String expandedName) {
        StringBuilder nameBuilder = new StringBuilder(name);
        while (nameBuilder.length()<3){
            nameBuilder.append((char) 0);
        }
        name = nameBuilder.toString();
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
    public int getEmptyBlockIndex(DiskBlock[] FATBlocks) {
        int index = -1;
        for(int i=0;i<2;i++){
            for(int j=0;j<128;j++){
                if(FATBlocks[i].getBytes()[j].isEmpty()){
                    index = i*128+j;
                    return index;
                }
            }
        }
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
    /**
     * @return
     */
    public ArrayList<Catalog> getFullCatalog(CatalogContainer tables, DiskBlock[] FATBlocks) {
        return getFullCatalog(tables.getTop(),FATBlocks);
    }

    public ArrayList<Catalog> getFullCatalog(Catalog catalog ,DiskBlock[] FATBlocks){
        DiskService diskService = DiskService.getInstance();
        int nextFat;
        Catalog tmp;
        int presentIndex = catalog.getIndex();
        ArrayList<Catalog> readingCatalog = new ArrayList<>();
        while ((nextFat = fileUtils.getNextBlockIndex(FATBlocks, catalog)) != 1) {
            tmp = new Catalog(diskService.getDiskBlock(presentIndex));
            readingCatalog.add(tmp);
            presentIndex = nextFat;
        }
        tmp = new Catalog(diskService.getDiskBlock(presentIndex));
        readingCatalog.add(tmp);
        return readingCatalog;
    }

    public void modifyNextBlock(DiskBlock[] FATBlocks,DiskBlock targetDiskBlock) {
        int nextIndex = getNextBlockIndex(FATBlocks,targetDiskBlock);
        DiskService diskService = DiskService.getInstance();
        while (nextIndex!=1){
            modifyFAT(targetDiskBlock.getIndex(),0,FATBlocks);
            targetDiskBlock = diskService.getDiskBlock(nextIndex);
            nextIndex = getNextBlockIndex(FATBlocks,targetDiskBlock);
        }
        modifyFAT(targetDiskBlock.getIndex(),0,FATBlocks);
    }
}
