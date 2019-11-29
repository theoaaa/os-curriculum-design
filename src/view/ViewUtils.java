package view;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class ViewUtils {

    public static String getName(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("请输入名字");
        dialog.setHeaderText("");
        dialog.setContentText("");
        // Traditional way to get the response value.
        Optional<String> name = dialog.showAndWait();
        if (name.isPresent()){
            if(!name.equals("")){
                System.out.println("ViewUtils.getName() 's name.get():" + name.get());
                return name.get();
            }
        }
        return null;
    }

    public static void showAlter(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.show();
    }

    public static String getExtendName(String fullName){
        String[] nameArray = fullName.split("\\.");
        String lastName = nameArray[nameArray.length - 1];
        Character extendName = lastName.toUpperCase().charAt(0);
        //System.out.println(lastName.toUpperCase().charAt(0));
        return extendName.toString();
    }

    public static String getFirstName(String fullName){
        String[] nameArray = fullName.split("\\.");
        return nameArray[0];
    }

    public static String getFullExtend(String extendName){
        if(extendName.equals("D"))
            return ".dir";
        else if (extendName.equals("T"))
            return ".txt";
        else if (extendName.equals("E"))
            return ".exe";
        return "";
    }

    /*public static String getName(){
        String name = "***";
        Stage getNameStage = new Stage();
        BorderPane getNameBorderPane = new BorderPane();
        Label getNameLabel = new Label("请输入名字");
        TextField getNameTextField = new TextField();
        VBox getNameVBox = new VBox();
        getNameVBox.getChildren().addAll(getNameLabel,getNameTextField);
        HBox getNameHBox=new HBox();
        getNameHBox.setAlignment(Pos.CENTER);
        Button confirmButton = new Button("确认");
        getNameHBox.getChildren().add(confirmButton);
        getNameBorderPane.setCenter(getNameVBox);
        getNameBorderPane.setBottom(getNameHBox);
        Scene getNameScene = new Scene(getNameBorderPane);
        getNameStage.setScene(getNameScene);
        getNameStage.show();
        confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                name = getNameTextField.getText().getBytes();
                //fileNode.setFileName(renameTextField.getText());
                getNameStage.close();
            }
        });
        return name;
    }*/
}


