package com.demon.agv.debug;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static Context mcontext;
    private static String PROJECT = "SpSave";//我用项目名当作大标识

    public static void initSp(Context context) {
        mcontext = context;
    }

    public static SharedPreferences getSp(String file) {
        SharedPreferences sp = mcontext.getSharedPreferences(PROJECT + file, 0);
        return sp;
    }

    /**
     * 保存上传的文件
     *
     * @param file
     */
    public static void saveFile(String file, String fileName) {
        SharedPreferences sp = getSp(file);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("filename", fileName);
        editor.commit();
    }

    /**
     * 获取文件名
     *
     * @param file
     * @return
     */
    public static String getFileName(String file) {
        SharedPreferences sp = getSp(file);
        String filename = sp.getString("filename", "null");
        return filename;
    }
}
//5、因为sp要初始化，所以，我们需要自己写一个MyApplication 来继承系统的Application，同时，里面需要初始化两个类，代码如下：
//
//public class MyApplication extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        CrashHandler.getInstance().init(this);
//        SpUtil.initSp(this);
//    }
//}
