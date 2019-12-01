package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.processManege.CPU;
import view.home.HomeController;
import view.processManagement.ProcessManagementController;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private AnchorPane rootLayout;
    private static ProcessManagementController processManagementController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("操作系统");

        initRootLayout();
        CPU.work();

        // 全屏
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                CPU.shutdown();
            }
        });
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("home/home.fxml"));
            rootLayout = (AnchorPane) loader.load();
            HomeController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

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
