package com.example.bt_rssi_scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothRSSIHelper {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private List<String> deviceAddresses;
    private BroadcastReceiver broadcastReceiver;
    private List<Integer> newRssiValues; // Добавлен список для новых значений RSSI

    public BluetoothRSSIHelper(Context context) {
        this.context = context;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            this.bluetoothAdapter = bluetoothAdapter;
        } else {
            // Bluetooth не поддерживается или выключен
        }
    }

    public List<Integer> startScan(List<String> deviceAddresses) {
        this.deviceAddresses = deviceAddresses;
        newRssiValues = new ArrayList<>(); // Инициализация списка новых значений RSSI
        registerReceiver();
        bluetoothAdapter.startDiscovery();
        return newRssiValues; // Возвращаем список новых значений RSSI
    }

    public void stopScan() {
        context.unregisterReceiver(broadcastReceiver);
        bluetoothAdapter.cancelDiscovery();
    }

    private void registerReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            private Map<String, Integer> rssiMap = new HashMap<>();

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (deviceAddresses.contains(device.getAddress())) {
                        int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                        rssiMap.put(device.getAddress(), rssi);
                        // Добавляем новое значение RSSI в список
                        newRssiValues.add(rssi);
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(broadcastReceiver, filter);
    }
}
