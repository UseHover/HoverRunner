package com.hover.runner.settings;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.hover.runner.ApplicationInstance;
import com.hover.runner.R;
import com.hover.runner.api.Apis;
import com.hover.runner.utils.Utils;


import static android.content.Context.ACTIVITY_SERVICE;

public class SettingsHelper {
    public final static String ENV = "hoverEnv";
    public final static String EMAIL = "hoverEmail";
    public final static String PWD = "encryptedPwd";
    private final static String API_KEY_LABEL = "apiKey";
    private final static String DELAY = "delay";
    private final static String TOKEN = "token";
    private final static String ORG = "org_id";

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int getCurrentEnv() { return Utils.getSavedInt(SettingsHelper.ENV, ApplicationInstance.getContext()); }
    public static void setEnv(int mode) { Utils.saveInt(SettingsHelper.ENV, mode, ApplicationInstance.getContext()); }

    public static void chooseEnv(int pos) {
        switch (pos) {
            case R.id.mode_normal: setEnv(Apis.PROD_ENV);
                break;
            case R.id.mode_debug: setEnv(Apis.DEBUG_ENV);
                break;
            case R.id.mode_noSim: setEnv(Apis.TEST_ENV);
                break;
        }
    }

    public static void saveEmail(String value, Context c) { Utils.saveString(EMAIL, value, c); }
    public static String getEmail(Context c) { return Utils.getSavedString(EMAIL, c); }

    public static String getPackage(Context c) {
        try {
            return c.getApplicationContext().getPackageName();
        } catch (NullPointerException e) {
            return "fail";
        }
    }

    public static void saveToken(String value, Context c) { Utils.saveString(TOKEN, value, c); }
    public static String getToken(Context c) { return Utils.getSavedString(TOKEN, c); }

    public static void saveOrgId(int value, Context c) { Utils.saveInt(ORG, value, c); }
    public static int getOrgId(Context c) { return Utils.getSavedInt(ORG, c); }

    public static void saveApiKey(String value, Context c) { Utils.saveString(API_KEY_LABEL, value, c); }
    public static String getApiKey(Context c) { return Utils.getSavedString(API_KEY_LABEL, c); }

    public static void setDelay(int value, Context c) { Utils.saveInt(DELAY, value, c); }
    public static int getDelay(Context c) { return Utils.getSavedInt(DELAY, c); }

    public static void clearData(Context c) {
        Utils.getSharedPrefs(ApplicationInstance.getContext()).edit().clear().apply();
        if (Build.VERSION.SDK_INT >= 19)
            ((ActivityManager) c.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
    }
}
