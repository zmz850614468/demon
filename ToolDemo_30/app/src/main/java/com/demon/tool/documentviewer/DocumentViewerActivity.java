package com.demon.tool.documentviewer;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demon.tool.R;
import com.demon.tool.download.DownloadDialog;
import com.demon.tool.download.DownloadThread;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentViewerActivity extends AppCompatActivity {

    @BindView(R.id.layout_root)
    ViewGroup layoutRoot;

    //    @BindView(R.id.reader_view)
//    private TbsReaderView readerView;

    private String url = "http://tm.lilanz.com/qywx/test/file11.pdf";
//        String url = "http://tm.lilanz.com/qywx/test/pos_questions0614.docx";

    private DocumentControl documentControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);
        ButterKnife.bind(this);

        documentControl = new DocumentControl();
        documentControl.onCreate(this, layoutRoot);

        download();
    }

    private void download() {
        String fileUrl = "http://tm.lilanz.com/qywx/test/pos_questions0614.docx";

        List<String> list = new ArrayList<>();
        list.add(fileUrl);
        // 文件保存的根地址;默认目录：Download
//        private static String BASE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        String filePath = DownloadThread.BASE_PATH + fileName;
        if (!new File(filePath).exists()) {
            showLog("本地不存在，需要下载");
            DownloadDialog downloadDialog = new DownloadDialog(this, R.style.DialogStyleOne);
            downloadDialog.setOnDownloadCompleteListener(new DownloadDialog.OnDownloadCompleteListener() {
                @Override
                public void onComplete(String url, String disPath) {
//                tbsViewer(disPath);
                    if (DocumentControl.isX5InitSucceed) {
                        documentControl.fileViewer(disPath);
                    } else {
                        runOnUiThread(() -> showToast("文件阅读内核没初始化成功"));
                    }
                }

                @Override
                public void onAllComplete() {

                }
            });
            downloadDialog.startDownload(list);
            downloadDialog.show();
        } else {
            showLog("本地存在，直接打开文件");
            if (DocumentControl.isX5InitSucceed) {
                documentControl.fileViewer(filePath);
            } else {
                runOnUiThread(() -> showToast("文件阅读内核没初始化成功"));
            }
        }
    }
//
//    private void initUI() {
//        readerView = new TbsReaderView(this, null);
//        readerView.setBackgroundColor(
//                ContextCompat.getColor(this, R.color.white));
//        layoutRoot.addView(
//                readerView,
//                new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.MATCH_PARENT,
//                        RelativeLayout.LayoutParams.MATCH_PARENT));
//    }

    /**
     * 通过tbs 在应用内打开文档
     */
//    private void tbsViewer(String path) {
////        String extensionName = FileUtils.getFileType(mReaderFile.getPath());
//        Bundle bundle = new Bundle();
//        bundle.putString(TbsReaderView.KEY_FILE_PATH, path);
//        bundle.putString(TbsReaderView.KEY_TEMP_PATH, Environment.getExternalStorageDirectory().getPath());
//        boolean result = readerView.preOpen(parseFormat(path), false);
//        if (result) {
//            readerView.openFile(bundle);
//            showLog("打开文档");
//        } else {
//            showLog("打开文档失败");
//        }
//    }
//
//    private String parseFormat(String fileName) {
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        documentControl.onDestroy();
//        readerView.onStop();
    }

    /**
     * 通过第三方应用打开文档
     */
    private void qbSdkViewer() {
        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        base += "/file11.pdf";
//        base += "/file11.docx";
//        base += "/file11.xlsx";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("style", "1");
        params.put("local", "true");
//                            params.put("memuData", jsondata);
        QbSdk.openFileReader(this, base, null, null);

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("documentViewer", msg);
    }
}
