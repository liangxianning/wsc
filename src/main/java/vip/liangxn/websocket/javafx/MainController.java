package vip.liangxn.websocket.javafx;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import vip.liangxn.websocket.client.MyWebSocketClient;
import vip.liangxn.websocket.constant.AppConst;
import vip.liangxn.websocket.data.MessageData;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URI;
import java.net.URISyntaxException;

public class MainController {
    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
    @FXML
    private TextField wsUrlText;
    @FXML
    private TextArea messageTextInput;
    @FXML
    private ListView messageList;

    @FXML
    private Button connectBtn;

    @FXML
    protected void connectBtnClick(ActionEvent event) throws Exception {
        String wsUrl = wsUrlText.getText();
        if (StringUtils.isBlank(wsUrl)) {
            JavaFxDialog.error("地址错误");
            return;
        }

        Button connectBtn = (Button) event.getTarget();
        connectBtn.setDisable(true);
        wsUrlText.setDisable(true);

        messageList.setItems(MessageData.dataList);

        wsUrl = wsUrl.replaceAll("\\s*", "");
        final URI uri;
        try {
            uri = new URI(wsUrl);
        } catch (URISyntaxException e) {
            JavaFxDialog.alert("错误", "地址错误");
            connectBtn.setDisable(false);
            wsUrlText.setDisable(false);
            return;
        }

        String host = uri.getHost();
        if (host == null) {
            JavaFxDialog.alert("错误", "服务器地址错误");
            connectBtn.setDisable(false);
            wsUrlText.setDisable(false);
            return;
        }

        String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
        if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
            JavaFxDialog.alert("错误", "仅支持WS(S)，暂不支持其他方式");
            connectBtn.setDisable(false);
            wsUrlText.setDisable(false);
            return;
        }

        MyWebSocketClient client = new MyWebSocketClient(uri);
        client.connect();

        MessageData.clients.put(wsUrl, client);

    }

    @FXML
    protected void disconnectBtnClick(ActionEvent event) {
        connectBtn.setDisable(false);
        wsUrlText.setDisable(false);

        String wsUrl = wsUrlText.getText();
        wsUrl = wsUrl.replaceAll("\\s*", "");
        if (MessageData.clients.containsKey(wsUrl)) {
            MessageData.clients.get(wsUrl).close();
        }
    }

    @FXML
    protected void sendMessageBtnClick(ActionEvent event) {
        String wsUrl = wsUrlText.getText();
        wsUrl = wsUrl.replaceAll("\\s*", "");
        String msg = messageTextInput.getText();
        LOG.info("msg={}", msg);
        if (!StringUtils.isBlank(msg)) {
            MessageData.clients.get(wsUrl).send(msg);
            MessageData.addMessage(AppConst.MsgType.SEND, msg.replaceAll("\\s*", ""));
        }
    }

    @FXML
    protected void cleanMessageBtnClick(ActionEvent event) {
        MessageData.cleanMessage();
    }


    public void initlistViewContextMenu() {
        messageList.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();

            /*MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Edit \"%s\"", cell.itemProperty()));
            editItem.setOnAction(event -> {
                String item = cell.getItem();
                // code to edit item...
            });
            MenuItem deleteItem = new MenuItem();
            deleteItem.textProperty().bind(Bindings.format("Delete \"%s\"", cell.itemProperty()));
            deleteItem.setOnAction(event -> messageList.getItems().remove(cell.getItem()));*/

            MenuItem copyItem = new MenuItem();
            copyItem.textProperty().bind(Bindings.format("复制"));
            copyItem.getStyleClass().add("menu");
            copyItem.setOnAction(event -> {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable tText = new StringSelection(cell.itemProperty().getValue());
                clip.setContents(tText, null);
            });

            contextMenu.getItems().addAll(/*editItem, deleteItem*/copyItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });
    }

}
