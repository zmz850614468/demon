package com.demon.agv.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.demon.agv.util.PublicFileUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;

public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getSimpleName();

    private static final String SINGLE_RETURN = "\n";
    private static final String SINGLE_LINE = "--------------------------------";

    private static CrashHandler mCrashHandler;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;
    private StringBuffer mErrorLogBuffer = new StringBuffer();
    private String format;
    private String path;

    /**
     * 获取CrashHandler实例，单例模式。
     *
     * @return 返回CrashHandler实例
     */
    public static CrashHandler getInstance() {
        if (mCrashHandler == null) {
            synchronized (CrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new CrashHandler();
                }
            }
        }

        return mCrashHandler;
    }

    public void init(Context context) {
        mContext = context;
        // 获取系统默认的uncaughtException处理类实例
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置成我们处理uncaughtException的类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.d(TAG, "uncaughtException:" + ex);
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理异常就由系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    //处理异常事件
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出." + ex.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }
        }).start();
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 收集错误日志
        collectCrashInfo(ex);
        // 保存错误日志
        saveErrorLog();
        //TODO: 这里可以加一个网络的请求，发送错误log给后台
//        sendErrorLog(sendNetUrl);
        return true;
    }

    //保存日志到/mnt/sdcard/AppLog/目录下，文件名已时间yyyy-MM-dd_hh-mm-ss.log的形式保存
    private void saveErrorLog() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            format = sdf.format(new Date()) + ".log";
            path = Environment.DIRECTORY_DOCUMENTS + "/agvDebug";
            // 写日志到文件中去
            PublicFileUtil.saveTxt2Public(mContext, path, format, mErrorLogBuffer.toString(), "wa");
        }
        Log.e("liushengjie", "over");
    }

    //收集错误信息
    private void collectCrashInfo(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();
        //将错误信息加入mErrorLogBuffer中
        append("", result);
        mErrorLogBuffer.append(SINGLE_LINE + SINGLE_RETURN);
    }

    //收集应用和设备信息
    private void collectDeviceInfo(Context context) {
        //每次使用前，清掉mErrorLogBuffer里的内容
        mErrorLogBuffer.setLength(0);
        mErrorLogBuffer.append(SINGLE_RETURN + SINGLE_LINE + SINGLE_RETURN);
        //获取应用的信息
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                append("versionCode", pi.versionCode);
                append("versionName", pi.versionName);
                append("packageName", pi.packageName);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        mErrorLogBuffer.append(SINGLE_LINE + SINGLE_RETURN);
        //获取设备的信息
        Field[] fields = Build.class.getDeclaredFields();
        getDeviceInfoByReflection(fields);
        fields = Build.VERSION.class.getDeclaredFields();
        getDeviceInfoByReflection(fields);
        mErrorLogBuffer.append(SINGLE_LINE + SINGLE_RETURN);
    }

    //获取设备的信息通过反射方式
    private void getDeviceInfoByReflection(Field[] fields) {
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                append(field.getName(), field.get(null));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //mErrorLogBuffer添加友好的log信息
    private void append(String key, Object value) {
        mErrorLogBuffer.append("" + key + ":" + value + SINGLE_RETURN);
    }

    /**
     * 设置最大日志数量 10
     *
     * @param logDir 日志目录
     */
    private void clearExLogWhenMax(File logDir) {
        File[] logFileList = logDir.listFiles();
        if (logFileList == null || logFileList.length == 0) {
            return;
        }
        int length = logFileList.length;
        if (length >= 5) {
            for (File aFile : logFileList) {
                try {
                    if (aFile.delete()) {
                        Log.d(TAG, "clearExLogWhenMax:" + aFile.getName());
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "clearExLogWhenMax:" + ex);
                }
            }
        }
    }

    /**
     * 上传日志文件
     *
     * @param url
     */
    private void sendErrorLog(String url) {
        //创建OkHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        File file = new File(path, format);
    }
}
