package instruction.bean;


/**
 * @author Rorke
 * @date 2019/11/5 15:22
 * 赋值指令 所有指令中唯一一条双字节的指令 用于将值赋给寄存器
 * 结构 100-reg-value，其中reg（寄存器编号，2位）
 * value（值，3位）
 */
public class AsnInstr extends Instruction {
    public AsnInstr() {
        this.prefix = "100";
    }

    /**
     * @param regNum  寄存器编号，0-3分别对应不同的寄存器
     * @param value   希望给寄存器的值
     * @return 返回8位指令
     */
    public String getAsnInstr(int regNum, int value) {
        buffer = new StringBuffer();
        this.register = instrUtils.decToBinary(regNum, 2);
        this.value = instrUtils.decToBinary(value, 3);
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        buffer.append(register);
        buffer.append(value);
        super.instructionsAppend();
    }
}
