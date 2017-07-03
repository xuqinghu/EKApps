package com.fugao.formula.utils;

import android.content.Context;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class DisplayUtils {
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
