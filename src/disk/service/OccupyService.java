package disk.service;

import disk.bean.DiskBlock;
import disk.util.DiskUtils;

import java.util.ArrayList;

class OccupyService {
    private DiskUtils diskUtils;

    OccupyService() {
        String occupyPath = "simulateDisk/Occupy.txt";
        this.diskUtils = new DiskUtils(occupyPath);
    }

    boolean[] getOccupy() {
        boolean[] occupyList = new boolean[256];
        diskUtils.read(occupyList);
        return occupyList;
    }

    void writeOccupy(ArrayList<DiskBlock> blocks) {
        boolean[] occupyList = new boolean[256];
        int i = 0;
        for (DiskBlock b : blocks) {
            occupyList[i++] = b.isOccupy();
        }
        diskUtils.write(occupyList);
    }
}
