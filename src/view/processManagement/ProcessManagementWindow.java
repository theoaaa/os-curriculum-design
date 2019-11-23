package view.processManagement;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.MainApp;

public class ProcessManagementWindow extends Application {

    private static ProcessManagementWindow instance;

    private static ProcessManagementController controller = null;

    private Stage primaryStage; // 窗口 Stage 对象

    private Boolean isExisted = false; // 窗口是否已存在

    private Boolean isMinimized = false; // 窗口是否最小化

    private ProcessManagementWindow() {}

    public static ProcessManagementWindow getInstance() {
        if (instance == null)
            instance = new ProcessManagementWindow();
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(ProcessManagementWindow.class.getResource("page.fxml"));
        AnchorPane rootPane = loader.load();

        // 获取页面对应的 controller 实例
        controller = loader.getController();
        MainApp.setProcessManagementController(controller);

        Scene scene = new Scene(rootPane);

        this.primaryStage.setScene(scene);
        this.primaryStage.setTitle("进程调度");
        this.primaryStage.show();

        // 设置窗口始终位于顶部
        this.primaryStage.setAlwaysOnTop(true);
        isExisted = true;

        // 监听窗口关闭事件
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isExisted = false;
            }
        });

        // 绑定 isMinimized 值
        bindIsMinimized(scene);
    }

    /**
     * 绑定 isMinimized 值与当前窗口最小化状态
     * @param scene
     */
    private void bindIsMinimized(Scene scene) {

        // 监听窗口 最小化/最大化 事件；oldValue 和 newValue 均表示是否最大化
        scene.getWindow().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isMinimized =  !newValue;
            }
        });
    }

    /**
     * 显示窗口
     */
    public void show() {
        if (isExisted) { // 窗口是否已存在
            // 当窗口最小化时显示窗口
            if (isMinimized) {
                this.primaryStage.setIconified(false);
            }
        } else {
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static ProcessManagementController getController() {
        return controller;
    }
}
