package com.lilanz.tooldemo.prints.jiabo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.API.APIRequest;
import com.lilanz.tooldemo.multiplex.API.ParseListener;
import com.lilanz.tooldemo.utils.BitmapUtil;
import com.lilanz.tooldemo.utils.StringUtil;
import com.tools.io.PortManager;
import com.tools.io.UsbPort;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 佳博打印机
 */
public class JiaBoActivity extends Activity {

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

    @OnClick({R.id.bt_request_image, R.id.bt_print, R.id.bt_creqte_qrcode})
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
        }
    }


    private APIRequest<String> requestImage;

    /**
     * 请求打印机图片资源
     */
    private void requestImgage() {
        if (requestImage == null) {
            requestImage = new APIRequest<>(String.class);
            requestImage.setRequestBasePath("http://192.168.36.178:15005/");
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
        values.put("bodyJsonStr", "{\"bm\":\"M82002845\",\"ys\":\"黑\",\"mc\":\"OX/2016吊牌-4.5X20.3cm-黑-铜板线\",\"kh\":\"20QXF32\",\"sl\":\"-14.00个\",\"ph\":\"\",\n" +
                ",\"zl\":\"\",\"dw\":\"个\",\"rq\":\"2020-7-4\",\"fk\":\"\",\"bz\":\"101629\",\"barCode\":\"20070400369\"}");
        values.put("templateId", "2");

        requestImage.requestNormal(values, "getBitmapStr", APIRequest.PARSE_TYPE_JSON);
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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
