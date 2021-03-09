package com.lilanz.tooldemo.multiplex.download;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lilanz.tooldemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import demon.controls.PermissionControl;
import okhttp3.Call;
import okhttp3.Response;

public class DownloadActivity extends AppCompatActivity {

    private List<String> imgList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        new PermissionControl(this).storagePermission();
    }

    @OnClick(R.id.tv_download_1)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_download_1:
                String dis = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/IM";
                File file = new File(dis);
                if (!file.exists()) {
                    file.mkdirs();
                }

                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4");
                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/LightBusiness.mp4");
                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/pad/NewBusiness.mp4");
//                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showcase.mp4");
//                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/LightBusiness.mp4");
//                imgList.add("https://oos-fj2.ctyunapi.cn/lilanz/showVideos/20213/pad/NewBusiness.mp4");

                imgList.add("https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/9c16fdfaaf51f3de9ba8ee1194eef01f3a2979a8.jpg");
                imgList.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=4277010421,1238629898&fm=11&gp=0.jpg");
                imgList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1692236545,1995714621&fm=11&gp=0.jpg");
//                DownloadThread downloadThread = new DownloadThread(this, imgList, msgHandle);
//                downloadThread.start();
                DownloadDialog dialog = new DownloadDialog(this, R.style.DialogStyleOne);
                dialog.startDownload(imgList);
                dialog.show();
//                dialog.dismiss();


                break;
        }
    }

    private Handler msgHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadThread.MSG_UPDATE:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    String url = map.get("url");
                    String present = map.get("present");
                    update(url, present);
                    break;
                case DownloadThread.MSG_FAILURE:
                    url = (String) msg.obj;
                    failure(url);
                    break;
                case DownloadThread.MSG_COMPLETE:
                    map = (Map<String, String>) msg.obj;
                    url = map.get("url");
                    String savePath = map.get("savePath");
                    complete(url, savePath);
                    break;

            }
            return true;
        }

        private void update(String url, String present) {
//            showLog(url + " : " + present);
        }

        private void failure(String url) {
            showLog(url + " : failure");
        }

        private void complete(String url, String savePath) {
            try {
                if (MediaFile.isImageFileType(savePath)) {
                    MediaStore.Images.Media.insertImage(DownloadActivity.this.getContentResolver(), savePath, savePath, null);
                } else if (MediaFile.isVideoFileType(savePath)) {
                    //是否添加到相册
                    ContentResolver localContentResolver = DownloadActivity.this.getContentResolver();
                    ContentValues localContentValues = MediaFile.getVideoContentValues(DownloadActivity.this, new File(savePath), System.currentTimeMillis());
                    localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            showLog(url + " To " + savePath);
        }
    });


//    private void download(String url, String saveFilePathAndName) {
//        showLog("thread:" + Thread.currentThread().getId());
//        DownloadControl okHttpDownUtil = new DownloadControl();
//        File disFile = new File(saveFilePathAndName);
//        okHttpDownUtil.getDownRequest(url, disFile, new DownloadListener() {
//            @Override
//            public void onResponse(Call call, Response response, long totalLength, long alreadyDownLength) {
//            }
//
//            @Override
//            public void onFailure(Call call, Exception e) {
//            }
//
//            @Override
//            public void onComplete() {
//                showLog("完成下载:" + Thread.currentThread().getId());
//                try {
//                    MediaStore.Images.Media.insertImage(DownloadActivity.this.getContentResolver(), saveFilePathAndName, saveFilePathAndName, null);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        showLog("main:" + Thread.currentThread().getId());
//    }

    private void showLog(String msg) {
        Log.e("download", msg);
    }
}
