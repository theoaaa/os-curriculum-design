package model.processManege;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PCB {
    //进程状态
    public static final int BLANK = 0;  //空的PCB
    public static final int EXECUTING = 1;
    public static final int READY = 2;
    public static final int BLOCK = 3;
    public static final int END = 4;

    //阻塞原因
    public static final int NOT_BLOCKED = 0;
    public static final int IO_INTERRUPT = 1;

    //进程队列
    private static List<PCB> emptyPCBList = new LinkedList<>();
    private static List<PCB> readyProcessPCBList = new LinkedList<>();
    private static List<PCB> blockedProcessPCBList = new LinkedList<>();

    //保存寄存器组数据
    private Integer registers[] = new Integer[4];

    //PCB属性
    private int pcbId;
    private Integer processID;
    private int restTime;
    private int processState;
    private int processBlockReason;
    private int processBlockTime;

    private List<String> processInstructions;
    private int currentInstructionIndex;
    private String intermediateResult;

    //初始化进程队列
    static {
        for(int pcbId = 0; pcbId < 10; pcbId++){
            PCB pcb = new PCB();
            pcb.pcbId = pcbId;
            emptyPCBList.add(pcb);
        }
    }

    // constructor
    public PCB(){}

    public Integer[] readRegister(){
        return this.registers;
    }

    public void resetRestTime(){
        this.restTime = CPU.timeSliceLength;
    }

    public void initPCBToReady(int processID){
        this.processID = processID;
        this.restTime = CPU.timeSliceLength;
        for(int i = 0; i < 4; ++ i){
            this.registers[i] = new Integer(0);
        }
    }

    public void clearPCBToBlank(){
        this.restTime = 0;
        this.processID = null;
        this.currentInstructionIndex = 0;
        this.intermediateResult = "";
        this.processBlockReason = NOT_BLOCKED;
        this.processState = BLANK;
        this.processInstructions = null;
        for(int i = 0; i < 4; ++ i){
            this.registers[i] = new Integer(0);
        }
    }

    public void decreaseRestTime() {
        this.restTime--;
    }

    public void increaseCurrentInstructionIndex(){
        currentInstructionIndex++;
        if(processInstructions == null || currentInstructionIndex >= processInstructions.size())
            this.processState = END;
    }

    // getter & setter
    public Integer getProcessID() {
        return processID;
    }

    public Integer getRestTime() {
        return restTime;
    }

    public static List<PCB> getEmptyPCBList() {
        return emptyPCBList;
    }

    public static List<PCB> getReadyProcessPCBList() {
        return readyProcessPCBList;
    }

    public static List<PCB> getBlockedProcessPCBList() {
        return blockedProcessPCBList;
    }

    public int getProcessState() {
        return processState;
    }

    public String getIntermediateResult() {
        return intermediateResult;
    }

    public int getProcessBlockReason() {
        return processBlockReason;
    }

    public void setProcessState(int processState) {
        this.processState = processState;
    }

    public void setProcessBlockReason(int processBlockReason) {
        this.processBlockReason = processBlockReason;
    }
    public String getCurrentInstruction(){
        return processInstructions == null || processInstructions.size() <= currentInstructionIndex ? null: processInstructions.get(currentInstructionIndex);
    }

    public void setIntermediateResult(String intermediateResult) {
        this.intermediateResult = intermediateResult;
    }
    public boolean isTimeSliceUsedUp(){
        return restTime==0;
    }
    public boolean isProcessEnd(){
        return processState == PCB.END || processInstructions==null || currentInstructionIndex == processInstructions.size();
    }

    public List<String> getProcessInstructions() {
        return processInstructions;
    }

    public void setProcessInstructions(List<String> processInstructions) {
        this.processInstructions = processInstructions;
    }

    public int getPcbId() {
        return pcbId;
    }

    public void setPcbId(int pcbId) {
        this.pcbId = pcbId;
    }

    public int getBlockedTime(){
        return CPU.getSystemTime() - this.processBlockTime;
    }

    public void setProcessBlockTime(int processBlockTime) {
        this.processBlockTime = processBlockTime;
    }

    @Override
    public String toString() {
        return "PCB{" +
                "pcbId=" + pcbId +
                ", processState=" + processState +
                ", processBlockTime=" + processBlockTime +
                ", intermediateResult='" + intermediateResult + '\'' +
                '}';
    }
}
