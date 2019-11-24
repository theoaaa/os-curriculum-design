package file.bean;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;

/**
 * @author Rorke
 * @date 2019/11/20 23:09
 */
public class Catalog extends DiskBlock {
    private final int CATALOG_SIZE = 8;
    private final int CATALOG_AMOUNT = SIZE_PER_BLOCK/CATALOG_SIZE;
    private CatalogEntry[] entries = new CatalogEntry[CATALOG_AMOUNT];
    public Catalog(DiskBlock catalogBlock){
        this.setBytes(catalogBlock.getBytes());
        this.setIndex(catalogBlock.getIndex());
        for(int i=0;i<CATALOG_AMOUNT;i++){
            DiskByte[] entry = new DiskByte[CATALOG_SIZE];
            for(int j=0;j<CATALOG_SIZE;j++){
                entry[j] = catalogBlock.getBytes()[i*CATALOG_SIZE+j];
            }
            entries[i] = new CatalogEntry(entry);
        }
    }
    public CatalogEntry[] getEntries(){
        return entries;
    }
}
