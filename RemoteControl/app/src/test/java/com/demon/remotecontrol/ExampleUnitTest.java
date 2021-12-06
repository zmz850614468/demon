package com.demon.remotecontrol;

import com.demon.remotecontrol.socketcontrol.SocketBean;
import com.google.gson.Gson;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        SocketBean bean = new SocketBean();
        bean.from = "android";
        bean.to = SocketBean.DEVICE_SERVER;

        bean.command = SocketBean.SOCKET_TYPE_QUERY_ALL_DEVICE;
        bean.msg = "";

        String data = new Gson().toJson(bean);
        bean.msg = "";

//        assertEquals(4, 2 + 2);
    }
}