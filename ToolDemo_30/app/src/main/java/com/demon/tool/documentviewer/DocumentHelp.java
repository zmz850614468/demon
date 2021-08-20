package com.demon.tool.documentviewer;

import android.content.Context;
import android.util.Log;

import com.demon.tool.activity.App;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

public class DocumentHelp {

    public static boolean isX5InitSucceed;

    /**
     * x5內核初始化
     *
     * @param context
     */
    public void init(Context context) {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);

        // 搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.setTbsListener(
                new TbsListener() {
                    @Override
                    public void onDownloadFinish(int i) {
                        showLog("onDownloadFinish -->下载X5内核完成：" + i);
                    }

                    @Override
                    public void onInstallFinish(int i) {
                        showLog("onInstallFinish -->安装X5内核进度：" + i);
                    }

                    @Override
                    public void onDownloadProgress(int i) {
                        showLog("onDownloadProgress -->下载X5内核进度：" + i);
                    }
                });
        QbSdk.setDownloadWithoutWifi(true);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                if (arg0) {
                    showLog("X5内核初始化成功");
                } else {
                    showLog("X5内核初始化失败");
                }
                isX5InitSucceed = arg0;
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context, cb);
    }

    private void showLog(String msg) {
        Log.e("documentHelp", msg);
    }
}
