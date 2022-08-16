package com.example.ble_adver_receiver.scanner

import android.annotation.SuppressLint
import android.bluetooth.*
import android.os.Looper
import android.util.Log
import com.example.ble_adver_receiver.constants.Constants
import java.util.*
import java.util.logging.Handler

class GallCallbackImpl: BluetoothGattCallback() {

    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        Log.e("jsjsjjs","onConnectionStateChange")
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            // successfully connected to the GATT Server
            Log.e("jsjsjjs","Connected")
            gatt?.discoverServices()
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // disconnected from the GATT Server
            Log.e("jsjsjjs","Disconnected")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        Log.e("jsjsjjs","onServicesDiscovered")
        var service = gatt?.getService(UUID.fromString(Constants.sampleUUID))
        for (serv in service!!.characteristics){
            Log.e("jsjsjjs","Discovered Character-"+serv.uuid)
        }
        var gattCharacteristic = service?.getCharacteristic(UUID.fromString(Constants.CHARACTERISTIC_UUID_READ))
        Log.e("jsjsjjs","Read: "+gattCharacteristic?.uuid)

        var notificationset = gatt?.setCharacteristicNotification(gattCharacteristic,true)
        Log.e("jsjsjjs", "Notification Result- $notificationset")

        var gattWrite = service?.getCharacteristic(UUID.fromString(Constants.CHARACTERISTIC_UUID_WRITE))
        gattWrite?.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        gattWrite?.setValue("tststts")
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            var writeResult = gatt?.writeCharacteristic(gattWrite)

            Log.e("jsjsjjs", "Write Result- $writeResult")
        },1000)

    }

    override fun onCharacteristicRead(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        super.onCharacteristicRead(gatt, characteristic, status)
        Log.e("jsjsjjs","onCharacteristicRead")
        if (status == BluetoothGatt.GATT_SUCCESS){
            Log.e("jsjsjjs", "Characteristics read success")
        }
        else{
            Log.e("jsjsjjs", "Characteristics read failed")
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        super.onCharacteristicWrite(gatt, characteristic, status)
        Log.e("jsjsjjs","onCharacteristicWrite")
    }

    override fun onDescriptorRead(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorRead(gatt, descriptor, status)
        Log.e("jsjsjjs","onDescriptorRead")
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        super.onDescriptorWrite(gatt, descriptor, status)
        Log.e("jsjsjjs","onDescriptorWrite")
    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        super.onCharacteristicChanged(gatt, characteristic)
        Log.e("jsjsjjs","onCharacteristicChanged")
    }
}