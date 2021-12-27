/**
 *
 */
package com.demon.tool.ftp;

/**
 * 执行每一个动作后响应的结果，包括成功的和失败的.
 *
 * @author hao_yujie, cui_tao
 *
 */
public class Result {

    /**
     * 上传的文件大小   M
     * 响应的内容.
     */
    private String response;

    /**
     * 响应的结果.
     */
    private boolean succeed;

    /**
     * 上传文件所用时间
     * 响应的时间.
     */
    private String time;

    /**
     * 无参的构造方法.
     */
    public Result() {
    }

    /**
     * 构造方法.
     *
     * @param res 响应的内容
     */
    public Result(String res) {
        this.response = res;
    }

    /**
     * 构造方法.
     *
     * @param suc 响应的结果
     * @param ti 响应的时间
     * @param res 响应的内容
     */
    public Result(boolean suc, String ti, String res) {
        this.succeed = suc;
        this.time = ti;
        this.response = res;
    }

    /**
     * 得到相应内容.
     *
     * @return 相应内容
     */
    public String getResponse() {
        return response;
    }

    /**
     * 设置相应内容.
     *
     * @param response 响应内容
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * 得到相应结果.
     *
     * @return 相应结果
     */
    public boolean isSucceed() {
        return succeed;
    }

    /**
     * 设置响应结果.
     *
     * @param succeed 响应结果
     */
    public void setSucceed(boolean succeed) {
        this.succeed = succeed;
    }

    /**
     * 得到响应时间.
     *
     * @return 响应时间
     */
    public String getTime() {
        return time;
    }

    /**
     * 设置响应时间.
     *
     * @param time 响应时间
     */
    public void setTime(String time) {
        this.time = time;
    }

}
