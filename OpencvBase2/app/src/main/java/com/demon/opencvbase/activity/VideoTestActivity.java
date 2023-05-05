package com.demon.opencvbase.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.demon.opencvbase.R;
import com.demon.opencvbase.control.PermissionControl;
import com.demon.opencvbase.jni_opencv.jni.AgvDealNative;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoTestActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);
        ButterKnife.bind(this);

        new PermissionControl(this).storagePermission();
    }

    @OnClick(R.id.bt_test)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_test:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                + File.separator + "test" + File.separator + "exa.mp4";
                        String p = AgvDealNative.video2pic(path);
                        showLog("read:" + new File(path).canRead());
                        showLog("图片切割完成：" + p);
                    }
                }.start();
                break;
        }
    }

    private void showLog(String msg) {
        Log.e("VideoTestActivity", msg);
    }
}
