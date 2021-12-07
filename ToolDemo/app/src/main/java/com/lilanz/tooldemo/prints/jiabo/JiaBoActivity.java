package com.lilanz.tooldemo.prints.jiabo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.beans.ResultBean;
import com.lilanz.tooldemo.multiplex.API.APIRequest;
import com.lilanz.tooldemo.multiplex.API.ParseListener;
import com.lilanz.tooldemo.utils.BitmapUtil;
import com.lilanz.tooldemo.utils.StringUtil;
import com.tools.io.PortManager;
import com.tools.io.UsbPort;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 佳博打印机
 */
public class JiaBoActivity extends Activity {

    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.iv_image)
    protected ImageView ivImage;
    private Bitmap bitmap;

    // USB打印机
    private static PortManager usbManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jia_bo);
        ButterKnife.bind(this);

        initUI();
        initUsbManager();
    }

    @OnClick({R.id.bt_request_image, R.id.bt_print, R.id.bt_creqte_qrcode, R.id.bt_request_print_byte})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_creqte_qrcode:
                try {
//                    bitmap = BitmapUtil.createOneDCode("gs:101458$2020-05-15$18$605$1945570$27256", 800, 50);
                    bitmap = BitmapUtil.createOneDCode("9", 800, 50);
                    ivImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_request_image:
                requestImgage();
                break;
            case R.id.bt_print:
                if (usbManager != null && bitmap != null) {
                    PrintUtil.tablePrint(usbManager, bitmap);
                }
                break;
            case R.id.bt_request_print_byte:    // 请求打印数据byte
                requestPrintData();
                break;
        }
    }

    private APIRequest<String> requestImage;

    /**
     * 请求打印机图片资源
     */
    private void requestImgage() {
        if (requestImage == null) {
            requestImage = new APIRequest<>(String.class);
//            requestImage.setRequestBasePath("http://192.168.36.178:15005/");
            requestImage.setRequestBasePath("http://192.168.35.136:15002/");
//            requestImage.setRequestBasePath("http://192.168.37.43:15002/");
            requestImage.setParseListener(new ParseListener<String>() {
                @Override
                public void jsonResult(String jsonStr) {
                    if (!StringUtil.isEmpty(jsonStr)) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            //  当返回码为0时，请求才是上传成功的，其他情况都返回false
                            if (jsonObject.has("errcode") && jsonObject.getString("errcode").equals("0")) {
                                String data = jsonObject.getString("data");
                                bitmap = BitmapUtil.base64ToBitmap(data);

                                ivImage.setImageBitmap(bitmap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("返回空就结果");
                    }
                }

                @Override
                public void onError(int errCode, String errMsg) {
                    showToast(errCode + ":" + errMsg);
                }
            });
        }


        Map<String, String> values = new HashMap<>();
//        values.put("bodyJsonStr", "{\"bm\":\"M82002845\",\"ys\":\"黑\",\"mc\":\"OX/2016吊牌-4.5X20.3cm-黑-铜板线\",\"kh\":\"20QXF32\",\"sl\":\"-14.00个\",\"ph\":\"\",\n" +
//                ",\"zl\":\"\",\"dw\":\"个\",\"rq\":\"2020-7-4\",\"fk\":\"\",\"bz\":\"101629\",\"barCode\":\"20070400369\"}");
        values.put("bodyJsonStr", "{\"qrCode\":\"12312329\"}");
        String id = etId.getText().toString();
        values.put("templateId", id);

        requestImage.requestNormal(values, "getBitmapStr", APIRequest.PARSE_TYPE_JSON);
    }

    private APIRequest requestPrintData;

    private void requestPrintData() {
        if (requestPrintData == null) {
            requestPrintData = new APIRequest(String.class);
            requestPrintData.setRequestBasePath("http://192.168.37.128:9993/express/");
            requestPrintData.setParseListener(new ParseListener<String>() {
                @Override
                public void onError(int errCode, String errMsg) {
                    showLog(errCode + " - " + errMsg);
                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void jsonResult(String jsonStr) {
                    super.jsonResult(jsonStr);

                    showLog("请求到打印数据：");
//                    ResultBean bean = new Gson().fromJson(jsonStr, ResultBean.class);
//                    if ("0".equals(bean.errcode)) {
//                        Vector<Byte> datas = new Vector<>();
//                        if (bean.data == null) {
//                            showLog("没有byte 数据");
//                            return;
//                        }
//                        for (byte b : bean.data) {
//                            datas.add(b);
//                        }
////                    /* 发送数据 */
//                        showLog("开始打印数据");
//                        try {
//                            usbManager.writeDataImmediately(datas, 0, datas.size());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }


                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        //  当返回码为0时，请求才是上传成功的，其他情况都返回false
                        if (jsonObject.has("errcode") && jsonObject.getString("errcode").equals("0")) {
                            showLog("请求到打印数据：");
                            String data = jsonObject.getString("data");

                            bitmap = BitmapUtil.base64ToBitmap(data);
                            ivImage.setImageBitmap(bitmap);

                            if (usbManager != null && bitmap != null) {
                                PrintUtil.tablePrint(usbManager, bitmap);
                            }

//                            JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//
//                            byte[] decodeByte = android.util.Base64.decode(data, android.util.Base64.DEFAULT);
////                            byte[] bytes = Base64.getDecoder().decode(data);
//
//                            Vector<Byte> datas = new Vector<>();
//                            for (byte b : decodeByte) {
//                                datas.add(b);
//                            }
////                    /* 发送数据 */
//                            try {
//                                usbManager.writeDataImmediately(datas, 0, datas.size());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void jsonParsed(String data) {
                    super.jsonParsed(data);
                    showLog("请求到打印数据：");
                    byte[] bytes = Base64.getDecoder().decode(data);

//                    Vector<Byte> datas = tsc.getCommand();
//                    /* 发送数据 */
//                    try {
//                        usbManager.writeDataImmediately(datas, 0, datas.size());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }

        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", "145-2021120120877");
        map.put("locid", "2");
        requestPrintData.requestByJson(map, "getPrintData", APIRequest.PARSE_TYPE_JSON);

    }

    private void initUsbManager() {
        if (usbManager == null) {
            UsbDevice usbDevice = PrintUtil.getUsbPrintDevice(this);
            if (usbDevice != null) {
                usbManager = new UsbPort(this, usbDevice);
                boolean isOpenUsbPort = usbManager.openPort();
                if (isOpenUsbPort) {
                    showToast("usb打印端口开启");
                } else {
                    showToast("usb打印端口没有开启");
                }
            }
        }
    }

    private void initUI() {
        ;
    }

    @Override
    public void onDestroy() {

//        if (lengthSerialPort != null) {
//            lengthSerialPort.closeSerialPort();
//        }
//        if (weightSerialPort != null) {
//            weightSerialPort.closeSerialPort();
//        }
//        if (printPort != null) {
//            printPort.closePort();
//        }

// TODO 验布机这款打印机，无法正常关闭usb打印，所以不关闭打印机设备
//        if (usbManager != null) {
//            usbManager.closePort();
//            usbManager = null;
//            showToast("关闭usb打印端口");
//        }
        super.onDestroy();
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void base64ToByte(String base64) {
//        byte[] bytes = Base64.getDecoder().decode(base64);
//
//
//    }

    private void showToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("JiaBoActivity", msg);
    }
}
