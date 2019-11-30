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
    CatalogEntry(DiskByte[] context){
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

    public String getExpandedName(){
        return String.valueOf((char)fileUtils.binaryToDec(context[3].getDiskByte()));
    }
    public String getAttribute(){
        return String.valueOf((char)fileUtils.binaryToDec(context[4].getDiskByte()));
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

    public void setContext(DiskByte[] lastContext) {
        String[] context = new String[8];
        for (int i = 0; i < 8; i++) {
            context[i] = lastContext[i].getDiskByte();
        }
        setContext(context);
    }

    public void setEmpty() {
        for (DiskByte diskByte : context) {
            diskByte.setDiskByte("00000000");
        }
    }

    public void setExpandedName(String t) {
        context[3].setDiskByte(fileUtils.decToBinary(t.charAt(0),1));
    }

    public void setName(String newName, String oldExpandedName) {
        String name = fileUtils.formatName(newName,oldExpandedName);
        int i=0;
        while (i<3){
            context[i].setDiskByte(fileUtils.decToBinary(name.charAt(i), 8));
            i++;
        }
    }
}
