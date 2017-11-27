package com.fugao.formula.wifi;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public interface IConstants {
    interface WIFI_TYPE {
        String WEP = "0";
        String WPA = "1";
        String WAP2 = "2";
        String NONE = "3";
    }

    interface Wifi {
        String SSID = "Children's Hospital";
        String PASSWORD = "12345678";
        String TYPE = WIFI_TYPE.WAP2;
        boolean AUTO = true;
    }

    interface IBroadcastReceiver {
        String AUTO_CHANGE_WIFI_ENABLE = "com.fugao.formula.wifi.enable";
        String AUTO_CHANGE_WIFI_DISABLE = "com.fugao.formula.wifi.disable";
    }
}
