package com.fugao.formula.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class LogUtils {
    public static final int LOG_ERROR_TO_FILE = 2;
    protected static boolean isEnable = true;
    public static String TAG = "SNOWDREAM";
    protected static String path = "";
    public static void setPath(String path) {
        createLogDir(path);
    }
    private static void createLogDir(String path) {
        if(TextUtils.isEmpty(path)) {
            android.util.Log.e("Error", "The path is not valid.");
        } else {
            File file = new File(path);
            boolean exist = file.getParentFile().exists();
            if(!exist) {
                boolean ret = file.getParentFile().mkdirs();
                if(!ret) {
                    android.util.Log.e("Error", "The Log Dir can not be created!");
                    return;
                }

                android.util.Log.i("Success", "The Log Dir was successfully created! -" + file.getParent());
            }

        }
    }
    public static void setTag(String tag) {
        TAG = tag;
    }
    public static void setEnabled(boolean enabled) {
        isEnable = enabled;
    }
}
