package com.example.gedeffe.bluetoothlowenergy;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gcscb2 on 27/11/16.
 */
public class LeDeviceListAdapter extends ArrayAdapter<DeviceItem> implements ListAdapter {

    private List<BluetoothDevice> realDevices = new ArrayList<>();

    public LeDeviceListAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void addDevice(BluetoothDevice device) {
        // when we add a new device, we would like to display its name and its UUID

        // first, check that provided device is not already listed
        if (!this.realDevices.contains(device)) {
            this.realDevices.add(device);
            DeviceItem deviceItem = new DeviceItem(device.getName(), device.getAddress(), device);
            this.add(deviceItem);
        }
    }
}
