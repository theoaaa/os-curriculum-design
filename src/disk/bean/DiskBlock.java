package disk.bean;

/**
 * @author Rorke
 * @Date 2019/11/4
 */
public class DiskBlock {
    public final int SIZE_PER_BLOCK = 128;
    private DiskByte[] bytes = new DiskByte[SIZE_PER_BLOCK];
    private final String emp = "00000000";
    private int index;
    private int nextIndex;
    public DiskByte[] getBytes() {
        return bytes;
    }

    public void setBytes(DiskByte[] bytes) {
        this.bytes = bytes;
    }

    public int getBlockStatus(){
        int cnt = 0;
        for (int i = SIZE_PER_BLOCK - 1; i >= 0; i--) {
            if (!bytes[i].getDiskByte().equals(emp)) {
                cnt = i+1;
                break;
            }
        }
        return cnt;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }
}
