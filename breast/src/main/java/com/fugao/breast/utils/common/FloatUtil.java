package com.fugao.breast.utils.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class FloatUtil {

    public static String moveZero(String s) {
        if (s == null || "".equals(s)) {
            s = "0";
        }
        String data;
        NumberFormat nf = new DecimalFormat("#.#");
        data = nf.format(Float.parseFloat(s));
        return data;
    }
}
