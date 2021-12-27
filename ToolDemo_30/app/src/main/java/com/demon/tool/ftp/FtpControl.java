package com.demon.tool.ftp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * ftp 控制类
 */
public class FtpControl {

    private Context context;

    private FTP ftp;

    public FtpControl(Context context) {
        this.context = context;
    }


    /**
     * 登录ftp服务器
     *
     * @param host
     * @param userName
     * @param pwd
     */
    public void loginFtp(String host, int port, String userName, String pwd) {
        if (ftp == null) {
            ftp = new FTP(host, port, userName, pwd);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        boolean isLogin = ftp.openConnect();
                        if (isLogin) {
                            if (onFtpListener != null) {
                                onFtpListener.onLoginSucceed();
                            }
                        } else {
                            if (onFtpListener != null) {
                                onFtpListener.onLoginFailure("ftp登录失败");
                            }
                            ftp.closeConnect();
                            ftp = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (onFtpListener != null) {
                            onFtpListener.onLoginFailure("ftp登录异常:" + e.getMessage());
                        }
                        try {
                            ftp.closeConnect();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        ftp = null;
                    }
                }
            }.start();
        } else {
            showToast("已经登录ftp账号");
        }
    }

    /**
     * 登出ftp 服务器
     */
    public void logoutFtp() {
        if (ftp != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        ftp.closeConnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ftp = null;
                }
            }.start();
        }
    }

    /**
     * 上传 ftp 文件
     *
     * @param localPath
     * @param remotePath
     */
    public void uploadFile(final String localPath, final String remotePath) {
        if (ftp != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Result result = ftp.uploading(new File(localPath), remotePath);
                        if (result.isSucceed()) {
                            // 文件上传成功;
                            if (onFtpListener != null) {
                                onFtpListener.onUploadSucceed(localPath);
                            }
                        } else {
                            // 文件上传失败
                            if (onFtpListener != null) {
                                onFtpListener.onUoloadFailure(localPath, "文件上传失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 文件上传异常
                        if (onFtpListener != null) {
                            onFtpListener.onUoloadFailure(localPath, "文件上传异常:" + e.getMessage());
                        }
                    }
                }
            }.start();
        } else {
            if (onFtpListener != null) {
                onFtpListener.onUoloadFailure(localPath, "还未登录ftp账号");
            }
        }
    }

    /**
     * 下载 ftp 文件
     *
     * @param remotePath
     * @param remoteFileName
     * @param localPath
     */
    public void downloadFile(final String remotePath, final String remoteFileName, final String localPath) {
        if (ftp != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        Result result = ftp.download(remotePath, remoteFileName, localPath);
                        if (result.isSucceed()) {
                            // 下载成功;
                            if (onFtpListener != null) {
                                onFtpListener.onDownloadSucceed(remoteFileName);
                            }
                        } else {
                            // 下载失败
                            if (onFtpListener != null) {
                                onFtpListener.onDownloadFailure(remoteFileName, "文件下载失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 下载异常
                        if (onFtpListener != null) {
                            onFtpListener.onDownloadFailure(remoteFileName, "文件下载异常:" + e.getMessage());
                        }
                    }
                }
            }.start();
        } else {
            if (onFtpListener != null) {
                onFtpListener.onDownloadFailure(remoteFileName, "还未登录ftp账号");
            }
        }
    }

    public interface OnFtpListener {

        void onLoginSucceed();

        void onLoginFailure(String msg);

        void onUploadSucceed(String filePath);

        void onUoloadFailure(String filePath, String msg);

        void onDownloadSucceed(String remoteFileName);

        void onDownloadFailure(String remoteFileName, String msg);
    }

    private OnFtpListener onFtpListener;

    public void setOnFtpListener(OnFtpListener onFtpListener) {
        this.onFtpListener = onFtpListener;
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FtpControl", msg);
    }

}
