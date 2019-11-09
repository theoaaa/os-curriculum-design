package instruction.bean;

import instruction.util.InstrUtils;

/**
 * @author Rorke
 * @date 2019/11/9 17:12
 * 存值指令，将寄存器的值存入内存中 结构010-reg-mem
 * reg（寄存器编号，2位） mem（内存地址，3位）
 */
public class MTRInstr extends Instruction {
    public MTRInstr() {
        this.prefix = "010";
    }

    /**
     * @param regNum  寄存器编号，2位，范围0-3
     * @param memAddr 内存地址，3位，范围0-7
     * @return 返回八位指令
     */
    public String getMTRInstr(int regNum, int memAddr) {
        buffer = new StringBuffer();
        this.register = InstrUtils.decToBinary(regNum, 2);
        this.memory = InstrUtils.decToBinary(memAddr, 3);
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        buffer.append(register);
        buffer.append(memory);
        super.instructionsAppend();
    }
}
