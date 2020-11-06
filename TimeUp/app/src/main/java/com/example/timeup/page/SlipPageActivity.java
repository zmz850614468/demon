package com.example.timeup.page;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;


import com.example.timeup.R;
import com.example.timeup.beans.TypeBean;
import com.example.timeup.services.TiminigService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlipPageActivity extends FragmentActivity {

    @BindView(R.id.tv_page1)
    protected TextView tvPage1;
    @BindView(R.id.tv_page2)
    protected TextView tvPage2;
    @BindView(R.id.tv_page3)
    protected TextView tvPage3;

    @BindView(R.id.view_page)
    protected ViewPager viewPager;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;

    public static List<TypeBean> typeBeanList = new ArrayList<>();  // 执行中的任务

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_page);
        ButterKnife.bind(this);

        initPage();
        initPermission();
//        startBackService();
    }

    @OnClick({R.id.tv_page1, R.id.tv_page2, R.id.tv_page3})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_page1:
                viewPager.setCurrentItem(0);
                selected(0);
                break;
            case R.id.tv_page2:
                viewPager.setCurrentItem(1);
                selected(1);
                break;
            case R.id.tv_page3:
                viewPager.setCurrentItem(2);
                selected(2);
                break;
        }
    }

    private void selected(int index) {
        tvPage1.setTextColor(getResources().getColor(R.color.black));
        tvPage2.setTextColor(getResources().getColor(R.color.black));
        tvPage3.setTextColor(getResources().getColor(R.color.black));
        switch (index) {
            case 0:
                tvPage1.setTextColor(getResources().getColor(R.color.red));
                break;
            case 1:
                tvPage2.setTextColor(getResources().getColor(R.color.red));
                break;
            case 2:
                tvPage3.setTextColor(getResources().getColor(R.color.red));
                break;
        }
    }

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    private void initPage() {
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();

        fragmentList = new ArrayList<>();
        fragmentList.add(fragmentOne);
        fragmentList.add(fragmentTwo);
//        fragmentList.add(new FragmentThr());

        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        selected(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        selected(0);
                        break;
                    case 1:
                        selected(1);
                        fragmentTwo.updateUI();
                        break;
                    case 2:
                        selected(2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    /**
     * 添加计时任务
     *
     * @param name   名称
     * @param during 执行时长 秒
     */
    public static void addTimer(@NonNull String name, int during) {
        TypeBean typeBean = new TypeBean(name, during);

        typeBeanList.add(typeBean);
    }

}
