package com.demon.tool.documentviewer;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.demon.tool.R;
import com.demon.tool.activity.App;
import com.demon.tool.download.DownloadDialog;
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
    TbsReaderView readerView;

//        String url = "http://tm.lilanz.com/qywx/test/file11.pdf";
//        String url = "http://tm.lilanz.com/qywx/test/pos_questions0614.docx";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);
        ButterKnife.bind(this);

        initUI();

//        String base = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        base += "/file11.pdf";
//        base += "/file11.docx";
//        base += "/file11.xlsx";
//        tbsViewer(base);

        download();
    }

    private void download() {
        List<String> list = new ArrayList<>();
        list.add("http://tm.lilanz.com/qywx/test/pos_questions0614.docx");

        DownloadDialog downloadDialog = new DownloadDialog(this, R.style.DialogStyleOne);
        downloadDialog.setOnDownloadCompleteListener(new DownloadDialog.OnDownloadCompleteListener() {
            @Override
            public void onComplete(String url, String disPath) {
                tbsViewer(disPath);
            }

            @Override
            public void onAllComplete() {

            }
        });
        downloadDialog.startDownload(list);
        downloadDialog.show();
    }

    private void initUI() {
        readerView = new TbsReaderView(this, null);
        readerView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.white));
        layoutRoot.addView(
                readerView,
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * 通过tbs 在应用内打开文档
     *
     * @param path
     */
    private void tbsViewer(String path) {
//        String extensionName = FileUtils.getFileType(mReaderFile.getPath());
        Bundle bundle = new Bundle();
        bundle.putString(TbsReaderView.KEY_FILE_PATH, path);
        bundle.putString(TbsReaderView.KEY_TEMP_PATH, Environment.getExternalStorageDirectory().getPath());
        boolean result = readerView.preOpen(parseFormat(path), false);
        if (result) {
            readerView.openFile(bundle);
            showLog("打开文档");
        } else {
            showLog("打开文档失败");
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        readerView.onStop();
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

    private void showLog(String msg) {
        Log.e("documentViewer", msg);
    }
}
