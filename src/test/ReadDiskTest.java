package test;

import disk.service.DiskService;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 测试类
 */
public class ReadDiskTest {
    @Test
    public void getDisk() {
        DiskService service = DiskService.getDiskService();
        service.readDisk();
        ArrayList<Integer> list = service.getDiskStatus();
        for (Integer i : list) {
            System.out.println(i);
        }
//        service.modifyDisk();
    }

}
