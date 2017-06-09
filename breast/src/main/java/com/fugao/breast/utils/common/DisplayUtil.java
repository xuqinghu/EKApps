package com.fugao.breast.utils.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DisplayUtil {
    /**
     * 获取屏幕的宽高
     *
     * @param context
     * @return
     */
    public static Map<String, Integer> getScreenWH(Context context) {
        Map<String, Integer> resultMap = new HashMap<>();
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            resultMap.put("width", width);
            resultMap.put("height", height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
