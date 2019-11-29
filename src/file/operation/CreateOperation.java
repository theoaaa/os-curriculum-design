package file.operation;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;

/**
 * @author Rorke
 * @date 2019/11/11 12:35
 */
public class CreateOperation extends AbstractOperation{
    private String[] entryContext = new String[8];
    private final int SIZE_PER_BLOCK = 128;

    public String[] getEntryContext(String name, String expandedName, String attribute, int startedIndex, int size) {
        for(int i=0;i<3;i++){
            if(i<name.length()) {
                entryContext[i] = fileUtils.decToBinary(name.charAt(i), 8);
            }else {
                entryContext[i] = "00000000";
            }
        }
        entryContext[3] = fileUtils.decToBinary(expandedName.toCharArray()[0],8);
        entryContext[4] = fileUtils.decToBinary(attribute.toCharArray()[0],8);
        entryContext[5] = fileUtils.decToBinary(startedIndex,8);
        String str = fileUtils.decToBinary(size,16);
        entryContext[6] = str.substring(0, 8);
        entryContext[7] = str.substring(8);
        return entryContext;
    }

    public void setFileContext(String[] context, DiskBlock block) {
        if (context != null) {
            DiskByte[] bytes = block.getBytes();
            int length = context.length;
            int index = 0;
            for (int i = 0; i < context.length; i++) {
                bytes[i].setDiskByte(context[i]);
                index = i;
            }
            if(length<SIZE_PER_BLOCK){
                index++;
                for(;index<SIZE_PER_BLOCK;index++){
                    bytes[index].setDiskByte("00000000");
                }
            }
        }
    }
}
