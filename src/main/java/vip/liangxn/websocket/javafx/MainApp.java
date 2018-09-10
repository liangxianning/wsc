package vip.liangxn.websocket.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class MainApp extends Application {
    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL location = getClass().getClassLoader().getResource("main.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        //如果使用 Parent root = FXMLLoader.load(...) 静态读取方法，无法获取到Controller的实例对象
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        primaryStage.setTitle("WebSocket Client");
        primaryStage.setScene(new Scene(root));

        primaryStage.getIcons().add(new Image(
                getClass().getClassLoader().getResourceAsStream("wsc.png")));

        MainController controller = fxmlLoader.getController();   //获取Controller的实例对象
        controller.initlistViewContextMenu();

        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            LOG.info("程序关闭");

        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
