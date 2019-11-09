package instruction.bean;

import instruction.util.InstrUtils;

/**
 * @author Rorke
 * @date 2019/11/5 15:23
 * 设备调度指令，结构001-eqpNum-time
 * eqpNum（设备号，2位） time（设备运行事件，3位）
 */
public class EqpInstr extends Instruction {
    private String equipmentNum;

    public EqpInstr() {
        this.prefix = "001";
    }

    /**
     * @param equipmentNum 设备号，根据题目意思是0-2
     * @param time         设备运行的时间 输入的范围是1-8 转化为指令内容为0-7
     * @return 返回8位指令
     */
    public String getEqpInstr(int equipmentNum, int time) {
        buffer = new StringBuffer();
        this.equipmentNum = InstrUtils.decToBinary(equipmentNum, 2);
        this.value = InstrUtils.decToBinary(time, 3);
        instructionsAppend();
        return String.valueOf(buffer);
    }

    @Override
    protected void instructionsAppend() {
        buffer.append(prefix);
        buffer.append(equipmentNum);
        buffer.append(value);
        super.instructionsAppend();
    }
}
