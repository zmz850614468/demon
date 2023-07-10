package com.demon.fit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.fit.R;
import com.demon.fit.bean.CostBean;
import com.demon.fit.util.SharePreferencesUtil;
import com.demon.fit.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TipSelectDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.tv_voice_tip)
    public TextView tvVoiceTip;
    @BindView(R.id.tv_vibrator_tip)
    public TextView tvVibratorTip;
    private Context context;
    private boolean voiceTip;
    private boolean vibratorTip;

    public TipSelectDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_tip);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initData();
    }

    @OnClick({R.id.tv_voice_tip, R.id.tv_vibrator_tip})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_voice_tip:
                voiceTip = !voiceTip;
                changeBg(tvVoiceTip, voiceTip);
                SharePreferencesUtil.saveVoiceTip(context, voiceTip);
                break;
            case R.id.tv_vibrator_tip:
                vibratorTip = !vibratorTip;
                changeBg(tvVibratorTip, vibratorTip);
                SharePreferencesUtil.saveVibratorTip(context, vibratorTip);
                break;
        }
    }

    private void initData() {
        voiceTip = SharePreferencesUtil.getVoiceTip(context);
        vibratorTip = SharePreferencesUtil.getVibratorTip(context);
        changeBg(tvVoiceTip, voiceTip);
        changeBg(tvVibratorTip, vibratorTip);
    }

    private void changeBg(TextView tv, boolean selected) {
        if (selected) {
            tv.setBackgroundResource(R.drawable.shape_box_red);
        } else {
            tv.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
