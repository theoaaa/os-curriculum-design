package model;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ProcessControl {

    public static List<File> waitingExecutableFilePathQueue = new LinkedList<>();

    //创建新进程，为该进程分配进程控制块并返回，如果内存空间不足则创建失败，返回null
    public static PCB create(File executableFile){
        List<PCB> emptyPCBList = PCB.getEmptyPCBList();
        PCB pcb = emptyPCBList.get(0);
        if(pcb != null){
            emptyPCBList.remove(0);
            pcb.initPCBToReady(UUID.randomUUID().toString());
            List<String> instructions = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(executableFile))) {
                String instruction;
                while ((instruction = reader.readLine()) != null) {
                    instructions.add(instruction);
                }
                pcb.setProcessInstructions(instructions);
                PCB.getReadyProcessPCBList().add(pcb);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            waitingExecutableFilePathQueue.add(executableFile);
        }
        return pcb;
    }
    public static void destory(PCB pcb){
        pcb.clearPCBToBlank();
        if(! waitingExecutableFilePathQueue.isEmpty()){
            pcb.initPCBToReady(UUID.randomUUID().toString());
            create(waitingExecutableFilePathQueue.get(0));
        }else {
            PCB.getEmptyPCBList().add(pcb);
        }
    }
    public static void block(PCB pcb, int blockReason){
        pcb.setProcessState(PCB.BLOCK);
        pcb.setProcessBlockReason(blockReason);
        PCB.getBlockedProcessPCBList().add(pcb);
    }
    public static void awake(PCB pcb){
        PCB.getBlockedProcessPCBList().remove(pcb);
        pcb.resetRestTime();
        PCB.getReadyProcessPCBList().add(pcb);
    }
}
