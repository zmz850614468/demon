package com.demo.addcontact.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.addcontact.R;
import com.demo.addcontact.contol.ContactControl;
import com.demo.addcontact.contol.PermissionControl;
import com.demo.addcontact.util.PhoneUitl;
import com.demo.addcontact.util.RandInfo;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).contactsPermission();

    }

    @OnClick(R.id.tv_test)
    public void onClicked(View v) {
//        ContactControl.addContact(this, "aaa", "18060666666");

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            String phone = PhoneUitl.createMobile(random.nextInt(3));
            String name = RandInfo.getNameBySex(random.nextInt(2) == 1 ? "男" : "女");

            ContactControl.addContact(this, name, phone);
//            showLog("name = " + name + " -- phone = " + phone);
        }

    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}