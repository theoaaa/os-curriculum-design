package model.device;

public class DeviceB extends Device {

    private String deviceType;

    public DeviceB(){
        super();
        deviceType = "B";
    }

    public String getDeviceType(){
        return deviceType;
    }

    @Override
    public String toString(){
        return "B";
    }
}
