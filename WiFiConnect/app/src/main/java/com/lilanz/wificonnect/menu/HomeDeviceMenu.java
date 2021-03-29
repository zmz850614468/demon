package com.lilanz.wificonnect.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.AddDeviceActivity;
import com.lilanz.wificonnect.activity_new.AppSettingActivity;
import com.lilanz.wificonnect.activity_new.SearchDeviceActivity;
import com.lilanz.wificonnect.activitys.SettingActivity;
import com.lilanz.wificonnect.esptouch.EsptouchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 家庭设备类，对应的滑动菜单
 */
public class HomeDeviceMenu {

    public static void add(Activity activity) {
        new HomeDeviceMenu(activity);
    }

    private Activity activity;
    private SlidingMenu menu;

    private HomeDeviceMenu(Activity activity) {
        this.activity = activity;
        menu = new SlidingMenu(activity);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_menu);
        ButterKnife.bind(this, menu);
    }

    @OnClick({R.id.tv_search_device_menu, R.id.tv_add_device_menu, R.id.tv_smart_config_menu,
            R.id.tv_setting_menu})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_search_device_menu:
                Intent intent = new Intent(activity, SearchDeviceActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.tv_add_device_menu:
                intent = new Intent(activity, AddDeviceActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.tv_smart_config_menu:
                intent = new Intent(activity, EsptouchActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.tv_setting_menu:
                intent = new Intent(activity, AppSettingActivity.class);
                activity.startActivity(intent);
                break;
        }
        menu.toggle();
    }
}
