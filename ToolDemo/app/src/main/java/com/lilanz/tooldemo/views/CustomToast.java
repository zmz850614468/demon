package com.lilanz.tooldemo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;

public class CustomToast {

    private static CustomToast customToast;

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    private static CustomToast getInstance(Context context) {
        if (customToast == null) {
            customToast = new CustomToast(context);
        }
        return customToast;
    }

    private Toast toast;
    private Context context;
    private TextView tvTip;

    private CustomToast(Context context) {
        this.context = context;
        toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_type_1, null);
        tvTip = view.findViewById(R.id.tv_tip);
        toast.setView(view);
    }

    /**
     * 显示提示信息
     *
     * @param tip
     */
    public void show(String tip) {
        tvTip.setTextColor(context.getResources().getColor(R.color.black));
        tvTip.setText(tip);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 错误信息的提醒
     *
     * @param tip
     */
    public void errShow(String tip) {
        tvTip.setTextColor(context.getResources().getColor(R.color.red));
        tvTip.setText(tip);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示提醒信息
     *
     * @param context
     * @param tip
     */
    public static void show(Context context, String tip) {
        getInstance(context).show(tip);
    }

    /**
     * 错误提醒信息
     *
     * @param context
     * @param tip
     */
    public static void errShow(Context context, String tip) {
        getInstance(context).errShow(tip);
    }

}
