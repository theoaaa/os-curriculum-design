package model.device;

public class DeviceC extends Device {

    private String deviceType;

    public DeviceC(){
        super();
        deviceType = "C";
    }

    public String getDeviceType(){
        return deviceType;
    }

    @Override
    public String toString(){
        return "C";
    }

}
