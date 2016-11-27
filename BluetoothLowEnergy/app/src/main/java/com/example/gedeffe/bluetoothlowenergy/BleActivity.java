package com.example.gedeffe.bluetoothlowenergy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

/**
 * Implements discovery of Bluetooth Low Energy devices.
 * <p>
 * https://developer.android.com/guide/topics/connectivity/bluetooth-le.html
 */
public class BleActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    // Stops scanning after 60 seconds.
    private static final long SCAN_PERIOD = 60000;

    // Initializes Bluetooth adapter.
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private Handler mHandler;

    private Switch startScanSwitch;
    private ListView devicesListView;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        this.startScanSwitch = (Switch) findViewById(R.id.startScanSwitch);
        this.startScanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*
                 As this method is called frequently (not only once), we have to use a flag to avoid
                  recursion on device handling.
                  */
                if (isChecked && mHandler == null) {
                    scanLeDevice(isChecked);
                } else {
                    scanLeDevice(isChecked);
                }
            }
        });

        this.devicesListView = (ListView) findViewById(R.id.devicesListView);
        // initialize list adapter
        this.mLeDeviceListAdapter = new LeDeviceListAdapter(this, android.R.layout.simple_list_item_1);
        this.devicesListView.setAdapter(this.mLeDeviceListAdapter);

        // retrieve bluetooth settings
        bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    /**
     * Action to enable scanning process when we click on Start Scan button.
     */
    private void scanLeDevice(final boolean enable) {
        // Device scan callback.
        final BluetoothAdapter.LeScanCallback mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi,
                                         byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                };
        if (enable) {
            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                // wait for bluetooth activation ....
                while (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    // just wait ...
                }
            }
            // Automatically stops scanning after a pre-defined scan period.
            this.mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    startScanSwitch.setChecked(false);
                    mHandler = null;
                }
            }, SCAN_PERIOD);

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            if (mBluetoothAdapter != null) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
            mHandler = null;
        }
    }

    /**
     * React on bluetooth activation (synchronous wait, so our internal loop should stop).
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                mBluetoothAdapter = bluetoothManager.getAdapter();
                break;
            }
        }
    }
}
