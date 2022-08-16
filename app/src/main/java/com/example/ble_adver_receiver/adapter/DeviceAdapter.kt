package com.example.ble_adver_receiver.adapter

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ble_adver_receiver.R

class DeviceAdapter(var items: ArrayList<ScanResult>, var onClick: (Int) -> Unit): RecyclerView.Adapter<DeviceHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder
    = DeviceHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.device_item,parent,false))

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.deviceName.text = items[position].device.address!!.toString()
        holder.connect.setOnClickListener {
            onClick.invoke(position)
        }
    }

    override fun getItemCount(): Int = items.size
}

class DeviceHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var deviceName: TextView
    var connect: Button
    init {
        deviceName = itemView.findViewById(R.id.device_name)
        connect = itemView.findViewById(R.id.connect_device)
    }
}