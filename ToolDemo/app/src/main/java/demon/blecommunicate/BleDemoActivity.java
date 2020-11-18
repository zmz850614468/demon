package demon.blecommunicate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.RootUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleDemoActivity extends Activity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.et_input)
    EditText etInput;
    private BleCommunicateDialog listDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_demo);
        ButterKnife.bind(this);

        initDialog();
    }

    @OnClick(R.id.bt_ble_com)
    public void onBleClicked(View v) {
        listDialog.show();
    }

    @OnClick(R.id.tv_send)
    public void onSendClicked(View v) {
        String msg = etInput.getText().toString();
        if (StringUtil.isEmpty(msg)) {
            showToast("输入内容不能为空!");
            return;
        }

        listDialog.sendMsg(msg);
        etInput.setText("");
    }

    @Override
    protected void onDestroy() {
        listDialog.onDestroy();
        super.onDestroy();
    }

    private void initDialog() {
        listDialog = new BleCommunicateDialog(this, R.style.DialogStyleOne);
        listDialog.show();
        listDialog.dismiss();

        listDialog.setOnBleCallBackListener(new BleCommunicateDialog.OnBleCallBackListener() {
            @Override
            public void onCallBack(String msg) {
                showMsg(msg);
            }
        });

        if (RootUtil.isRoot(this)) {
            listDialog.setAutoService(true);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
