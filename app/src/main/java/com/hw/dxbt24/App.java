package com.hw.dxbt24;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class App extends Application {
    private static final String TAG = "DXBT24";
    public static String getTag(Class c) {
        return TAG + "-" + c.getSimpleName();
    }

    private static App sInstance;

    private BluetoothManager mBTManager;
    private BluetoothAdapter mBTAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mBTManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBTAdapter = mBTManager.getAdapter();
    }

    public static BluetoothAdapter getBTAdapter() {
        return sInstance.mBTAdapter;
    }
}
