package file.util;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogContainer;
import file.bean.CatalogEntry;
import instruction.util.InstrUtils;

import java.util.ArrayList;

//import org.jetbrains.annotations.NotNull;

/**
 * @author Rorke
 * @date 2019/11/20 20:50
 */
public class FileUtils extends InstrUtils {
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
        int highestIndex = binaryString.length() - 1;
        int index = highestIndex;
        int result = 0;
        for(;index>=0;index--){
            result += Math.pow(2, (highestIndex - index)) * (binaryString.charAt(index) - '0');
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
     * 格式化名字，将名字和扩展名连接起来
     * @param name 文件名字
     * @param expandedName 扩展名
     * @return 格式化后的名字
     */
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
     * 修改fat表的内容
     * @param blockIndex 需要修改的下标
     * @param nextIndex  下一个磁盘号
     * @param FATBlocks  fat表
     */
    public void modifyFAT(int blockIndex, int nextIndex,DiskBlock[] FATBlocks){
        FATBlocks[blockIndex/128].getBytes()[blockIndex%128].setDiskByte(fileUtils.decToBinary(nextIndex,8));
    }

    /**
     * 获得下一个未被占用的磁盘块编号
     * @param FATBlocks fat表
     * @return 下一个未被占用的磁盘块编号
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

    /**
     * 获得文件占用的磁盘块的fat表内容
     *
     * @param fatTable  fat表
     * @param diskBlock 文件所占的磁盘块
     * @return fat内容
     */
    public int getFileFatResult(DiskBlock[] fatTable, DiskBlock diskBlock) {
        int row = diskBlock.getIndex() / 128;
        int column = diskBlock.getIndex() % 128;
        return fileUtils.binaryToDec(fatTable[row].getBytes()[column].getDiskByte());
    }

    /**
     * 获得完整的目录
     * @param tables 装载目录的容器
     * @param FATBlocks fat表
     * @return 完整的目录
     */
    public ArrayList<Catalog> getFullCatalog(CatalogContainer tables, DiskBlock[] FATBlocks) {
        return getFullCatalog(tables.getTop(),FATBlocks);
    }

    /**
     * 获得完整的目录
     *
     * @param catalog   目录，包含了它所占的磁盘号
     * @param FATBlocks fat表
     * @return 完整的目录
     */
    public ArrayList<Catalog> getFullCatalog(Catalog catalog, DiskBlock[] FATBlocks) {
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

    /**
     * 修改fat表，使得文件消失
     *
     * @param FATBlocks       fat表
     * @param targetDiskBlock 目标所占的磁盘块
     */
    public void modifyNextBlock(DiskBlock[] FATBlocks, DiskBlock targetDiskBlock) {
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
