package com.lilanz.tooldemo.multiplex.scanhelper;

import android.os.Handler;
import android.view.KeyEvent;

/**
 * 扫码枪事件解析类
 */
public class ScanGunKeyEventHelper {


    //延迟500ms，判断扫码是否完成。
    private final static long MESSAGE_DELAY = 500;
    //扫码内容
    private StringBuffer mStringBufferResult = new StringBuffer();
    //大小写区分
    private boolean mCaps;
    private OnScanSuccessListener mOnScanSuccessListener;
    private Handler mHandler = new Handler();
    private int id;

    public ScanGunKeyEventHelper(int id) {
        this.id = id;
    }

    private final Runnable mScanningFishedRunnable = new Runnable() {
        @Override
        public void run() {
            performScanSuccess();
        }
    };

    //返回扫描结果
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null)
            mOnScanSuccessListener.onScanSuccess(id, barcode);
        mStringBufferResult.setLength(0);
    }

    //key事件处理
    public void analysisKeyEvent(KeyEvent event) {

        int keyCode = event.getKeyCode();

        //字母大小写判断
//        checkLetterStatus(event);
        if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) {    // Caps lock
            if (KeyEvent.META_CAPS_LOCK_ON == (event.getMetaState() & KeyEvent.META_CAPS_LOCK_ON)) {
                //表示大写
                mCaps = true;
            } else {
                //表示小写
                mCaps = false;
            }
            // 左右 Shift
        } else if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                mCaps = true;
            } else {
                //松开shift键，表示小写
                mCaps = false;
            }
        } else if (event.getAction() == KeyEvent.ACTION_DOWN) {

            char aChar = getInputCode(event);

            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }

            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                //若为回车键，直接返回
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else {
                //延迟post，若500ms内，有其他事件
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }
        }
    }

    //检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                mCaps = true;
            } else {
                //松开shift键，表示小写
                mCaps = false;
            }
        }
    }


    //获取扫描内容
    private char getInputCode(KeyEvent event) {

        int keyCode = event.getKeyCode();

        char aChar;

        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            //字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            //数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
            // 小键盘中的数字
        } else if (keyCode >= KeyEvent.KEYCODE_NUMPAD_0 && keyCode <= KeyEvent.KEYCODE_NUMPAD_9) {
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_NUMPAD_0);
        } else {
            //其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_EQUALS:
                    aChar = mCaps ? '+' : '=';
                    break;
                case KeyEvent.KEYCODE_PERIOD:
                case KeyEvent.KEYCODE_NUMPAD_DOT:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_SUBTRACT:
                    aChar = '-';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_DIVIDE:
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_MULTIPLY:
                    aChar = '*';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_ADD:
                    aChar = '+';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }

        return aChar;

    }


    public interface OnScanSuccessListener {
        public void onScanSuccess(int id, String barcode);
    }

    public void setOnBarCodeCatchListener(OnScanSuccessListener onScanSuccessListener) {
        mOnScanSuccessListener = onScanSuccessListener;
    }

    public void onDestroy() {
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mOnScanSuccessListener = null;
    }

    public int getId() {
        return id;
    }
}