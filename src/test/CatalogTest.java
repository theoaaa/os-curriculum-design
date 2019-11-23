package test;

import disk.bean.DiskByte;
import disk.service.DiskService;
import file.bean.Catalog;
import org.junit.Test;

/**
 * @author Rorke
 * @date 2019/11/20 22:50
 */
public class CatalogTest {
    String test = "01000001";
    @Test
    public void Test(){
        DiskService diskService  =  DiskService.getInstance();
        Catalog rootTable = new Catalog(diskService.getDiskBlock(2));
        rootTable.printCatalogIsEmpty();
        DiskByte[] bytes = rootTable.getBytes();
        for(DiskByte tmp:bytes){
            System.out.println(tmp.getDiskByte());
        }
        bytes[1].setDiskByte("00000110");
        diskService.modifyDisk();
    }
}
