package com.fugao.breast.utils.common;

import android.app.Activity;
import com.fugao.breast.utils.dialog.BaseProgressDialog;


/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class DialogUtils {
    private static BaseProgressDialog progDialog;

    /**
     * 显示进度框
     */
    public static void showProgressDialog(Activity context, String title) {
        progDialog = new BaseProgressDialog(context);
        progDialog.setCanceledOnTouchOutside(false);
        progDialog.setLoadText(title);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    public static void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}
