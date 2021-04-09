package com.demon.agv.control;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限控制类
 */
public class PermissionControl {

    public static final String STORAGE = "storage";         // 读写权限
    public static final String CAMERA = "camera";           // 相机权限
    public static final String MICROPHONE = "microphone";   // 麦克风权限
    public static final String LOCATION = "location";       // 位置权限
    public static final String CONTACTS = "contacts";       // 联系人权限
    public static final String PHONE = "phone";             // 电话权限
    public static final String CALENDAR = "calendar";       // 日历权限
    public static final String SENSORS = "sensors";         // 传感器权限
    public static final String SMS = "sms";                 // SMS权限

    public static final int STORAGE_RESULT_CODE = 1;        // 读写权限返回结果
    public static final int CAMERA_RESULT_CODE = 2;         // 相机权限返回结果
    public static final int MICROPHONE_RESULT_CODE = 3;     // 麦克风权限返回结果
    public static final int LOCATION_RESULT_CODE = 4;       // 位置权限返回结果
    public static final int CONTACTS_RESULT_CODE = 5;       // 联系人权限返回结果
    public static final int PHONE_RESULT_CODE = 6;          // 电话权限返回结果
    public static final int CALENDAR_RESULT_CODE = 7;       // 日历权限返回结果
    public static final int SENSORS_RESULT_CODE = 8;        // 传感器权限返回结果
    public static final int SMS_RESULT_CODE = 9;            // SMS权限返回结果
    public static final int RESULT_CODE = 10;               // 多个权限返回结果


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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{STORAGE}), STORAGE_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{CAMERA}), CAMERA_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{MICROPHONE}), MICROPHONE_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{LOCATION}), LOCATION_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{CONTACTS}), CONTACTS_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{PHONE}), PHONE_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{CALENDAR}), CALENDAR_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{SENSORS}), SENSORS_RESULT_CODE);
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
            ActivityCompat.requestPermissions(activity, requestPermission(new String[]{SMS}), SMS_RESULT_CODE);
            return false;
        }
        return true;
    }


    /**
     * 同时请求多个权限
     *
     * @param permission
     */
    public void requestPermissions(String[] permission) {
        ActivityCompat.requestPermissions(activity, requestPermission(permission), RESULT_CODE);
    }

    /**
     * 组合各种权限
     *
     * @param permission
     * @return
     */
    private String[] requestPermission(String[] permission) {
        List<String> permissionList = new ArrayList<>();

        for (String s : permission) {
            switch (s) {
                case STORAGE:
                    permissionList.addAll(Arrays.asList(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE));
                    break;
                case CAMERA:
                    permissionList.addAll(Arrays.asList(Manifest.permission.CAMERA));
                    break;
                case MICROPHONE:
                    permissionList.addAll(Arrays.asList(Manifest.permission.RECORD_AUDIO));
                    break;
                case LOCATION:
                    permissionList.addAll(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION));
                    break;
                case PHONE:
                    permissionList.addAll(Arrays.asList(Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.WRITE_CALL_LOG,
                            Manifest.permission.USE_SIP,
                            Manifest.permission.PROCESS_OUTGOING_CALLS));
                    break;
                case CONTACTS:
                    permissionList.addAll(Arrays.asList(Manifest.permission.WRITE_CONTACTS,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.GET_ACCOUNTS));
                    break;
                case SENSORS:
                    permissionList.addAll(Arrays.asList(Manifest.permission.BODY_SENSORS));
                    break;
                case CALENDAR:
                    permissionList.addAll(Arrays.asList(Manifest.permission.READ_CALENDAR,
                            Manifest.permission.WRITE_CALENDAR));
                    break;
                case SMS:
                    permissionList.addAll(Arrays.asList(Manifest.permission.READ_SMS,
                            Manifest.permission.RECEIVE_WAP_PUSH,
                            Manifest.permission.RECEIVE_MMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.SEND_SMS));
                    break;
            }
        }

        String[] permissions = new String[permissionList.size()];
        permissionList.toArray(permissions);
        return permissions;
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
