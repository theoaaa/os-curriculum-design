package model.deviceManage;

import javafx.application.Platform;
import model.processManege.CPU;
import model.processManege.PCB;
import model.device.Device;
import model.device.DeviceA;
import model.device.DeviceB;
import model.device.DeviceC;
import model.processManege.ProcessControl;
import view.deviceManagement.DeviceManagementController;
import view.deviceManagement.DeviceManagementWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 设备分配
 */
public class DeviceAllocation {

    // 设备列表
    private static final ArrayList<Device> deviceList = new ArrayList<>(8);

    // 设备等待使用进程队列
    public static final ArrayList<PCB> aWaitingList = new ArrayList<>();
    public static final ArrayList<PCB> bWaitingList = new ArrayList<>();
    public static final ArrayList<PCB> cWaitingList = new ArrayList<>();

    // 可用的空闲设备数量
    private static int freeNumOfDeviceA = 2;
    private static int freeNumOfDeviceB = 3;
    private static int freeNumOfDeviceC = 3;

    // 各类设备的数量
    private static final int countA = 2;
    private static final int countB = 3;
    private static final int countC = 3;

    // PCB与设备映射
    private static Map<PCB, Device> map = new HashMap<>();
    // 分配了设备的进程列表
    private static ArrayList<PCB> procList = new ArrayList<>();
    private static DeviceManagementController controller;
    private static ExecutorService ThreadPool = Executors.newCachedThreadPool();


    // 初始化各类设备
    static {
        deviceList.add(new DeviceA());
        deviceList.add(new DeviceA());
        deviceList.add(new DeviceB());
        deviceList.add(new DeviceB());
        deviceList.add(new DeviceB());
        deviceList.add(new DeviceC());
        deviceList.add(new DeviceC());
        deviceList.add(new DeviceC());
    }

    // CPU调用此方法申请分配设备
    // 传入进程控制块， 设备类型（A、B、C）， 占用时间
    public static boolean allocate(PCB proc, String deviceType, int useTime) {
        controller = DeviceManagementWindow.getController();
        switch (deviceType) {
            case "A":
                if (freeNumOfDeviceA < 1) {
                    // 进程加入等待队列，进程阻塞
                    aWaitingList.add(proc);
                    update();
                    return false;
                }
                for (int i = 0; i < countA; i++) {
                    if (!deviceList.get(i).isAllocated) {
                        // 对应设备空闲数量减1
                        freeNumOfDeviceA--;
                        // 分配设备
                        allocateDevice(deviceList.get(i), proc, useTime);
                        //ioInterrupt(proc, useTime);
                        return true;
                    }
                }
                return false;
            case "B":
                if (freeNumOfDeviceB < 1) {
                    // 设备B没有空闲设备
                    bWaitingList.add(proc);
                    update();
                    return false;
                }
                for (int i = countA; i < countB + countA; i++) {
                    if (!deviceList.get(i).isAllocated) {
                        allocateDevice(deviceList.get(i), proc, useTime);
                        //System.out.println("分配B");
                        freeNumOfDeviceB--;
                        //ioInterrupt(proc, useTime);
                        return true;
                    }
                }
                return false;
            case "C":
                if (freeNumOfDeviceC < 1) {
                    cWaitingList.add(proc);
                    update();
                    return false;
                }
                for (int i = countA + countB; i < deviceList.size(); i++) {
                    if (!deviceList.get(i).isAllocated) {
                        freeNumOfDeviceC--;
                        allocateDevice(deviceList.get(i), proc, useTime);
                        //ioInterrupt(proc, useTime);
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    // 分配设备，设置设备各类参数
    private static void allocateDevice(Device device, PCB pcb, int useTime) {
        device.setPcb(pcb);
        device.setRemainTime(useTime);
        device.setAllocated(true);
        map.put(pcb, device);
        procList.add(pcb);
        // 更新界面显示
        update();
        ioInterrupt(pcb,useTime);
    }

    // 线程中断，模拟设备使用期间IO
    private static void ioInterrupt(PCB pcb, int remainTime) {
        CPU.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 休眠对应占用时间
                    Thread.sleep(remainTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deviceRecycle(pcb);
            }
        });
    }

    // CPU调用方法回收
    // 传入进程控制块
    private static void deviceRecycle(PCB pcb) {
        controller = DeviceManagementWindow.getController();

        // 恢复设备参数
        Device device = map.get(pcb);
        device.setPcb(null);
        device.setRemainTime(0);
        device.setAllocated(false);


        // 唤醒进程进入就绪队列
        ProcessControl.awake(pcb);
        switch (device.toString()) {
            case "A":
                freeNumOfDeviceA++;
                if (!aWaitingList.isEmpty()) {
                    // 若有等待进程，等待队列第一个进程分配设备
                    allocate(aWaitingList.get(0), "A", 6000);
                    // 将第一个进程从等待队列移除
                    aWaitingList.remove(0);
                }
                break;
            case "B":
                freeNumOfDeviceB++;
                if (!bWaitingList.isEmpty()) {
                    allocate(bWaitingList.get(0), "B", 6000);
                    bWaitingList.remove(0);
                }
                break;
            case "C":
                freeNumOfDeviceC++;
                if (!cWaitingList.isEmpty()) {
                    allocate(cWaitingList.get(0), "C", 6000);
                    cWaitingList.remove(0);
                }
                break;
        }
        // 更新界面显示
        update();
    }

    private static void update(){
        if (controller!=null){
            final CountDownLatch doneLatch = new CountDownLatch(1);
            Platform.runLater(() ->{
                try {
                    controller.updateMessage();
                    controller.updateWaiting();
                }finally {
                    doneLatch.countDown();
                }
            });
            try {
                doneLatch.await();
            } catch (InterruptedException e) {
                // ignore exception
            }
        }
    }

    // 返回设备使用列表
    public static ArrayList<Device> getDeviceUsage() {
        return deviceList;
    }

    // 返回ABC设备等待进程队列
    public static ArrayList<PCB> getAWaitingList() {
        return aWaitingList;
    }

    public static ArrayList<PCB> getBWaitingList() {
        return bWaitingList;
    }

    public static ArrayList<PCB> getCWaitingList() {
        return cWaitingList;
    }
}