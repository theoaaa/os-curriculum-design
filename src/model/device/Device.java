package model.device;

import model.PCB;

public class Device {

    public int remainTime;
    public boolean isAllocated;
    public PCB pcb;

    public Device(){
        this.isAllocated = false;
        this.remainTime = 0;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }


    public void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }

    public int getRemainTime() {
        return remainTime;
    }


    public boolean isAllocated() {
        return isAllocated;
    }

    public PCB getPcb(){
        return pcb;
    }

    public void setPcb(PCB pcb){
        this.pcb = pcb;
    }

}
