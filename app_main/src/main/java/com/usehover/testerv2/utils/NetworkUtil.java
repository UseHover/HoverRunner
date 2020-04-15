package com.usehover.testerv2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.usehover.testerv2.enums.PassageEnum;

public class NetworkUtil {
    private Context context;
    public NetworkUtil(Context context) {
        this.context = context;
    }
    public PassageEnum isNetworkAvailable() {
        if(context == null)  return PassageEnum.REJECT;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return PassageEnum.ACCEPT;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return PassageEnum.ACCEPT;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return PassageEnum.ACCEPT;
                    }
                }
            }

            else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return PassageEnum.ACCEPT;
                    }
                } catch (Exception e) {
                }
            }
        }
        return PassageEnum.REJECT;
    }
}
