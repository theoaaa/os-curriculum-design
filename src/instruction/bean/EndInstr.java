package instruction.bean;

/**
 * @author Rorke
 * @date 2019/11/5 15:23
 * 结束指令
 */
public class EndInstr extends Instruction {
    public EndInstr() {
        this.prefix = "00000000";
    }

    public String getEndInstr() {
        buffer = new StringBuffer();
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        super.instructionsAppend();
    }
}
