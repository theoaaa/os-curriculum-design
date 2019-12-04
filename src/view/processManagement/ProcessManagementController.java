package view.processManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.processManege.CPU;
import model.processManege.PCB;

public class ProcessManagementController {
    @FXML
    private Label systemTime;

    @FXML
    private Label processId;

    @FXML
    private Label implementingCommand;

    @FXML
    private Label implementResolve;

    @FXML
    private Label remainTimePart;

    @FXML
    private ListView readyListView;

    @FXML
    private ListView blockingListView;

    private static ObservableList<String> readyList = FXCollections.observableArrayList();

    private static ObservableList<String> blockingList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 设置列表视图的列表项
        readyListView.setItems(readyList);
        blockingListView.setItems(blockingList);

    }

    /**
     * 更新界面数据
     *
     * @param pcb
     */
    public void updateData(PCB pcb) {
        systemTime.setText(CPU.getSystemTime() + "");
        readyList.clear();
        for (PCB readyPCB : PCB.getReadyProcessPCBList()) {
            if (readyPCB.getProcessID() != null)
                readyList.add("进程ID:"+readyPCB.getProcessID().toString());
        }

        blockingList.clear();
        for (PCB blockingPCB : PCB.getBlockedProcessPCBList()) {
            if (blockingPCB.getProcessID() != null)
                blockingList.add("进程ID:"+blockingPCB.getProcessID().toString() + "；已经等待：" + blockingPCB.getBlockedTime());
        }

        if (pcb == null) {
            implementingCommand.setText("");
            implementResolve.setText("");
            processId.setText("无进程");
            remainTimePart.setText("");
            return;
        }

        implementResolve.setText(pcb.getIntermediateResult());

        implementingCommand.setText(pcb.getCurrentInstruction());

        processId.setText(pcb.getProcessID().toString());

        remainTimePart.setText(pcb.getRestTime() + "");
    }

}
