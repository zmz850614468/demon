package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.lilanz.wificonnect.utils.FileUtil;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.StringUtil;

import java.io.File;

public class InfoFileControl {

    private Context context;
    private String basePath;            // 日志文件夹路径

    private StringBuffer infoBuffer;    // 日志缓存
    private String lastSaveDay;         // 上次保存日志的时间(按天计算)
    private String filePath;            // 日志文件路径

    private static InfoFileControl infoFileControl;

    public static InfoFileControl getInstance(Context context) {
        if (infoFileControl == null) {
            synchronized (context) {
                if (infoFileControl == null) {
                    infoFileControl = new InfoFileControl(context);
                }
            }
        }

        return infoFileControl;
    }


    private InfoFileControl(Context context) {
        this.context = context;
        infoBuffer = new StringBuffer();
        lastSaveDay = SharePreferencesUtil.getLastSaveDay(this.context);
        basePath = Environment.getExternalStorageDirectory() + "/AInfo";
    }

    /**
     * 添加新的日志信息
     *
     * @param info
     */
    public void addInfo(String info) {
        String dayStr = StringUtil.getDay();
        String dateStr = StringUtil.getDataStr();

        // 不是同一天的时间信息时
        if (!dayStr.equals(lastSaveDay)) {
            // 1. 保存之前的日志
            if (infoBuffer.length() > 0) {
                FileUtil.appendFile(new File(filePath), infoBuffer.toString());
                infoBuffer.setLength(0);
            }

            // 2. 创建新的日志文件
            File file = FileUtil.createIfNotExit(basePath, dayStr + ".txt");
            if (file == null) {
                showToast("创建文件失败！");
                return;
            }

            filePath = file.getAbsolutePath();
            // 3. 保存新的日志时间
            lastSaveDay = dayStr;
            SharePreferencesUtil.saveLastSaveDay(context, lastSaveDay);
        }

        // 添加信息
        infoBuffer.append(dateStr).append(" : ").append(info).append("\n");

        // 信息比较多时，保存信息
//        if (infoBuffer.length() > 2048) {
        if (filePath == null) {
            File file = FileUtil.createIfNotExit(basePath, dayStr + ".txt");
            if (file == null) {
                showToast("创建文件失败！");
                return;
            }
            filePath = file.getAbsolutePath();
        }

        FileUtil.appendFile(new File(filePath), infoBuffer.toString());
        infoBuffer.setLength(0);
//        }
    }

    public void close() {
        if (infoBuffer.length() > 0 && filePath != null) {
            FileUtil.appendFile(new File(filePath), infoBuffer.toString());
            infoBuffer.setLength(0);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
