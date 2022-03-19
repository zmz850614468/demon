package com.demon.tool.API_2;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ReqMethod<T> {

    /**
     * All: 返回所有结果信息
     * Data: 只返回 data 内容
     */
    public enum ReturnType {All, Data}

    private String rootPath;                // (可选) 网络请求的根目录，不设置使用默认地址
    private OnReqListener<T> onReqListener; // (必选)

    private ReturnType returnType = ReturnType.Data;    //(可选) 数据返回类型，默认只返回 data 内容
    private boolean isSynchrony = true;     // (可选) 是否异步请求，默认异步处理

    /**
     * 可以使用的方法：
     * Params
     * Body-x-www-form-urlencoded
     *
     * @param object     可以是RequestBody，也可以是Map对象
     * @param methodName
     */
    public void requestParams(Object object, String methodName) {
        ReqService service = ReqManager.getService(rootPath, ReqService.class);

        Observable<ReqResponse<T>> observable = null;
        try {
            Method[] methods = service.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (methodName.equals(name)) {
                    observable = (Observable<ReqResponse<T>>) method.invoke(service, new Object[]{object});
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onReqListener.onError(-1, "未找到对应方法名 - " + e.toString());
            return;
        }

        if (observable != null) {
            // 判断异步处理，还是同步处理数据
            if (isSynchrony) {
                observable.subscribeOn(Schedulers.io())   // 异步处理
                        .observeOn(AndroidSchedulers.mainThread());  // 返回结果，在主线程处理
            }

            observable.subscribe(reqResponse -> dealResult(reqResponse)
                    , throwable -> onReqListener.onError(-3, "请求结果异常 - " + throwable.toString()));
        } else {
            onReqListener.onError(-2, "未找到对应方法名 - ");
        }
    }

    /**
     * Body-raw-json 请求方式
     *
     * @param map
     * @param methodName
     */
    public void requestBodyRawJson(Map<String, Object> map, String methodName) {
        String entity = new Gson().toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, entity);

        requestParams(body, methodName);
    }

    /**
     * 处理网络请求后的结果
     *
     * @param reqResponse
     */
    private void dealResult(ReqResponse<T> reqResponse) {
        if (reqResponse.getErrcode() == 0) {
            switch (returnType) {
                case All:
                    onReqListener.onResult(reqResponse);
                    break;
                case Data:
                    onReqListener.onResult(reqResponse.getData());
                    break;
            }
        } else {
            onReqListener.onError(reqResponse.getErrcode(), reqResponse.getErrmsg());
        }
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public void setOnReqListener(OnReqListener<T> onReqListener) {
        this.onReqListener = onReqListener;
    }

    public void setSynchrony(boolean synchrony) {
        isSynchrony = synchrony;
    }

    private void showLog(String msg) {
        Log.e("ReqMethod", msg);
    }
}
