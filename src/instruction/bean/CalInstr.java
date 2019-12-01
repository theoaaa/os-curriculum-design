package instruction.bean;


/**
 * @author Rorke
 * @date 2019/11/5 15:22
 * 运算指令 结构 110-regA-sign-regB，regA、regB（寄存器编号，两位） sign（符号，1位）
 */
public class CalInstr extends Instruction {
    private String signed, regA, regB;

    public CalInstr() {
        this.prefix = "110";
    }

    /**
     * @param signed 符号，0表示加法 1表示减法
     * @param regA   目标寄存器，储存a+b和a-b中的a以及他们的结果
     * @param regB   辅助寄存器，储存b
     * @return 返回8位的指令
     */
    public String getCalInstr(int signed, int regA, int regB) {
        buffer = new StringBuffer();
        this.signed = instrUtils.decToBinary(signed, 1);
        this.regA = instrUtils.decToBinary(regA, 2);
        this.regB = instrUtils.decToBinary(regB, 2);
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        buffer.append(regA);
        buffer.append(signed);
        buffer.append(regB);
        super.instructionsAppend();
    }
}
