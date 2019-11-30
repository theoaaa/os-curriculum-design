package view.memoryManagement;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MemoryManagementWindow extends Application {
    private static MemoryManagementWindow instance;    // 单例模式

    private static MemoryManagementController controller = null;   // 界面controller

    private Stage primaryStage;           // 窗口 Stage 对象

    private Boolean isExisted = false;    // 窗口是否已存在

    private Boolean isMinimized = false;  // 窗口是否最小化

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MemoryManagementWindow.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        this.primaryStage.setTitle("MemoryManagementBlock");
        Scene scene = new Scene(root,850,500);
        this.primaryStage.setScene(scene);
        this.primaryStage.setResizable(false);
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
                primaryStage.setIconified(false);
            }
        } else {
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单例模式
     */
    public static MemoryManagementWindow getInstance(){
        if (instance == null)
            instance = new MemoryManagementWindow();

        return instance;
    }

    // 获取界面的controller
    public static MemoryManagementController getController() {
        return controller;
    }
}