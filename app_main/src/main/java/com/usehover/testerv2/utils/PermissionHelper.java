package com.usehover.testerv2.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class PermissionHelper {
    private Context context;
    private String[] permissions;

    public PermissionHelper(Context context, String[] permissions) {
        this.context = context;
        this.permissions = permissions;
    }

    public  boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
