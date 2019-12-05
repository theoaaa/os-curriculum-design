package view.deviceManagement;

import javafx.application.Platform;
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
    private static StringBuilder context1 = new StringBuilder();
    private static StringBuilder context2 = new StringBuilder();
    private static StringBuilder context3 = new StringBuilder();
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
        updateWaiting();
    }

    // 初始化/更新数据
    public void updateMessage(){

        Platform.runLater(()->{
            // 显示正在使用的设备
            if(deviceList.size() >= 7){
                if (deviceList.get(0).getPcb() != null){
                    if (deviceList.get(0).getPcb().getProcessID()!=null)
                        A01.setText(deviceList.get(0).getPcb().getProcessID()+" ");
                    else A01.setText("");
                }
                else
                    A01.setText("");
                if (deviceList.get(1).getPcb() != null) {
                    if (deviceList.get(1).getPcb().getProcessID() != null)
                        A02.setText(deviceList.get(1).getPcb().getProcessID() + " ");
                    else A02.setText("");
                }
                else
                    A02.setText("");
                if (deviceList.get(2).getPcb() != null)
                    B01.setText(deviceList.get(2).getPcb().getProcessID()+" ");
                else
                    B01.setText("");
                if (deviceList.get(3).getPcb() != null)
                    B02.setText(deviceList.get(3).getPcb().getProcessID()+" ");
                else
                    B02.setText("");
                if (deviceList.get(4).getPcb() != null)
                    B03.setText(deviceList.get(4).getPcb().getProcessID()+" ");
                else
                    B03.setText("");
                if (deviceList.get(5).getPcb() != null)
                    C01.setText(deviceList.get(5).getPcb().getProcessID()+" ");
                else
                    C01.setText("");
                if (deviceList.get(6).getPcb() != null)
                    C02.setText(deviceList.get(6).getPcb().getProcessID()+" ");
                else
                    C02.setText("");
                if (deviceList.get(7).getPcb() != null)
                    C03.setText(deviceList.get(7).getPcb().getProcessID()+" ");
                else
                    C03.setText("");
            }
        });
    }

    public void updateWaiting(){
        // 显示等待队列中的进程
        if (context1 != null)
            context1.setLength(0);
        for(int i=0;i<aWaitingList.size();i++) {
            System.out.println("大小："+aWaitingList.size());
            PCB p = aWaitingList.get(i);
            context1.append(p.getProcessID() + "      ");
        }
        AWaitingList.setText(context1.toString());
        if (context2 != null)
            context2.setLength(0);
        for(int i=0;i<bWaitingList.size();i++) {
            PCB p = bWaitingList.get(i);
            context2.append(p.getProcessID() + "      ");
        }
        BWaitingList.setText(context2.toString());
        if (context3 != null)
            context3.setLength(0);
        for(int i=0;i<cWaitingList.size();i++) {
            PCB p = cWaitingList.get(i);
            context3.append(p.getProcessID() + "      ");
        }
        CWaitingList.setText(context3.toString());
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
