package com.example.gedeffe.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Tutorials used for this application:
 *
 * http://www.tutos-android.com/utilisation-bluetooth-application-android
 *
 * https://code.tutsplus.com/tutorials/create-a-bluetooth-scanner-with-androids-bluetooth-api--cms-24084
 *
 * appareil 0405T0F119081B
 */
public class MainActivity extends AppCompatActivity {

    Button turnOnButton, getVisibleButton, listDevicesButton, turnOffButton;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView devicesListView;

    private final List<DeviceItem> deviceList = new ArrayList<>();
    private boolean discoveryFlag = false;

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                internalOnDeviceFound(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOnButton = (Button) findViewById(R.id.buttonTurnOn);
        getVisibleButton = (Button) findViewById(R.id.buttonGetVisible);
        listDevicesButton = (Button) findViewById(R.id.buttonListDevices);
        turnOffButton = (Button) findViewById(R.id.buttonTurnOff);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devicesListView = (ListView) findViewById(R.id.listView);
    }

    public void on(View v) {
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v) {
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }


    public void visible(View v) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    public void listBackup(View v) {
        // clear existing list
        this.deviceList.clear();

        pairedDevices = bluetoothAdapter.getBondedDevices();

        BluetoothDevice tatate = null;
        for(BluetoothDevice device : pairedDevices) {
            // Create a new device item
            DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), false);
            // Add it to our adapter
            deviceList.add(newDevice);
            // and now, let the show begin ...
            if ("1405T0".equals(device.getName())) {
                tatate = device;
            }
        }
        Toast.makeText(getApplicationContext(), "Discovering Bounded Devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);

        devicesListView.setAdapter(adapter);
        if (tatate != null) {
            // start association as current pin code is not correct ...
            while (tatate.getBondState() != BluetoothDevice.BOND_BONDED) {
                tatate.setPin(new byte[] {1,2,3,4});
                tatate.createBond();
            }
        }
    }

    public void list(View v) {
        if (discoveryFlag) {
            discoveryFlag = false;
            bluetoothAdapter.cancelDiscovery();
            unregisterReceiver(bluetoothReceiver);
            Toast.makeText(getApplicationContext(), "Stop discovery of Devices", Toast.LENGTH_SHORT).show();

        } else {
            discoveryFlag = true;
            // clear existing list
            this.deviceList.clear();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(bluetoothReceiver, filter);

            bluetoothAdapter.startDiscovery();

            Toast.makeText(getApplicationContext(), "Discovering Devices", Toast.LENGTH_SHORT).show();

            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);

            devicesListView.setAdapter(adapter);
        }
    }

    void internalOnDeviceFound(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        // Create a new device item
        DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), false);
        // Add it to our adapter
        deviceList.add(newDevice);

        // update displayed list
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceList);
        devicesListView.setAdapter(adapter);

        // and now, let the show begin ...
        if ("1405T0".equals(device.getName())) {
            // enable some place for a connection as discovery use a lot of resources.
            this.discoveryFlag = false;
            this.bluetoothAdapter.cancelDiscovery();
            // expected address of our device is B4:99:4C:68:F3:EF
            Toast.makeText(getApplicationContext(), "Start connection with our device.", Toast.LENGTH_SHORT).show();
            UUID uuid = UUID.randomUUID();
            // client part
            ConnectThread connectThread = new ConnectThread(device, uuid);
            connectThread.start();
            // server part
            AcceptThread acceptThread = new AcceptThread(bluetoothAdapter, uuid);
            acceptThread.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(bluetoothReceiver);
    }
}
