package com.lilanz.tooldemo.multiplex.API;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.beans.LoginBean;
import com.lilanz.tooldemo.beans.NumberBean;
import com.lilanz.tooldemo.beans.UploadBean;
import com.lilanz.tooldemo.utils.FileUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        ButterKnife.bind(this);
    }

    private APIRequest<LoginBean> hasAuthority;

    /**
     * Query 请求方式
     * 查看是否有登录权限
     *
     * @param v
     */
    @OnClick(R.id.bt_has_authority)
    public void hasAuthority(View v) {
        if (hasAuthority == null) {
            hasAuthority = new APIRequest<LoginBean>(LoginBean.class);
            hasAuthority.setRequestBasePath(APIManager.LOGIN_BASE_PATH);
            hasAuthority.setParseListener(new ParseListener<LoginBean>() {
                @Override
                public void jsonParsed(LoginBean bean) {
                    // 获取Object数据
                    showMsg(bean.toString());
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showMsg(errCode + ";" + errMsg);
                }
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", "984417e84c4eecb4");
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
    public void getNumberList(View v) {
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
    public void uploadImage(View v) {
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
//        // TODO 正式版，一定要修改
//        builder.addFormDataPart("zlmxidStr", "412079");
//        builder.addFormDataPart("typeStr", "1");
//        builder.addFormDataPart("file", file.getName(), body);
//
//        List<MultipartBody.Part> parts = builder.build().parts();
//
//        uploadImageRequest.requestNormal(parts, "uploadPic", APIRequest.PARSE_TYPE_BEAN);
    }

//    /**
//     * 上传图片
//     *
//     * @param zldh      指令单号
//     * @param type      类型
//     * @param imagePath
//     */
//    private void uploadPic(String zldh, String type, String imagePath) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//
//        File file = new File(imagePath);
//        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
////        builder.addFormDataPart("zlmxidStr", zldh);
//        // TODO 正式版，一定要修改
//        builder.addFormDataPart("zlmxidStr", "412079");
//        builder.addFormDataPart("typeStr", type);
//        builder.addFormDataPart("file", file.getName(), body);
//
//        List<MultipartBody.Part> parts = builder.build().parts();
//
//
//        APIService apiService = APIManager.getService(APIService.class);
//        Call<ResponseBody> uploadPic = apiService.uploadPic(parts);
//        uploadPic.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.body() != null) {
//                    try {
//                        String msg = response.body().string();
//                        JSONObject object = new JSONObject(msg);
//                        if (object.has("errcode") && object.getInt("errcode") == 0) {
////                            showToast("照片上传成功");
//                        } else {
////                            showToast("照片上传失败");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
////                showToast("照片上传请求失败");
//            }
//        });
//    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }
}
