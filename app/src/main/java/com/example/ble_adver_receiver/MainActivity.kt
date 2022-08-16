package com.example.ble_adver_receiver

import android.annotation.SuppressLint
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ble_adver_receiver.adapter.DeviceAdapter
import com.example.ble_adver_receiver.scanner.GallCallbackImpl
import com.example.ble_adver_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var bleController : BLEController ?= null
    lateinit var deviceAdapter: DeviceAdapter
    var deviceList = ArrayList<ScanResult>()

    lateinit var activityMainBinding: ActivityMainBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        bleController = BLEController.getInstance(this)
        deviceAdapter = DeviceAdapter(deviceList){
            deviceList[it].device.connectGatt(this@MainActivity,false, GallCallbackImpl())
        }

        activityMainBinding.deviceRcv.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activityMainBinding.deviceRcv.adapter = deviceAdapter

    }

    override fun onResume() {
        super.onResume()
        if (BuildConfig.BleType == 2){
            if (bleController?.bluetoothAdapter!!.isEnabled) {
                Log.e("YoAdvertise", "Enabled")
                bleController?.bleAdvertiser!!.startAdvertising()
            }
            else{

            }
        }
        else{
            bleController?.bleScanner?.startScan(scanCallback)
            Handler(Looper.getMainLooper()).postDelayed({
                bleController?.bleScanner?.stopScan(scanCallback)
            },5000)
        }
    }

    val scanCallback = object : ScanCallback() {
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.e("YoScan", "Error-$errorCode")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.e("YoScan",""+ result.toString())
            if (deviceList.size == 0){
                deviceList.add(result!!)
            }
            else{
                for (res in deviceList){
                    if (res.device.address == result?.device?.address){
                        Log.e("sksksk",";sslsl")
                        return
                    }
                }
                deviceList.add(result!!)
            }
            if (!deviceList.contains(result)){
                deviceList.add(result)
            }
            deviceAdapter.notifyDataSetChanged()
        }
    }
}