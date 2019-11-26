package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CPU;
import view.processManagement.ProcessManagementController;
import view.processManagement.ProcessManagementWindow;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private static ProcessManagementController processManagementController;

    @Override
    public void start(Stage primaryStage) {
        CPU.work();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("操作系统");

        initRootLayout();

        // 全屏
        primaryStage.setMaximized(true);
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            rootLayout = (AnchorPane) FXMLLoader.load(MainApp.class.getResource("home/home.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ProcessManagementController getProcessManagementController() {
        return processManagementController;
    }

    public static void setProcessManagementController(ProcessManagementController processManagementController) {
        MainApp.processManagementController = processManagementController;
    }
}
