package com.lilanz.kotlintool.qrcode

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class CodeScanHelper {
    // 扫描二维码
    private var capture: CaptureManager? = null
    private var barcodeScannerView: DecoratedBarcodeView? = null
    private var qrCodeResult // 二维码扫描结果
            : String? = null

    private lateinit var activity: Activity
    private var isOpened = false // 二维码是否在开启


    constructor(
        activity: Activity,
        barcodeScannerView: DecoratedBarcodeView?
    ) {
        this.activity = activity
        this.barcodeScannerView = barcodeScannerView
        initUI()
    }


    /**
     * 开启条码扫描
     */
    fun startScan() {
        isOpened = true
        capture!!.decode()
        capture!!.onResume()
        barcodeScannerView!!.visibility = View.VISIBLE
    }

    /**
     * 结束条码扫描
     */
    fun stopScanCode() {
        isOpened = false
        capture!!.onPause()
        capture!!.onDestroy()
        barcodeScannerView!!.visibility = View.INVISIBLE
    }

    private val listener: CaptureManager.OnScanResultListener = object :
        CaptureManager.OnScanResultListener {
        override fun onResultBack(resultCode: Int, data: Intent?) {
            if (resultCode == Activity.RESULT_OK) {
                val scanResult =
                    IntentIntegrator.parseActivityResult(resultCode, data)
                val qrContent = scanResult.contents
                if (onScanResultListener != null) {
                    onScanResultListener!!.onScanResult(qrContent)
                }
                qrCodeResult = qrContent
                // 扫描二维码成功后，开始视频录制
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "取消二维码扫描", Toast.LENGTH_SHORT).show()
            }
            isOpened = false
            barcodeScannerView!!.visibility = View.INVISIBLE
        }
    }

    private fun initUI() {
        capture = barcodeScannerView?.let { CaptureManager(activity, it) }
        val intent = Intent(activity, activity.javaClass)
        capture!!.initializeFromIntent(intent, null)
        capture!!.setOnScanResultListener(listener)
    }

    private var onScanResultListener: OnScanResultListener? = null

    fun setOnScanResultListener(listener: OnScanResultListener?) {
        onScanResultListener = listener
    }

    interface OnScanResultListener {
        fun onScanResult(content: String?)
    }

    fun isOpened(): Boolean {
        return isOpened
    }
}