package instruction.bean;

/**
 * @author Rorke
 * @date 2019/11/9 17:12
 * 取值指令，将内存地址中的值取到寄存器中，结构011-reg-mem
 * 其余说明与存值指令相同
 */
public class RTMInstr extends MTRInstr {
    public RTMInstr() {
        this.prefix = "011";
    }

    public String getRTMInstr(int regNum, int memAddr) {
        super.getMTRInstr(regNum, memAddr);
        return String.valueOf(buffer);
    }
}
