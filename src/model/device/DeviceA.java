package model.device;

public class DeviceA extends Device {

    private String devieType;

    public DeviceA(){
        super();
        super.isAllocated = false;
        devieType = "A";
    }

    @Override
    public String toString(){
        return "A";
    }

    public String getDevieType(){
        return devieType;
    }

}
