package com.demon.agv.control_ui;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.demon.agv.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * 主界面的UI控制类
 */
public class MianUiControl {

    @BindView(R.id.tv_up)
    TextView tvUp;
    @BindView(R.id.tv_down)
    TextView tvDown;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private Activity activity;

    public MianUiControl(Activity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);

//        tvUp.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });
    }


}
