package com.lilanz.videoexchange;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.MainThread;

import java.io.File;

import io.microshow.rxffmpeg.RxFFmpegInvoke;

public class ExchangeControl {

    private Context context;

    public ExchangeControl(Context context) {
        this.context = context;
    }

    public void exchange() {
        // 压缩后文件的输出路径目录
        String destOutDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
        if (!new File(destOutDir).exists()) { // 如果目录不存在，则创建目录
            new File(destOutDir).mkdirs();
        }
        // todo
        String destOutPath = destOutDir + "test.mp4";
        String dis = destOutDir + "out.mp4";

        showLog("地址：" + destOutPath);
        // realPath 是选择的图库的视频文件全路径，替换成要压缩的文件全路径即可
        String text = "ffmpeg -y -i " + destOutPath + " -vf scale=-2:720 -preset superfast " + dis;
//        String text = "ffmpeg -y -i " + destOutPath + " -b:v 500k -r 25 -crf 20 -vcodec libx264 -vf scale=-2:720 -preset superfast " + dis;
//        ffmpeg -y -i /storage/emulated/0/documents/test.mp4 -vf scale=-2:720 -preset superfast /storage/emulated/0/documents/result.mp4
//        ffmpeg -y -i /storage/emulated/0/documents/test.mp4 -b:v 600k -r 15 -crf 20 -vcodec libx264 -vf scale=-2:720 -preset superfast /storage/emulated/0/documents/result1.mp4
        String[] commands = text.split(" ");
        // 这里是同步执行的，直到执行完成，才会走到下一步，也就是说是当前线程同步执行的
        // ret等于0则执行成功，否则执行失败
        showLog("开始转码");
        int ret = RxFFmpegInvoke.getInstance().runCommand(commands, new RxFFmpegInvoke.IFFmpegListener() {
            @Override
            public void onFinish() {
                // 压缩完成...
                showLog("转码完成");
            }

            /**
             *@param progress 当前转化的进度
             */
            @Override
            public void onProgress(int progress, long progressTime) {
//                LogUtils.d("RxFFmpeg---Invoke---onProgress-----progress=" + progress + ",progressTime=" + progressTime);
//                MainThread.run(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (progress >= 0 && progress <= 100) {
////                            showProgress(String.format("视频压缩中%d%%...", progress));
//                        }
//                    }
//                });
                showLog("转码进度：" + progress);
            }

            @Override
            public void onCancel() {
//                MainThread.run(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideProgress();
//                        toastLong("压缩取消");
//                    }
//                });
                showLog("取消转码");
            }

            @Override
            public void onError(String message) {
//                MainThread.run(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideProgress();
//                        toastLong(message);
//                    }
//                });
                showLog("转码报错：" + message);
            }
        });

        showLog("转码结果：" + ret);
    }


    private void showLog(String msg) {
        Log.e("ExchangeControl", msg);
    }
}
