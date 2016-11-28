package com.example.gedeffe.bluetoothlowenergy;

import android.bluetooth.BluetoothDevice;

/**
 * Created by gcscb2 on 18/11/16.
 */
public class DeviceItem {

    private String deviceName;
    private String address;
    private BluetoothDevice realDevice;

    public String getDeviceName() {
        return deviceName;
    }

    public BluetoothDevice getRealDevice() {
        return this.realDevice;
    }

    public String getAddress() {
        return address;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceItem(String name, String address, BluetoothDevice device) {
        this.deviceName = name;
        this.address = address;
        this.realDevice = device;
    }

    @Override
    public String toString() {
        return this.getDeviceName() + "\n(" + this.getAddress() + ")";
    }
}
