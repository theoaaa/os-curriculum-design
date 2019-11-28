package test;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
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
        DiskBlock block = service.getDiskBlock(3);
        for (DiskByte tmpByte :
                block.getBytes()) {
            System.out.print(tmpByte.getDiskByte()+" ");
        }
        service.modifyDisk();
    }

}
