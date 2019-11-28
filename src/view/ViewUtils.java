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
            if(name.equals("")){
                System.out.println(name.get());
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
}
