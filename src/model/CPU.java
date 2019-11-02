package model;

import util.StringUtil;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CPU {
    //时间片长度
    public static int timeSliceLength = 3;
    //申请使用设备时休眠时间
    public static int IO_BLOCK_TIME = 6000;
    //一条指令执行后休眠的时间（显示中间结果）
    public static int SLEEP_TIME_FOR_EACH_INSTRUCTMENT = 1000;

    //系统时间
    private static int systemTime = 0;
    //程序状态字
    private static PSW psw = PSW.getPSW();
    //寄存器
    private static Integer reg[] = new Integer[4];

    //线程池
    private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private CPU() {
        run();
    }

    // cpu执行进程调度
    public void run() {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    PCB pcb = getReadyProcessPCB();
                    if (pcb == null) {
                        System.out.println("就绪队列为空");
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    } else {
                        psw.initPSW();
                        while (!(psw.isProcessEnd() || psw.isIOInterrupt() || psw.isTimeSliceUsedUp())) {
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            //取指令并将指令指针 +1
                            String currentInstruction = pcb.getCurrentInstruction();
                            pcb.increaseCurrentInstructionIndex();

                            //执行并保存中间结果
                            executeInstruction(currentInstruction, pcb);

                            //剩余时间片减一，修改PSW
                            pcb.decreaseRestTime();
                            psw.setTimeSliceUsedUp(pcb.isTimeSliceUsedUp());
                            psw.setProcessEnd(pcb.isProcessEnd());

                            System.out.println(pcb);
                            //检测并处理异常
                            handleInterrupt(pcb);
                        }
                    }
                }
            }

            //调度就绪队列执行
            private PCB getReadyProcessPCB() {
                List<PCB> readyProcessPCBList = PCB.getReadyProcessPCBList();
                PCB pcb = null;
                if (!readyProcessPCBList.isEmpty()) {
                    pcb = readyProcessPCBList.get(0);
                    readyProcessPCBList.remove(0);

                    pcb.setProcessState(PCB.EXECUTING);
                    pcb.resetRestTime();
                    reg = pcb.readRegister();
                }
                return pcb;
            }

            //解析并执行指令
            private void executeInstruction(String instruction, PCB pcb) {
                ++CPU.systemTime;
                if (instruction.startsWith("000")) {
                    //执行end
                    pcb.setProcessState(PCB.END);
//                    System.out.println(pcb.getProcessState());
//                    System.out.println(pcb);
                    pcb.setIntermediateResult("程序执行结束");
                } else if (instruction.startsWith("001")) {
                    //执行x++
                    String i = instruction.substring(3, 5);
                    int regNum = StringUtil.parseBinaryToDecimal(Integer.parseInt(i));
                    reg[regNum]++;
                    pcb.setIntermediateResult("reg" + regNum + " = " + reg[regNum]);
                } else if (instruction.startsWith("010")) {
                    //执行x--
                    String i = instruction.substring(3, 5);
                    int regNum = StringUtil.parseBinaryToDecimal(Integer.parseInt(i));
                    reg[regNum]--;
                    pcb.setIntermediateResult("reg" + regNum + " = " + reg[regNum]);
                } else if (instruction.startsWith("100")) {
                    //申请控制设备
                    String i = instruction.substring(3, 5);
                    int equipmentNum = StringUtil.parseBinaryToDecimal(Integer.parseInt(i));
                    //发起申请设备信号
                    // *************
                    // *************
                    // *************
                    pcb.setIntermediateResult("进程" + pcb.getProcessID() + "申请设备" + equipmentNum);
                    psw.setIOInterrupt(true);
                } else if (instruction.startsWith("110")) {
                    //给x赋值
                    int regIndex = StringUtil.parseBinaryToDecimal(Integer.parseInt(instruction.substring(3, 5)));
                    int value = StringUtil.parseBinaryToDecimal(Integer.parseInt(instruction.substring(5)));
                    reg[regIndex] = value;
                    pcb.setIntermediateResult("reg" + regIndex + " = " + value);
                } else {
                    pcb.setIntermediateResult("指令错误！");
                }
            }

            //检测并处理中断, 若有中断则返回true
            private void handleInterrupt(PCB pcb) {
                if (psw.isProcessEnd()) {
                    ProcessControl.destory(pcb);
                } else if (psw.isIOInterrupt()) {
                    //执行IO操作，进入阻塞队列，IO_BLOCK_TIME时间后从阻塞队列回到就绪队列
                    ProcessControl.block(pcb, PCB.IO_INTERRUPT);
                    cachedThreadPool.submit(new Runnable() {
                        @Override
                        public void run() {
                            //进程休眠模拟执行IO操作
                            try {
                                Thread.sleep(IO_BLOCK_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            ProcessControl.awake(pcb);
                        }
                    });
                } else if (psw.isTimeSliceUsedUp()) {
                    pcb.resetRestTime();
                    PCB.getReadyProcessPCBList().add(pcb);
                }
            }
        });
    }

    public static ExecutorService getThreadPool() {
        return cachedThreadPool;
    }

    public static Integer[] getRegisters() {
        return reg;
    }

    public static void main(String[] args) {
        ProcessControl.create(new File("resources/test.e"));
        ProcessControl.create(new File("resources/test.e"));
        ProcessControl.create(new File("resources/test.e"));
        ProcessControl.create(new File("resources/test.e"));
        CPU cpu = new CPU();
    }
}
