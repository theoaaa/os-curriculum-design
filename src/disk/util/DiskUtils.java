package disk.util;

import disk.bean.DiskBlock;
import disk.bean.DiskByte;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Rorke
 * @date 2019/11/5 10:07
 * 提供最基础的读写文件方法
 */
public class DiskUtils {
    private File file;
    private final int SIZE_PER_BLOCK = 128;
    public DiskUtils(String actualPath) {
        this.file = new File(actualPath);
    }

    /**
     * 读取文件的一般方法
     * @return 返回文件中的内容
     */
    private String read(){
        String line;
        StringBuffer stringBuffer = null;
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            stringBuffer = new StringBuffer();
            while ((line=buffer.readLine())!=null){
                stringBuffer.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(stringBuffer);
    }

    /**
     * 读取磁盘情况的方法
     * @param blocks 磁盘块
     */
    public void read(ArrayList<DiskBlock> blocks){
        int index = 0;
        String str = read();
        String[] lines = str.split("\n");
        for (String s : lines) {
            String[] tmp = s.split(" ");
            DiskByte[] bytes = new DiskByte[SIZE_PER_BLOCK];
            for (int i = 0; i < SIZE_PER_BLOCK; i++) {
                bytes[i] = new DiskByte();
                bytes[i].setDiskByte(tmp[i]);
            }
            DiskBlock block = new DiskBlock();
            block.setBytes(bytes);
            block.setIndex(index++);
            blocks.add(block);
        }
    }

    /**
     * 写文件的一般方法
     * @param str 写入文件的字符串
     */
    private void write(String str){
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改磁盘情况的写方法
     * @param blocks 磁盘块
     */
    public void write(ArrayList<DiskBlock> blocks){
        StringBuffer buffer = new StringBuffer();
        for (DiskBlock b : blocks) {
            DiskByte[] bytes = b.getBytes();
            for (DiskByte tmpByte : bytes) {
                buffer.append(tmpByte.getDiskByte()).append(" ");
            }
            buffer.append("\n");
        }
        write(String.valueOf(buffer));
    }
}
