package com.lilanz.tooldemo.prints.fuliaocang;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fujitsu.fitPrint.Library.FitPrintAndroidLan_v1011.FitPrintAndroidLan;
import com.lilanz.tooldemo.R;


/**
 * 辅料仓使用的打印机
 */
public class FuLiaoCangActivity extends Activity
        implements View.OnClickListener {

    FitPrintAndroidLan mPrinter = new FitPrintAndroidLan();

    //////////////////////////////////////////////////////////
    // Layout
    private Button mBtnLanConnect = null;
    private Button mBtnLanDisconnect = null;
    private Button mBtnPrint = null;
    private Button mBtnPrintPageMode = null;
    private Button mBtnGetStatus = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_liao_cang_lan);

        mBtnLanConnect = (Button)findViewById(R.id.btnLanConnect);
        mBtnLanConnect.setOnClickListener(this);
        mBtnLanDisconnect = (Button)findViewById(R.id.btnLanDisconnect);
        mBtnLanDisconnect.setOnClickListener(this);
        mBtnPrint = (Button)findViewById(R.id.btnPrint);
        mBtnPrint.setOnClickListener(this);
        mBtnPrintPageMode = (Button)findViewById(R.id.btnPageMode);
        mBtnPrintPageMode.setOnClickListener(this);
        mBtnGetStatus = (Button)findViewById(R.id.btnGetStatus);
        mBtnGetStatus.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fit_print_sample_lan, menu);
        return true;
    }

    private final Handler handler = new Handler();
    public int mRtn = 0 ;
    public int i = 0;


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == mBtnLanConnect)
        {
            Thread thread= new Thread(new Runnable(){ public void run(){

                EditText editText = (EditText)findViewById(R.id.editIpAddress);
                String strText = editText.getText().toString();
                mRtn = mPrinter.Connect(strText);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FuLiaoCangActivity.this,  ErrorValue(mRtn), Toast.LENGTH_SHORT).show();
                    }});
            }});
            thread.start();
        }

        else if (v == mBtnLanDisconnect)
        {
            Thread thread= new Thread(new Runnable(){ public void run(){

                mPrinter.Disconnect();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FuLiaoCangActivity.this,  "Success", Toast.LENGTH_SHORT).show();
                    }});
            }});
            thread.start();
        }

        else if (v == mBtnPrint)
        {
            Thread thread= new Thread(new Runnable(){ public void run(){

                int nRtn;
                // Text
                nRtn = mPrinter.SetLocale(8);
                EditText editText = (EditText)findViewById(R.id.editTextText);
                String strText = editText.getText().toString() + '\n';
                nRtn = mPrinter.PrintText(strText , "SJIS");
                nRtn = mPrinter.PaperFeed(64);

                // Image
                editText = (EditText)findViewById(R.id.editImagePath);
                strText = editText.getText().toString() ;
                nRtn = mPrinter.PrintImageFile(strText) ;
                nRtn = mPrinter.PaperFeed(64);

                // Barcode (JAN13)
                editText = (EditText)findViewById(R.id.editBarcode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintBarcode(2, strText, 2, 0, 2, 100, 0) ;
                nRtn = mPrinter.PaperFeed(64);

                // QR-Code
                editText = (EditText)findViewById(R.id.editQrCode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintQrCode(strText, 0, 6, false, 0);

                // Feed and Cut
                nRtn = mPrinter.PaperFeed(64);
                nRtn = mPrinter.CutPaper(0);
            }});

            thread.start();
        }

        else if (v == mBtnPrintPageMode)
        {
            Thread thread= new Thread(new Runnable(){ public void run(){

                int nRtn = 0;

                nRtn = mPrinter.SetLocale(8);
                nRtn = mPrinter.SetPageMode();
                nRtn = mPrinter.SetPageArea(0, 0, 576, 1200);

                // Rotate 180
                nRtn = mPrinter.SetPageDirection(2);

                nRtn = mPrinter.SetLeftPos(true, 0);
                nRtn = mPrinter.SetTopPos(true, 100);
                nRtn = mPrinter.PrintText("Page Mode Test(Rotate 180)" + '\n' + '\n', "SJIS");
                EditText editText = (EditText)findViewById(R.id.editTextText);
                String strText = editText.getText().toString() + '\n';
                nRtn = mPrinter.PrintText(strText , "SJIS");

                nRtn = mPrinter.SetLeftPos(true, 0);
                nRtn = mPrinter.SetTopPos(false, 530);
                editText = (EditText)findViewById(R.id.editImagePath);
                strText = editText.getText().toString() ;
                nRtn = mPrinter.PrintImageFile(strText) ;

                nRtn = mPrinter.SetLeftPos(true, 0);
                nRtn = mPrinter.SetTopPos(false, 210);
                editText = (EditText)findViewById(R.id.editBarcode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintBarcode(2, strText, 2, 0, 2, 100, 0) ;

                nRtn = mPrinter.SetLeftPos(true, 288);
                nRtn = mPrinter.SetTopPos(false, -204);
                editText = (EditText)findViewById(R.id.editQrCode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintQrCode(strText, 0, 6, false, 0);

                nRtn = mPrinter.PrintPage();

                nRtn = mPrinter.CancelPage();

                mPrinter.CutPaper(0);

                // Rotate 90
                nRtn = mPrinter.SetPageDirection(3);

                nRtn = mPrinter.PrintText("Page Mode Test(Rotate 90)" + '\n' + '\n', "SJIS");
                editText = (EditText)findViewById(R.id.editTextText);
                strText = editText.getText().toString() + '\n';
                nRtn = mPrinter.PrintText(strText , "SJIS");

                nRtn = mPrinter.SetLeftPos(true, 0);
                nRtn = mPrinter.SetTopPos(false, 265);
                editText = (EditText)findViewById(R.id.editImagePath);
                strText = editText.getText().toString() ;
                nRtn = mPrinter.PrintImageFile(strText) ;

                nRtn = mPrinter.SetLeftPos(true, 0);
                nRtn = mPrinter.SetTopPos(false, 105);
                editText = (EditText)findViewById(R.id.editBarcode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintBarcode(2, strText, 2, 0, 2, 100, 0) ;

                nRtn = mPrinter.SetLeftPos(true, 576);
                nRtn = mPrinter.SetTopPos(false, -102);
                editText = (EditText)findViewById(R.id.editQrCode);
                strText = editText.getText().toString();
                nRtn = mPrinter.PrintQrCode(strText, 0, 6, false, 0);

                nRtn = mPrinter.PrintPage();

                nRtn = mPrinter.CancelPage();

                mPrinter.CutPaper(0);

                nRtn = mPrinter.SetStandardMode();
            }});

            thread.start();

        }
        else if (v == mBtnGetStatus)
        {
            Thread thread= new Thread(new Runnable(){ public void run(){

                // GetStatus
                mRtn = mPrinter.GetPrinterStatus();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FuLiaoCangActivity.this,  StatusValue(mRtn), Toast.LENGTH_SHORT).show();
                    }});

            }});
            thread.start();

        }
    }

    private String StatusValue(int StatusNum)
    {
        String Result = "Online(0)";

        // 200
        if(StatusNum == 200)
        {
            Result = "Offline(200)";
        }
        else if(StatusNum == 202)
        {
            Result = "Paper Near End(202) ";
        }
        else if(StatusNum == 301)
        {
            Result = "Cover Open(301) ";
        }
        else if(StatusNum == 302)
        {
            Result = "Paper End(302) ";
        }
        else if(StatusNum == 303)
        {
            Result = "Head Hot(303) ";
        }
        else if(StatusNum == 304)
        {
            Result = "Paper Layout Error(304) ";
        }
        else if(StatusNum == 305)
        {
            Result = "Cutter Jam(305) ";
        }
        else if(StatusNum == 700)
        {
            Result = "Hard Error(700) ";
        }
        else if(StatusNum == 1500)
        {
            Result = "Communication Error(1500) ";
        }
        else if(StatusNum == -3003)
        {
            Result = "Not Ready Status(-3003) ";
        }

        return Result;

    }

    private String ErrorValue(int ErrorStatus)
    {
        String Result = "Success(0)";

        // -1000
        if(ErrorStatus == -1000)
        {
            Result = "Parameter Error(-1000)";
        }
        else if(ErrorStatus == -1001)
        {
            Result = "Invalid Devices(-1001) ";
        }
        else if(ErrorStatus == -1002)
        {
            Result = "Parameter is Null(-1002) ";
        }
        else if(ErrorStatus == -1003)
        {
            Result = "Illegal data length(-1003) ";
        }
        else if(ErrorStatus == -1004)
        {
            Result = "Encoding undefined(-1004) ";
        }
        else if(ErrorStatus == -1005)
        {
            Result = "Value out of range(-1005) ";
        }
        // -1100
        else if(ErrorStatus == -1100)
        {
            Result = "Illegal characters bar code data(-1100) ";
        }
        else if(ErrorStatus == -1101)
        {
            Result = "Illegal length bar code data(-1101) ";
        }
        // -2000
        else if(ErrorStatus == -2000)
        {
            Result = "Communication Error(-2000) ";
        }
        else if(ErrorStatus == -2001)
        {
            Result = "Connect failure(-2001) ";
        }
        else if(ErrorStatus == -2002)
        {
            Result = "Not connected(-2002) ";
        }
        else if(ErrorStatus == -2003)
        {
            Result = "Time out(-2003) ";
        }
        // -3000
        else if(ErrorStatus == -3000)
        {
            Result = "File access failure(-3000) ";
        }
        else if(ErrorStatus == -3001)
        {
            Result = "File failed to read(-3001) ";
        }
        else if(ErrorStatus == -3002)
        {
            Result = "Failures to receive status(-3002) ";
        }
        else if(ErrorStatus == -3003)
        {
            Result = "Not Ready Status(-3003) ";
        }

        return Result;

    }
}
