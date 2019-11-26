package model.memoryManage;

import model.PCB;

public class ProcessMemoryBlock {

    // 当前块的内存大小
    private int memorySize;
    // 起始内存地址
    private int startAddr;
    // 结束内存地址
    private int endAddr;
    // 当前块的PCB
    private PCB pcb;
    // 内存分配表是否有下一个已分配的内存块
    private boolean next;
    // 占用内存比例
    private double takeupRatio;

    public ProcessMemoryBlock(){

    }

    public ProcessMemoryBlock(int memorySize, int startAddr, int endAddr, PCB pcb, boolean next) {
        this.memorySize = memorySize;
        this.startAddr = startAddr;
        this.endAddr = endAddr;
        this.pcb = pcb;
        this.next = next;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public int getStartAddr() {
        return startAddr;
    }

    public int getEndAddr(){
        return endAddr;
    }

    public PCB getPcb() {
        return pcb;
    }

    public boolean isNext() {
        return next;
    }

    public double getTakeupRatio() {
        return takeupRatio;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public void setStartAddr(int startAddr) {
        this.startAddr = startAddr;
    }

    public void setEndAddr(int endAddr) {
        this.endAddr = endAddr;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public void setTakeupRatio(double ratio){
        this.takeupRatio = ratio;
    }
}
