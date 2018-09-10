package vip.liangxn.websocket.javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JavaFxDialog {

    public static void alert(String title, String msg){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static void error(String msg){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}