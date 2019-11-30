package view.memoryManagement;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import model.memoryManage.MemoryManage;
import model.memoryManage.ProcessMemoryBlock;
import model.processManege.PCB;

import java.util.ArrayList;
import java.util.List;

public class MemoryManagementController {
    @FXML
    FlowPane flowPane;     // 用户区
    @FXML
    Pane pane;             // 系统区
    private  int[] memory = new int[512];           // 内存使用情况
    private  String[] color = new String[10];
    private  String buttonName = "";                // 占用进程名称
    private  String buttonBg = "";                  // 内存背景颜色
    private  List<ProcessMemoryBlock> processMemoryAllocatedList = new ArrayList<>();
    private  int processCount = 0;                  // 进程个数

    public  void initialize() {
        // 测试数据
//        PCB pcb = new PCB();
//        ProcessMemoryBlock p1 = new ProcessMemoryBlock(512,12,38,pcb,true,26/512.0);
//        ProcessMemoryBlock p2 = new ProcessMemoryBlock(512,65,127,pcb,true,(127-65)/512.0);
//        ProcessMemoryBlock p3 = new ProcessMemoryBlock(512,256,312,pcb,false,(312-256)/512.0);
//        processMemoryAllocatedList.add(p1);
//        processMemoryAllocatedList.add(p2);
//        processMemoryAllocatedList.add(p3);

        processMemoryAllocatedList = MemoryManage.getMemoryUsage();
        color[0] = "#00FFFF";
        color[1] = "#FF0000";
        color[2] = "#FFD700";
        color[3] = "#9932CC";
        color[4] = "#00BFFF";
        color[5] = "#00FFFF";
        color[6] = "#FF0000";
        color[7] = "#FFD700";
        color[8] = "#9932CC";
        color[9] = "#00BFFF";
        updateMessage(processMemoryAllocatedList);
    }

    // 初始/更新数据
    public void updateMessage(List<ProcessMemoryBlock> processMemoryAllocatedList){
        this.processMemoryAllocatedList = processMemoryAllocatedList;
        // 将ArrayList中的信息初始化过来
        int padding = 10;            //不同进程间的纵向间距
        for(int i=0;i<512;i++)
            memory[i] = 0;
        for(int i=0;i<processMemoryAllocatedList.size();i++) {
            ProcessMemoryBlock PMB = processMemoryAllocatedList.get(i);
            for(int j=PMB.getStartAddr();j<=PMB.getEndAddr();j++){
                memory[j] = i+1;                   // 将内存使用情况初始化到数组中
            }
            //  将进程信息显示到面板中
            Pane paneChildren = new Pane();
            Label name = new Label(PMB.getPcb().getProcessID()+"");
            paneChildren.getChildren().add(name);
            name.setLayoutX(15);
            name.setStyle(String.format("-fx-font-size: %dpx;", 15));
            Label startAddr = new Label(PMB.getStartAddr()+"");
            paneChildren.getChildren().add(startAddr);
            startAddr.setLayoutX(140);
            startAddr.setStyle(String.format("-fx-font-size: %dpx;", 15));
            Label endAddr = new Label(PMB.getEndAddr()+"");
            paneChildren.getChildren().add(endAddr);
            endAddr.setLayoutX(250);
            endAddr.setStyle(String.format("-fx-font-size: %dpx;", 15));
            Label takeUpRatio = new Label(String.format("%.2f",PMB.getTakeupRatio())+"%");
            paneChildren.getChildren().add(takeUpRatio);
            takeUpRatio.setLayoutX(360);
            takeUpRatio.setStyle(String.format("-fx-font-size: %dpx;", 15));
            pane.getChildren().add(paneChildren);
            paneChildren.setLayoutY(padding);
            padding += 40;
        }
        int colorIndex = 0;                        // 进程背景颜色下标
        String white = "#FFFFFF";                  // 空闲状态下为白色
        //给内存区设置占用情况
        for (int i = 0; i < 512; i++) {
            if (memory[i] == 0)                                 //  0 表示空闲区
                buttonBg = white;
            else {
                if (i >= 1 && memory[i] != memory[i - 1])      //   不同进程改变颜色
                    colorIndex = (colorIndex + 1) % 10;
                buttonBg = color[colorIndex];
            }
            Button btn = new Button(buttonName);
            btn.setMaxHeight(20);
            btn.setMinHeight(20);
            btn.setMaxWidth(100);
            btn.setMinWidth(100);
            btn.setStyle(String.format("-fx-font-size: %dpx;", 7));
            BackgroundFill bgf = new BackgroundFill(Paint.valueOf(buttonBg), new CornerRadii(0), new Insets(0));
            Background bg = new Background(bgf);
            btn.setBackground(bg);
            BorderStroke bos = new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(0.2));
            Border bo = new Border(bos);
            btn.setBorder(bo);
            flowPane.getChildren().add(btn);
        }
    }

    public void setProcessMemoryAllocatedList(List<ProcessMemoryBlock> processMemoryAllocatedList) {
        this.processMemoryAllocatedList = processMemoryAllocatedList;
    }
}
