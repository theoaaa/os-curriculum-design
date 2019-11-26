package test;

import disk.service.DiskService;
import org.junit.Test;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 测试类
 */
public class ReadDiskTest {
    @Test
    public void getDisk() {
        DiskService service = DiskService.getInstance();
        for (int i = 0; i < 256; i++) {
            System.out.println(service.getDiskBlock(i).getIndex());
        }
        service.formatDisk();
        service.modifyDisk();
    }

}
