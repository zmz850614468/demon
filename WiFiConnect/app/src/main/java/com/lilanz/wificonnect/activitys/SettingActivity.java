package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.utils.BuildUtil;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.et_wifi_name)
    EditText etIp;
    @BindView(R.id.et_inside_ip)
    EditText etInsideIp;
    @BindView(R.id.et_wifi_pwd)
    EditText etPort;
    @BindView(R.id.rb_ip)
    RadioButton rbIp;
    @BindView(R.id.rb_inside_ip)
    RadioButton rbInsideIp;
    @BindView(R.id.tv_update_check)
    TextView tvUpdateCheck;
    @BindView(R.id.et_voice_sensitivity)
    EditText etVoiceSensitivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initUI();
    }

    @OnClick({R.id.tv_update_check, R.id.tv_exit})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_update_check:
                Beta.checkUpgrade();
                break;
            case R.id.tv_exit:
                SharePreferencesUtil.saveBecome(this, "");
                Intent intent = new Intent(SettingActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    private void initUI() {
        String become = SharePreferencesUtil.getBecome(this);
        tvExit.setText("退出" + become);

        etIp.setText(SharePreferencesUtil.getServiceIp(this));
        etInsideIp.setText(SharePreferencesUtil.getInsideServiceIp(this));
        etPort.setText(SharePreferencesUtil.getServicePort(this) + "");

        String selectedIP = SharePreferencesUtil.getSelectedIpType(this);
        if (selectedIP.equals("局域网")) {
            rbInsideIp.setChecked(true);
        } else {
            rbIp.setChecked(true);
        }

        rbInsideIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbIp.setChecked(false);
                }
                SharePreferencesUtil.saveSelectedIpType(SettingActivity.this, "局域网");
            }
        });
        rbIp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbInsideIp.setChecked(false);
                }
                SharePreferencesUtil.saveSelectedIpType(SettingActivity.this, "广域网");
            }
        });

        int voiceSensitivity = SharePreferencesUtil.getVoiceSensitivity(this);
        etVoiceSensitivity.setText(voiceSensitivity + "");

        tvUpdateCheck.append("(" + BuildUtil.getVersionName(this) + "." + BuildUtil.getVersionCode(this) + ")");
    }

    @Override
    protected void onDestroy() {
        SharePreferencesUtil.saveServiceIp(this, etIp.getText().toString());
        SharePreferencesUtil.saveInsideServiceIp(this, etInsideIp.getText().toString());
        if (rbIp.isChecked()) {
            SharePreferencesUtil.saveSelectedIpType(this, "广域网");
        } else {
            SharePreferencesUtil.saveSelectedIpType(this, "局域网");
        }

        try {
            int port = Integer.parseInt(etPort.getText().toString());
            SharePreferencesUtil.saveServicePort(this, port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String voiceStr = etVoiceSensitivity.getText().toString();

        try {
            int voiceSensitivity = Integer.parseInt(voiceStr);
            if (voiceSensitivity < 1) {
                voiceSensitivity = 1;
            } else if (voiceSensitivity > 3000) {
                voiceSensitivity = 3000;
            }
            SharePreferencesUtil.saveVoiceSensitivity(this, voiceSensitivity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }
}
