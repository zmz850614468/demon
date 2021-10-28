package com.demon.tool.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.controls.PermissionControl;
import com.demon.tool.thread.SaveDataThread;
import com.demon.tool.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_save);
        ButterKnife.bind(this);

        new PermissionControl(this).storagePermission();
    }

    int index;

    @OnClick(R.id.bt_add_data)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_add_data:
                String time = StringUtil.getDataStr();
                SaveDataThread.DataBean bean = null;
                switch (index % 4) {
                    case 0:
                        bean = new SaveDataThread.DataBean(SaveDataThread.CHILE_FILE_1,
                                "test-2021-10-14.txt", time + "：" + ++index);
                        break;
                    case 1:
                        bean = new SaveDataThread.DataBean(SaveDataThread.CHILE_FILE_1,
                                "test-2021-10-15.txt", time + "：" + ++index);
                        break;
                    case 2:
                        bean = new SaveDataThread.DataBean(SaveDataThread.CHILE_FILE_2,
                                "test-2021-10-14.txt", time + "：" + ++index);
                        break;
                    case 3:
                        bean = new SaveDataThread.DataBean(SaveDataThread.CHILE_FILE_2,
                                "test-2021-10-15.txt", time + "：" + ++index);
                        break;
                }
                SaveDataThread.getInstance().addData(bean);
                break;
        }
    }
}
