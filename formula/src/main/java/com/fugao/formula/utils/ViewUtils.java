package com.fugao.formula.utils;

import android.view.View;

/**
 * view 操作工具集
 * Created by Administrator on 2017/6/23 0023.
 */

public class ViewUtils {
    /**
     * View 可见
     *
     * @param v
     */
    public static void setVisible(View v) {
        if (v.getVisibility() == View.GONE || v.getVisibility() == View.INVISIBLE) {
            v.setVisibility(View.VISIBLE);
        }
    }

    /**
     * view 消失不可见
     *
     * @param v
     */
    public static void setGone(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * view 消失不可见并且占住位置
     *
     * @param v
     */
    public static void setInVisible(View v) {
        if (v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }
    }
}
