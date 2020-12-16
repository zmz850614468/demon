package com.lilanz.wificonnect.threads;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activitys.App;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.beans.StopBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.controls.DeviceControl;
import com.lilanz.wificonnect.controls.InfoFileControl;
import com.lilanz.wificonnect.controls.MediaControl;
import com.lilanz.wificonnect.controls.MusicTimerControl;
import com.lilanz.wificonnect.controls.SongControl;
import com.lilanz.wificonnect.controls.SoundControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.utils.PingUtil;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WifiService extends Service {

    public static final int SERVICE = 1;    // 服务端
    private static final int CLIENT = 2;    // 客户端

    private ServerThread serverThread;
    private SocketThread socketThread;
    private int identify;

    private MusicTimerControl musicTimerControl;


    public class WifiBind extends Binder {
        public WifiService getService() {
            return WifiService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
        initListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WifiBind();
    }

    /**
     * 开启服务端口
     *
     * @param port
     */
    public void startService(int port) {
        identify = SERVICE;
        serverThread = new ServerThread(port);
        serverThread.setListener(new OnReceiverListener() {
            @Override
            public void onError(SocketThread thread, String msg) {
                super.onError(thread, msg);
                if (listener != null) {
                    listener.onCallBack(0, "onError:" + msg);
                }
//                close();
            }

            @Override
            public void onTip(int type, String msg) {
                if (listener != null) {
                    listener.onCallBack(type, "onTip:" + msg);
                }
            }

            @Override
            public void onReceiver(SocketThread thread, String msg) {
                try {
                    MsgBean msgBean = new Gson().fromJson(msg, MsgBean.class);
                    handleMsg(thread, msgBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReceiver(String msg) {
                if (listener != null) {
                    listener.onCallBack(0, "准备废弃: " + msg);
                }
            }
        });
        serverThread.start();
        MediaControl.getInstance(this).setWifiService(this);

        pingService();
    }

    private Timer timer;

    /**
     * Ping服务器
     */
    private void pingService() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String ip = SharePreferencesUtil.getServiceIp(WifiService.this);
                PingUtil.pingIp(ip);
            }
        }, 1000, 5 * 60 * 1000);
    }

    /**
     * 连接服务端口
     *
     * @param ip
     * @param port
     */
    public void startConnect(String ip, int port) {
        identify = CLIENT;
        socketThread = new SocketThread();
        socketThread.setListener(new OnReceiverListener() {
            @Override
            public void onError(SocketThread thread, String msg) {
                super.onError(thread, msg);
                if (listener != null) {
                    listener.onCallBack(2, "onError: " + msg);
                }
                close();
            }

            @Override
            public void onTip(int type, String msg) {
                if (listener != null) {
                    listener.onCallBack(type, "onTip: " + msg);
                }
            }

            @Override
            public void onReceiver(String msg) {
                try {
                    MsgBean msgBean = new Gson().fromJson(msg, MsgBean.class);
                    handleMsg(null, msgBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReceiver(SocketThread thread, final String msg) {
                MsgBean msgBean = new Gson().fromJson(msg, MsgBean.class);
                handleMsg(thread, msgBean);
                if (listener != null) {
                    listener.onCallBack(0, msg);
                }
            }
        });
        socketThread.startConnect(ip, port);
    }

    /**
     * 写数据
     *
     * @param msg
     */
    public void sendMsg(@NonNull final String msg) {
        // TODO 修改成线程队列
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (socketThread != null) {
                    socketThread.sendMsg(msg);
                }
                if (serverThread != null) {
                    serverThread.sendMsg(msg);
                }
            }
        }.start();
    }

    /**
     * 写数据队列
     *
     * @param msgList
     */
    public void sendMsg(List<MsgBean> msgList) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (socketThread != null) {
                    for (MsgBean bean : msgList) {
                        socketThread.sendMsg(bean.toString());
                    }
                }
                if (serverThread != null) {
                    for (MsgBean bean : msgList) {
                        serverThread.sendMsg(bean.toString());
                    }
                }
            }
        }.start();
    }

    public void close() {
        if (socketThread != null) {
            socketThread.close();
            socketThread = null;
        }
        if (serverThread != null) {
            serverThread.close();
            serverThread = null;
        }
    }

    public boolean canOpen() {
        if (socketThread != null) {
//            showToast("已经连接上服务端口, 无法进行此操作");
            return false;
        }
        if (serverThread != null) {
//            showToast("已经打开服务端口, 无法进行此操作");
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        showLog("service onDestroy");
        super.onDestroy();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("WifiService", msg);
        }
        InfoFileControl.getInstance(this).addInfo(StringUtil.getDataStr() + " : " + msg);
    }


    private OnDataCallbackListener listener;

    public void setListener(OnDataCallbackListener listener) {
        this.listener = listener;
    }

    public interface OnDataCallbackListener {
        /**
         * 本地端的提示信息
         *
         * @param type 0：基础信息 1:连接成功 2：断开连接
         * @param msg
         */
        void onCallBack(int type, String msg);
    }

    // ==========================     信息处理     =================================

    /**
     * 处理服务器收的所有信息
     *
     * @param bean
     */
    private void handleMsg(SocketThread thread, MsgBean bean) {
        switch (bean.type) {
            case MsgBean.COMMUNICATE:     // 聊天或测试用
                if (listener != null) {
                    listener.onCallBack(0, bean.content);
                }
                break;
            case MsgBean.PLAY_VOICE:     // 播放音效
                try {
                    SoundControl.getInstance(this).play(Integer.parseInt(bean.content));
                    thread.sendMsg(new MsgBean(MsgBean.RECEIVE_TIP, "服务器收到音效信息提示").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.PLAY_MUSIC:    // 播放音乐
                AppDataControl.isPlaying = true;
                AppDataControl.playingPath = bean.content;
                MediaControl.getInstance(this).play(bean.content);
                break;
            case MsgBean.UPDATE_MUSIC: // 更新音乐
                if (thread != null) {
                    List<SongBean> songList = SongControl.getSongList(this);
                    Collections.sort(songList, new Comparator<SongBean>() {
                        @Override
                        public int compare(SongBean o1, SongBean o2) {
                            return o1.singer.compareTo(o2.singer);
                        }
                    });
                    MediaControl.getInstance(this).setSongList(songList);
                    int size = songList.size();
                    for (int i = 0; i < size; i++) {
                        if (i + 1 == size) {
                            songList.get(i).isLastOne = true;
                        }
                        thread.sendMsg(new MsgBean(MsgBean.UPDATE_MUSIC_RESULT, songList.get(i).toString()).toString());
                    }
                }
                break;
            case MsgBean.UPDATE_MUSIC_RESULT: // 服务端返回：更新后的音乐列表
                SongBean songBean = new Gson().fromJson(bean.content, SongBean.class);
                for (MsgCallbackListener msgCallbackListener : callbacklistenerList) {
                    msgCallbackListener.onMusicUpdate(songBean);
                }
                break;
            case MsgBean.MUSIC_PAUSE_OR_START:
                if (identify == 1) {    // 服务端接收的消息
                    MediaControl.getInstance(this).switchPlay(bean.content);
                } else if (identify == 2) { // 客户端接收的消息
                    String isPlaying = bean.content.substring(0, 2);
                    AppDataControl.playingPath = bean.content.substring(2, bean.content.length());

                    AppDataControl.isPlaying = "1-".equals(isPlaying) ? true : false;

                    for (MsgCallbackListener callback : callbacklistenerList) {
                        callback.onPlayNextMusic();
                    }
                }
                break;
            case MsgBean.MUSIC_GET_VOICE_INFO:   // 获取音乐音量信息
                MsgBean msgBean = new MsgBean(MsgBean.MUSIC_VOICE_INFO, MediaControl.getInstance(App.context).getVoiceInfo());
                thread.sendMsg(msgBean.toString());
                break;
            case MsgBean.MUSIC_VOICE_INFO:
                AppDataControl.parseVoiceInfo(bean.content);
                if (identify == 1) {    // 服务端接收的消息
                    MediaControl.getInstance(this).setPlayingVoice(AppDataControl.curMusicVoice);
                }
                break;
            case MsgBean.MUSIC_PLAY_MODE:       // 歌曲播放模式
                try {
                    int playMode = Integer.parseInt(bean.content);
                    AppDataControl.playMode = playMode;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case MsgBean.RECEIVE_TIP:   // 服务器收到消息，反馈提示
                SoundControl.getInstance(App.context).play(R.raw.pi_ka_qiu);
                break;
            case MsgBean.MUSIC_STOP_MODE:   // 歌曲停止模式
                try {
                    musicTimerControl.updateStopMode(new Gson().fromJson(bean.content, StopBean.class));
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.DEVICE_CONTROL:    // 控制设备
                try {
                    DeviceControlBean deviceControlBean = new Gson().fromJson(bean.content, DeviceControlBean.class);
                    DeviceControl.getInstance(this).handleMsg(deviceControlBean);
                    // 回复客户端，服务器成功收的消息
                    msgBean = new MsgBean(MsgBean.DEVICE_CONTROL_CALLBACK, deviceControlBean.toString());
                    thread.sendMsg(msgBean.toString());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.DEVICE_CONTROL_CALLBACK:   // 控制设备回调
                try {
                    DeviceControlBean deviceControlBean = new Gson().fromJson(bean.content, DeviceControlBean.class);
                    for (MsgCallbackListener callback : callbacklistenerList) {
                        callback.onControlCallback(deviceControlBean);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.DEVICE_ADD_OR_UPDATE:      // 更新设备信息
                try {
                    DeviceBean deviceBean = new Gson().fromJson(bean.content, DeviceBean.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("ip", deviceBean.ip);
                    DBControl.deleteByColumn(this, DeviceBean.class, map);
                    DBControl.createOrUpdate(this, DeviceBean.class, deviceBean);
                    // 回复客户端，服务器成功收的消息
                    msgBean = new MsgBean(MsgBean.DEVICE_ADD_OR_UPDATE_CALLBACK, "添加或更新设备成功");
                    thread.sendMsg(msgBean.toString());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.DEVICE_ADD_OR_UPDATE_CALLBACK:     // 更新设备信息回调
                for (MsgCallbackListener callback : callbacklistenerList) {
                    callback.onDeviceUpdateCallback(bean.content);
                }
                break;
            case MsgBean.DEVICE_REFRESH:        // 服务端：获取所有设备信息 ； 客户端：更新所有设备信息
                if (identify == 1) {    // 服务端
                    List<DeviceBean> list = DBControl.quaryAll(App.context, DeviceBean.class);
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        msgBean = new MsgBean(MsgBean.DEVICE_REFRESH, list.get(i).toString());
                        if (i == size - 1) {
                            msgBean.isLast = true;
                        }
                        thread.sendMsg(msgBean.toString());
                    }
                } else if (identify == 2) {         // 客户端
                    try {
                        DeviceBean deviceBean = new Gson().fromJson(bean.content, DeviceBean.class);
                        for (MsgCallbackListener callback : callbacklistenerList) {
                            callback.onDeviceRefresh(deviceBean, bean.isLast);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MsgBean.DEVICE_DELETE:         // 删除设备
                try {
                    DeviceBean deviceBean = new Gson().fromJson(bean.content, DeviceBean.class);
                    Map<String, Object> map = new HashMap<>();
                    map.put("ip", deviceBean.ip);
                    DBControl.deleteByColumn(this, DeviceBean.class, map);
                    // 回复客户端，服务器成功收的消息
                    msgBean = new MsgBean(MsgBean.DEVICE_ADD_OR_UPDATE_CALLBACK, "删除设备成功!");
                    thread.sendMsg(msgBean.toString());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            case MsgBean.DEVICE_STATUS_BALLBACK:
                try {
                    DeviceBean deviceBean = new Gson().fromJson(bean.content, DeviceBean.class);
                    for (MsgCallbackListener callback : callbacklistenerList) {
                        callback.onDeviceStatusUpdate(deviceBean);
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initData() {
        musicTimerControl = new MusicTimerControl();
        musicTimerControl.setOnMusicStopListener(new MusicTimerControl.OnMusicStopListener() {
            @Override
            public void onMusicStop() {
                MediaControl.getInstance(WifiService.this).stopPlay();
                sendMsg(MediaControl.getInstance(WifiService.this).getPlayingInfo().toString());
            }
        });
    }

    private void initListener() {
        callbacklistenerList = new ArrayList<>();
    }

    private List<MsgCallbackListener> callbacklistenerList;

    public void addMsgCallBackListener(MsgCallbackListener callback) {
        callbacklistenerList.add(callback);
    }

    public void removeCallbackListener(MsgCallbackListener callback) {
        callbacklistenerList.remove(callback);
    }

}
