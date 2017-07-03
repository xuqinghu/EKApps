package com.fugao.formula.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class SDCardUtils {
    public SDCardUtils() {
    }

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static boolean hasSdk() {
        boolean flag = false;
        if (Environment.getExternalStorageState().equals("mounted")) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

}
