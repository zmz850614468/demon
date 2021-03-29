package com.demon.agv.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Android Q(29) 版本以上也可使用
 */
public class PublicFileUtil {

    /**
     * 读取文件内容
     *
     * @param context
     * @param publicFile 公共文件名: "DCIM/" or "DCIM/dir_name/"
     * @param fileName   目标文件名：test.txt
     * @return
     */
    public static String getTextFromPublic(Context context, String publicFile, String fileName) {
        // 确保公共文件夹有效
        String subDirection;
        if (!StringUtil.isEmpty(publicFile)) {
            if (publicFile.endsWith("/")) {
                subDirection = publicFile.substring(0, publicFile.length() - 1);
            } else {
                subDirection = publicFile;
            }
        } else {
            subDirection = "Documents";
        }

        Cursor cursor = searchTxtFromPublic(context, publicFile, fileName);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                Uri uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), "" + id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);
                if (uri != null) {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int len = -1;
                    byte[] buffer = new byte[1024]; //1kb
                    while ((len = inputStream.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    inputStream.close();
                    String content = new String(baos.toByteArray());
                    return content;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 读写普通文件，例如txt
     *
     * @param context
     * @param publicFile 公共文件名: "DCIM/" or "DCIM/dir_name/"
     * @param fileName   目标文件名：test.txt
     * @param content
     * @param mode       打开模式："w", "wa", "rw", or "rwt"
     */
    public static void saveTxt2Public(Context context, String publicFile, String fileName, String content, String mode) {
        // 确保公共文件夹有效
        String subDirection;
        if (!StringUtil.isEmpty(publicFile)) {
            if (publicFile.endsWith("/")) {
                subDirection = publicFile.substring(0, publicFile.length() - 1);
            } else {
                subDirection = publicFile;
            }
        } else {
            subDirection = "Documents";
        }

        Cursor cursor = searchTxtFromPublic(context, publicFile, fileName);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
                Uri uri = Uri.withAppendedPath(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), "" + id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), id);
                if (uri != null) {
                    // OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    //  "w", "wa", "rw", or "rwt"
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri, mode);
                    if (outputStream != null) {
                        outputStream.write(content.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, subDirection);
            } else {

            }
            //设置文件类型
            contentValues.put(MediaStore.Files.FileColumns.MEDIA_TYPE, MediaStore.Files.FileColumns.MEDIA_TYPE_NONE);
            Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), contentValues);
            if (uri != null) {
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    outputStream.write(content.getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param publicFile relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                   absolute path before Q
     * @return
     */
    private static Cursor searchTxtFromPublic(Context context, String publicFile, String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            showLog("searchTxtFromPublic: fileName is null");
            return null;
        }
        if (!publicFile.endsWith("/")) {
            publicFile = publicFile + "/";
        }

        String queryPathKey = MediaStore.Files.FileColumns.RELATIVE_PATH;
        String selection = queryPathKey + "=? and " + MediaStore.Files.FileColumns.DISPLAY_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL),
                new String[]{MediaStore.Files.FileColumns._ID, queryPathKey, MediaStore.Files.FileColumns.DISPLAY_NAME},
                selection,
                new String[]{publicFile, fileName},
                null);

        return cursor;
    }

    /**
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     * @param fileName just file name, not include path
     * @param image
     */
    public static void saveImage2Public(Context context, String filePath, String fileName, byte[] image) {
        String subDirection;
        if (!StringUtil.isEmpty(filePath)) {
            if (filePath.endsWith("/")) {
                subDirection = filePath.substring(0, filePath.length() - 1);
            } else {
                subDirection = filePath;
            }
        } else {
            subDirection = "DCIM";
        }

        Cursor cursor = searchImageFromPublic(context, filePath, fileName);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));                     // uri的id，用于获取图片
                Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                if (uri != null) {
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        outputStream.write(image);
                        outputStream.flush();
                        outputStream.close();
                    }
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            //设置保存参数到ContentValues中
            ContentValues contentValues = new ContentValues();
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            //兼容Android Q和以下版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, subDirection);
                //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/sample");
            } else {
                contentValues.put(MediaStore.Images.Media.DATA, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    outputStream.write(image);
                    outputStream.flush();
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                 absolute path before Q
     * @return
     */
    public static byte[] loadImageFromPublic(Context context, String filePath, String fileName) {
        Cursor cursor = searchImageFromPublic(context, filePath, fileName);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                //循环取出所有查询到的数据
                do {
                    //一张图片的基本信息
                    int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));                     // uri的id，用于获取图片
//                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH));   // 图片的相对路径
//                    String type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));       // 图片类型
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));    // 图片名字
                    showLog("loadImageFromPublic: id = " + id);
                    showLog("loadImageFromPublic: name = " + name);
                    //根据图片id获取uri，这里的操作是拼接uri
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
                    //官方代码：
//                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    if (uri != null) {
                        byte[] image;
                        InputStream inputStream = context.getContentResolver().openInputStream(uri);
                        if (null == inputStream || 0 == inputStream.available()) {
                            return null;
                        }
                        image = new byte[inputStream.available()];
                        inputStream.read(image);
                        inputStream.close();
                        return image;
                    }
                } while (cursor.moveToNext());
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                 absolute path before Q
     * @return
     */
    public static void deleteImageFromPublic(Context context, String filePath, String fileName) {
        Cursor cursor = searchImageFromPublic(context, filePath, fileName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));                     // uri的id，用于获取图片
//                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.RELATIVE_PATH));   // 图片的相对路径
//                String type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));       // 图片类型
//                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));    // 图片名字
                context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + " LIKE ?", new String[]{String.valueOf(id)});
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * @param context
     * @param filePath relative path in Q, such as: "DCIM/" or "DCIM/dir_name/"
     *                 absolute path before Q
     * @return
     */
    private static Cursor searchImageFromPublic(Context context, String filePath, String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            showLog("searchImageFromPublic: fileName is null");
            return null;
        }
        if (StringUtil.isEmpty(filePath)) {
            filePath = "DCIM/";
        } else {
            if (!filePath.endsWith("/")) {
                filePath = filePath + "/";
            }
        }

        //兼容androidQ和以下版本
        String queryPathKey = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ? MediaStore.Images.Media.RELATIVE_PATH : MediaStore.Images.Media.DATA;
        String selection = queryPathKey + "=? and " + MediaStore.Images.Media.DISPLAY_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID, queryPathKey, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.DISPLAY_NAME},
                selection,
                new String[]{filePath, fileName},
                null);

        return cursor;
    }

    private static void showLog(String msg) {
        Log.e("publicFileUtil", msg);
    }
}
