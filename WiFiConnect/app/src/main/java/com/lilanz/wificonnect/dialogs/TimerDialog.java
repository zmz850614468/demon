package com.lilanz.wificonnect.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.controls.MediaControl;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TimerDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.rg_stop_mode)
    RadioGroup rgStopMode;
    @BindView(R.id.et_count)
    EditText etCount;

    private Context context;
    private int selectedMode;

    public TimerDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void updateUI() {
        int stopMode = SharePreferencesUtil.getStopMode(context);
        switch (stopMode) {
            case MediaControl.STOP_NONE:
                rgStopMode.check(R.id.rb_nothing);
                break;
            case MediaControl.STOP_TIME:
                rgStopMode.check(R.id.tb_time);
                break;
            case MediaControl.STOP_SONG_COUNT:
                rgStopMode.check(R.id.rb_song_count);
                break;
        }
    }

    private void initUI() {
        rgStopMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_nothing:
                        etCount.setVisibility(View.INVISIBLE);
                        selectedMode = MediaControl.STOP_NONE;
                        break;
                    case R.id.tb_time:
                        int time = SharePreferencesUtil.getStopTime(context);
                        etCount.setText(time + "");
                        etCount.setVisibility(View.VISIBLE);
                        selectedMode = MediaControl.STOP_TIME;
                        break;
                    case R.id.rb_song_count:
                        int songCount = SharePreferencesUtil.getStopSongCount(context);
                        etCount.setText(songCount + "");
                        etCount.setVisibility(View.VISIBLE);
                        selectedMode = MediaControl.STOP_SONG_COUNT;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        SharePreferencesUtil.saveStopMode(context, selectedMode);

        int count = 0;
        try {
            count = Integer.parseInt(etCount.getText().toString());
            if (selectedMode == MediaControl.STOP_TIME) {
                SharePreferencesUtil.saveStopTime(context, count);
            } else if (selectedMode == MediaControl.STOP_SONG_COUNT) {
                SharePreferencesUtil.saveStopSongCount(context, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onConfirm(selectedMode, count);
        }
        dismiss();
    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        void onConfirm(int stopMode, int count);
    }
}
