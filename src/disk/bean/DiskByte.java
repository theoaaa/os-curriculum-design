package disk.bean;
/**
 * @author Rorke
 * @Date 2019/11/4
 */
public class DiskByte {
    private String diskByte;//磁盘的字节，8位
    public String getDiskByte() {
        return diskByte;
    }
    public void setDiskByte(String diskByte) {
        this.diskByte = diskByte;
    }
    public boolean isEmpty(){
        return diskByte.equals("00000000");
    }
}
