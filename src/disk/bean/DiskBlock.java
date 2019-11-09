package disk.bean;

/**
 * @author Rorke
 * @Date 2019/11/4
 */
public class DiskBlock {
    private final int SIZE_PER_BLOCK = 64;
    private DiskByte[] bytes = new DiskByte[SIZE_PER_BLOCK];

    public DiskByte[] getBytes() {
        return bytes;
    }

    public void setBytes(DiskByte[] bytes) {
        this.bytes = bytes;
    }

    public int getDiskStatus(){
        int cnt = 0;
        for (int i = SIZE_PER_BLOCK - 1; i >= 0; i--) {
            String emp = "00000000";
            if (i != 0) {
                if (!bytes[i - 1].getDiskByte().equals(emp) && bytes[i].getDiskByte().equals(emp)) {
                    cnt = i;
                    break;
                }
            }else {
                if (!bytes[i].getDiskByte().equals(emp)) {
                    cnt = 1;
                }
            }
        }
        return cnt;
    }

}
