package instruction.service;

import instruction.bean.*;

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

    public static InstrService getInstrService() {
        if (instrService != null) {
            instrService = new InstrService();
        }
        return instrService;
    }

    public String createEndInstr() {
        return endInstr.getEndInstr();
    }

    public String createEqpInstr(int eqpNum, int time) {
        return eqpInstr.getEqpInstr(eqpNum, time);
    }

    public String createAsnInstr(int regNum, int value) {
        return assignInstr.getAsnInstr(regNum, value, 0);
    }

    public String createForInstr(int control, int loopTimes) {
        return assignInstr.getAsnInstr(2, loopTimes, control);
    }

    public String createRCRInstr(int regA, int regB, int valueA, int valueB, int signed) {
        return assignInstr.getAsnInstr(regA, valueA, 0) +
                assignInstr.getAsnInstr(regB, valueB, 0) +
                calculateInstr.getCalInstr(signed, regA, regB);
    }

    public String createMCRInstr(int memAddr, int regA, int regB, int valueB, int signed) {
        return regToMemInstr.getRTMInstr(regA, memAddr) +
                assignInstr.getAsnInstr(regB, valueB, 0) +
                calculateInstr.getCalInstr(signed, regA, regB) +
                memToRegInstr.getMTRInstr(regA, memAddr);
    }

    public String createMCMInstr(int memAddrA, int regA, int memAddrB, int regB, int signed) {
        return memToRegInstr.getMTRInstr(regA, memAddrA) +
                memToRegInstr.getMTRInstr(regB, memAddrB) +
                calculateInstr.getCalInstr(signed, regA, regB) +
                memToRegInstr.getMTRInstr(regA, memAddrA);
    }
}
