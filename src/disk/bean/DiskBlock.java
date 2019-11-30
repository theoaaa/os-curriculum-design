package disk.bean;

/**
 * @author Rorke
 * @Date 2019/11/4
 */
public class DiskBlock {
    protected final int SIZE_PER_BLOCK = 128;
    private DiskByte[] bytes = new DiskByte[SIZE_PER_BLOCK];
    private final String emp = "00000000";
    private int index;
    public DiskByte[] getBytes() {
        return bytes;
    }

    public void setBytes(DiskByte[] bytes) {
        this.bytes = bytes;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isEmpty() {
        boolean statement = true;
        for (DiskByte diskByte : bytes) {
            if (!diskByte.isEmpty()) {
                statement = false;
                break;
            }
        }
        return statement;
    }

    public void setEmpty() {
        for (DiskByte diskByte : bytes) {
            diskByte.setDiskByte("00000000");
        }
    }
}
