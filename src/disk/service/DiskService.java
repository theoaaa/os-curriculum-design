package disk.service;

import disk.bean.DiskBlock;
import disk.util.RAWUtils;

import java.util.ArrayList;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 为disk界面服务
 */
public class DiskService {
    private final int BLOCKS_SIZE = 256;
    private final int FAT_SIZE = 2;
    private ArrayList<DiskBlock> blocks = new ArrayList<>(BLOCKS_SIZE);
    private DiskBlock[] fatBlocks = new DiskBlock[FAT_SIZE];
    private RAWUtils diskUtils;
    private static DiskService diskService = new DiskService();

    private DiskService() {
        String diskPath = "simulateDisk/Disk.txt";
        this.diskUtils = new RAWUtils(diskPath);
    }

    private void setFileAccessTableBlock() {
        for (int i = 0; i < FAT_SIZE; i++) {
            fatBlocks[i] = blocks.get(i);
        }
    }

    /**
     * 系统启动时执行，读取disk的情况
     */
    public void readDisk() {
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
