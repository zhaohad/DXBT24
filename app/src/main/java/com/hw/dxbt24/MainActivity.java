package com.hw.dxbt24;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hw.dxbt24.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import javax.security.auth.login.LoginException;

public class MainActivity extends Activity {
    private static final String TAG = App.getTag(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.checkPermissions(this, true);

        startActivity(new Intent(this, BTControlActivity.class));
        // registerBTReceiver();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.checkPermissions(this, false)) {
            return;
        }

        BluetoothAdapter btAdapter = App.getBTAdapter();
        Log.e(TAG, "startDiscovery");
        btAdapter.startDiscovery();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionUtils.checkPermissions(this, false)) {
            finish();
        }
    }

    private void registerBTReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver, filter);
    }

    private BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive action = " + action);
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    break;
                case BluetoothDevice.ACTION_FOUND: {
                    Bundle data = intent.getExtras();
                    BluetoothDevice btDevice = (BluetoothDevice) data.get(BluetoothDevice.EXTRA_DEVICE);
                    BluetoothClass btClass = (BluetoothClass) data.get(BluetoothDevice.EXTRA_CLASS);
                    if ("hw".equals(btDevice.getName())) {
                        Log.e(TAG, "BT: " + btDevice.getName() + " MAC: " + btDevice.getAddress() + " Class: " + btClass);
                    }
                    break;
                }
            }
        }
    };
}