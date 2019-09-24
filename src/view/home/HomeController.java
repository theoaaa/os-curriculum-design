package view.home;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import view.processManagement.ProcessManagementWindow;

public class HomeController {
    @FXML
    private Button processManagementBtn;

    @FXML
    private void openProcessManagement() {
        ProcessManagementWindow window = new ProcessManagementWindow();

        try {
            window.start(new Stage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
