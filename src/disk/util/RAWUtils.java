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
public class RAWUtils {
    private File file;

    public RAWUtils(String actualPath) {
        this.file = new File(actualPath);
    }

    /**
     * 读取文件的一般方法
     * @return 返回文件中的内容
     */
    public String read(){
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
        String str = read();
        String[] lines = str.split("\n");
        for (String s : lines) {
            String[] tmp = s.split(" ");
            DiskByte[] bytes = new DiskByte[64];
            for (int i = 0; i < 64; i++) {
                bytes[i] = new DiskByte();
                bytes[i].setDiskByte(tmp[i]);
            }
            DiskBlock block = new DiskBlock();
            block.setBytes(bytes);
            blocks.add(block);
        }
    }

    /**
     * 写文件的一般方法
     * @param str 写入文件的字符串
     */
    public void write(String str){
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
