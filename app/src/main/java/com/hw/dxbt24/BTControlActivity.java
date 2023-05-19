package com.hw.dxbt24;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BTControlActivity extends Activity {
    private static final String TAG = App.getTag(BTControlActivity.class);

    private BluetoothGatt mBTGatt;
    private BluetoothDevice mBTDevice;

    private UUID UUID_S1 = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S1_C1 = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb"); // BT_NAME hw
    private UUID UUID_S1_C2 = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S2 = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S2_C1 = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S2_C1_D1 = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S3 = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    private UUID UUID_S3_C1 = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb"); // DX-smart
    private UUID UUID_S3_C2 = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb"); // DX-BT24
    private UUID UUID_S3_C3 = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb"); // +VERSION=V2.1.0
    private UUID UUID_S3_C4 = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb"); // +VERSION=V2.1.0
    private UUID UUID_S3_C5 = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb"); // DX-smart-BT24
    private UUID UUID_S3_C6 = UUID.fromString("00002a50-0000-1000-8000-00805f9b34fb"); // DX-smart-BT24
    private UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private UUID UUID_NOTIFY = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private UUID UUID_NOTIFY_D1 = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private UUID UUID_NOTIFY_D2 = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb"); // TX & RX
    private UUID UUID_WRITE = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    private UUID UUID_WRITE_D1 = UUID.fromString("00002901-0000-1000-8000-00805f9b34fb"); // TX

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mac = "48:87:2D:9B:13:DC";
        mBTDevice = App.getBTAdapter().getRemoteDevice(mac);
        mBTGatt = mBTDevice.connectGatt(this, true, mBTGattCallback);
        boolean connected = mBTGatt.connect();
        Log.e(TAG, "connected = " + connected);
    }

    private BluetoothGattCallback mBTGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, "onConnectionStateChange status = " + status + " newState = " + newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e(TAG, "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.e(TAG, "STATE_CONNECTING");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    Log.e(TAG, "STATE_DISCONNECTING");
                    break;
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "discover services failed, status = " + status);
                return;
            }
            Log.e(TAG, "onServiceDiscovered status = " + status);
            List<BluetoothGattService> services = gatt.getServices();
            for (BluetoothGattService service : services) {
                Log.e(TAG, "onServicesDiscovered service = " + service.getUuid());
                List<BluetoothGattCharacteristic> gattChars = service.getCharacteristics();
                for (BluetoothGattCharacteristic gattChar : gattChars) {
                    int property = gattChar.getProperties();
                    Log.e(TAG, "gattChar uuid = " + gattChar.getUuid() + " property = " + property + " getPermissions = " + gattChar.getPermissions() + " getWriteType = " + gattChar.getWriteType());
                    /*if ((property & BluetoothGattCharacteristic.PROPERTY_WRITE) == BluetoothGattCharacteristic.PROPERTY_WRITE) {
                        Log.e(TAG, "writeable");
                        byte[] value = new byte[20];
                        value[0] = (byte) 0x00;
                        byte[] data = gattChar.getUuid().toString().getBytes();
                        gattChar.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                        gattChar.setValue(data);
                    }
                    if ((property & BluetoothGattCharacteristic.PROPERTY_NOTIFY) == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                        Log.e(TAG, "PROPERY_NOTIFY");
                        byte[] value = new byte[20];
                        value[0] = (byte) 0x00;
                        byte[] data = "Notify".getBytes();
                        gattChar.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                        gattChar.setValue(data);
                        boolean w = gatt.setCharacteristicNotification(gattChar, true);
                        Log.e(TAG, "w = " + w);
                    }*/
                    List<BluetoothGattDescriptor> gattDescs = gattChar.getDescriptors();
                    for (BluetoothGattDescriptor gattDesc : gattDescs) {
                        Log.e(TAG, "descriptor uuid: " + gattDesc.getUuid() + " getPermissions: " + gattDesc.getPermissions());
                    }
                    /*boolean a = gatt.readCharacteristic(gattChar);
                    Log.e(TAG, "read a = " + a);*/
                }
            }
            new Thread(mCheck).start();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicRead 1");
            UUID uuid = characteristic.getUuid();
            byte[] data = characteristic.getValue();
            if (data != null) {
                String s = "";
                for (byte b : data) {
                    s += Integer.toHexString(b) + " ";
                }
                Log.e(TAG, "data.length = " + data.length + " s: " + s);
                Log.e(TAG, "uuid = " + uuid.toString() + " read data: " + new String(data));
            } else {
                Log.e(TAG, "read null");
            }
        }

        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
            super.onCharacteristicRead(gatt, characteristic, value, status);
            Log.e(TAG, "onCharacteristicRead 2");
            UUID uuid = characteristic.getUuid();
            byte[] data = characteristic.getValue();
            if (data != null) {
                String s = "";
                for (byte b : data) {
                    s += Integer.toHexString(b) + " ";
                }
                Log.e(TAG, "data.length = " + data.length + " s: " + s);
                Log.e(TAG, "uuid = " + uuid.toString() + " read data: " + new String(data));
            } else {
                Log.e(TAG, "read null");
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.e(TAG, "onCharacteristicWrite");
            byte[] data = characteristic.getValue();
            if (data != null) {
                Log.e(TAG, "onCharacteristicWrite data: " + new String(data));
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] data = characteristic.getValue();
            Log.e(TAG, "onCharacteristicChanged 1 " + new String(data));
        }

        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic, value);
            Log.e(TAG, "onCharacteristicChanged 2");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorRead 1");
            UUID uuid = descriptor.getUuid();
            byte[] data = descriptor.getValue();
            if (data != null) {
                String s = "";
                for (byte b : data) {
                    s += Integer.toHexString(b) + " ";
                }
                Log.e(TAG, "data.length = " + data.length + " s: " + s);
                Log.e(TAG, "uuid = " + uuid.toString() + " read data: " + new String(data));
            } else {
                Log.e(TAG, "read null");
            }
        }

        @Override
        public void onDescriptorRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattDescriptor descriptor, int status, @NonNull byte[] value) {
            super.onDescriptorRead(gatt, descriptor, status, value);
            UUID uuid = descriptor.getUuid();
            byte[] data = descriptor.getValue();
            if (data != null) {
                Log.e(TAG, "uuid = " + uuid.toString() + " read data: " + new String(data));
            } else {
                Log.e(TAG, "read null");
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorWrite 1");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.e(TAG, "onReliableWriteCompleted 2");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.e(TAG, "onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.e(TAG, "onMtuChanged");
        }

        @Override
        public void onServiceChanged(@NonNull BluetoothGatt gatt) {
            super.onServiceChanged(gatt);
            Log.e(TAG, "onServiceChanged");
        }
    };

    private Runnable mCheck = new Runnable() {
        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            Log.e(TAG, "Check runnable start");
            List<BluetoothGattService> services = mBTGatt.getServices();
            for (BluetoothGattService s : services) {
                List<BluetoothGattCharacteristic> characteristics = s.getCharacteristics();
                for (BluetoothGattCharacteristic c : characteristics) {
                    UUID uuid = c.getUuid();
                    boolean suc = mBTGatt.readCharacteristic(c);
                    Log.e(TAG, "read characteristic: " + uuid + " suc = " + suc);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "", e);
                    }
                    List<BluetoothGattDescriptor> descriptors = c.getDescriptors();
                    for (BluetoothGattDescriptor d : descriptors) {
                        uuid = d.getUuid();
                        suc = mBTGatt.readDescriptor(d);
                        Log.e(TAG, "read descriptor: " + uuid + " suc = " + suc);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "", e);
                        }
                    }
                }
            }

            BluetoothGattService service = mBTGatt.getService(UUID_SERVICE);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID_NOTIFY);

            // characteristic.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            characteristic.setValue("Send Test");
            mBTGatt.writeCharacteristic(characteristic);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Log.e(TAG, "", e);
            }



            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_NOTIFY_D1);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBTGatt.writeDescriptor(descriptor);
            boolean suc = mBTGatt.setCharacteristicNotification(characteristic, true);
            Log.e(TAG, "suc = " + suc);
        }
    };
}
