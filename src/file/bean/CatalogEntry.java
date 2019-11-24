package file.bean;

import disk.bean.DiskByte;
import file.util.FileUtils;

/**
 * @author Rorke
 * @date 2019/11/20 22:41
 */
public class CatalogEntry {
    private DiskByte[] context;
    private final String emp = "00000000";
    private FileUtils fileUtils = FileUtils.getInstance();
    public CatalogEntry(DiskByte[] context){
        this.context = context;
    }
    public CatalogEntry(){ }

    public String getName(){
        StringBuffer buf = new StringBuffer();
        for(int i = 0;i<3;i++){
            buf.append((char)fileUtils.binaryToDec(context[i].getDiskByte()));
        }
        return String.valueOf(buf);
    }

    public Character getExpandedName(){
        return (char)fileUtils.binaryToDec(context[3].getDiskByte());
    }
    public Character getAttribute(){
        return (char)fileUtils.binaryToDec(context[4].getDiskByte());
    }
    public int getStartedBlockIndex(){
        return fileUtils.binaryToDec(context[5].getDiskByte());
    }
    public int getSize(){
        String str = context[6].getDiskByte()+context[7].getDiskByte();
        return fileUtils.binaryToDec(str);
    }
    public boolean isEmpty(){
        boolean statement = true;
        for(DiskByte myByte:context){
            if(!myByte.getDiskByte().equals(emp)){
                statement = false;
                break;
            }
        }
        return statement;
    }

    public DiskByte[] getContext(){
        return context;
    }
    public void setContext(String[] context) {
        int i=0;
        for(DiskByte tmp:this.context){
            tmp.setDiskByte(context[i++]);
        }
    }

    public void setSize(int length) {
        String str = fileUtils.decToBinary(length, 16);
        context[6].setDiskByte(str.substring(0, 8));
        context[7].setDiskByte(str.substring(8));
    }
}
