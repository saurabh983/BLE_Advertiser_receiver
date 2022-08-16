package com.example.ble_adver_receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.ble_adver_receiver.advertiser.BleAdvertiser
import com.example.ble_adver_receiver.scanner.BleScanner

class BLEController private constructor(private val context: Context) {

    private val btManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    val bluetoothAdapter: BluetoothAdapter by lazy {
        btManager.adapter
    }

    val bleAdvertiser = BleAdvertiser.getInstance(context, bluetoothAdapter, btManager)

    val bleScanner = BleScanner.getInstance(context, bluetoothAdapter)

    companion object : SingleArgSingletonHolder<BLEController, Context>(::BLEController)
}