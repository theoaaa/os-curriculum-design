package model.processManege;

import model.memoryManage.MemoryManage;
import util.ProcessIdGenerator;
import util.StringUtil;
import java.util.*;

public class ProcessControl {

    public static List<String[]> waitingExecutableFilePathQueue = new LinkedList<>();

    /**
     * 检查可执行文件，创建新进程，为该进程分配进程控制块并返回，如果内存空间不足则创建，进入等待队列
     * 返回结果：可执行文件内容正确与否
     */
    public static boolean create(String[] fileContext){
        boolean res = true;
        //从文件中读取指令
        for(int i=0;i<fileContext.length;i++){
            if(!StringUtil.checkInstruction(fileContext[i])){
                res = false;
                break;
            }
        }

        //可执行文件正确
        if(res) {
            List<String> instructions = new ArrayList<>();
            Collections.addAll(instructions, fileContext);
            //申请内存
            List<PCB> emptyPCBList = PCB.getEmptyPCBList();
            PCB pcb = null;
            System.out.println(emptyPCBList.size());
            if (emptyPCBList.size() > 0) {
                pcb = emptyPCBList.get(0);
                pcb.setProcessInstructions(instructions);
                pcb.initPCBToReady(ProcessIdGenerator.generateProcessId());
                if (MemoryManage.memoryAllocate(pcb, pcb.getProcessInstructions().size())) {
                    emptyPCBList.remove(pcb);
                    PCB.getReadyProcessPCBList().add(pcb);
                } else {
                    //内存不足
                    pcb.clearPCBToBlank();
                    waitingExecutableFilePathQueue.add(fileContext);
                }
            } else {
                //pcb已达到10个
                waitingExecutableFilePathQueue.add(fileContext);
            }
        }
        return res;
    }
    public static void destroy(PCB pcb){
        synchronized (pcb) {
            MemoryManage.memoryRecycle(pcb);
            pcb.clearPCBToBlank();
            if (!waitingExecutableFilePathQueue.isEmpty()) {
                pcb.initPCBToReady(ProcessIdGenerator.generateProcessId());
                create(waitingExecutableFilePathQueue.get(0));
                waitingExecutableFilePathQueue.remove(0);
            } else {
                PCB.getEmptyPCBList().add(pcb);
            }
        }
    }
    public static void block(PCB pcb, int blockReason){
        synchronized (pcb) {
            pcb.setProcessState(PCB.BLOCK);
            pcb.setProcessBlockTime(CPU.getSystemTime());
            pcb.setProcessBlockReason(blockReason);
            PCB.getBlockedProcessPCBList().add(pcb);
        }
    }

    public static void awake(PCB pcb){
        synchronized (pcb) {
            PCB.getBlockedProcessPCBList().remove(pcb);
            pcb.resetRestTime();
            PCB.getReadyProcessPCBList().add(pcb);
        }
    }
}