package disk.service;
import disk.bean.DiskBlock;
import disk.bean.DiskByte;
import java.io.*;
import java.util.ArrayList;

/**
 * @author Rorke
 * @Date 2019/11/4
 * 为disk界面服务
 */
public class DiskService {
    private static ArrayList<DiskBlock> blocks = new ArrayList<>(256);
    private static final String diskPath = "simulateDisk/Disk.txt";

    /**
     * 系统启动时执行，读取disk的情况
     * @throws IOException
     */
    public static void ReadDisk() throws IOException {
        File file = new File(diskPath);
        FileReader reader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(reader);
        String line = buffer.readLine();
        String[] str;
        while (line != null) {
            str = line.split(" ");
            DiskByte[] bytes = new DiskByte[8];
            for (int j = 0; j < 8; j++) {
                bytes[j] = new DiskByte();
                bytes[j].setDiskByte(str[j]);
            }
            DiskBlock block = new DiskBlock();
            block.setBytes(bytes);
            blocks.add(block);
            line = buffer.readLine();
        }

    }

    /**
     * 获得磁盘占用情况
     * @return 磁盘占用情况表，每一个元素都表示该块被占用了多少字节
     */
    public static ArrayList<Integer> getDiskStatus(){
        ArrayList<Integer> statusList = new ArrayList<>();
        for (DiskBlock b : blocks) {
            statusList.add(b.getDiskStatus());
        }
        return statusList;
    }
}
