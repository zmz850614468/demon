package com.demon.opencvbase.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.demon.opencvbase.R;
import com.demon.opencvbase.control.PermissionControl;
import com.demon.opencvbase.jni_opencv.jni.LearningNative;
import com.demon.opencvbase.util.BitmapUtil;
import com.demon.opencvbase.util.FileUtil;
import com.demon.opencvbase.util.ImageSplitterUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SvmTestActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.iv_result)
    ImageView ivResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svm_test);
        ButterKnife.bind(this);

        new PermissionControl(this).storagePermission();
    }

    @OnClick({R.id.bt_test, R.id.bt_split_image, R.id.bt_svm_base})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_svm_base:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        boolean is = LearningNative.svmBase();
                        showLog("建模结果：" + is);
                    }
                }.start();
                break;
            case R.id.bt_split_image:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_110);
                bitmap = BitmapUtil.scaleBitmap(bitmap, 320, 240);
//                bitmap = BitmapUtil.scaleBitmap(bitmap, 320, 134);
                List<Bitmap> list = ImageSplitterUtil.split(bitmap, 1, 24);

                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
                int index = 25;
                for (Bitmap b : list) {
                    String disPath = path + "svm_" + index + ".jpg";
                    BitmapUtil.saveBitmap(this, b, disPath);
                    index++;
                }
                showToast("图片数据保存成功");
//            /storage/emulated/0/Download/svm_1.jpg
//            {
//                122, 122, 122, 122, 122, 122, 122, 122, 123, 123, 123, 123, 123, 123, 123, 123, 124, 124, 124, 124, 124, 124, 124, 124
//            }
                break;
            case R.id.bt_test:
                initSvmTest();
                break;
        }
    }


    Bitmap bitmap;

    private void initSvmTest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
//                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.test_101);
                bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                LearningNative.svmTest(null, bitmap);
                runOnUiThread(() -> ivResult.setImageBitmap(bitmap));
            }
        }.start();

    }

    /**
     * 用svm识别图片轨迹中点
     * 1.切割图片
     * 2.找到图片轨迹中点
     * 3.用现有资源，训练模型
     * 4.验证结果
     */


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("SvmTestActivity", msg);
    }
}
