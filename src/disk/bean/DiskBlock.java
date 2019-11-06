package disk.bean;

/**
 * @author Rorke
 * @Date 2019/11/4
 */
public class DiskBlock {
    private DiskByte[] bytes = new DiskByte[64];

    public DiskByte[] getBytes() {
        return bytes;
    }

    public void setBytes(DiskByte[] bytes) {
        this.bytes = bytes;
    }

    public int getDiskStatus(){
        int cnt = 0;
        for (DiskByte b : bytes) {
            if(b.getDiskByte().equals("00000000")){
                break;
            }else {
                cnt++;
            }
        }
        return cnt;
    }

    public String getBlock(){
        StringBuffer buffer = new StringBuffer();
        for (DiskByte b: bytes) {
            buffer.append(b.getDiskByte()+" ");
        }
        buffer.append("\n");
        return String.valueOf(buffer);
    }

}
