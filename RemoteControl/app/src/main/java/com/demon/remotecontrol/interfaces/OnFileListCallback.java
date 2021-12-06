package com.demon.remotecontrol.interfaces;

import com.demon.remotecontrol.bean.FileInfoBean;

import java.util.List;

public interface OnFileListCallback {
    void onResult(List<FileInfoBean> list);
}
