package com.lilanz.tooldemo.multiplex.API;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.beans.LoginBean;
import com.lilanz.tooldemo.beans.NumberBean;
import com.lilanz.tooldemo.beans.UploadBean;
import com.lilanz.tooldemo.utils.BitmapUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class APIActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        ButterKnife.bind(this);

        // 保存图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_setting);

        File dir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        dir.mkdirs();
        String imagePath = new File(dir, "test.jpg").getAbsolutePath();
        BitmapUtil.saveBitmap(this, bitmap, imagePath);
    }

    private APIRequest<LoginBean> hasAuthority;

    /**
     * Query 请求方式
     * 查看是否有登录权限
     *
     * @param v
     */
    @OnClick(R.id.bt_has_authority)
    public void hasAuthorityRequest(View v) {
        if (hasAuthority == null) {
            hasAuthority = new APIRequest<>(LoginBean.class);
            hasAuthority.setRequestBasePath(APIManager.LOGIN_BASE_PATH);
            hasAuthority.setParseListener(new ParseListener<LoginBean>() {
                @Override
                public void jsonParsed(LoginBean bean) {
                    // 获取Object数据
                    showMsg(bean.toString());
                }

                @Override
                public void jsonResult(String jsonStr) {
                    showMsg("jsonResult:" + jsonStr);
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showMsg(errCode + ";" + errMsg);
                }
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", "8e8e353ffe0ceb3c");
        map.put("AppName", "0");
        hasAuthority.requestNormal(map, "hasAuthority", APIRequest.PARSE_TYPE_BEAN);
    }

    private APIRequest<NumberBean> numberRequest;

    /**
     * body-json 格式请求方式
     * 获取所有的开发编号
     *
     * @param v
     */
    @OnClick(R.id.bt_get_number_list)
    public void getNumberListRequest(View v) {
        if (numberRequest == null) {
            numberRequest = new APIRequest<NumberBean>(NumberBean.class);
            numberRequest.setParseListener(new ParseListener<NumberBean>() {
                @Override
                public void jsonParsed(@NonNull List<NumberBean> beanList) {
                    showMsg(beanList.toString());
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showMsg(errCode + ";" + errMsg);
                }
            });
        }
        Map<String, Object> values = new HashMap<>();
        numberRequest.requestByJson(values, "getNumberList", APIRequest.PARSE_TYPE_LIST);
    }

    private APIRequest<UploadBean> uploadImageRequest;

    /**
     * body-formdata 请求格式
     * 图片上传
     *
     * @param v
     */
    @OnClick(R.id.bt_upload_image)
    public void uploadImageRequest(View v) {
        if (uploadImageRequest == null) {
            uploadImageRequest = new APIRequest<UploadBean>(UploadBean.class);
            uploadImageRequest.setParseListener(new ParseListener<UploadBean>() {
                @Override
                public void jsonParsed(UploadBean uploadBean) {
                    showMsg(uploadBean.toString());
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showMsg(errMsg + ";" + errMsg);
                }
            });
        }

        File dir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        dir.mkdirs();
        String imagePath = new File(dir, "test.jpg").getAbsolutePath();
        File file = new File(imagePath);
        Map<String, String> map = new HashMap<>();
        map.put("zlmxidStr", "412079");
        map.put("typeStr", "1");

        uploadImageRequest.requestUploadImage("file", file, map, "uploadPic", APIRequest.PARSE_TYPE_BEAN);
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//
//        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
////        builder.addFormDataPart("zlmxidStr", zldh);
//        builder.addFormDataPart("zlmxidStr", "412079");
//        builder.addFormDataPart("typeStr", "1");
//        builder.addFormDataPart("file", file.getName(), body);
//
//        List<MultipartBody.Part> parts = builder.build().parts();
//
//        uploadImageRequest.requestNormal(parts, "uploadPic", APIRequest.PARSE_TYPE_BEAN);
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
