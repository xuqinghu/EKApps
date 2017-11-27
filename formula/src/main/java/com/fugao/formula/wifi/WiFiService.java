package com.fugao.formula.wifi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fugao.formula.utils.StringUtils;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class WiFiService extends Service {
    private final static String TAG = "alvin-wifi";
    private ConnectionChangeReceiver connectionChangeReceiver;
    private IntentFilter mFilter;
    private ConnectEnableReceiver connectEnableReceiver;

    @Override
    public void onCreate() {
        Log.d(TAG, "wifi server onCreate");
        super.onCreate();
        //wifi切换管理广播
        connectionChangeReceiver = new ConnectionChangeReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        if (AppConfiguration.WIFI_AUTO) {
            registerReceiver(connectionChangeReceiver, mFilter);
        }
        //管理wifi切换管理广播
        connectEnableReceiver = new ConnectEnableReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_DISABLE);
        filter.addAction(IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_ENABLE);
        registerReceiver(connectEnableReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "wifi server onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "wifi server onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (connectionChangeReceiver != null) {
                unregisterReceiver(connectionChangeReceiver);
            }

            if (connectEnableReceiver != null) {
                unregisterReceiver(connectEnableReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "wifi server destroy");
    }

    /**
     * wifi
     */
    class ConnectEnableReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.equals(action, IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_ENABLE)) {
                if (connectionChangeReceiver != null) {
                    registerReceiver(connectionChangeReceiver, mFilter);
                }
            } else if (StringUtils.equals(action, IConstants.IBroadcastReceiver.AUTO_CHANGE_WIFI_DISABLE)) {
                if (connectionChangeReceiver != null) {
                    unregisterReceiver(connectionChangeReceiver);
                }
            }
        }
    }
}
