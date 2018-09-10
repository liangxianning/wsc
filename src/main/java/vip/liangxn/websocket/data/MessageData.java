package vip.liangxn.websocket.data;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.java_websocket.client.WebSocketClient;
import vip.liangxn.websocket.constant.AppConst.MsgType;

import java.util.HashMap;
import java.util.Map;

public class MessageData {
    /**
     * listciew数据
     */
    public static ObservableList<String> dataList = FXCollections.observableArrayList();

    public static Map<String, WebSocketClient> clients = new HashMap<>();

    public static void addMessage(MsgType type, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String data;
                if (dataList.size() > 100) {
                    dataList.remove(0);
                }
                switch (type) {
                    case SEND:
                        data = "Send：" + msg;
                        break;
                    case RECEIVED:
                        data = "Received：" + msg;
                        break;
                    default:
                        data="Log：" + msg;
                        break;
                }
                dataList.add(data);
            }
        });
    }

    public static void cleanMessage() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dataList.clear();
            }
        });
    }

}
