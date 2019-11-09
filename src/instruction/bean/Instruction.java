package instruction.bean;

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

    void instructionsAppend() {
        buffer.append("\n");
    }
}
