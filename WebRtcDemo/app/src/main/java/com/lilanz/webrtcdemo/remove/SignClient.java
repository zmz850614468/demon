package com.lilanz.webrtcdemo.remove;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

/**
 * 通信客户端
 */
public class SignClient {

    public static final String HOST = "https://192.168.36.105:8443";

    private Socket socket;

    private CallBack signCallBack;

    public SignClient(@NonNull CallBack callBack){
        this.signCallBack = callBack;
        initSocket();
    }

    private void initSocket(){
        //SSL加密连接
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(getSSLSocketFactory(), new TrustAllCerts())
                .build();

        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.Options options = new IO.Options();
        options.callFactory = okHttpClient;
        options.webSocketFactory = okHttpClient;
        try {
            socket = IO.socket(HOST, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //created [id,room,peers]
        socket.on("created", args -> {signCallBack.onRoomCreate((JSONObject) args[0]);});
        socket.on("join", args -> {signCallBack.onRemoveJoin((JSONObject) args[0]);});
        socket.on("joined", args -> {signCallBack.onSelfJoin((JSONObject) args[0]);});
        socket.on("exit", args -> {signCallBack.onExit((JSONObject) args[0]);});
        socket.on("offer",args -> {signCallBack.onOffer((JSONObject) args[0]);});
        socket.on("answer",args -> {signCallBack.onAnswer((JSONObject) args[0]);});
        socket.on("candidate", args -> {signCallBack.onCandidate((JSONObject) args[0]);});

        socket.connect();
    }

    /** 信令服务器发送消息 **/
    public void sendMessage(String event, JSONObject message){
        socket.emit(event, message);
    }

    public void setSignCallBack(CallBack callBack){
        this.signCallBack = callBack;
    }

    public interface CallBack{
        public void onRoomCreate(JSONObject jsonObject);
        public void onRemoveJoin(JSONObject jsonObject);
        public void onSelfJoin(JSONObject jsonObject);
        public void onExit(JSONObject jsonObject);
        public void onOffer(JSONObject jsonObject);
        public void onAnswer(JSONObject jsonObject);
        public void onCandidate(JSONObject jsonObject);
    }

    //返回SSLSocketFactory 用于ssl连接
    private SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }
}

/**
 * SSL全部信任
 * Created by chengshaobo on 2018/10/25.
 */
class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
}
