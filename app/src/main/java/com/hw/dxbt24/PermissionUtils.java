package com.hw.dxbt24;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

public class PermissionUtils {
    private static final String[] PERMS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
    };

    public static boolean checkPermissions(Activity activity, boolean forceRequest) {
        boolean allGranted = true;
        for (String perm : PERMS) {
            if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted && forceRequest) {
            activity.requestPermissions(PERMS, 0);
        }

        return allGranted;
    }
}
