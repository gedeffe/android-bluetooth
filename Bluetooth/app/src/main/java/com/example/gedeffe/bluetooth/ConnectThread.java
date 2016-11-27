package com.example.gedeffe.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by gcscb2 on 18/11/16.
 */

public class ConnectThread extends Thread {
    private final BluetoothSocket bluetoothSocketSocket;
    private final BluetoothDevice bluetoothDevice;


    public ConnectThread(BluetoothDevice device, UUID uuid) {
        BluetoothSocket tmp = null;
        bluetoothDevice = device;
        if(bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            bluetoothDevice.setPin(new byte[] {1,2,3,4});
            bluetoothDevice.createBond();
        }
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothSocketSocket = tmp;
    }

    public void run() {
        try {
            try {
                bluetoothSocketSocket.connect();
                manageConnectedSocket(bluetoothSocketSocket);
            } finally {
                bluetoothSocketSocket.close();
            }
        } catch (IOException connectException) {
            connectException.printStackTrace();
        }

    }

    private void manageConnectedSocket(BluetoothSocket tmp) {
        // send new date and time ...
        final SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String clockToUpdate = localSimpleDateFormat.format(new Date());
        try {
            tmp.getOutputStream().write(clockToUpdate.getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    public void cancel() {
        try {
            bluetoothSocketSocket.close();
        } catch (IOException e) {
        }
    }

}
