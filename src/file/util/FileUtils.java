package file.util;

import disk.bean.DiskBlock;
import instruction.util.InstrUtils;

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
}
