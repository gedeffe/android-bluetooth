package com.example.gedeffe.bluetooth;

/**
 * Created by gcscb2 on 18/11/16.
 */
public class DeviceItem {

    private String deviceName;
    private String address;
    private boolean connected;

    public String getDeviceName() {
        return deviceName;
    }

    public boolean getConnected() {
        return connected;
    }

    public String getAddress() {
        return address;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceItem(String name, String address, boolean connected){
        this.deviceName = name;
        this.address = address;
        this.connected = connected;
    }

    @Override
    public String toString() {
        return this.getDeviceName() + "\n(" + this.getAddress() + ")";
    }
}
