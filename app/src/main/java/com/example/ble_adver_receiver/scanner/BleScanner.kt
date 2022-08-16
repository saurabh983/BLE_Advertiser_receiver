package com.example.ble_adver_receiver.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import com.example.ble_adver_receiver.PairArgsSingletonHolder
import com.example.ble_adver_receiver.constants.Constants
import java.util.*
import kotlin.collections.ArrayList

class BleScanner private constructor(private val context: Context, private val bluetoothAdapter: BluetoothAdapter){

    companion object: PairArgsSingletonHolder<BleScanner, Context, BluetoothAdapter>(::BleScanner)

    private val parcelUUID = ParcelUuid(UUID.fromString(Constants.sampleUUID))

    val bleScanner = bluetoothAdapter.bluetoothLeScanner

    private val scanFilter = ScanFilter.Builder().setServiceUuid(parcelUUID)
        .build()

    private var scanFilters = ArrayList<ScanFilter>()

    private val scanSettings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()


    @SuppressLint("MissingPermission")
    fun startScan(scanCallback: ScanCallback){
        scanFilters.add(scanFilter)
        bleScanner?.startScan(scanFilters, scanSettings, scanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan(scanCallback: ScanCallback){
        bleScanner?.stopScan(scanCallback)
    }

}