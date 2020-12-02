package com.lilanz.wificonnect.controls;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * 权限控制类
 */
public class PermissionControl {

    public static final int STORAGE_RESULT_CODE = 1;       // 读写权限
    public static final int CAMERA_RESULT_CODE = 2;       // 相机权限
    public static final int MICROPHONE_RESULT_CODE = 3;       // 麦克风权限
    public static final int LOCATION_RESULT_CODE = 4;       // 位置权限
    public static final int CONTACTS_RESULT_CODE = 5;       // 联系人权限
    public static final int PHONE_RESULT_CODE = 6;       // 电话权限
    public static final int CALENDAR_RESULT_CODE = 7;       // 日历权限
    public static final int SENSORS_RESULT_CODE = 8;       // 日历权限
    public static final int SMS_RESULT_CODE = 9;       // 日历权限

    private Activity activity;

    public PermissionControl(Activity activity) {
        this.activity = activity;
    }

    /**
     * 读写权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    public boolean storagePermission() {
        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 相机权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.CAMERA" />
    public boolean cameraPermission() {
        if (!checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.CAMERA}, CAMERA_RESULT_CODE);
            return false;
        }

        return true;
    }

    /**
     * 麦克风权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    public boolean microphonePerssion() {
        if (!checkPermission(Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.RECORD_AUDIO}, MICROPHONE_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 位置权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
//    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    public boolean locationPermission() {
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 联系人权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.READ_CONTACTS"/>
//    <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
//    <uses-permission android:name="android.permission.GET_ACCOUNTS"/>
    public boolean contactsPermission() {
        if (!checkPermission(Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS}, CONTACTS_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 电话权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.READ_CALL_LOG"/>
//    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
//    <uses-permission android:name="android.permission.CALL_PHONE"/>
//    <uses-permission android:name="android.permission.WRITE_CALL_LOG"/>
//    <uses-permission android:name="android.permission.USE_SIP"/>
//    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
//    <uses-permission android:name="android.permission.ADD_VOICEMAIL"/>
    public boolean phonePermission() {
        if (!checkPermission(Manifest.permission.WRITE_CALL_LOG)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS
//                    Manifest.permission.ADD_VOICEMAIL // 这个权限请求不了
            }, PHONE_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 日历权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.READ_CALENDAR"/>
//    <uses-permission android:name="android.permission.WRITE_CALENDAR"/>
    public boolean calendarPermission() {
        if (!checkPermission(Manifest.permission.WRITE_CALENDAR)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR}, CALENDAR_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 传感器权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.BODY_SENSORS"/>
    public boolean sensorsPermission() {
        if (!checkPermission(Manifest.permission.BODY_SENSORS)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.BODY_SENSORS}, SENSORS_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * SMS权限
     *
     * @return true:已经授权过来，否则没有
     */
//    <uses-permission android:name="android.permission.READ_SMS"/>
//    <uses-permission android:name="android.permission.RECEIVE_WAP_PUSH"/>
//    <uses-permission android:name="android.permission.RECEIVE_MMS"/>
//    <uses-permission android:name="android.permission.RECEIVE_SMS"/>
//    <uses-permission android:name="android.permission.SEND_SMS"/>
//    <uses-permission android:name="android.permission.BROADCAST_SMS"/>
    public boolean smsPermission() {
        if (!checkPermission(Manifest.permission.RECEIVE_SMS)) {
            ActivityCompat.requestPermissions(activity, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS,
//                    Manifest.permission.BROADCAST_SMS   // 这个权限请求不了
            }, SMS_RESULT_CODE);
            return false;
        }
        return true;
    }

    /**
     * 判断是否有改权限
     *
     * @param permission
     * @return
     */
    public boolean checkPermission(String permission) {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(activity, permission);
    }


    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}
