package model.memoryManage;

public class ProcessControlBlock {

    private int blockID;
    private String blockName;

    public ProcessControlBlock(){

    }

    public ProcessControlBlock(String bolckName){
        this.blockName = bolckName;
    }

    @Override
    public String toString(){
        return blockName;
    }

}
