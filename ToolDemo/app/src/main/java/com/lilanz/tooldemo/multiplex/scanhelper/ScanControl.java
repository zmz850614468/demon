package com.lilanz.tooldemo.multiplex.scanhelper;

import android.view.InputDevice;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanControl {

    private static final int SCAN_GUN_COUNT = 3;                // 扫码枪的最多数量
    private List<String> scanDeviceList = new ArrayList<>();    // 扫码枪的扫码顺序
    private Map<String, ScanGunKeyEventHelper> scanGunKeyEventMap = new HashMap<>();    // 扫码枪对应的解析对象

    /**
     * 解析按键信息
     *
     * @param event
     */
    public void analysis(KeyEvent event) {
        InputDevice device = event.getDevice();
        String type = device.getName() + device.getId();
        if (scanDeviceList.contains(type)) {    // 已经包含当前扫码枪
            scanDeviceList.remove(type);
            scanDeviceList.add(type);
        } else {            // 如果没有包含当前扫码枪
            scanDeviceList.add(type);
            if (scanDeviceList.size() <= SCAN_GUN_COUNT) {
                int size = scanDeviceList.size();
                scanGunKeyEventMap.put(type, new ScanGunKeyEventHelper(size));
                scanGunKeyEventMap.get(type).setOnBarCodeCatchListener(new ScanGunKeyEventHelper.OnScanSuccessListener() {
                    @Override
                    public void onScanSuccess(int id, String barcode) {
                        if (onScanResult != null) {
                            onScanResult.scanResult(id, barcode);
                        }
                    }
                });
            } else { // 如果扫码枪数量已满，需要移除之前的扫码枪
                String scanDeviceId = scanDeviceList.remove(0);
                ScanGunKeyEventHelper scanHelper = scanGunKeyEventMap.remove(scanDeviceId);
                scanGunKeyEventMap.put(type, scanHelper);
            }
        }

        scanGunKeyEventMap.get(type).analysisKeyEvent(event);
    }

    private OnScanResult onScanResult;

    public void setOnScanResult(OnScanResult onScanResult) {
        this.onScanResult = onScanResult;
    }

    public interface OnScanResult {
        public void scanResult(int id, String code);
    }

}
