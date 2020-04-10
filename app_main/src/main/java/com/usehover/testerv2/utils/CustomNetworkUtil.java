package com.usehover.testerv2.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.usehover.testerv2.enums.NetworkStatusEnum;

public class CustomNetworkUtil {
    private Context context;
    public CustomNetworkUtil(Context context) {
        this.context = context;
    }
    public NetworkStatusEnum isNetworkAvailable() {
        if(context == null)  return NetworkStatusEnum.OFFLINE;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return NetworkStatusEnum.ONLINE;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return NetworkStatusEnum.ONLINE;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return NetworkStatusEnum.ONLINE;
                    }
                }
            }

            else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return NetworkStatusEnum.ONLINE;
                    }
                } catch (Exception e) {
                }
            }
        }
        return NetworkStatusEnum.OFFLINE;
    }
}
