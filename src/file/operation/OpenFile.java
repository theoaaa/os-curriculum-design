package file.operation;

import disk.bean.DiskBlock;
import disk.service.DiskService;
import file.bean.Catalog;
import file.bean.CatalogEntry;

import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/22 16:01
 */
public class OpenFile extends AbstractOperation {
    private DiskService diskService;

    public OpenFile() {
        DiskService diskService = DiskService.getInstance();
    }

    public String openTextFile(DiskBlock[] fatTable, Catalog catalog, String fileName) {
        int result;
        ArrayList<CatalogEntry> entries = new ArrayList<>();
        for (CatalogEntry entry : catalog.getEntries()) {
            entries.add(entry);
        }
        while ((result = getFileFatResult(fatTable, catalog)) != 1) {
            catalog = new Catalog(diskService.getDiskBlock(result));
            for (CatalogEntry entry : catalog.getEntries()) {
                entries.add(entry);
            }
        }
        CatalogEntry targetEntry = null;
        for (CatalogEntry entry : entries) {
            if (entry.getName().equals(fileName)) {
                targetEntry = entry;
            }
        }
        StringBuffer buf = new StringBuffer();
        DiskBlock fileBlock = diskService.getDiskBlock(targetEntry.getStartedBlockIndex());
        while ((result = getFileFatResult(fatTable, fileBlock)) != 1) {

        }
        return String.valueOf(buf);
    }

    private int getFileFatResult(DiskBlock[] fatTable, DiskBlock diskBlock) {
        int row = diskBlock.getIndex() / 128;
        int column = diskBlock.getIndex() % 128;
        return fileUtils.binaryToDec(fatTable[row].getBytes()[column].getDiskByte());
    }
}
