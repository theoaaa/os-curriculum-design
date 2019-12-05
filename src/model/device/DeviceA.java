package model.device;

public class DeviceA extends Device {

    private String deviceType;

    public DeviceA(){
        super();
        deviceType = "A";
    }

    @Override
    public String toString(){
        return "A";
    }

    public String getDevieType(){
        return deviceType;
    }

}
