package com.fugao.breast.utils;

import android.content.Context;
import android.widget.Toast;

import com.fugao.breast.R;
import com.fugao.breast.utils.dialog.MyToast;

/**
 * Created by huxq on 2017/5/21 0021.
 * Toast显示工具类
 */

public class ToastUtils {
    private static Toast instance;//Toast实例

    /**
     * ShortToast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, message, Toast.LENGTH_SHORT, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * ShortToast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, context.getResources().getString(message), Toast.LENGTH_SHORT, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * 显示吐司内容
     * @param content 要显示的内容
     */
    public static void showPrompt(Context context,String content)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, content, 0, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * LongToast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, message, Toast.LENGTH_LONG, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * LongToast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, context.getResources().getString(message), Toast.LENGTH_LONG, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * 自定义显示时间Toast
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, message, duration, R.style.Animation_Toast);
        instance.show();
    }

    /**
     * 自定义显示时间Toast
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration)
    {
        if(instance != null)
        {
            instance.cancel();
            instance = null;
        }
        instance = MyToast.makeTextAnim(context, context.getResources().getString(message), duration, R.style.Animation_Toast);
        instance.show();
    }

    /** Hide the toast, if any. */
    public static void hideToast()
    {
        if (null != instance)
        {
            instance.cancel();
        }
    }

}
