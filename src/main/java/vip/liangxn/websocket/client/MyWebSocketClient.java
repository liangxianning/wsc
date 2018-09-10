package vip.liangxn.websocket.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.LoggerFactory;
import vip.liangxn.websocket.constant.AppConst;
import vip.liangxn.websocket.data.MessageData;

import javax.net.ssl.*;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MyWebSocketClient extends WebSocketClient {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MyWebSocketClient.class);

    public MyWebSocketClient(URI serverURI) {
        super(serverURI);
        if (serverURI.toString().contains("wss://"))
            trustAllHosts(this);
    }

    public MyWebSocketClient(URI serverURI, Draft draft) {
        super(serverURI, draft);
        if (serverURI.toString().contains("wss://")) {
            trustAllHosts(this);
        }

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOG.info("Connected");
        MessageData.addMessage(AppConst.MsgType.LOG, "Connected");
    }

    @Override
    public void onMessage(String message) {
        LOG.info("Received: " + message);
        MessageData.addMessage(AppConst.MsgType.RECEIVED, message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LOG.info("Disconnected");
        MessageData.addMessage(AppConst.MsgType.LOG, "Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        LOG.error("MyWebSocketClient error", ex);
        MessageData.addMessage(AppConst.MsgType.LOG, "error");
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    static void trustAllHosts(MyWebSocketClient appClient) {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // TODO Auto-generated method stub

            }
        }};

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            appClient.setSocket(factory.createSocket());
        } catch (Exception e) {
            LOG.error("MyWebSocketClient error", e);
        }
    }
}