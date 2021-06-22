package demon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lilanz.tooldemo.R;

import demon.BLELib.BLELibDemoActivity;
import demon.blecommunicate.BleDemoActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import demon.websocket.WebSocketActivity;

public class CopyUseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_use);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_ble_communicate, R.id.bt_ble, R.id.bt_web_socket})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_ble_communicate:
                Intent intent = new Intent(this, BleDemoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_ble:
                intent = new Intent(this, BLELibDemoActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_web_socket:
                intent = new Intent(this, WebSocketActivity.class);
                startActivity(intent);
                break;
        }
    }
}
