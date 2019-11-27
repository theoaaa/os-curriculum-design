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
    private ArrayList<Device> deviceList = new ArrayList<>(8);

    // 设备等待使用进程队列
    private ArrayList<PCB> aWaitingList = new ArrayList<>();
    private ArrayList<PCB> bWaitingList = new ArrayList<>();
    private ArrayList<PCB> cWaitingList = new ArrayList<>();
    @FXML Text A01;
    @FXML Text A02;
    @FXML Text B01;
    @FXML Text B02;
    @FXML Text B03;
    @FXML Text C01;
    @FXML Text C02;
    @FXML Text C03;
    @FXML TextField AWaitingList;
    @FXML TextField BWaitingList;
    @FXML TextField CWaitingList;
    public void initialize(){
        aWaitingList = DeviceAllocation.getAWaitingList();
        bWaitingList = DeviceAllocation.getBWaitingList();
        cWaitingList = DeviceAllocation.getCWaitingList();
        deviceList = DeviceAllocation.getDeviceUsage();
        // 显示正在使用的设备
        /*A01.setText(deviceList.get(0).getPcb().getProcessID());
        A02.setText(deviceList.get(1).getPcb().getProcessID());
        B01.setText(deviceList.get(2).getPcb().getProcessID());
        B02.setText(deviceList.get(3).getPcb().getProcessID());
        B03.setText(deviceList.get(4).getPcb().getProcessID());
        C01.setText(deviceList.get(5).getPcb().getProcessID());
        C02.setText(deviceList.get(6).getPcb().getProcessID());
        C03.setText(deviceList.get(7).getPcb().getProcessID());*/
        // 显示等待队列中的进程
        /*String context = "";
        for(int i=0;i<aWaitingList.size();i++) {
            ProcessControllBlock p = aWaitingList.get(i);
            context += p.getProcessID();
        }
        AWaitingList.setText(context);
        context = "";
        for(int i=0;i<bWaitingList.size();i++) {
            ProcessControllBlock p = bWaitingList.get(i);
            context += p.getProcessID();
        }
        BWaitingList.setText(context);
        context = "";
        for(int i=0;i<cWaitingList.size();i++) {
            ProcessControllBlock p = cWaitingList.get(i);
            context += p.getProcessID();
        }
        CWaitingList.setText(context);*/
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

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }
}
