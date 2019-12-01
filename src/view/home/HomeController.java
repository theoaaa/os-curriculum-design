package view.home;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.processManege.CPU;
import view.DiskManagementWindow;
import view.deviceManagement.DeviceManagementWindow;
import view.memoryManagement.MemoryManagementWindow;
import view.processManagement.ProcessManagementWindow;

import javax.swing.text.html.ImageView;

public class HomeController {

    @FXML private VBox userInterfaceBox;

    @FXML private VBox fileManagementBox;

    @FXML private VBox deviceManagementBox;

    @FXML private VBox storageManagementBox;

    @FXML private VBox processManagementBox;

    private Stage primaryStage;

    public void initialize() {
        setIconActive(userInterfaceBox);
        setIconActive(fileManagementBox);
        setIconActive(deviceManagementBox);
        setIconActive(storageManagementBox);
        setIconActive(processManagementBox);
    }

    /**
     * 设置图标鼠标经过状态
     * @param iconBox
     */
    private void setIconActive(VBox iconBox) {
        iconBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                iconBox.setStyle("-fx-background-color: #e6e6e6; -fx-opacity: 0.8;");
            }
        });

        iconBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                iconBox.setStyle("");
            }
        });
    }

    @FXML
    private void openUserInterface() {

    }

    @FXML
    private void openFileManagement() {
        DiskManagementWindow window = DiskManagementWindow.getInstance();
        window.show();
    }

    @FXML
    private void openDeviceManagement() {
        DeviceManagementWindow window = DeviceManagementWindow.getInstance();
        window.show();
    }

    @FXML
    private void openStorageManagement() {
        MemoryManagementWindow window = MemoryManagementWindow.getInstance();
        window.show();
    }

    @FXML
    private void openProcessManagement() {
        ProcessManagementWindow window = ProcessManagementWindow.getInstance();
        window.show();
    }

    @FXML
    private void exit() {
        primaryStage.close();
        CPU.shutdown();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
