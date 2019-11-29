package file.operation;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import disk.service.DiskService;
import file.bean.CatalogEntry;

/**
 * @author Rorke
 * @date 2019/11/22 16:01
 */
public class OpenOperation extends AbstractOperation {
    private DiskService diskService;
    private String[] targetStr;
    private int index = 0;
    public OpenOperation() {
         diskService = DiskService.getInstance();
    }

    public String[] open(DiskBlock[] fatTable, CatalogEntry targetEntry) {
        targetStr = new String[targetEntry.getSize()];
        int startIndex = targetEntry.getStartedBlockIndex();
        if(fileUtils.getFileFatResult(fatTable,diskService.getDiskBlock(startIndex))==1){
            getContext(startIndex);
        }else{
            int nextIndex;
            while ((nextIndex = fileUtils.getFileFatResult(fatTable,diskService.getDiskBlock(startIndex)))!=1){
                getContext(startIndex);
                startIndex = nextIndex;
            }
            getContext(startIndex);
        }
        return targetStr;
    }

    private void getContext(int blockIndex){
        DiskByte[] bytes = diskService.getDiskBlock(blockIndex).getBytes();
        for(int i=0;i<128;i++){
            if(index==targetStr.length){
                break;
            }
            targetStr[index++] = bytes[i].getDiskByte();
        }
    }
}
