package demon.BLELib;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.SerialPortUtil;
import com.lilanz.tooldemo.utils.SoftKeyboardUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BLELibDemoActivity extends Activity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.et_input)
    EditText etInput;

    private BLELibDialog bleLibDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_lib_demo);
        ButterKnife.bind(this);

        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftKeyboardUtil.hideSoftKeyboard(BLELibDemoActivity.this);
            }
        }, 100);
    }

    @OnClick({R.id.bt_ble_com, R.id.tv_send})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_ble_com:
                bleLibDialog.show();
                break;
            case R.id.tv_send:
                String msg = etInput.getText().toString();
                if (!StringUtil.isEmpty(msg)) {
                    bleLibDialog.send(SerialPortUtil.hexStr2Byte(msg));
                }
                break;
        }
    }

    private void initDialog() {
        bleLibDialog = new BLELibDialog(this, R.style.DialogStyleOne);
        bleLibDialog.show();
        bleLibDialog.dismiss();

        bleLibDialog.setOnBleCallBackListener(new BLELibDialog.OnBleCallBackListener() {
            @Override
            public void onCallBack(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(msg);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        bleLibDialog.onDestroy();
        super.onDestroy();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

}
