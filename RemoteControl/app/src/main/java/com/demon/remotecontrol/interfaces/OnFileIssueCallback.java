package com.demon.remotecontrol.interfaces;

public interface OnFileIssueCallback {

    void onBeginReceiver(String msg);

    void onReceiverComplete(String msg);
}
