package model.memoryManage;

import model.processManege.PCB;
import view.memoryManagement.MemoryManagementController;
import view.memoryManagement.MemoryManagementWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存管理于分配
 */
public class MemoryManage {

    // 系统区内存
    private static List<PCB> pcbMemory = new ArrayList<>();
        // 内存分配表
    private static final ArrayList<ProcessMemoryBlock> processMemoryAllocatedList = new ArrayList<>();

    // 用户区内存 512B
    private static final int[] memory = new int[512];
    private static final int memorySize = 512;

    // 内存进程映射
    private static final Map<PCB, ProcessMemoryBlock> map = new HashMap<>();

    public static MemoryManagementController controller;

    // CPU创建PCB时调用，分配内存
    // 传入参数进程控制块和申请大小
    public static boolean memoryAllocate(PCB pcb, int allocSize){
        controller = MemoryManagementWindow.getController();

        // 分配内存结果
        int allocateResult = checkMemory(allocSize);
        if (allocateResult == -1){
            System.out.println("内存分配失败，没有足够的空闲内存.");
            return false;
        }
        ProcessMemoryBlock processMemory = new ProcessMemoryBlock();
        if (allocateResult == processMemoryAllocatedList.size()){
            // 分配的内存块是内存最后
            processMemory.setNext(false);
        }else {
            // 分配的内存块不再在内存最后
            processMemory.setNext(true);
        }
        if (allocateResult == 0){
            processMemory.setStartAddr(0);
        }else{
            System.out.println(allocateResult);
            processMemory.setStartAddr(processMemoryAllocatedList.get(allocateResult-1).getEndAddr()+1);
            processMemoryAllocatedList.get(allocateResult-1).setNext(true);
        }
        processMemory.setEndAddr(processMemory.getStartAddr() + allocSize -1);
        processMemory.setPcb(pcb);
        processMemory.setMemorySize(allocSize);
        processMemory.setTakeupRatio((double) allocSize/memorySize);
        processMemoryAllocatedList.add(allocateResult, processMemory);
        map.put(pcb, processMemory);
        for (int i=processMemory.getStartAddr(); i <= processMemory.getEndAddr(); i++){
            memory[i] = 1;
        }
        pcbMemory.add(pcb);
        // 更新界面显示
        if (controller != null)
            controller.updateMessage(processMemoryAllocatedList);
        System.out.println("内存分配成功.");
        return true;
    }

    private static int checkMemory(int size){
        ProcessMemoryBlock mem;
        ProcessMemoryBlock nextMem;
        if (processMemoryAllocatedList.size() == 0){
            // 如果内存分配表没有进程，检查申请的内存
            if (memorySize-8 >= size){
                return 0;
            }
            return -1;
        }
        for (int i = 0; i< processMemoryAllocatedList.size(); i++){
            mem = processMemoryAllocatedList.get(i);
            if (i == 0){
                // 如果是分配表中第一个进程
                if (mem.getStartAddr()-8 >= size)
                    return i;
            }
            if (mem.isNext()){
                // 判断分配表当前项是否有下一项
                nextMem = processMemoryAllocatedList.get(i+1);
                if (nextMem.getStartAddr() - mem.getEndAddr() > size)
                    // 下一进程其实地址和当前结束地址的大小
                    return i+1;
                else continue;
            }else {
                // 没有下一项，当前是分配表最后一项，检查内存大小与当前进程结束地址的差
                if (memorySize - mem.getEndAddr() > size)
                    return i+1;
                // 分配失败
                else return -1;
            }
        }
        return -1;
    }


    // CPU调用方法回收内存
    // 传入进程控制块
    public static void memoryRecycle(PCB pcb){
        controller = MemoryManagementWindow.getController();

        ProcessMemoryBlock processMemory = map.get(pcb);
        System.out.println(pcb);
        int index = processMemoryAllocatedList.indexOf(processMemory);
        if (index != 0){
            if (!processMemory.isNext())
                processMemoryAllocatedList.get(index-1).setNext(false);
        }
        processMemoryAllocatedList.remove(processMemory);
        for (int i=processMemory.getStartAddr(); i<=processMemory.getEndAddr(); i++){
            memory[i] = 0;
        }
        pcbMemory.remove(pcb);

        // 更新界面显示
        if (controller != null)
            controller.updateMessage(processMemoryAllocatedList);
    }

    public static void show(){
        for (int i=0; i<memory.length; i++){
            System.out.print(memory[i]);
            if ((i+1)%100 == 0)
                System.out.println("\n");
        }
    }

    // 返回内存分配表
    public static ArrayList<ProcessMemoryBlock> getMemoryUsage(){
        return processMemoryAllocatedList;
    }

    // 内存存值
    public static void storeValue(int address, int value){
        if (address>=0 && address<=7)
            memory[address] = value;
        else System.out.println("地址越界");
    }

    // 取值
    public static int getValue(int address){
        return memory[address];
    }
}
