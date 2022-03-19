package com.demon.tool.API_2;

/**
 * 网络请求监听类
 */
public abstract class OnReqListener<T> {

    /**
     * 返回请求结果的 data对象
     *
     * @param t
     */
    public void onResult(T t) {
    }

    /**
     * 返回请求结果 对象
     *
     * @param reqResponse
     */
    public void onResult(ReqResponse<T> reqResponse) {
    }

    /**
     * 返回错误结果
     *
     * @param errCode
     * @param errMsg
     */
    public abstract void onError(int errCode, String errMsg);

}
