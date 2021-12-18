package com.demon.opencvbase.control_ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.demon.opencvbase.R;
import com.demon.opencvbase.activity.AgvImgDealActivity;
import com.demon.opencvbase.jni_opencv.jni.AgvDealNative;
import com.demon.opencvbase.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgvTestUi {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    @BindView(R.id.iv_pic1)
    ImageView ivPic1;
    @BindView(R.id.iv_pic2)
    ImageView ivPic2;
    @BindView(R.id.iv_pic3)
    ImageView ivPic3;

    private AgvImgDealActivity activity;

    public AgvTestUi(AgvImgDealActivity activity) {
        this.activity = activity;
        ButterKnife.bind(this, activity);
    }

    public void testBitmap(Bitmap bitmap) {
        ivPic1.setImageBitmap(bitmap);
        agvDeal(bitmap);
    }

    int index = 0;

    private void agvDeal(Bitmap src) {
        Bitmap des = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        String result = AgvDealNative.bitmapTest(src, des);
        index++;
        if (!StringUtil.isEmpty(result) && result.contains("&")) {
            String str[] = result.split("&");
            activity.showMsg("test - (" + str[0] + "," + str[1] + ") : " + index);
            showLog("test - (" + str[0] + "," + str[1] + ") : " + index);
        } else {
            activity.showMsg("test - 没有找到垂点:" + result + " ; " + index);
            showLog("test - 没有找到垂点:" + result + " ; " + index);
        }
        ivPic2.setImageBitmap(des);

        showLog("center: " + result);
    }

    private void showLog(String msg) {
        Log.e("AgvTestUi", msg);
    }

}
