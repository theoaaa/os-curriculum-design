package disk.service;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import disk.util.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 为disk界面服务
 */
public class DiskService {
    private ArrayList<DiskBlock> blocks = new ArrayList<>(256);
    private final String diskPath = "simulateDisk/Disk.txt";
    private final int fatSize = 2;
    private DiskBlock[] fatBlocks = new DiskBlock[fatSize];
    private Utils diskUtils;
    public static DiskService diskService = new DiskService();

    private DiskService() {
        this.diskUtils = new Utils(diskPath);
    }

    private void setFileAccessTableBlock() {
        for (int i = 0; i < fatSize; i++) {
            fatBlocks[i] = blocks.get(i);
        }
    }

    /**
     * 系统启动时执行，读取disk的情况
     */
    public void ReadDisk() {
        diskUtils.read(blocks);
        setFileAccessTableBlock();
    }

    /**
     * 程序结束时执行，保存对磁盘的修改
     */
    public void modifyDisk() {
        diskUtils.write(blocks);
    }

    /**
     * 获得磁盘占用情况
     * @return 磁盘占用情况表，每一个元素都表示该块被占用了多少字节
     */
    public ArrayList<Integer> getDiskStatus() {
        ArrayList<Integer> statusList = new ArrayList<>();
        for (DiskBlock b : blocks) {
            statusList.add(b.getDiskStatus());
        }
        return statusList;
    }

    /**
     * 单例模式
     * @return 确保程序中所有diskService实例为同一个
     */
    public static DiskService getDiskService() {
        return diskService;
    }
}
