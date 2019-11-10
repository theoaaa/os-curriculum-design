package instruction.service;

import instruction.bean.*;

/**
 * @author Rorke
 * @date 2019/11/10 10:35
 * 用于模拟可执行文件的创建指令
 */
public class InstrService {
    private static InstrService instrService = new InstrService();
    private AsnInstr assignInstr = new AsnInstr();
    private CalInstr calculateInstr = new CalInstr();
    private EndInstr endInstr = new EndInstr();
    private EqpInstr eqpInstr = new EqpInstr();
    private MTRInstr memToRegInstr = new MTRInstr();
    private RTMInstr regToMemInstr = new RTMInstr();

    private InstrService() {
    }

    /**
     * 单例模式
     *
     * @return instrService
     */
    public static InstrService getInstrService() {
        if (instrService != null) {
            instrService = new InstrService();
        }
        return instrService;
    }

    /**
     * 创建终止指令
     * @return 8位的终止指令
     */
    public String createEndInstr() {
        return endInstr.getEndInstr();
    }

    /**
     * 创建设备控制指令
     * @param eqpNum 设备号
     * @param time 设备运行时间
     * @return 8位的设备指令
     */
    public String createEqpInstr(int eqpNum, int time) {
        return eqpInstr.getEqpInstr(eqpNum, time);
    }

    /**
     * 创建赋值指令
     * @param regNum 寄存器号
     * @param value  数值
     * @return 16位的赋值指令
     */
    public String createAsnInstr(int regNum, int value) {
        return assignInstr.getAsnInstr(regNum, value, 0);
    }

    /**
     * 创建循环指令
     * @param control   接下来control条指令进行循环
     * @param loopTimes 循环次数
     * @return 16位的赋值指令
     */
    public String createForInstr(int control, int loopTimes) {
        return assignInstr.getAsnInstr(2, loopTimes, control);
    }

    /**
     * 创建两个常量相加的运算指令
     * @param regA  寄存器编号A
     * @param regB  寄存器编号B
     * @param valueA 值A
     * @param valueB 值B
     * @param signed 符号，0为+ 1为-
     * @return 2条赋值指令+1条运算指令
     */
    public String createRCRInstr(int regA, int regB, int valueA, int valueB, int signed) {
        return assignInstr.getAsnInstr(regA, valueA, 0) +
                assignInstr.getAsnInstr(regB, valueB, 0) +
                calculateInstr.getCalInstr(signed, regA, regB);
    }

    /**
     * 创建内存中的变量加一个常量的运算指令，最终结果存回内存地址中
     * @param memAddr 内存的地址
     * @param regA    寄存器编号A，存储内存地址中的值
     * @param regB    寄存器编号B，存放常量
     * @param valueB  值B，常量
     * @param signed  符号，0为+ 1为-
     * @return 1条取值指令+1条赋值指令+1条运算指令+1条存值指令
     */
    public String createMCRInstr(int memAddr, int regA, int regB, int valueB, int signed) {
        return regToMemInstr.getRTMInstr(regA, memAddr) +
                assignInstr.getAsnInstr(regB, valueB, 0) +
                calculateInstr.getCalInstr(signed, regA, regB) +
                memToRegInstr.getMTRInstr(regA, memAddr);
    }

    /**
     * 创建内存中的变量A和B的运算指令，最终结果存回内存的A地址中
     * @param memAddrA 内存地址A
     * @param regA     寄存器编号A
     * @param memAddrB 内存地址B
     * @param regB     寄存器编号B
     * @param signed   符号，0为+ 1为-
     * @return 2条取值指令+1条运算指令+1条存值指令
     */
    public String createMCMInstr(int memAddrA, int regA, int memAddrB, int regB, int signed) {
        return memToRegInstr.getMTRInstr(regA, memAddrA) +
                memToRegInstr.getMTRInstr(regB, memAddrB) +
                calculateInstr.getCalInstr(signed, regA, regB) +
                memToRegInstr.getMTRInstr(regA, memAddrA);
    }
}
//push
