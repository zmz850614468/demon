package com.lilanz.tooldemo.API;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lilanz.tooldemo.beans.StudentBean;
import com.lilanz.tooldemo.daos.BeanDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Exa {

    private APIRequest<StudentBean> numberBeanAPIRequest;

    /**
     * 第一种请求方式
     */
    public void requestDemo1() {

        if (numberBeanAPIRequest == null) {
            numberBeanAPIRequest = new APIRequest<StudentBean>(StudentBean.class);
            numberBeanAPIRequest.setParseListener(new ParseListener<StudentBean>() {
                @Override
                public void jsonParsed(@NonNull List<StudentBean> beanList) {
                    // 获取List数据
                }

                @Override
                public void jsonParsed(StudentBean studentBean) {
                    // 获取Object数据
                }

                @Override
                public void onTip(String msg) {
                    // 请求成功后的提示信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
        Map<String, String> map = new HashMap<String, String>();
        numberBeanAPIRequest.requestForList(map, "getNumberList", APIRequest.PARSE_TYPE_LIST);

    }


    /**
     * 视频文件上传成功后，通知服务器
     */
    public boolean requestDemo2() throws Exception {
        Map<String, String> values = new HashMap<>();
        values.put("videoUrl", "videoName");
        values.put("vStatus", "1");
        String entity = new Gson().toJson(values);
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        APIService apiService = APIManager.getService(APIService.class);
        RequestBody body = RequestBody.create(mediaType, entity);
        Call<ResponseBody> call = apiService.getNumberList(body);

        // 同步请求
        // 异步请求：call.enqueue();
        Response<ResponseBody> response = call.execute();
        if (response.body() != null) {
            String msg = response.body().string();
            JSONObject jsonObject = new JSONObject(msg);
            //  当返回码为0时，请求才是上传成功的，其他情况都返回false
            if (jsonObject.has("errcode") && jsonObject.getString("errcode").equals("0")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 上传图片
     *
     * @param zldh      指令单号
     * @param type      类型
     * @param imagePath
     */
    private void uploadPic(String zldh, String type, String imagePath) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        File file = new File(imagePath);
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        builder.addFormDataPart("zlmxidStr", zldh);
        // TODO 正式版，一定要修改
        builder.addFormDataPart("zlmxidStr", "412079");
        builder.addFormDataPart("typeStr", type);
        builder.addFormDataPart("file", file.getName(), body);

        List<MultipartBody.Part> parts = builder.build().parts();

        APIService apiService = APIManager.getService(APIService.class);
        Call<ResponseBody> uploadPic = apiService.uploadPic(parts);
        uploadPic.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String msg = response.body().string();
                        JSONObject object = new JSONObject(msg);
                        if (object.has("errcode") && object.getInt("errcode") == 0) {
//                            showToast("照片上传成功");
                        } else {
//                            showToast("照片上传失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                showToast("照片上传请求失败");
            }
        });

    }
}
