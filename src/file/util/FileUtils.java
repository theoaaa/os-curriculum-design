package file.util;

import instruction.util.InstrUtils;

import java.io.File;

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
}
