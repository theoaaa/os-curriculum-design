package view.deviceManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DeviceManagementWindow extends Application {
    private static DeviceManagementWindow instance;     // 单例模式

    private static DeviceManagementController controller = null;   // 界面的controller

    private Stage primaryStage;                   // 窗口 Stage 对象

    private static Boolean isExisted = false;    // 窗口是否已存在

    private Boolean isMinimized = false;  // 窗口是否最小化

    public static  void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DeviceManagementWindow.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("DeviceManagementBlocks");
        primaryStage.setScene(new Scene(root,800,400));
        primaryStage.setResizable(false);
        primaryStage.show();

        /*// 设置窗口始终位于顶部
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
        bindIsMinimized(scene);*/
    }
    /**
     * 绑定 isMinimized 值与当前窗口最小化状态
     * @param scene
     */
    /*private void bindIsMinimized(Scene scene) {
        // 监听窗口 最小化/最大化 事件；oldValue 和 newValue 均表示是否最大化
        scene.getWindow().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isMinimized =  !newValue;
            }
        });
    }*/

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

    /**
     * 单例模式
     */
    public static DeviceManagementWindow getInstance(){
        if(isExisted == false && instance == null){
            instance = new DeviceManagementWindow();
            isExisted = true;
        }
        return instance;
    }

    // 获取界面的controller
    public static DeviceManagementController getController() {
        return controller;
    }
}