package com.fugao.formula.utils.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fugao.formula.R;

import java.lang.reflect.Field;

/**
 * 自定义动画的Toast
 * Created by huxq on 2017/5/21 0021.
 */

public class MyToast extends Toast {
    public MyToast(Context context) {
        super(context);
    }

    /**
     * 调用有动画的Toast
     *
     * @param context
     * @param text
     * @param duration
     * @param styleId  自定义的动画id
     * @return
     */
    public static Toast makeTextAnim(Context context, CharSequence text, int duration, int styleId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.toast_view, null);
        TextView textView = (TextView) view.findViewById(R.id.toastText);
        textView.setText(text);
        Toast toast = makeText(context, text, duration);
        toast.setView(view);
        toast.setDuration(duration);
        try {
            Object mTN = null;
            mTN = getField(toast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams != null && mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    params.windowAnimations = styleId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toast;
    }

    /**
     * 反射字段
     *
     * @param object    要反射的对象
     * @param fieldName 要反射的字段名称
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
}
