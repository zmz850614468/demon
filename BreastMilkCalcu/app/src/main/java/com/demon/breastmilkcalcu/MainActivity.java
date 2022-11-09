package com.demon.breastmilkcalcu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.demon.breastmilkcalcu.bean.BreastMilkBean;
import com.demon.breastmilkcalcu.control.PermissionControl;
import com.demon.breastmilkcalcu.util.ClipboardUtil;
import com.demon.breastmilkcalcu.util.StringUtil;
import com.demon.breastmilkcalcu.view.MainUi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private MainUi mainUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainUi = new MainUi(this);

        new PermissionControl(this).storagePermission();
    }

    /**
     * 粘贴并处理数据
     */
    public void pasteContent() {
        String content = ClipboardUtil.getContent(this);
//        showLog("处理前的数据：" + content);
        content = content.replace(".", " ");
        content = content.replace(",", " ");
        content = content.replace("、", " ");
        content = content.replace("\t", "");
        content = content.replace("\n", ";");

        while (content.contains("  ")) {
            content = content.replace("  ", " ");
        }
        while (content.contains(";;")) {
            content = content.replace(";;", ";");
        }

//        showLog("处理后的数据：" + content);
        if (StringUtil.isEmpty(content)) {
            showToast("数据不能为空");
            return;
        }

        List<BreastMilkBean> breastMilkList = new ArrayList<>();
        BreastMilkBean breastMilkBean = null;

        String[] lineStr = content.split(";");
        List<String> bedNumList = new ArrayList<>();
        for (String line : lineStr) {
            breastMilkBean = new BreastMilkBean();
            breastMilkBean.todayData = line;
            String[] bedNum = breastMilkBean.todayData.split(" ");
            if (bedNumList.isEmpty()) {
                bedNumList.addAll(Arrays.asList(bedNum));
                showLog("第一行数据：" + new Gson().toJson(bedNumList));
            } else {
                String newBedNum = "";
                for (String s : bedNum) {
                    if (!bedNumList.contains(s)) {
                        bedNumList.add(s);
                        newBedNum += s + ",";
                    }
                }
                breastMilkBean.todayAppend = newBedNum;
                showLog("新添加数据：" + newBedNum);
            }
            breastMilkList.add(breastMilkBean);
        }

        mainUi.updateData(breastMilkList, bedNumList.size());
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
