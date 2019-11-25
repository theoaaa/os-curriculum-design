package test;

import disk.service.DiskService;
import file.service.FileService;
import file.util.FileUtils;
import org.junit.Test;

/**
 * @author Rorke
 * @date 2019/11/20 22:50
 */
public class CatalogTest {
    String test = "01000001";
    @Test
    public void Test(){
        DiskService diskService = DiskService.getInstance();
        FileService fileService = FileService.getInstance();
        FileUtils fileUtils = FileUtils.getInstance();
        System.out.println(fileService.createFile("D:", " ", "D", 0));
        System.out.println(fileService.createFile("E:", " ", "D", 0));
        System.out.println(fileService.createFile("F:", " ", "D", 0));
        diskService.modifyDisk();
    }
}
