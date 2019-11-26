package file.operation;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import disk.service.DiskService;
import file.bean.CatalogEntry;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/11 12:36
 */
public class CopyOperation extends AbstractOperation {
    private CatalogEntry entry =null;

    public CatalogEntry getEntry() {
        return entry;
    }

    public void setEntry(CatalogEntry entry) {
        this.entry = entry;
    }

    public String[] getFileContext(DiskBlock[] FATBlocks) {
        DiskService diskService = DiskService.getInstance();
        ArrayList<DiskBlock> context = new ArrayList<>();
        context.add(diskService.getDiskBlock(entry.getStartedBlockIndex()));
        int nextIndex = entry.getStartedBlockIndex();
        while ((nextIndex = fileUtils.getNextBlockIndex(FATBlocks,diskService.getDiskBlock(nextIndex)))!=1){
            context.add(diskService.getDiskBlock(nextIndex));
        }
        int i=0;
        String[] fileContext = new String[entry.getSize()];
        for (DiskBlock block:context) {
            for(DiskByte tmp:block.getBytes()){
                fileContext[i] = tmp.getDiskByte();
            }
        }
        return fileContext;
    }
}
