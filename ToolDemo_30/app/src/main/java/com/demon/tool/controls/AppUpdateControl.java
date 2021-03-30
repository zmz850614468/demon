package com.demon.tool.controls;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.demon.tool.API.APIRequest;
import com.demon.tool.API.ParseListener;
import com.demon.tool.R;
import com.demon.tool.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用升级控制类
 */
public class AppUpdateControl {

    private Activity activity;

    public AppUpdateControl(Activity context) {
        this.activity = context;
    }

    private APIRequest<String> updateRequest;

    /**
     * 检测版本更新
     */
    public void checkUpdate() {
        // 有读写权限;
        if (new PermissionControl(activity).checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            updateRequest = new APIRequest<>(String.class);
            updateRequest.setRequestBasePath("TODO");
            updateRequest.setParseListener(new ParseListener<String>() {
                @Override
                public void jsonResult(String jsonStr) {
                    super.jsonResult(jsonStr);
                    // TODO 判断是否要更新应用
//                    showUpdataDialog(null);
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showLog(errMsg + " ： " + errMsg);
                }
            });

            Map<String, Object> map = new HashMap<>();
            updateRequest.requestNormal(map, "checkAppUpdate", APIRequest.PARSE_TYPE_JSON);

//            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
//            path += File.separator + "im.apk";
//            if (new File(path).exists()) {
//                installApk(path);
//            }
        }
    }

    /**
     * 显示应用下载弹框
     */
    private void showUpdataDialog(final String apkPath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("应用升级")
                .setCancelable(false)
                .setMessage("检测到新版本，是否更新")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 下载安装包

//                        installApk(apkPath);
                    }
                });
        builder.create().show();
    }

    // TODO
//    private void downloadApp(String apkPath) {
//        List<String> list = new ArrayList<>();
//        list.add(apkPath);
//        DownloadDialog dialog = new DownloadDialog(activity, R.style.DialogStyleOne);
//        dialog.setOnDownloadCompleteListener(new DownloadDialog.OnDownloadCompleteListener() {
//            @Override
//            public void onComplete(String url, String disPath) {
//                installApk(disPath);
//            }
//
//            @Override
//            public void onAllComplete() {
//            }
//        });
//        dialog.startDownload(list);
//        dialog.show();
//        dialog.dismiss();
//    }

    /**
     * 安装应用包
     *
     * @param path
     */
    private void installApk(String path) {
        if (StringUtil.isEmpty(path)) {
            return;
        }
        File apk = new File(path);
        if (apk.exists() && path.endsWith(".apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(activity, "com.lilanz.im.fileProvider", apk);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
            }
            activity.startActivity(intent);
        }
    }

    private void showLog(String msg) {
        Log.e("appUpdateCheck", msg);
    }
}
