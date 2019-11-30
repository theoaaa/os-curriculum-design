package model.processManege;

import model.memoryManage.MemoryManage;
import util.ProcessIdGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ProcessControl {

    public static List<File> waitingExecutableFilePathQueue = new LinkedList<>();

    //创建新进程，为该进程分配进程控制块并返回，如果内存空间不足则创建失败，返回null
    public static PCB create(File executableFile){
        //从文件中读取指令
        List<String> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(executableFile))) {
            String instruction;
            while ((instruction = reader.readLine()) != null) {
                instructions.add(instruction);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //申请内存
        List<PCB> emptyPCBList = PCB.getEmptyPCBList();
        PCB pcb = null;
        if(emptyPCBList.size()>0){
            pcb = emptyPCBList.get(0);
            pcb.setProcessInstructions(instructions);
            pcb.initPCBToReady(ProcessIdGenerator.generateProcessId());
            if(MemoryManage.memoryAllocate(pcb, pcb.getProcessInstructions().size())) {
                emptyPCBList.remove(pcb);
                PCB.getReadyProcessPCBList().add(pcb);
            }else {
                //内存不足
                pcb.clearPCBToBlank();
                waitingExecutableFilePathQueue.add(executableFile);
            }
        }else {
            //pcb已达到10个
            waitingExecutableFilePathQueue.add(executableFile);
        }
        return pcb;
    }
    public static void destroy(PCB pcb){
        MemoryManage.memoryRecycle(pcb);
        pcb.clearPCBToBlank();
        if(! waitingExecutableFilePathQueue.isEmpty()){
            pcb.initPCBToReady(ProcessIdGenerator.generateProcessId());
            create(waitingExecutableFilePathQueue.get(0));
        }else {
            PCB.getEmptyPCBList().add(pcb);
        }
    }
    
    public static void block(PCB pcb, int blockReason){
        pcb.setProcessState(PCB.BLOCK);
        pcb.setProcessBlockTime(CPU.getSystemTime());
        pcb.setProcessBlockReason(blockReason);
        PCB.getBlockedProcessPCBList().add(pcb);
    }

    public static void awake(PCB pcb){
        PCB.getBlockedProcessPCBList().remove(pcb);
        pcb.resetRestTime();
        PCB.getReadyProcessPCBList().add(pcb);
    }
}