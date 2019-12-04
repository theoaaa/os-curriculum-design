package view;

import disk.service.DiskService;
import file.bean.CatalogEntry;
import file.bean.RootCatalog;
import file.service.FileService;
import file.util.FileUtils;
import instruction.service.InstrService;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.processManege.ProcessControl;

import java.util.ArrayList;

public class DiskManagementWindow extends Application {
    FileUtils fileUtils = FileUtils.getInstance();
    DiskService diskService = DiskService.getInstance();
    FileService fileService = FileService.getInstance();
    InstrService instrService = InstrService.getInstrService();
    RootCatalog rootCatalog = fileService.getRootCatalog();
    private String choicedNodeName;    //选中图标的名字
    private Pane choicedNodePane;   //选中的图标
    ContextMenu blankRightClickedMenu = new ContextMenu();  //在空白处点击右键弹出的菜单
    ContextMenu nodeRightClickedMenu = new ContextMenu();   //在图标上点击右键弹出的菜单
    MenuItem newTxtFileMenuItem = new MenuItem("新建文本文件");
    MenuItem newExeFileMenuItem = new MenuItem("新建可执行文件");
    MenuItem newFileDirMenuItem = new MenuItem("新建文件夹");
    MenuItem pasteMenuItem = new MenuItem("粘贴");
    MenuItem openMenuItem = new MenuItem("打开");
    MenuItem deleteMenuItem = new MenuItem("删除");
    MenuItem renameMenuItem = new MenuItem("重命名");
    MenuItem copyMenuItem = new MenuItem("复制");
    MenuItem changeAttrMenuItem = new MenuItem("改变属性");
    String copyFileExtendName = null;


    private Stage primaryStage; // 窗口 Stage 对象
    private Boolean isExisted = false; // 窗口是否已存在
    private Boolean isMinimized = false; // 窗口是否最小化
    private static DiskManagementWindow diskManagementWindow = new DiskManagementWindow();

    /*private DiskManagementWindow(){

    }*/

    public static DiskManagementWindow getInstance(){
        if(diskManagementWindow == null){
            diskManagementWindow = new DiskManagementWindow();
        }
        return diskManagementWindow;
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


    EventHandler<ContextMenuEvent> blankRightClickedEvent;  //鼠标点击空白处事件
    EventHandler<ContextMenuEvent> nodeRightClickedEvent = new EventHandler<ContextMenuEvent>() { //鼠标点击图标事件
        @Override
        public void handle(ContextMenuEvent contextMenuEvent) {
            nodeRightClickedMenu.show(choicedNodePane,contextMenuEvent.getSceneX(),contextMenuEvent.getSceneY());
            contextMenuEvent.consume();
        }
    };

    @Override
    public void start(Stage primaryStage) {
        try{
            DoubleProperty zoomProperty = new SimpleDoubleProperty(20);
            BorderPane diskMainBorderPane = new BorderPane(); //磁盘操作主页面
            FlowPane flowPane = new FlowPane();
            choicedNodePane = flowPane;
            flowPane.setPadding(new Insets(0,20,0,20));
            flowPane.setHgap(20);
            flowPane.setVgap(20);

            diskMainBorderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    choicedNodePane = flowPane;
                }
            });


            Label diskLabel = new Label("磁盘情况");
            Image diskImage = new Image("images/diskPic.jpg");
            ImageView diskImageView=new ImageView();
            diskImageView.setSmooth(false);
            diskImageView.setFitWidth(zoomProperty.get() * 2);
            diskImageView.setFitHeight(zoomProperty.get() * 2);  //控制图片的大小
            diskImageView.setPreserveRatio(false);
            diskImageView.setImage(diskImage);
            VBox diskVBox = new VBox();
            diskVBox.getChildren().addAll(diskImageView,diskLabel);

            diskVBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                        Stage stage5=new Stage();
                        BorderPane pn=new BorderPane();
                        int sumDisk = 128*256;//总磁盘量
                        int usedDisk = fileService.getDiskUsedStatus();//占用的磁盘量
                        int emptyDisk = sumDisk-usedDisk;;//空闲磁盘量
                       /* ArrayList<Integer> diskStatus = diskService.getDiskStatus();
                        for (Integer i:diskStatus){
                            sumDisk+=256;
                            usedDisk+=i;
                        }*/
                        HBox h1=new HBox();
                        Label la1=new Label("磁盘块总量为");
                        Label la11=new Label(String.valueOf(sumDisk));
                        h1.getChildren().addAll(la1,la11);
                        HBox h2=new HBox();
                        Label la2=new Label("剩余磁盘量");
                        Label la22=new Label(String.valueOf(emptyDisk));
                        h2.getChildren().addAll(la2,la22);
                        HBox h3=new HBox();
                        Label la3=new Label("占用的磁盘量");
                        Label la33=new Label(String.valueOf(usedDisk));
                        h3.getChildren().addAll(la3,la33);

                        VBox ss=new VBox();
                        diskVBox.setSpacing(20);
                        ss.getChildren().addAll(h1,h2,h3);
                        pn.setTop(ss);

                        PieChart.Data d1=new PieChart.Data("占用磁盘", usedDisk);
                        PieChart.Data d2=new PieChart.Data("空闲磁盘", emptyDisk);
                        ObservableList<PieChart.Data> datelistDatas= FXCollections.observableArrayList();
                        datelistDatas.addAll(d1,d2);
                        PieChart pieChart=new PieChart(datelistDatas);

                        pn.setCenter(pieChart);
                        Scene scene=new Scene(pn,400,400);
                        stage5.setScene(scene);

                        stage5.setTitle("磁盘使用情况");
                        stage5.show();
                    }
                }
            });


            Label backLabel = new Label("返回");
            Image backImage = new Image("images/backPic.jpg");
            ImageView backImageView = new ImageView();
            backImageView.setSmooth(false);
            backImageView.setFitWidth(zoomProperty.get() * 2);
            backImageView.setFitHeight(zoomProperty.get() * 2);  //控制图片的大小
            backImageView.setPreserveRatio(false);
            backImageView.setImage(backImage);
            VBox backVBox = new VBox();
            backVBox.getChildren().addAll(backImageView,backLabel);

            //点击返回按钮，退出界面
            backVBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                        primaryStage.close();
                        isExisted = false;
                        fileService.closeMethod();
                    }
                }
            });

            Label formatLabel = new Label("格式化");
            Image formatImage = new Image("images/formatDiskPic.jpg");
            ImageView formatImageView = new ImageView();
            formatImageView.setSmooth(false);
            formatImageView.setFitWidth(zoomProperty.get() * 2);
            formatImageView.setFitHeight(zoomProperty.get() * 2);  //控制图片的大小
            formatImageView.setPreserveRatio(false);
            formatImageView.setImage(formatImage);
            VBox formatVBox = new VBox();
            formatVBox.getChildren().addAll(formatImageView,formatLabel);

            //点击返回按钮，退出界面
            formatVBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                        ViewUtils.showAlter("磁盘已格式化！");
                        fileService.formatDisk();
                    }
                }
            });

            flowPane.getChildren().add(backVBox);
            flowPane.getChildren().add(formatVBox);
            flowPane.getChildren().add(diskVBox);

            //加载根目录下的主文件
            CatalogEntry[] catalogEntries = rootCatalog.getEntries();
            for(CatalogEntry ce:catalogEntries){
                if (!ce.isEmpty()){
                    String cataName = ce.getName();
                    VBox aVBox = fileUI(cataName+".dir","D");
                    aVBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
                    // choicedNodePane.getChildren().add(aVBox);

                    flowPane.getChildren().add(aVBox);
                }
            }

            diskMainBorderPane.getChildren().add(flowPane);

            //upsetUI2(pane);


            renameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for(Node node: choicedNodePane.getChildren()){
                        VBox aVBox = (VBox)node;
                        Label aLabel = (Label)aVBox.getChildren().get(1);
                        String aString = aLabel.getText();
                        if (aString.equals(choicedNodeName)) {
                            Stage renameStage = new Stage();
                            BorderPane renameBorderPane = new BorderPane();
                            Label renameLabel = new Label("请输入名字");
                            TextField renameTextField = new TextField();
                            VBox renameVBox = new VBox();
                            renameVBox.getChildren().addAll(renameLabel,renameTextField);
                            HBox renameHBox=new HBox();
                            renameHBox.setAlignment(Pos.CENTER);
                            Button confirmButton = new Button("确认");
                            renameHBox.getChildren().add(confirmButton);
                            renameBorderPane.setCenter(renameVBox);
                            renameBorderPane.setBottom(renameHBox);
                            Scene renameScene = new Scene(renameBorderPane);
                            renameStage.setScene(renameScene);
                            renameStage.show();
                            confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    aLabel.setText(renameTextField.getText()+ViewUtils.getFullExtend(ViewUtils.getExtendName(choicedNodeName)));
                                    //fileNode.setFileName(renameTextField.getText());
                                    fileService.renameFile(ViewUtils.getFirstName(choicedNodeName),ViewUtils.getExtendName(choicedNodeName),renameTextField.getText());
                                    renameStage.close();
                                }
                            });
                            break;
                        }
                    }
                }
            });


            openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //创建新的stage  从event里面获得按钮
                    String name = choicedNodeName;
                    System.out.println("choicedNodeName" + choicedNodeName + "ViewUtils.getFirstName(name),ViewUtils.getExtendName(name)---"
                            + ViewUtils.getFirstName(name).trim() + "," + ViewUtils.getExtendName(name));
                    if(true){
                        System.out.println("存在");
                        if(ViewUtils.getExtendName(name).equals("D")){
                            //打开的是文件夹
                            Stage fileDirStage = new Stage();
                            FlowPane fileDirPane = new FlowPane();
                            fileDirPane.setPadding(new Insets(0,20,0,20));
                            fileDirPane.setHgap(20);
                            fileDirPane.setVgap(20);
                            fileDirPane.addEventHandler(ContextMenuEvent.ANY, blankRightClickedEvent);
                            fileDirPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    choicedNodePane = fileDirPane;
                                }
                            });
                            //创建关闭按钮
                            VBox backBox = fileUI("关闭" ,"关闭");
                            fileDirPane.getChildren().add(backBox);
                            loadUI(name,fileDirPane);
                            Scene fileDirScene = new Scene(fileDirPane, 500, 400);
                            fileDirStage.setScene(fileDirScene);
                            fileDirStage.setMaximized(true);
                            fileDirStage.setTitle(name);
                            fileDirStage.show();
                            //fileDirStage.setAlwaysOnTop(true);

                            backBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if(event.getButton().name().equals(MouseButton.PRIMARY.name())){
                                        //pane_close
                                        fileDirStage.close();
//                                        fileService.backward();
                                        fileService.closeMethod();
                                    }
                                }
                            });
                        } else if (ViewUtils.getExtendName(name).equals("T") || ViewUtils.getExtendName(name).equals("E")) {
                            //打开的是文本文件
                            //获取文本文件
                            String[] inFile = fileService.openFile(ViewUtils.getFirstName(name),ViewUtils.getExtendName(name));
                            if (ViewUtils.getExtendName(name).equals("T")) {
                                Stage txtFileStage = new Stage();
                                String txtString = "";
                                for (int i = 0; i < inFile.length; i++) {
                                    txtString += Character.toString((char) fileUtils.binaryToDec(inFile[i]));
                                }
                                BorderPane txtFileBorderPane = new BorderPane();
                                TextArea txtTextArea = new TextArea();
                                txtTextArea.setText(txtString);
                                Pane txtFilePane = new Pane();

                                Button saveButton = new Button("保存");
                                saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        //public void saveTextFile(String textInArea,String actualPath){} //保存文本文件
                                        String textInArea = txtTextArea.getText();
                                        //ArrayList<String> outTxtFile = new ArrayList<>();
                                        String[] outString = new String[10000];
                                        int index = 0;
                                        char[] textChar = textInArea.toCharArray();
                                        for (char c : textChar) {
                                            //outTxtFile.add(Character.toString(c));
                                            outString[index++] = Character.toString(c);
                                        }
                                        String[] outStringFinal = new String[index];
                                        for (int i = 0; i < index; i++) {
                                            outStringFinal[i] = outString[i];
                                        }
                                        //fileService.saveFile(ViewUtils.getFirstName(choicedNodeName),"T",(String[])outTxtFile.toArray());
                                        fileService.saveFile(ViewUtils.getFirstName(choicedNodeName), "T", outStringFinal);
                                        txtFileStage.close();
                                    }
                                });

                                txtFilePane.getChildren().add(saveButton);


                                txtFileBorderPane.setCenter(txtTextArea);
                                txtFileBorderPane.setBottom(txtFilePane);
                                Scene txtFileScene = new Scene(txtFileBorderPane);
                                txtFileStage.setScene(txtFileScene);
                                txtFileStage.show();
                            } else {
                                ProcessControl.create(inFile);
                            }
                        }
                    }
                }
            });

            nodeRightClickedMenu.getItems().addAll(openMenuItem, copyMenuItem, deleteMenuItem, renameMenuItem, changeAttrMenuItem);

            blankRightClickedEvent =new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    blankRightClickedMenu.show(choicedNodePane,event.getSceneX(),event.getSceneY());
                }
            };

            deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for(Node aNode: choicedNodePane.getChildren()){

                        VBox avBox =(VBox)aNode;
                        Label aLabel = (Label)avBox.getChildren().get(1);
                        String aString = aLabel.getText();
                        if (aString.equals(choicedNodeName)) {
                            choicedNodePane.getChildren().remove(aNode);
                            //删除文件
                            if(!fileService.deleteFile(ViewUtils.getFirstName(choicedNodeName),ViewUtils.getExtendName(choicedNodeName)))
                                ViewUtils.showAlter("删除失败！");
                            break;
                        }
                    }
                }
            });

            copyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    copyFileExtendName = ViewUtils.getExtendName(choicedNodeName);
                    System.out.println("copyMenuItem.setOnAction  choicedNodeName:" + choicedNodeName + "   copyFileExtendName:" + copyFileExtendName);
                    fileService.copyFile(ViewUtils.getFirstName(choicedNodeName),copyFileExtendName);
                }
            });

            changeAttrMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String oldExtendName = ViewUtils.getExtendName(choicedNodeName);
                    if (oldExtendName.equals("T")){
                        fileService.changeFileAttribute(ViewUtils.getFirstName(choicedNodeName),oldExtendName);
                        System.out.println("changeAttrMenuItem.setOnAction  name:" + ViewUtils.getFirstName(choicedNodeName)
                                + ",oldExtendName:" + oldExtendName);
                        for(Node aNode: choicedNodePane.getChildren()){
                            VBox avBox =(VBox)aNode;
                            Label aLabel = (Label)avBox.getChildren().get(1);
                            String aString = aLabel.getText();
                            if (aString.equals(choicedNodeName)) {
                                choicedNodePane.getChildren().remove(aNode);
                                VBox newVBox = fileUI(ViewUtils.getFirstName(choicedNodeName)+".exe","E");
                                newVBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
                                choicedNodePane.getChildren().add(newVBox);
                                break;
                            }
                        }
                    }
                    else if (oldExtendName.equals("E")){
                        fileService.changeFileAttribute(ViewUtils.getFirstName(choicedNodeName),oldExtendName);
                        System.out.println("changeAttrMenuItem.setOnAction  name:" + ViewUtils.getFirstName(choicedNodeName)
                                + ",oldExtendName:" + oldExtendName);
                        for(Node aNode: choicedNodePane.getChildren()){
                            VBox avBox =(VBox)aNode;
                            Label aLabel = (Label)avBox.getChildren().get(1);
                            String aString = aLabel.getText();
                            if (aString.equals(choicedNodeName)) {
                                choicedNodePane.getChildren().remove(aNode);
                                VBox newVBox = fileUI(ViewUtils.getFirstName(choicedNodeName)+".txt","T");
                                newVBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
                                choicedNodePane.getChildren().add(newVBox);
                                break;
                            }
                        }
                    }else {
                        System.out.println("该文件属性不可改！ choicedNodeName:" + choicedNodeName + "    oldExtendName:" + oldExtendName);
                        ViewUtils.showAlter("该文件属性不可改！");
                    }
                }
            });

            pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(fileService.copyable()){
                        if(!fileService.pasteFile()){
                            ViewUtils.showAlter("粘贴失败！");
                        } else{
                            //新建一个对应属性的图标
                            if (copyFileExtendName.equals("T") || copyFileExtendName.equals("E") || copyFileExtendName.equals("D")){
                                String pasteFirstName = ViewUtils.getName("请输入新的文件名");
                                if (pasteFirstName == null){
                                    ViewUtils.showAlter("名字错误！");
                                }else {
                                    VBox aVBox = fileUI(pasteFirstName+ViewUtils.getFullExtend(copyFileExtendName),copyFileExtendName);
                                    choicedNodePane.getChildren().add(aVBox);
                                }
                            }else {
                                ViewUtils.showAlter("复制错误！");
                            }
                        }
                    }else {
                        ViewUtils.showAlter("无复制文件！");
                    }
                }
            });

            newTxtFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String name = ViewUtils.getName();
                    if (name == null){

                    }else {
                        System.out.println("ViewUtils.getName() " +
                                "in newTxtFileMenuItem:" + name);
                        if(fileService.createFile(name,"T","W",0)) {
                            VBox aVBox = fileUI(name+".txt","T");
                            choicedNodePane.getChildren().add(aVBox);
                        }else {
                            ViewUtils.showAlter("创建失败！");
                        }
                    }
                }
            });

            newFileDirMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String name = ViewUtils.getName();
                    if (name == null){
                        ViewUtils.showAlter("请输入正确的名字！");
                    }else {
                        if(fileService.createFile(name,"D","W",96)){
                            VBox aVBox = fileUI(name+".dir","D");
                            aVBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
                            choicedNodePane.getChildren().add(aVBox);
                        }else {
                            ViewUtils.showAlter("新建失败！");
                        }

                    }
                }
            });


            newExeFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ArrayList<String> outExeFile = new ArrayList<>();
                    Stage exeFileStage = new Stage();
                    FlowPane exeFileFlowPane = new FlowPane();
                    exeFileFlowPane.setPadding(new Insets(20));
                    exeFileFlowPane.setPrefSize(600,700);
                    exeFileFlowPane.setHgap(10);
                    exeFileFlowPane.setVgap(10);
                    final TextArea outputTextArea = new TextArea();
                    outputTextArea.setEditable(false);
                    outputTextArea.setPrefSize(440, 500);
                    final ComboBox<String> instructionType = new ComboBox<String>();
                    instructionType.setPrefWidth(550);
                    instructionType.getItems().addAll("两个常量运算", "内存中变量和一个常量运算", "内存中两个变量的运算", "赋值命令","设备控制命令","进程终止命令");
                    instructionType.setValue("请选择指令");
                    final TextArea param1Text = new TextArea();
                    param1Text.setEditable(true);
                    param1Text.setPrefSize(80,15);
                    final TextArea param2Text = new TextArea();
                    param2Text.setEditable(true);
                    param2Text.setPrefSize(80,15);
                    final TextArea param3Text = new TextArea();
                    param3Text.setEditable(true);
                    param3Text.setPrefSize(80,15);
                    final TextArea param4Text = new TextArea();
                    param4Text.setEditable(true);
                    param4Text.setPrefSize(80,15);
                    final TextArea param5Text = new TextArea();
                    param5Text.setEditable(true);
                    param5Text.setPrefSize(80,15);
                    Button confirmButton = new Button("确定");

                    instructionType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                        @Override
                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                            switch(newValue.toString()) {
                                case "两个常量运算":
                                    param1Text.setText("寄存器A");
                                    param2Text.setText("寄存器B");
                                    param3Text.setText("值A");
                                    param4Text.setText("值B");
                                    param5Text.setText("+/-");
                                    break;
                                case "内存中变量和一个常量运算":
                                    param1Text.setText("内存地址");
                                    param2Text.setText("寄存器A");
                                    param3Text.setText("寄存器B");
                                    param4Text.setText("值B");
                                    param5Text.setText("+/-");
                                    break;
                                case "内存中两个变量的运算":
                                    param1Text.setText("内存A");
                                    param2Text.setText("寄存器A");
                                    param3Text.setText("内存B");
                                    param4Text.setText("寄存器B");
                                    param5Text.setText("+/-");
                                    break;
                                case "赋值命令":
                                    param1Text.setText("寄存器号");
                                    param2Text.setText("数值");
                                    param3Text.setText("");
                                    param4Text.setText("");
                                    param5Text.setText("");
                                    break;
                                case "设备控制命令":
                                    param1Text.setText("设备号");
                                    param2Text.setText("运行时间");
                                    param3Text.setText("");
                                    param4Text.setText("");
                                    param5Text.setText("");
                                    break;
                                case "进程终止命令":
                                    param1Text.setText("");
                                    param2Text.setText("");
                                    param3Text.setText("");
                                    param4Text.setText("");
                                    param5Text.setText("");
                                    break;
                            }
                        }
                    });

                    confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            String param1 = param1Text.getText();
                            String param2 = param2Text.getText();
                            String param3 = param3Text.getText();
                            String param4 = param4Text.getText();
                            String param5 = param5Text.getText();
                            switch(instructionType.getValue()) {
                                case "两个常量运算":
                                    if((param1.equals("A")||param1.equals("B")||param1.equals("C")||param1.equals("D"))&&
                                            (param2.equals("A")||param2.equals("B")||param2.equals("C")||param2.equals("D"))) {
                                        int RA = param1.charAt(0) - 'A';
                                        int RB = param2.charAt(0) - 'A';
                                        int ope = 0;
                                        if(param5.equals("-"))
                                            ope = 1;
                                        String outExe1 = instrService.createRCRInstr(RA,RB,Integer.valueOf(param3),Integer.valueOf(param4),ope);
                                        outExeFile.add(outExe1);
                                        outputTextArea.appendText("R" + param1 + "=" + param3 + param5 + param4 + ";\n");
                                    }
                                    else ViewUtils.showAlter("该寄存器不存在！");
                                    break;
                                case "内存中变量和一个常量运算":
                                    int R0 = Integer.valueOf(param1);
                                    if(R0>=0&&R0<=7 &&
                                            (param2.equals("A")||param2.equals("B")||param2.equals("C")||param2.equals("D"))&&
                                            (param3.equals("A")||param3.equals("B")||param3.equals("C")||param3.equals("D"))) {
                                        int RA = param2.charAt(0) - 'A';
                                        int RB = param3.charAt(0) - 'A';
                                        int ope = 0;
                                        if(param5.equals("-"))
                                            ope = 1;
                                        String outExe2 = instrService.createMCRInstr(R0,RA,RB,Integer.valueOf(param4),ope);
                                        outExeFile.add(outExe2);
                                        outputTextArea.appendText("R" + param1 + "=R" + param2 + param5 + param4 + ";\n");
                                    }
                                    else ViewUtils.showAlter("该地址或寄存器不存在！");
                                    break;
                                case "内存中两个变量的运算":
                                    int R1 = Integer.valueOf(param1);
                                    int R2 = Integer.valueOf(param3);
                                    if(R1>=0&&R1<=7 && R2>=0&&R2<=7 &&
                                            (param2.equals("A")||param2.equals("B")||param2.equals("C")||param2.equals("D"))&&
                                            (param4.equals("A")||param4.equals("B")||param4.equals("C")||param4.equals("D"))) {
                                        int RA = param2.charAt(0) - 'A';
                                        int RB = param4.charAt(0) - 'A';
                                        int ope = 0;
                                        if(param5.equals("-"))
                                            ope = 1;
                                        String outExe3 = instrService.createMCMInstr(R1,RA,R2,RB,ope);
                                        outExeFile.add(outExe3);
                                        outputTextArea.appendText("R" + param2 + "=R" + param2 + param5 + "R" + param4 + ";\n");
                                    }
                                    else ViewUtils.showAlter("该地址或寄存器不存在！");
                                    break;
                                case "赋值命令":
                                    if(param1.equals("A")||param1.equals("B")||param1.equals("C")||param1.equals("D")){
                                        int RA = param1.charAt(0) - 'A';
                                        String outExe4 = instrService.createAsnInstr(RA,Integer.valueOf(param2));
                                        outExeFile.add(outExe4);
                                        outputTextArea.appendText("R" + param1 + "=" + param2 + ";\n");
                                    }
                                    else ViewUtils.showAlter("该寄存器不存在！");
                                    break;
                                case "设备控制命令":
                                    String outExe5 = instrService.createEqpInstr(Integer.valueOf(param1),Integer.valueOf(param2));
                                    outExeFile.add(outExe5);
                                    outputTextArea.appendText("!" + param1 + " " + param2 + ";\n");
                                    break;
                                case "进程终止命令":
                                    String outExe6 = instrService.createEndInstr();
                                    outExeFile.add(outExe6);
                                    outputTextArea.appendText("end" + ";\n");
                                    break;
                            }
                            param1Text.setText("");
                            param2Text.setText("");
                            param3Text.setText("");
                            param4Text.setText("");
                            param5Text.setText("");
                        }
                    });
                    Button saveButton = new Button("保存");
                    saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            String name = ViewUtils.getName();
                            if (name == null){
                                ViewUtils.showAlter("请输入正确的名字！");
                            }else {
                                if (fileService.createFile(name,"E","W",0)){
                                    String[] outFiles = new String[outExeFile.size()];
                                    for(int i=0;i<outExeFile.size();i++){
                                        outFiles[i] = outExeFile.get(i);
                                    }
                                    fileService.saveFile(name,"E", outFiles);
                                    VBox aVBox=fileUI(name+".exe","E");
                                    choicedNodePane.getChildren().add(aVBox);
                                }else {
                                    ViewUtils.showAlter("新建失败！");
                                }
                                isExisted = false;
                                exeFileStage.close();
                            }
                        }
                    });
                    exeFileFlowPane.getChildren().addAll(instructionType, param1Text, param2Text, param3Text, param4Text,param5Text, confirmButton, outputTextArea, saveButton);
                    Scene secondScene = new Scene(exeFileFlowPane);
                    exeFileStage.setScene(secondScene);
                    exeFileStage.show();
                }
            });

            blankRightClickedMenu.getItems().add(newTxtFileMenuItem );
            blankRightClickedMenu.getItems().add(newExeFileMenuItem);
            blankRightClickedMenu.getItems().add(newFileDirMenuItem);
            blankRightClickedMenu.getItems().add(pasteMenuItem);

            diskMainBorderPane.addEventHandler(ContextMenuEvent.ANY, blankRightClickedEvent);
            Scene diskMainScene = new Scene(diskMainBorderPane);
            primaryStage.setScene(diskMainScene);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("磁盘管理");
            primaryStage.show();

            //primaryStage.setAlwaysOnTop(true);
            isExisted = true;

            // 监听窗口关闭事件
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.out.println("closed");
                    isExisted = false;
                }
            });

            // 绑定 isMinimized 值
            bindIsMinimized(primaryStage.getScene());

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //获得文件夹中文件的类型:Character.toString((char)fileUtils.binaryToDec(fileNames[i+3]))
    private void loadUI(String name,Pane pane){//打开文件夹，目录加载方法
        // name:带后缀的全名
        String[] fileNames = fileService.openFile(ViewUtils.getFirstName(name).trim(),"D");
        System.out.println("private void loadUI(String name,Pane pane){//打开文件夹，目录加载方法    " + name);
        String firstName = "";
        for(int i=0;i<fileNames.length;i+=8){
            firstName = Character.toString((char)fileUtils.binaryToDec(fileNames[i]))
                    + Character.toString((char)fileUtils.binaryToDec(fileNames[i+1]))
                    + Character.toString((char)fileUtils.binaryToDec(fileNames[i+2]));
            String extendName = Character.toString((char)fileUtils.binaryToDec(fileNames[i+3]));
            if (extendName.equals("D") ||extendName.equals("T") ||extendName.equals("E")){
                VBox vBox = fileUI(firstName + ViewUtils.getFullExtend(extendName), extendName);
                pane.getChildren().add(vBox);
            }
        }
    }

    //创造一个文件或文件夹
    private VBox fileUI(String fullName,String extendName){// fullName:带后缀的全名
        DoubleProperty zoomProperty = new SimpleDoubleProperty(20);
        Label label;
        Image image;
        if (extendName.equals("T")) {
            label = new Label(fullName);
            image = new Image("images/txtFilePic.jpg");
        }else if(extendName.equals("E")){
            label = new Label(fullName);
            image = new Image("images/exeFilePic.png");
        }else if(extendName.equals("D")){
            label = new Label(fullName);
            image = new Image("images/fileDirPic.jpg");
        }else {
            label = new Label(fullName);
            image = new Image("images/backPic.jpg");
        }

        ImageView imageView=new ImageView();
        imageView.setSmooth(false);
        imageView.setFitWidth(zoomProperty.get() * 2);
        imageView.setFitHeight(zoomProperty.get() * 2);  //控制图片的大小
        imageView.setPreserveRatio(false);
        imageView.setImage(image);
        imageView.setMouseTransparent(true);

        VBox vBox=new VBox();
        vBox.getChildren().addAll(imageView,label);
        vBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
        vBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                VBox aVBox = (VBox)event.getTarget();
                Label aLabel = (Label)aVBox.getChildren().get(1);
                String aString = aLabel.getText();
                choicedNodeName = aString;
            }
        });
        return vBox;
    }

}
