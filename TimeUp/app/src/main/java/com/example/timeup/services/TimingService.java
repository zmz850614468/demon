package com.example.timeup.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.timeup.beans.TypeBean;
import com.example.timeup.controls.MediaControl;
import com.example.timeup.page.App;
import com.example.timeup.page.FragmentTwo;
import com.example.timeup.page.SlipPageActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimingService extends Service {

    private Timer timer;
    private List<TypeBean> typeBeanList;

    public class TimingBind extends Binder {
        public TimingService getService() {
            return TimingService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        typeBeanList = SlipPageActivity.typeBeanList;

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                for (TypeBean bean : typeBeanList) {
                    bean.during--;
                    if (bean.during == 0) {
                        MediaControl.getInstance(TimingService.this).playRandon();

                        Intent intent = new Intent(TimingService.this, SlipPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(intent);

                    }
                    if (listener != null && !FragmentTwo.isDestroy) {
                        listener.onUpdate();
                    }
                    showLog("倒计时：" + bean.during);
                }
            }
        }, 1000, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public TimingBind onBind(Intent intent) {
        return new TimingBind();
    }

    @Override
    public void onDestroy() {
        showLog("service onDestroy");
        super.onDestroy();
    }

    private void showLog(String msg){
        if (!App.isDebug) {
            Log.e("timeup", msg);
        }
    }

    private OnUpdateUIListener listener;

    public void setListener(OnUpdateUIListener listener) {
        this.listener = listener;
    }

    public interface OnUpdateUIListener {
        void onUpdate();
    }
}
