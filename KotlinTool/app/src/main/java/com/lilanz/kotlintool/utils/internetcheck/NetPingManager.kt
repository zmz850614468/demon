package com.lilanz.kotlintool.utils.internetcheck

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.text.TextUtils
import java.io.IOException
import java.net.*
import java.util.*

class NetPingManager {
    private var mDomain // 接口域名
            : String? = null
    private var mAddress: Array<InetAddress>? = null
    private var mAddressIpList: MutableList<String>? = null
    private var mIOnNetPingListener // 将监控日志上报到前段页面
            : IOnNetPingListener? = null
    private var mHandlerThread: HandlerThread? = null

    private var DELAY_TIME = 1000
    private var manager: ConnectivityManager? = null
    private var mHandleMessage: Handler? = null

    /**
     * 延迟
     */
    fun setDuration(delay: Int) {
        DELAY_TIME = delay
    }

    /**
     * 初始化网络诊断服务
     */
    constructor(context: Context?, domain: String?, theListener: IOnNetPingListener?) {
        mDomain = domain
        mIOnNetPingListener = theListener
        mAddressIpList = ArrayList()
        if (null != context) manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        mHandlerThread = HandlerThread("ping")
        mHandlerThread!!.start()
        mHandleMessage = object : Handler(mHandlerThread!!.looper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    0 -> {
                        //每次请求清空上传集合
                        if (null != mAddressIpList) mAddressIpList?.clear()
                        startNetDiagnosis()
                        if (null != mHandlerThread) mHandleMessage!!.sendEmptyMessageDelayed(
                            0,
                            DELAY_TIME.toLong()
                        )
                    }
                }
            }
        }
    }

    /**
     * 开始监听
     */
    fun startGetDelay() {
        if (null != mHandleMessage) {
            mHandleMessage?.sendEmptyMessage(0)
        }
    }

    /**
     * 释放
     */
    fun release() {
        synchronized(NetPingManager::class.java) {
            if (null != manager) manager = null
            if (null != mHandleMessage) {
                mHandleMessage?.removeMessages(0)
            }
            if (null != mHandlerThread) {
                val looper = mHandlerThread!!.looper
                if (looper != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        looper.quitSafely()
                    } else {
                        looper.quit()
                    }
                }
            }
            mHandlerThread = null
            mIOnNetPingListener = null
            if (null != mAddressIpList) mAddressIpList!!.clear()
            mAddressIpList = null
            manager = null
        }
    }

    /**
     * 监控网络诊断的跟踪信息
     */
    interface IOnNetPingListener {
        open fun ontDelay(log: Long)
        open fun onError()
    }

    /**
     * 开始诊断网络
     */
    private fun startNetDiagnosis() {
        if (!TextUtils.isEmpty(mDomain)) { // 网络状态
            if (isNetworkConnected()) {
                parseDomain(mDomain) // 域名解析
                // TCP三次握手时间测试
                execUseJava()
            } else {
                if (null != mIOnNetPingListener) {
                    mIOnNetPingListener!!.onError()
                }
                //                Log.e("tag", "当前主机未联网,请检查网络！");
            }
        }
    }

    /**
     * 使用java执行connected
     */
    private fun execUseJava(): Boolean {
        if (mAddress != null && mAddressIpList != null) {
            val len = mAddress!!.size
            if (len > 0) {
                execIP(mAddress!![0], mAddressIpList!![0])
            }
        }
        return false
    }

    private val PORT = 80
    private val CONN_TIMES = 4
    // 设置每次连接的timeout时间
    private var TIME_OUT = 6000
    private val RttTimes = LongArray(CONN_TIMES) // 用于存储三次测试中每次的RTT值


    /**
     * 返回某个IP进行5次connect的最终结果
     */
    private fun execIP(inetAddress: InetAddress?, ip: String?): Boolean {
        var isConnected = true
        val socketAddress: InetSocketAddress
        if (inetAddress != null && ip != null) {
            socketAddress = InetSocketAddress(inetAddress, PORT)
            var flag = 0
            for (i in 0 until CONN_TIMES) {
                execSocket(socketAddress, i)
                if (RttTimes[i] == -1L) { // 一旦发生timeOut,则尝试加长连接时间
                    TIME_OUT += 4000
                    if (i > 0 && RttTimes[i - 1] == -1L) { // 连续两次连接超时,停止后续测试
                        flag = -1
                        break
                    }
                } else if (RttTimes[i] == -2L) {
                    if (i > 0 && RttTimes[i - 1] == -2L) { // 连续两次出现IO异常,停止后续测试
                        flag = -2
                        break
                    }
                }
            }
            var time: Long = 0
            var count = 0
            if (flag == -1) {
                isConnected = false
            } else if (flag == -2) {
                isConnected = false
            } else {
                for (i in 0 until CONN_TIMES) {
                    if (RttTimes[i] > 0) {
                        time += RttTimes[i]
                        count++
                    }
                }
                if (count > 0) {
                    if (mIOnNetPingListener != null) mIOnNetPingListener!!.ontDelay(time / count)
                }
            }
        } else {
            isConnected = false
        }
        return isConnected
    }

    /**
     * 针对某个IP第index次connect
     */
    private fun execSocket(
        socketAddress: InetSocketAddress,
        index: Int
    ) {
        val start: Long
        val end: Long
        val mSocket = Socket()
        try {
            start = System.currentTimeMillis()
            mSocket.connect(socketAddress, TIME_OUT)
            end = System.currentTimeMillis()
            RttTimes[index] = end - start
        } catch (e: SocketTimeoutException) {
            RttTimes[index] = -1 // 作为TIMEOUT标识
            e.printStackTrace()
        } catch (e: IOException) {
            RttTimes[index] = -2 // 作为IO异常标识
            e.printStackTrace()
        } finally {
            if (mSocket != null) {
                try {
                    mSocket.close()
                } catch (io: IOException) {
                    io.printStackTrace()
                }
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private fun isNetworkConnected(): Boolean {
        return if (manager == null) {
            false
        } else try {
            val networkinfo = manager!!.activeNetworkInfo
            !(networkinfo == null || !networkinfo.isAvailable)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 域名解析
     */
    private fun parseDomain(domain: String?): Boolean {
        var flag = false
        var map = getDomainIp(domain)
        val useTime = map["useTime"] as String?
        mAddress = map["remoteInet"] as Array<InetAddress>?
        if (mAddress != null) { // 解析正确
            mAddressIpList!!.add(mAddress!![0].hostAddress)
            flag = true
        } else { // 解析不到，判断第一次解析耗时，如果大于10s进行第二次解析
            if (useTime!!.toInt() > 10000) {
                map = getDomainIp(domain)
                mAddress = map["remoteInet"] as Array<InetAddress>?
                if (mAddress != null) {
                    mAddressIpList!!.add(mAddress!![0].hostAddress)
                    flag = true
                }
            }
        }
        return flag
    }

    /**
     * 解析IP
     */
    private fun getDomainIp(domain: String?): Map<String?, Any?> {
        val map: MutableMap<String?, Any?> =
            HashMap()
        var start: Long = 0
        var end: Long
        var time: String? = null
        var remoteInet: Array<InetAddress?>? = null
        try {
            start = System.currentTimeMillis()
            remoteInet = InetAddress.getAllByName(domain)
            if (remoteInet != null) {
                end = System.currentTimeMillis()
                time = (end - start).toString() + ""
            }
        } catch (e: UnknownHostException) {
            end = System.currentTimeMillis()
            time = (end - start).toString() + ""
            remoteInet = null
            e.printStackTrace()
        } finally {
            map["remoteInet"] = remoteInet
            map["useTime"] = time
        }
        return map
    }
}