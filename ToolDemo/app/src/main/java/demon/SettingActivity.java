package demon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.SharePreferencesUtil;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @BindView(R.id.tv_exit)
    TextView tvExit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_update_check, R.id.tv_exit})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_update_check:
                Beta.checkUpgrade();
                break;
            case R.id.tv_exit:

//                Intent intent = new Intent(SettingActivity.this, WelcomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                break;
        }
    }


}
