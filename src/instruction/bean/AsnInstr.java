package instruction.bean;


/**
 * @author Rorke
 * @date 2019/11/5 15:22
 * 赋值指令 所有指令中唯一一条双字节的指令 用于将值赋给寄存器
 * 结构 100-reg-control value，其中reg（寄存器编号，2位）
 * control（控制位，3位，000为简单的赋值语句，001为循环语句只能对专用的循环寄存器赋值） value（值，8位）
 */
public class AsnInstr extends Instruction {
    private String control;

    public AsnInstr() {
        this.prefix = "100";
    }

    /**
     * @param regNum  寄存器编号，0-3分别对应不同的寄存器
     * @param value   希望给寄存器的值
     * @param control 控制，000为赋值操作 001-111都为循环，对应的数值就是接下来多少条操作进行循环
     * @return 返回16位指令
     */
    public String getAsnInstr(int regNum, int value, int control) {
        buffer = new StringBuffer();
        this.register = instrUtils.decToBinary(regNum, 2);
        this.control = instrUtils.decToBinary(control, 3);
        this.value = instrUtils.decToBinary(value, 8);
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        buffer.append(register);
        buffer.append(control);
        super.instructionsAppend();
        buffer.append(value);
        super.instructionsAppend();
    }
}
