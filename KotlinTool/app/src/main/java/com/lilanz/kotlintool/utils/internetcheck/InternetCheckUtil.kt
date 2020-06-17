package com.lilanz.kotlintool.utils.internetcheck

import android.content.Context
import android.os.Handler
import android.os.Message
import android.widget.Toast

class InternetCheckUtil {
    companion object {

        private var netPingManager: NetPingManager? = null

        fun internetCheck(context: Context?, url: String?) {
            val canHandle: Handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        1 -> if (netPingManager != null) {
                            Toast.makeText(context, "网络延迟：" + msg.obj + "ms", Toast.LENGTH_LONG)
                                .show()
                            netPingManager!!.release()
                            netPingManager = null
                        }
                        2 -> if (netPingManager != null) {
                            netPingManager!!.release()
                            netPingManager = null
                            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, msg.obj?.toString() + "", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
            netPingManager =
                NetPingManager(context, url, object : NetPingManager.IOnNetPingListener {
                    var count = 0
                    var total = 0
                    override fun ontDelay(log: Long) {
                        count++
                        total += log.toInt()
                        if (count == 3) {
                            val msg = Message()
                            msg.what = 1
                            msg.obj = total / count
                            canHandle.sendMessage(msg)
                        }
                    }

                    override fun onError() {
                        val msg = Message()
                        msg.what = 2
                        canHandle.sendMessage(msg)
                    }
                })
            netPingManager!!.startGetDelay()
        }

    }
}