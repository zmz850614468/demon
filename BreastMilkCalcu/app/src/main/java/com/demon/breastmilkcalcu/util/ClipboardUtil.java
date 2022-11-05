package com.demon.breastmilkcalcu.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class ClipboardUtil {

    /**
     * 获取剪切板内容
     *
     * @return
     */
    public static String getContent(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
//            showLog(manager.hasPrimaryClip() + "");
//            showLog(manager.getPrimaryClip().getItemCount() + "");
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    private static void showLog(String msg) {
        Log.e("ClipboardUtil", msg);
    }
}
