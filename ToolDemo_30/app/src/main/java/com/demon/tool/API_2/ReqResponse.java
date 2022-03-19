package com.demon.tool.API_2;

/**
 * 请求返回结果类
 *
 * @param <T>
 */
public class ReqResponse<T> {

    private int errcode;    // 返回的code
    private T data;         // 具体的数据结果
    private String errmsg;  // message 可用来返回接口的说明

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
