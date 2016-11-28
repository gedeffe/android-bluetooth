package com.example.gedeffe.bluetoothlowenergy;

/**
 * To share data between activities, but without having to serialize them.
 */
public class SharedDataHandler {
    private static DeviceItem deviceItem;

    public static DeviceItem getDeviceItem() {
        return deviceItem;
    }

    public static void setDeviceItem(DeviceItem deviceItem) {
        SharedDataHandler.deviceItem = deviceItem;
    }
}
