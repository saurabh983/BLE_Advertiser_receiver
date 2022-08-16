package com.example.ble_adver_receiver.advertiser

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import com.example.ble_adver_receiver.PairArgsSingletonHolder
import com.example.ble_adver_receiver.TripleArgsSingletonHolder
import com.example.ble_adver_receiver.constants.Constants
import java.util.*

class BleAdvertiser private constructor(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
    private val bluetoothManager: BluetoothManager
) {

    var readCharacteristic: BluetoothGattCharacteristic? = null
    var writeCharacteristic: BluetoothGattCharacteristic? = null
    var bluetoothGattService: BluetoothGattService? = null

    private val btAdvertiser: BluetoothLeAdvertiser by lazy {
        bluetoothAdapter.bluetoothLeAdvertiser
    }

    private val advertiserSetting: AdvertiseSettings = AdvertiseSettings.Builder()
        .setConnectable(true)
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH).build()

    private val parcelUUID = ParcelUuid(UUID.fromString(Constants.sampleUUID))

    private val advertiseData = AdvertiseData.Builder()
        .addServiceUuid(parcelUUID)
        .build()

    @SuppressLint("MissingPermission")
    private fun setAdvertisingProperties() {
        val gattServer = bluetoothManager.openGattServer(context, GattServerCallback())

        readCharacteristic = BluetoothGattCharacteristic(
            UUID.fromString(Constants.CHARACTERISTIC_UUID_READ),
            BluetoothGattCharacteristic.PROPERTY_READ or BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ
        )

        writeCharacteristic = BluetoothGattCharacteristic(
            UUID.fromString(Constants.CHARACTERISTIC_UUID_WRITE),
            BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY or
                    BluetoothGattCharacteristic.PROPERTY_INDICATE,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        val descripter = BluetoothGattDescriptor(
            UUID.fromString(Constants.DESCRIPTOR_CONFIG_UUID),
            BluetoothGattDescriptor.PERMISSION_READ or
                    BluetoothGattDescriptor.PERMISSION_WRITE
        )

//        readCharacteristic?.addDescriptor(descripter)

        bluetoothGattService = BluetoothGattService(UUID.fromString(Constants.sampleUUID),BluetoothGattService.SERVICE_TYPE_PRIMARY)

        bluetoothGattService?.addCharacteristic(readCharacteristic)
        bluetoothGattService?.addCharacteristic(writeCharacteristic)

        gattServer.services.clear()

        gattServer.addService(bluetoothGattService)

    }

    @SuppressLint("MissingPermission")
    fun startAdvertising() {
        setAdvertisingProperties()
        btAdvertiser.startAdvertising(advertiserSetting, advertiseData, advertiseCallback)
    }

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.e("YoAdvertise", "Success")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e("YoAdvertise", "Error-$errorCode")
        }
    }


    companion object :
        TripleArgsSingletonHolder<BleAdvertiser, Context, BluetoothAdapter, BluetoothManager>(::BleAdvertiser)
}