package com.fugao.test1.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class ConnectionChangeReceiver extends BroadcastReceiver {
    private WifiUtils wifiUtils;
    private final static String TAG = "alvin-wifi";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "connectivity action");
            if (wifiUtils == null) {
                wifiUtils = new WifiUtils(context);
            }
            wifiUtils.checkAndConnect(AppConfiguration.WIFI_SSID);
        }

    }

}
