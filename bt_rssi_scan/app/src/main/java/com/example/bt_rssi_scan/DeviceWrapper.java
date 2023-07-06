package com.example.bt_rssi_scan;

import android.bluetooth.BluetoothDevice;

public class DeviceWrapper {
    private String deviceAddress;
    private int rssi;

    public DeviceWrapper(String deviceAddress, int rssi) {
        this.deviceAddress = deviceAddress;
        this.rssi = rssi;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}