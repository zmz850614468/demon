package com.demo.addcontact.contol;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * 联系人相关类
 */
public class ContactControl {

    public static void addContact(Activity activity, String name, String phoneNumber) {
        // 联系人名字
        ContentValues row1 = new ContentValues();
        row1.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        // 手机号码
        ContentValues row2 = new ContentValues();
        row2.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        row2.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        row2.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);


        ContentValues contentValues = new ContentValues();
        //向RawContacts.CONTENT_URI执行一个空值插入
        //目的是获取系统返回的parseId
        Uri uri = activity.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        showLog("结果：" + uri);
        long parseId = ContentUris.parseId(uri);
        contentValues.clear();

        //联系人绑定parseId
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
        //设置内容类型
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //设置联系人名字
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        //向联系人Uri添加联系人名字
        activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
        //清理contentValues的数据
        contentValues.clear();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, parseId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        //设置联系人的电话号码
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        //设置电话类型为手机
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

        //向联系人电话号码Uri添加电话号码
        activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);

        contentValues.clear();


//        row2.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
//        Uri rawContactUri = activity.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, row2);
//        showLog("结果：" + rawContactUri);

        // 插入名字和电话号码
//        Uri rawContactUri = activity.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, row1);
//        showLog("结果：" + rawContactUri);
//        long rawContactId = ContentUris.parseId(rawContactUri);
//
//        row2.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//        activity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, row2);

//        Uri rawContactUri =
//                activity.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
//        long rawContactId = ContentUris.parseId(rawContactUri);
//        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//// 内容类型
//        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//// 联系人名字
//        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstName);
//        values.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastName);
//        values.put(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, middleName);
//// 向联系人URI添加联系人名字
//        mActivity.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }

    //封装的添加方法
    private void addPhoneNumber(
            ContentValues row, ArrayList<ContentValues> values, String phoneNumber, int type) {
        row.put(ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        row.put(ContactsContract.CommonDataKinds.Phone.TYPE, type);
        values.add(row);
    }

    private static void showLog(String msg) {
        Log.e("ContactControl", msg);
    }
}
