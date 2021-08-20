package com.demon.tool.download;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.tool.R;
import com.demon.tool.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadDialog extends Dialog implements DialogInterface.OnClickListener {

    private Context context;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.rv_download)
    RecyclerView recycler;
    private DownloadAdapter adapter;
    private List<DownloadBean> downloadList;

    private String basePath;
    private List<String> urlList = new ArrayList<>();
    private int downloadCount = 0;

    public DownloadDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        tvTitle.setText("文件下载(0/" + urlList.size() + ")");
        initAdapter();
    }

    public void startDownload(List<String> list) {
        urlList.addAll(list);
        DownloadThread downloadThread = new DownloadThread(context, list, msgHandle);
        if (!StringUtil.isEmpty(basePath)) {
            downloadThread.setBasePath(basePath);
        }
        downloadThread.start();
    }

    private Handler msgHandle = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadThread.MSG_UPDATE:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    String url = map.get("url");
                    int progress = Integer.parseInt(map.get("progress"));
                    int maxProgress = Integer.parseInt(map.get("maxProgress"));
                    update(url, progress, maxProgress);
                    break;
                case DownloadThread.MSG_FAILURE:
                    url = (String) msg.obj;
                    failure(url);
                    break;
                case DownloadThread.MSG_COMPLETE:
                    map = (Map<String, String>) msg.obj;
                    url = map.get("url");
                    String savePath = map.get("savePath");
                    complete(url, savePath);
                    break;
            }
            return true;
        }

        private void update(String url, int progress, int maxProgress) {
            DownloadBean bean = getBean(url);
            if (bean == null) {
                bean = new DownloadBean();
                bean.url = url;
                bean.maxProgress = maxProgress;
                bean.progress = progress;
                downloadList.add(bean);
            }

            bean.progress = progress;
            int percent = (int) (progress * 1.0f / maxProgress * 100);
            bean.percent = percent + "%";
            adapter.notifyDataSetChanged();
//            showLog(url + " : " + present);
        }

        private void failure(String url) {
            downloadCount++;
            tvTitle.setText("文件下载(" + downloadCount + "/" + urlList.size() + ")");

            DownloadBean bean = getBean(url);
            if (bean != null) {
                downloadList.remove(bean);
                adapter.notifyDataSetChanged();
            }
            if (downloadCount == urlList.size()) {
                dismiss();
//                showToast("文件下载完成");
                if (onDownloadCompleteListener != null) {
                    onDownloadCompleteListener.onAllComplete();
                }
            }
        }

        private void complete(String url, String savePath) {
            downloadCount++;
            showLog(downloadCount + " 下载完成：" + url);
            tvTitle.setText("文件下载(" + downloadCount + "/" + urlList.size() + ")");
            if (MediaFile.isImageFileType(savePath)) {  // 注册图片到相册
//                    MediaStore.Images.Media.insertImage(context.getContentResolver(), savePath, savePath, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(new File(savePath));
                intent.setData(uri);
                context.sendBroadcast(intent);
            } else if (MediaFile.isVideoFileType(savePath)) {   // 注册视频到相册
                //是否添加到相册
                ContentResolver localContentResolver = context.getContentResolver();
                ContentValues localContentValues = MediaFile.getVideoContentValues(context, new File(savePath), System.currentTimeMillis());
                localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
            }

            DownloadBean bean = getBean(url);
            if (bean != null) {
                downloadList.remove(bean);
                adapter.notifyDataSetChanged();
            }

            if (onDownloadCompleteListener != null) {
                onDownloadCompleteListener.onComplete(url, savePath);
            }

            if (downloadCount == urlList.size()) {
                dismiss();
                if (onDownloadCompleteListener != null) {
                    onDownloadCompleteListener.onAllComplete();
                }
            }
        }
    });

    /**
     * 获取已经下载的对象
     *
     * @param url
     * @return
     */
    public DownloadBean getBean(String url) {
        for (DownloadBean bean : downloadList) {
            if (bean.url.equals(url)) {
                return bean;
            }
        }
        return null;
    }

    private void initAdapter() {
        downloadList = new ArrayList<>();

        adapter = new DownloadAdapter(context, downloadList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

        adapter.setListener(new DownloadAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DownloadBean bean) {
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("download", msg);
    }

    private OnDownloadCompleteListener onDownloadCompleteListener;

    public void setOnDownloadCompleteListener(OnDownloadCompleteListener onDownloadCompleteListener) {
        this.onDownloadCompleteListener = onDownloadCompleteListener;
    }

    /**
     * 设置保存的路径
     * @param basePath
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public interface OnDownloadCompleteListener {
        void onComplete(String url, String disPath);

        void onAllComplete();
    }
}
