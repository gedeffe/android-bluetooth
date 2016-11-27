package com.example.gedeffe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by gcscb2 on 20/11/16.
 */
public class AcceptThread extends Thread {
    private BluetoothServerSocket bluetoothServerSocketerverSocket;
    private BluetoothAdapter bluetoothAdapter;

    public AcceptThread(BluetoothAdapter bluetoothAdapterParam, UUID uuid) {
        this.bluetoothAdapter = bluetoothAdapterParam;

        try {
            bluetoothServerSocketerverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Custom_service_name", uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int listeningIndex = 0;
        while (listeningIndex < 100) {
            try {
                BluetoothSocket socket = bluetoothServerSocketerverSocket.accept();
                if (socket != null) {
                    manageConnectedSocket(socket);
                    bluetoothServerSocketerverSocket.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            // exit listening mode.
            listeningIndex++;
        }
        // and stop listening
        this.cancel();
    }

    private void manageConnectedSocket(BluetoothSocket bluetoothSocket) throws IOException {
        byte[] buffer = new byte[4];
        ByteArrayInputStream input = new ByteArrayInputStream(buffer);
        InputStream inputStream = bluetoothSocket.getInputStream();
        System.err.println(inputStream.read(buffer));
    }

    public void cancel() {
        try {
            bluetoothServerSocketerverSocket.close();
        } catch (IOException e) {
        }
    }
}
