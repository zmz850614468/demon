package com.demon.tool.ftp_ssh;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FtpSshControl extends Thread {

    private Context context;
    private SFTPClient sftpClient;
    private boolean isFtpConnected;

    private List<UploadBean> uploadList;
    private boolean isContinue;
    private boolean isSleeping;

    private String host;
    private String userName;
    private String pwd;


    public FtpSshControl(Context context, String host, String userName, String pwd) {
        this.context = context;
        isContinue = true;
        uploadList = new ArrayList<>();
        this.host = host;
        this.userName = userName;
        this.pwd = pwd;
    }

    public synchronized void add(UploadBean bean) {
        uploadList.add(bean);
        if (isSleeping) {
            interrupt();
        }
    }

    public synchronized UploadBean get() {
        if (!uploadList.isEmpty()) {
            return uploadList.remove(0);
        }

        return null;
    }

    @Override
    public void run() {
        super.run();

        int failureCount = 0;
        while (isContinue) {

            UploadBean bean = get();
            while (bean != null && isContinue) {
                // 1.先登录
                while (!isFtpConnected && isContinue) {
                    connect(host, userName, pwd);

                    if (!isFtpConnected) {  // 连接异常后，先等待
                        try {
                            sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // 2.上传文件
                boolean isSucceed = upload(bean.localDir, bean.remoteDir);
                if (isSucceed) {
                    bean = get();
                    failureCount = 0;
                } else {
                    failureCount++;
                    if (failureCount >= 5) {    // 连续多次失败，则丢弃这个文件
                        bean = get();
                        failureCount = 0;
                    }
                }

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 3.结束后，断开连接
            if (isFtpConnected) {
                disconnect();
            }

            if (!uploadList.isEmpty()) {
                continue;
            }

            try {
                isSleeping = true;
                sleep(10 * 60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isSleeping = false;
        }
    }

    /**
     * 连接 sftp 服务器
     *
     * @param host
     * @param userName
     * @param pwd
     */
    private void connect(final String host, final String userName, final String pwd) {
        if (sftpClient == null) {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
            try {
                sftpClient = new SFTPClient(host, userName, pwd);
                sftpClient.connect();
                showLog("sftp 连接成功");
                isFtpConnected = true;
            } catch (Exception e) {
                e.printStackTrace();
                showLog("sftp 连接异常:" + e.getMessage());
                sftpClient.disconnect();
                sftpClient = null;
                isFtpConnected = false;
            }
//                }
//            }.start();
        } else {
            showToast("sftp 客户端已经初始化");
        }
    }

    /**
     * 上传 sftp 文件
     *
     * @param file   本地文件地址
     * @param target 远程文件地址
     */
    private boolean upload(String file, String target) {
        if (sftpClient != null) {
            try {
                sftpClient.putFile(file, target);
                showLog("文件上传成功");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                showLog("文件上传异常：" + e.getMessage());
            }
        } else {
            showToast("sftp 客户端还未初始化");
        }

        return false;
    }

    /**
     * 断开 sftp 连接
     */
    private void disconnect() {
        if (sftpClient != null) {
            sftpClient.disconnect();
            sftpClient = null;
            isFtpConnected = false;
            showLog("sftp 断开连接");
        }
    }

    public void close() {
        isContinue = false;
        interrupt();
    }

    public static class UploadBean {
        public String localDir;     // 本地文件地址
        public String remoteDir;    // 远程文件地址
    }

    private void showToast(String msg) {
        showLog(msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("FtpSshControl", msg);
    }

}
