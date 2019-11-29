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

import java.util.ArrayList;

public class DiskMainUI extends Application {
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
    MenuItem openMenuItem = new MenuItem("打开");
    MenuItem deleteMenuItem = new MenuItem("删除");
    MenuItem renameMenuItem = new MenuItem("重命名");

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
                    System.out.println("点击了diskBorderPane");
                }
            });


            Label diskLabel = new Label("磁盘");
            Image diskImage = new Image("./pictures/diskPic.jpg");
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
                        System.out.println("选中了磁盘图标");
                        ArrayList<Integer> diskStatus = diskService.getDiskStatus();
                        Stage stage5=new Stage();
                        BorderPane pn=new BorderPane();
                        int ano=0;//剩余磁盘量
                        int aal=0;//占用的磁盘量
                        int sum=0;
                        for (Integer i:diskStatus){
                            sum+=256;
                            aal+=i;
                        }
                        ano=sum-aal;
                       /* for (int i = 0; i < 512; i++) {
                            if(sys.bitmap[i]==1){
                                ano++;
                            }else {
                                aal++;
                            }
                        }*/
                        int s=ano/2;
                        HBox h1=new HBox();
                        Label la1=new Label("磁盘块总量为");
                        Label la11=new Label(String.valueOf(sum));
                        h1.getChildren().addAll(la1,la11);
                        HBox h2=new HBox();
                        Label la2=new Label("剩余磁盘量");
                        Label la22=new Label(String.valueOf(ano));
                        h2.getChildren().addAll(la2,la22);
                        HBox h3=new HBox();
                        Label la3=new Label("占用的磁盘量");
                        Label la33=new Label(String.valueOf(aal));
                        h3.getChildren().addAll(la3,la33);
                        HBox h4=new HBox();
                        Label la4=new Label("磁盘剩余");
                        Label la44=new Label(String.valueOf(s)+"kb");
                        int q=sum/2-s;

                        System.out.println(s);
                        System.out.println(q);


                        h4.getChildren().addAll(la4,la44);
                        VBox ss=new VBox();
                        diskVBox.setSpacing(20);
                        ss.getChildren().addAll(h1,h2,h3,h4);
                        pn.setTop(ss);



                        PieChart.Data d1=new PieChart.Data("占用磁盘", s);
                        PieChart.Data d2=new PieChart.Data("空闲磁盘", q);
                        ObservableList<PieChart.Data> datelistDatas= FXCollections.observableArrayList();
                        datelistDatas.addAll(d1,d2);
                        PieChart pieChart=new PieChart(datelistDatas);

                        pn.setCenter(pieChart);


                        Scene scene=new Scene(pn,400,400);
                        stage5.setScene(scene);


                        stage5.setTitle("磁盘管理");
                        stage5.show();
                    }
                }
            });


            Label backLabel = new Label("返回");
            Image backImage = new Image("./pictures/backPic.jpg");
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
                        System.out.println("选中了返回按钮");
                        primaryStage.close();
                        //sys.save();执行保存磁盘的操作
                    }
                }
            });


            flowPane.getChildren().add(backVBox);
            flowPane.getChildren().add(diskVBox);

            //加载根目录下的主文件
            CatalogEntry[] catalogEntries = rootCatalog.getEntries();
            for(CatalogEntry ce:catalogEntries){
                if (!ce.isEmpty()){
                    String cataName = ce.getName();
                    System.out.println("cataName:" + cataName);
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
                    /*FileNode fileNode = FileNode.getFileNode(choicedNodeName);
                    //if(fileNode != null){
                    if(true){

                    }*/
                }
            });


            openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //创建新的stage  从event里面获得按钮
                    String name = choicedNodeName;
                    System.out.println("ViewUtils.getFirstName(name),ViewUtils.getExtendName(name)---"
                            + ViewUtils.getFirstName(name).trim() + "," + ViewUtils.getExtendName(name));
                    //if(fileNode != null){
                    if(true){
                        System.out.println("存在");
                        //if (fileNode.getType()!=0) {
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
                            //upsetUI(name,pane1);//文件夹显示
                            loadUI(name,fileDirPane);
                            Scene fileDirScene = new Scene(fileDirPane, 300, 200);
                            fileDirStage.setScene(fileDirScene);
                            fileDirStage.show();
                        }
                        else if(ViewUtils.getExtendName(name).equals("T")){
                            //打开的是文本文件
                            System.out.println("打开的是文本文件");
                            Stage txtFileStage = new Stage();
                            //String txtString=sys.read(node); //获取文本文件
                            String[] inFile = fileService.openFile(ViewUtils.getFirstName(name),ViewUtils.getExtendName(name));

                            String txtString = "";
                            for(int i=0;i<inFile.length;i++) {
                                //System.out.println("binaryToDec test:"+(char)fileUtils.binaryToDec(fileNames[i]));
                                txtString += Character.toString((char) fileUtils.binaryToDec(inFile[i]));
                                System.out.println("文本文件txtString:" + txtString);
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
                                    for(char c:textChar){
                                        //outTxtFile.add(Character.toString(c));
                                        outString[index++] = Character.toString(c);
                                    }
                                    String[] outStringFinal = new String[index];
                                    for(int i=0;i<index;i++){
                                        outStringFinal[i] = outString[i];
                                    }
                                    System.out.println("outStringFinal:" + outStringFinal[0] + "," + outStringFinal[index-1]);
                                    System.out.println("ViewUtils.getFirstName(choicedNodeName)" + ViewUtils.getFirstName(choicedNodeName));
                                    //fileService.saveFile(ViewUtils.getFirstName(choicedNodeName),"T",(String[])outTxtFile.toArray());
                                    fileService.saveFile(ViewUtils.getFirstName(choicedNodeName),"T",outStringFinal);
                                    txtFileStage.close();
                                }
                            });

                            txtFilePane.getChildren().add(saveButton);


                            txtFileBorderPane.setCenter(txtTextArea);
                            txtFileBorderPane.setBottom(txtFilePane);
                            Scene txtFileScene = new Scene(txtFileBorderPane);
                            txtFileStage.setScene(txtFileScene);
                            txtFileStage.show();
                        }
                    }
                }
            });

            nodeRightClickedMenu.getItems().addAll(openMenuItem, deleteMenuItem, renameMenuItem);

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
                            //sys.delete(choicedNodeName);  //删除文件
                            if(!fileService.deleteFile(ViewUtils.getFirstName(choicedNodeName),ViewUtils.getExtendName(choicedNodeName)))
                                ViewUtils.showAlter("删除失败！");
                            break;
                        }
                    }
                }
            });

            newTxtFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String name = ViewUtils.getName();
                    System.out.println("ViewUtils.getName() in newTxtFileMenuItem:" + name);
                    VBox aVBox = fileUI(name+".txt","T");
                    choicedNodePane.getChildren().add(aVBox);
                    fileService.createFile(name,"T","W",0);
                }
            });

            newFileDirMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String name = ViewUtils.getName();
                    VBox aVBox = fileUI(name+".dir","D");
                    fileService.createFile(name,"D","W",0);
                    aVBox.addEventHandler(ContextMenuEvent.ANY, nodeRightClickedEvent);
                    choicedNodePane.getChildren().add(aVBox);
                    //sys.mkdir("新建文件夹");
                }
            });


            newExeFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ArrayList<String> outExeFile = new ArrayList<>();
                    //System.out.println("得到了名字" + ViewUtils.getName());
                    System.out.println("打开的是可执行文本文件");
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
                                    System.out.println("case \"两个常量运算\"");
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
                                    System.out.println("case \"内存中变量和一个常量运算\"");
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
                                    System.out.println("case \"内存中两个变量的运算\"");
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
                                    System.out.println("case \"赋值命令\"");
                                    if(param1.equals("A")||param1.equals("B")||param1.equals("C")||param1.equals("D")){
                                        int RA = param1.charAt(0) - 'A';
                                        String outExe4 = instrService.createAsnInstr(RA,Integer.valueOf(param2));
                                        outExeFile.add(outExe4);
                                        outputTextArea.appendText("R" + param1 + "=" + param2 + ";\n");
                                    }
                                    else ViewUtils.showAlter("该寄存器不存在！");
                                    break;
                                case "设备控制命令":
                                    System.out.println("case \"设备控制命令\"");
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
                            fileService.createFile(name,"E","W",0);
                            fileService.saveFile(name,"E",(String[]) outExeFile.toArray());
                            exeFileStage.close();
                            VBox aVBox=fileUI(name+".exe","E");
                            choicedNodePane.getChildren().add(aVBox);

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


//	root.setOnContextMenuRequested(blankRightClickedEvent);
            diskMainBorderPane.addEventHandler(ContextMenuEvent.ANY, blankRightClickedEvent);
            Scene diskMainScene = new Scene(diskMainBorderPane);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(diskMainScene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //怎么获得文件夹中文件的类型？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
    private void loadUI(String name,Pane pane){//打开文件夹，目录加载方法
    // name:带后缀的全名
        String[] fileNames = fileService.openFile(ViewUtils.getFirstName(name).trim(),"D");
        String firstName = "";
        for(int i=0;i<fileNames.length;i+=8){
            //System.out.println("binaryToDec test:"+(char)fileUtils.binaryToDec(fileNames[i]));
            firstName = Character.toString((char)fileUtils.binaryToDec(fileNames[i]))
                    + Character.toString((char)fileUtils.binaryToDec(fileNames[i+1]))
                    + Character.toString((char)fileUtils.binaryToDec(fileNames[i+2]));
            System.out.println("loadUI 's firstName:" + firstName + ",,extendName:"
                    + Character.toString((char)fileUtils.binaryToDec(fileNames[i+3])));
            if (Character.toString((char)fileUtils.binaryToDec(fileNames[i+3])).equals("D")
                    ||Character.toString((char)fileUtils.binaryToDec(fileNames[i+3])).equals("T")
                    ||Character.toString((char)fileUtils.binaryToDec(fileNames[i+3])).equals("E")){
                VBox vBox = fileUI(firstName,fileNames[i+3]);
                pane.getChildren().add(vBox);
            }
        }
    }
    /*
    private void upsetUI(String name,Pane pane) {//打开文件夹，目录加载方法
        sys.cd(name);
        for (int i = 0; i < sys.node_array.size(); i++) {

            entity.FileNode node=sys.node_array.get(i);



            if (node.getParentnode()==sys.getNum_cur()) {

                int num=node.getType();
                String filename=node.getFilename();
                VBox vBox=fileUI(num, filename);
                pane.getChildren().add(vBox);




            }

        }
    }
    /*

    private void upsetUI2(Pane pane) {//主页面加载方法

        for (int i = 0; i < sys.node_array.size(); i++) {

            entity.FileNode node=sys.node_array.get(i);



            if (node.getParentnode()==sys.getNum_cur()) {

                int num=node.getType();
                String filename=node.getFilename();
                VBox vBox=fileUI(num, filename);
                pane.getChildren().add(vBox);




            }

        }
    }

     */
    //创造一个文件或文件夹
    public VBox fileUI(String name,String extendName){// name:带后缀的全名
        DoubleProperty zoomProperty = new SimpleDoubleProperty(20);
        Label label;
        Image image;
        if (extendName.equals("T")) {
            label = new Label(name);
            image = new Image("pictures/txtFilePic.jpg");
        }else if(extendName.equals("E")){
            label = new Label(name);
            image = new Image("./pictures/exeFilePic.png");
        }else if(extendName.equals("D")){
            label = new Label(name);
            image = new Image("./pictures/fileDirPic.jpg");
        }else {
            label = new Label(name);
            image = new Image("./pictures/backPic.jpg");
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
                System.out.println("event.getTarget"+event.getTarget().toString());
                VBox aVBox = (VBox)event.getTarget();
                Label aLabel = (Label)aVBox.getChildren().get(1);
                String aString = aLabel.getText();
                System.out.println(aString);
                choicedNodeName = aString;
                System.out.println("choicedNodeName:" + choicedNodeName);
            }
        });
        return vBox;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
