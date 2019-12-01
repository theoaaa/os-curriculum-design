package view.deviceManagement;

import model.processManege.PCB;
import model.device.Device;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.deviceManage.DeviceAllocation;

import java.util.ArrayList;

public class DeviceManagementController {

    // 设备列表
    private static ArrayList<Device> deviceList = new ArrayList<>(8);

    // 设备等待使用进程队列
    private static ArrayList<PCB> aWaitingList = new ArrayList<>();
    private static ArrayList<PCB> bWaitingList = new ArrayList<>();
    private static ArrayList<PCB> cWaitingList = new ArrayList<>();
    @FXML TextField A01;
    @FXML TextField A02;
    @FXML TextField B01;
    @FXML TextField B02;
    @FXML TextField B03;
    @FXML TextField C01;
    @FXML TextField C02;
    @FXML TextField C03;
    @FXML TextField AWaitingList;
    @FXML TextField BWaitingList;
    @FXML TextField CWaitingList;
    public void initialize(){
        aWaitingList = DeviceAllocation.getAWaitingList();
        bWaitingList = DeviceAllocation.getBWaitingList();
        cWaitingList = DeviceAllocation.getCWaitingList();
        deviceList = DeviceAllocation.getDeviceUsage();
        updateMessage();
    }

    // 初始化/更新数据
    public void updateMessage(){

        // 显示正在使用的设备
        if(deviceList.size() >= 7){
            A01.setText(deviceList.get(0).getPcb().getProcessID()+"");
            A02.setText(deviceList.get(1).getPcb().getProcessID()+"");
            B01.setText(deviceList.get(2).getPcb().getProcessID()+"");
            B02.setText(deviceList.get(3).getPcb().getProcessID()+"");
            B03.setText(deviceList.get(4).getPcb().getProcessID()+"");
            C01.setText(deviceList.get(5).getPcb().getProcessID()+"");
            C02.setText(deviceList.get(6).getPcb().getProcessID()+"");
            C03.setText(deviceList.get(7).getPcb().getProcessID()+"");
        }
        // 显示等待队列中的进程
        String context = "";
        for(int i=0;i<aWaitingList.size();i++) {
            PCB p = aWaitingList.get(i);
            context += p.getProcessID();
        }
        AWaitingList.setText(context);
        context = "";
        for(int i=0;i<bWaitingList.size();i++) {
            PCB p = bWaitingList.get(i);
            context += p.getProcessID();
        }
        BWaitingList.setText(context);
        context = "";
        for(int i=0;i<cWaitingList.size();i++) {
            PCB p = cWaitingList.get(i);
            context += p.getProcessID();
        }
        CWaitingList.setText(context);
    }
    public void setAWaitingList(ArrayList<PCB> aWaitingList) {
        this.aWaitingList = aWaitingList;
    }

    public void setBWaitingList(ArrayList<PCB> bWaitingList) {
        this.bWaitingList = bWaitingList;
    }

    public void setCWaitingList(ArrayList<PCB> cWaitingList) {
        this.cWaitingList = cWaitingList;
    }

    public void setDeviceList(ArrayList<Device> deviceList) { this.deviceList = deviceList; }
}