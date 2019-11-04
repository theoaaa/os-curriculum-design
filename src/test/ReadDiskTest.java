package test;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import disk.service.DiskService;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 测试类
 */
public class ReadDiskTest {
    @Test
    public void getDisk() throws IOException {
        DiskService.ReadDisk();
        ArrayList<Integer> list = DiskService.getDiskStatus();
        for (Integer i: list) {
            System.out.println(i);
        }
    }
}
