package com.demon.tool.API_2.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.API_2.OnReqListener;
import com.demon.tool.API_2.ReqManager;
import com.demon.tool.API_2.ReqMethod;
import com.demon.tool.API_2.ReqService;
import com.demon.tool.API_2.ReqResponse;
import com.demon.tool.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_request)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_request:
                showLog("开始网络请求");
//                request_call();
//                request_observable();
                request();
                break;
        }
    }

    private void request() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 4);
        map.put("readFlag", 0);
        map.put("aggr", "bizType");

        ReqMethod<Map<String, Object>> reqMethod = new ReqMethod<>();
        reqMethod.setOnReqListener(new OnReqListener<Map<String, Object>>() {
            @Override
            public void onError(int errCode, String errMsg) {
                showLog(errCode + " - " + errMsg);
            }

            @Override
            public void onResult(Map<String, Object> map) {
                super.onResult(map);
                showLog("bean : " + new Gson().toJson(map));
            }

            @Override
            public void onResult(ReqResponse<Map<String, Object>> reqResponse) {
                super.onResult(reqResponse);
                showLog("json : " + new Gson().toJson(reqResponse));
            }
        });
        reqMethod.setReturnType(ReqMethod.ReturnType.All);
        reqMethod.requestBodyRawJson(map, "requestTest");
    }

    private void request_observable() {
        ReqService requestService = ReqManager.getService(ReqService.class);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", 4);
        map.put("readFlag", 0);
        map.put("aggr", "bizType");

        String entity = new Gson().toJson(map);
//        entity = "{\"bizData\":{\"bizType\":\"91\",\"msgType\":\"0\",\"secType\":\"3\",\"title\":\"有一条待办单据\",\"content\":\"XX单据待办\",\"url\":\"http://www.baidu.com\",\"total\":\"88\",\"voice\":\"1\"},\"toUserType\":\"wxid\",\"toUserId\":[40626],\"sendType\":\"IM\"}";
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, entity);

        requestService.requestTest(body)
                .subscribeOn(Schedulers.io())   // 异步处理
                .observeOn(AndroidSchedulers.mainThread())  // 返回结果，在主线程处理
                .subscribe(new Consumer<ReqResponse<Map<String, Integer>>>() {
                    @Override
                    public void accept(ReqResponse<Map<String, Integer>> mapResponse) throws Exception {
                        showLog(new Gson().toJson(mapResponse));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showLog("Throwable : " + throwable.toString());
                    }
                });

//        requestService.requestTest(body)
//                .subscribeOn(Schedulers.io())   // 异步处理
//                .observeOn(AndroidSchedulers.mainThread())  // 返回结果，在主线程处理
//                .subscribe(new Consumer<Response<RequestBean>>() {
//                    @Override
//                    public void accept(Response<RequestBean> requestBeanResponse) throws Exception {
//                        showLog(new Gson().toJson(requestBeanResponse));
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        showLog("Throwable : " + throwable.getLocalizedMessage());
//                    }
//                });

//        requestService.requestTest(body)
//                .subscribeOn(Schedulers.io())   // 异步处理
//                .observeOn(AndroidSchedulers.mainThread())  // 返回结果，在主线程处理
//                .subscribe(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) throws Exception {
//                        if (responseBody != null) {
//                            showLog("body:" + responseBody.string());
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        showLog("Throwable : " + throwable.getLocalizedMessage());
//                    }
//                });

//        requestService.requestTest(body)
//                .subscribe(new Consumer<Response<RequestBean>>() {
//                    @Override
//                    public void accept(Response<RequestBean> requestBeanResponse) throws Exception {
//                        if (requestBeanResponse != null) {
//                            showLog("body:" + requestBeanResponse.getErrcode());
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        showLog("Throwable : " + throwable.getMessage());
//                    }
//                });

    }

    private void request_call() {
        ReqService requestService = ReqManager.getService(ReqService.class);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", 4);
        map.put("readFlag", 0);
        map.put("aggr", "bizType");

        String entity = new Gson().toJson(map);
        entity = "{\"bizData\":{\"bizType\":\"91\",\"msgType\":\"0\",\"secType\":\"3\",\"title\":\"有一条待办单据\",\"content\":\"XX单据待办\",\"url\":\"http://www.baidu.com\",\"total\":\"88\",\"voice\":\"1\"},\"toUserType\":\"wxid\",\"toUserId\":[40626],\"sendType\":\"IM\"}";
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, entity);

//        requestService.requestTest(body)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.body() != null) {
//                            try {
//                                showLog(response.body().string());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        showLog("onFailure:" + t.getMessage());
//                    }
//                });

    }

    private void showLog(String msg) {
        Log.e("RequestActivity", msg);
    }
}
