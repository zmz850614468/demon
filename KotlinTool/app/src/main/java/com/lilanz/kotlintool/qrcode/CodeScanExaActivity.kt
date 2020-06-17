package com.lilanz.kotlintool.qrcode

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.lilanz.kotlintool.R

class CodeScanExaActivity :Activity() , View.OnClickListener{
    private var decoratedBarcodeView: DecoratedBarcodeView? = null
    private lateinit var btStartScan: Button
    private lateinit var btStopScan: Button
    private lateinit var tvMsg: TextView

    private lateinit var codeScanHelper: CodeScanHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scan_exa)
        initUI()
        codeScanHelper = CodeScanHelper(this, decoratedBarcodeView)
        codeScanHelper.setOnScanResultListener(listener)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_start_scan -> codeScanHelper.startScan()
            R.id.bt_stop_scan -> codeScanHelper.stopScanCode()
        }
    }

    private val listener: CodeScanHelper.OnScanResultListener = object : CodeScanHelper.OnScanResultListener {
        override fun onScanResult(content: String?) {
            tvMsg!!.append(content + "\n")
        }
    }

    private fun initUI() {
        decoratedBarcodeView = findViewById(R.id.scan_code)
        btStartScan = findViewById(R.id.bt_start_scan)
        btStopScan = findViewById(R.id.bt_stop_scan)
        tvMsg = findViewById(R.id.tv_msg)
        btStartScan.setOnClickListener(this)
        btStopScan.setOnClickListener(this)
    }
}