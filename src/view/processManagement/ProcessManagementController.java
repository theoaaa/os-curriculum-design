package view.processManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

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

    private ObservableList<String> readyList = FXCollections.observableArrayList();

    private ObservableList<String> blockingList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 设置列表视图的列表项
        readyListView.setItems(readyList);
        blockingListView.setItems(blockingList);

        systemTime.setText("00:00");

        processId.setText("0000");
        implementingCommand.setText("add x,y");
        implementResolve.setText("4");
        remainTimePart.setText("2");
    }

    public void updateData(String processId) {
        readyList.add(processId);
    }
}
