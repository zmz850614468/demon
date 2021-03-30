package com.lilanz.tooldemo.multiplex.download;

import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 文件下载控制端:一个对象一次只能下载一个文件不然会有问题
 */
public class DownloadControl {
    private static final String TAG = "OkHttpDownUtil";
    private Call mCall;
    private long mAlreadyDownLength = 0;//已经下载长度
    private long mTotalLength = 0;//整体文件大小
    private int mSign = 0; //标记当前运行的是那个方法
    private String mDownUrl;//下载网络地址
    private File mPath;//文件保存路径
    private JSONObject mJson;
    private DownloadListener mHttpDownListener;//下载进度接口回调

    /**
     * 没有断点续传功能的get请求下载
     *
     * @param downUrl             下载网络地址
     * @param saveFilePathAndName 保存路径
     */
    public void getDownRequest(final String downUrl, final File saveFilePathAndName, final DownloadListener listener) {
        mSign = 1;
        mDownUrl = downUrl;
        mPath = saveFilePathAndName;
        mHttpDownListener = listener;
        mAlreadyDownLength = 0;
        Request request = new Request.Builder()
                .url(mDownUrl)
                .get()
                .build();
        mCall = OkHttpClientCreate.createClient().newCall(request);//构建了一个完整的http请求
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpDownListener != null) {
                    mHttpDownListener.onFailure(call, e, downUrl);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                mTotalLength = responseBody.contentLength();//下载文件的总长度
                InputStream inp = responseBody.byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(mPath);
                try {
                    byte[] bytes = new byte[2048];
                    int len = 0;
                    while ((len = inp.read(bytes)) != -1) {
                        mAlreadyDownLength = mAlreadyDownLength + len;
                        fileOutputStream.write(bytes, 0, len);
                        if (mHttpDownListener != null) {
                            mHttpDownListener.onResponse(call, response, downUrl, mTotalLength, mAlreadyDownLength);
                        }
                    }
                    if (mHttpDownListener != null) {
                        mHttpDownListener.onComplete(downUrl, saveFilePathAndName.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mHttpDownListener != null) {
                        mHttpDownListener.onFailure(call, e, downUrl);
                    }
                } finally {
                    fileOutputStream.close();
                    inp.close();
//                    Log.e(TAG, "流关闭");
                }
            }
        });
    }

    /**
     * 有断点续传功能的get下载
     *
     * @param downUrl             下载地址
     * @param saveFilePathAndName 保存路径
     * @param listener            进度监听
     */
    public void getRenewalDownRequest(final String downUrl, final File saveFilePathAndName, final DownloadListener listener) {
        mSign = 2;
        mDownUrl = downUrl;
        mPath = saveFilePathAndName;
        mHttpDownListener = listener;
        Request request = new Request.Builder()
                .url(mDownUrl)
                .header("RANGE", "bytes=" + mAlreadyDownLength + "-")
                .build();
        mCall = OkHttpClientCreate.createClient().newCall(request);//构建了一个完整的http请求
        mCall.enqueue(new Callback() { //发送请求
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpDownListener != null) {
                    mHttpDownListener.onFailure(call, e, downUrl);
                }
                Log.e(TAG, "onFailure: 异常报错=" + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                InputStream inputStream = responseBody.byteStream();//得到输入流
                RandomAccessFile randomAccessFile = new RandomAccessFile(mPath, "rw");//得到任意保存文件处理类实例
                if (mTotalLength == 0) {
                    mTotalLength = responseBody.contentLength();//得到文件的总字节大小
                    randomAccessFile.setLength(mTotalLength);//预设创建一个总字节的占位文件
                }
                if (mAlreadyDownLength != 0) {
                    randomAccessFile.seek(mAlreadyDownLength);
                }
                byte[] bytes = new byte[2048];
                int len = 0;
                try {
                    while ((len = inputStream.read(bytes)) != -1) {
                        randomAccessFile.write(bytes, 0, len);
                        mAlreadyDownLength = mAlreadyDownLength + len;
                        if (mHttpDownListener != null) {
                            mHttpDownListener.onResponse(call, response, downUrl, mTotalLength, mAlreadyDownLength);
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Get下载异常");

                } finally {
                    mAlreadyDownLength = randomAccessFile.getFilePointer();//记录当前保存文件的位置
                    randomAccessFile.close();
                    inputStream.close();
                    Log.e(TAG, "流关闭 下载的位置=" + mAlreadyDownLength);
                }

            }
        });
    }

    /**
     * 没有断点续传的post下载
     *
     * @param downUrl
     * @param saveFilePathAndName
     * @param json
     * @param listener
     */
    public void postDownRequest(final String downUrl, final File saveFilePathAndName, final JSONObject json, final DownloadListener listener) {
        mSign = 3;
        mDownUrl = downUrl;
        mPath = saveFilePathAndName;
        mJson = json;
        mHttpDownListener = listener;
        mAlreadyDownLength = 0;
        Request request = new Request.Builder()
                .url(mDownUrl)
                .post(changeJSON(json))
                .build();
        mCall = OkHttpClientCreate.createClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpDownListener != null) {
                    mHttpDownListener.onFailure(call, e, downUrl);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody responseBody = response.body();
                mTotalLength = responseBody.contentLength();
                InputStream inputStream = responseBody.byteStream();
                FileOutputStream fileOutputStream = new FileOutputStream(mPath);
                byte[] bytes = new byte[2048];
                int len = 0;
                try {
                    while ((len = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, len);
                        mAlreadyDownLength = mAlreadyDownLength + len;
                        if (mHttpDownListener != null) {
                            mHttpDownListener.onResponse(call, response, downUrl, mTotalLength, mAlreadyDownLength);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Post下载异常");
                } finally {
                    fileOutputStream.close();
                    inputStream.close();
                    Log.e(TAG, "流关闭");

                }

            }

        });

    }

    /**
     * 支持断点续传的post下载
     *
     * @param downUrl             下载网址
     * @param saveFilePathAndName 文件保存路径
     * @param json                参数
     * @param listener            接口回调
     */
    public void postRenewalDownRequest(final String downUrl, final File saveFilePathAndName, final JSONObject json, final DownloadListener listener) {
        mSign = 4;
        mDownUrl = downUrl;
        mPath = saveFilePathAndName;
        mJson = json;
        mHttpDownListener = listener;
        Request request = new Request.Builder()
                .url(mDownUrl)
                .header("RANGE", "bytes=" + mAlreadyDownLength + "-")
                .post(changeJSON(json))
                .build();
        mCall = OkHttpClientCreate.createClient().newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpDownListener != null) {
                    mHttpDownListener.onFailure(call, e, downUrl);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                InputStream inputStream = responseBody.byteStream();
                RandomAccessFile randomAccessFile = new RandomAccessFile(mPath, "rw");
                if (mTotalLength == 0) {
                    mTotalLength = responseBody.contentLength();
                    randomAccessFile.setLength(mTotalLength);
                }
                if (mAlreadyDownLength != 0) {
                    randomAccessFile.seek(mAlreadyDownLength);
                }
                byte[] bytes = new byte[2048];
                int len = 0;
                try {
                    while ((len = inputStream.read(bytes)) != -1) {
                        randomAccessFile.write(bytes, 0, len);
                        mAlreadyDownLength = mAlreadyDownLength + len;
                        if (mHttpDownListener != null) {
                            mHttpDownListener.onResponse(call, response, downUrl, mTotalLength, mAlreadyDownLength);
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, "Post下载异常");

                } finally {
                    mAlreadyDownLength = randomAccessFile.getFilePointer();
                    randomAccessFile.close();
                    inputStream.close();
                    Log.e(TAG, "流关闭 下载的位置=" + mAlreadyDownLength);
                }

            }
        });
    }

    /**
     * 恢复下载
     */
    public void resume() {
        if (mSign == 0) {
            return;
        }
        switch (mSign) {
            case 1:
                getDownRequest(mDownUrl, mPath, mHttpDownListener);
                break;
            case 2:
                getRenewalDownRequest(mDownUrl, mPath, mHttpDownListener);
                break;
            case 3:
                postDownRequest(mDownUrl, mPath, mJson, mHttpDownListener);
                break;
            case 4:
                postRenewalDownRequest(mDownUrl, mPath, mJson, mHttpDownListener);
                break;
            default:
                break;
        }
    }

    /**
     * 暂停下载
     */
    public void stop() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    /**
     * 删除下载文件
     */
    public void deleteCurrentFile() {
        if (mPath == null) {
            Log.e(TAG, "deleteCurrentFile error : 没有路径");
            return;
        }
        if (!mPath.exists()) {
            Log.e(TAG, "deleteCurrentFile error: 文件不存在");
            return;
        }
        mPath.delete();
        mAlreadyDownLength = 0;
        mTotalLength = 0;
        mSign = 0;
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
        mSign = 0;
        mDownUrl = null;
        mPath = null;
        mHttpDownListener = null;
        mAlreadyDownLength = 0;
        mTotalLength = 0;
    }

    /**
     * 转换Json参数为RequestBody
     *
     * @param jsonParam json对象
     * @return RequestBody
     */
    private RequestBody changeJSON(JSONObject jsonParam) {
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , String.valueOf(jsonParam));
        return requestBody;
    }

    /**
     * 获取网络图片转bitmap
     *
     * @param downUrl 下载网络地址
     */
    public void getBitmapDownRequest(final String downUrl, final DownloadListener listener) {
        mSign = 1;
        mDownUrl = downUrl;
        mHttpDownListener = listener;
        mAlreadyDownLength = 0;
        Request request = new Request.Builder()
                .url(mDownUrl)
                .get()
                .build();
        mCall = OkHttpClientCreate.createClient().newCall(request);//构建了一个完整的http请求
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mHttpDownListener != null) {
                    mHttpDownListener.onFailure(call, e, downUrl);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                mTotalLength = responseBody.contentLength();//下载文件的总长度
                InputStream inp = responseBody.byteStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byte[] bytes = new byte[2048];
                    int len = 0;
                    while ((len = inp.read(bytes)) != -1) {
                        mAlreadyDownLength = mAlreadyDownLength + len;
                        byteArrayOutputStream.write(bytes, 0, len);
                        if (mHttpDownListener != null) {
                            mHttpDownListener.onResponse(call, response, downUrl, mTotalLength, mAlreadyDownLength);
                        }
                    }
                    if (mHttpDownListener != null) {
                        mHttpDownListener.onComplete(BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.size()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mHttpDownListener != null) {
                        mHttpDownListener.onFailure(call, e, downUrl);
                    }
                } finally {
                    byteArrayOutputStream.close();
                    inp.close();
                }
            }
        });
    }
}