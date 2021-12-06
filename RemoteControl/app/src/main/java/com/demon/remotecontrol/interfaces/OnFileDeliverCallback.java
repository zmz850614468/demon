package com.demon.remotecontrol.interfaces;

/**
 * 文件传输回调
 */
public interface OnFileDeliverCallback {

    void onFileNotFind(String msg);

    void onError(String msg);

    void onComplete(String msg);

    void onStartReceiver(String data);
}
