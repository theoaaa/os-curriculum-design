package instruction.bean;

import instruction.util.InstrUtils;

/**
 * @author Rorke
 * @date 2019/11/6 22:15
 */
abstract class Instruction {
    String prefix;
    String register;
    String memory;
    String value;
    StringBuffer buffer;
    InstrUtils instrUtils = new InstrUtils();
    void instructionsAppend() {
        buffer.append("\n");
    }
}
