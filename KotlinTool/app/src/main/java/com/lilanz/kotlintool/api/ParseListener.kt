package com.lilanz.kotlintool.api

/**
 * 监听器：获取网络请求解析后的数据
 */
interface ParseListener<T> {
    /**
     * 解析对象列表
     */
    fun jsonParsed(beanList: List<T>) {}

    /**
     * 解析单个对象
     */
    fun jsonParsed(t: T) {}

    /**
     * 请求成功后的回调信息
     */
    fun onTip(msg: String?) {}

    /**
     * 回调的错误信息
     */
    fun onError(msg: String?)
}