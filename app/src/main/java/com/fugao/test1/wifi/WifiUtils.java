package com.fugao.test1.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.fugao.breast.utils.StringUtils;
import com.fugao.breast.utils.ToastUtils;

import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class WifiUtils {
    private final static String TAG = "alvin-wifi";
    private Context mContext;
    private WifiManager mWifiManager;//管理wifi
    private ConnectivityManager mConnectivityManger;
    private Boolean mWifiChangingFlag = false;

    public WifiUtils(Context context) {
        this.mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        mConnectivityManger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 检查wifi状态，并自动打开wifi,尝试连接指定wifi
     */
    public void checkAndConnect(String SSID) {
        if (mWifiManager.isWifiEnabled()) {
            Log.d(TAG, "wifi is open");
            NetworkInfo netInfo = mConnectivityManger.getActiveNetworkInfo();
            if (netInfo != null && (netInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    && StringUtils.equals(netInfo.getExtraInfo().replace("\"", ""), SSID)) {
                if (mWifiChangingFlag) {
                    Log.d(TAG, "Connect Success");
                    ToastUtils.showShort(mContext, SSID + " 连接正常");
                    mWifiChangingFlag = false;
                }
            } else {
                mWifiChangingFlag = true;
                ToastUtils.showShort(mContext, "切换网络 " + SSID + " 中");
                Log.d(TAG, "just Connect");
                connectWIFi();
            }
        } else {
            Log.d(TAG, "wifi closed ,open wifi");
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 切换wifi
     */
    private void connectWIFi() {
        WifiConfiguration config = IsConfig(AppConfiguration.WIFI_SSID);
        //连接已连接过的wifi，儿科医院不提供wifi密码，只能由院方手动输入。
        if (config != null) {
            mWifiManager.enableNetwork(config.networkId, true);
            Log.d(TAG, "enable wifi");
        } else {
            Log.d(TAG, "no wifi configuration");
            ToastUtils.showShort(mContext, AppConfiguration.WIFI_SSID + " 连接失败，请检查网络或密码");
        }
    }

    /**
     * wifi 是否连接过
     *
     * @param SSID
     * @return
     */
    public WifiConfiguration IsConfig(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null && existingConfigs.size() > 0) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }
}
