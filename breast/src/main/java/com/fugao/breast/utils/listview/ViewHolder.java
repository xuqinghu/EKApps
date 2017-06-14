package com.fugao.breast.utils.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class ViewHolder {
    private int mPosition;
    private int mLayoutId;
    private Context mContext;
    private ViewGroup mParent;
    private View mConvertView;
    private SparseArray<View> views;

    public int getLayoutId() {
        return mLayoutId;
    }

    public int getPosition() {
        return mPosition;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * ViewHolder构造
     *
     * @param context  上下文
     * @param layoutId 布局资源id
     * @param parent
     * @param position 索引
     */
    public ViewHolder(Context context, int layoutId, ViewGroup parent, int position) {
        this.mParent = parent;
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mPosition = position;
        this.views = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 获取ViewHolder
     *
     * @param context     上下文
     * @param convertView 复用view
     * @param layoutId    布局资源id
     * @param parent
     * @param position    索引
     * @return
     */
    public static ViewHolder getViewHolder(Context context, View convertView, int layoutId, ViewGroup parent, int position) {
        if (convertView == null) {
            return new ViewHolder(context, layoutId, parent, position);
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    /**
     * 获取view控件
     *
     * @param viewId view控件id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为文本控件赋值
     *
     * @param viewId 文本控件id
     * @param text   文本控件值
     */
    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 为文本控件设置文本颜色
     *
     * @param viewId 文本控件id
     * @param color  文本控件文本颜色
     */
    public ViewHolder setTextColor(int viewId, int color) {
        TextView textView = getView(viewId);
        textView.setTextColor(color);
        return this;
    }

    /**
     * 为文本控件设置文本颜色
     *
     * @param viewId       文本控件id
     * @param textColorRes 文本控件文本颜色资源
     */
    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    /**
     * 为文本控件设置文本字体大小
     *
     * @param viewId 文本控件id
     * @param size   文本控件文本字体大小
     */
    public ViewHolder setTextSize(int viewId, float size) {
        TextView textView = getView(viewId);
        textView.setTextSize(size);
        return this;
    }

    /**
     * 设置文本控件的超链接
     *
     * @param viewId 文本控件id
     * @return
     */
    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * 为文本控件设置文本字体的样式
     *
     * @param typeface 样式
     * @param viewIds  文本控件id集合数组
     * @return
     */
    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * 为图片控件设置图片资源
     *
     * @param viewId 图片控件id
     * @param resId  图片资源id
     * @return
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 为图片控件设置图片bitmap
     *
     * @param viewId 图片控件id
     * @param bitmap 图片bitmap
     * @return
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 为图片控件设置图片drawable
     *
     * @param viewId   图片控件id
     * @param drawable 图片drawable
     * @return
     */
    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    /**
     * 设置进度条的属性值
     *
     * @param viewId   进度条控件id
     * @param progress 进度条当前进度值
     * @param max      进度条的最大进度值
     * @return
     */
    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setMax(max);
        progressBar.setProgress(progress);
        return this;
    }

    /**
     * 设置进度条的属性值
     *
     * @param viewId 进度条控件id
     * @param max    进度条的最大进度值
     * @return
     */
    public ViewHolder setProgressMax(int viewId, int max) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setMax(max);
        return this;
    }

    /**
     * 设置进度条的属性值
     *
     * @param viewId   进度条控件id
     * @param progress 进度条当前进度值
     * @return
     */
    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar progressBar = getView(viewId);
        progressBar.setProgress(progress);
        return this;
    }

    /**
     * 设置评分控件的属性
     *
     * @param viewId 评分控件id
     * @param rating 分数
     * @return
     */
    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * 设置评分控件的属性
     *
     * @param viewId 评分控件id
     * @param rating 分数
     * @param max    最大值
     * @return
     */
    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * 为view设置tag属性
     *
     * @param viewId view控件id
     * @param tag    tag标记
     * @return
     */
    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 为view设置tag属性
     *
     * @param viewId view控件id
     * @param key    键
     * @param tag    值
     * @return
     */
    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 为可选择控件设置选中状态
     *
     * @param viewId  控件id
     * @param checked 选中状态
     * @return
     */
    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 为View控件设置背景颜色
     *
     * @param viewId View控件id
     * @param color  View控件的背景颜色
     * @return
     */
    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * 为View控件设置背景
     *
     * @param viewId        View控件id
     * @param backgroundRes View控件的背景资源
     * @return
     */
    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 设置view的透明度
     *
     * @param viewId view控件id
     * @param value  透明度值
     * @return
     */
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * 设置View控件的可见性
     *
     * @param viewId  View控件id
     * @param visible 可见值
     * @return
     */
    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * 设置view的点击事件
     *
     * @param viewId   view控件id
     * @param listener 监听器
     * @return
     */
    public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置view的触摸事件
     *
     * @param viewId   view控件id
     * @param listener 监听器
     * @return
     */
    public ViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * 设置view的长按事件
     *
     * @param viewId   view控件id
     * @param listener 监听器
     * @return
     */
    public ViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * 设置view的长按事件
     *
     * @param listener 监听器
     * @return
     */
    public ViewHolder setOnItemClickListener(View.OnClickListener listener) {
        if (mConvertView != null) {
            mConvertView.setOnClickListener(listener);
        }
        return this;
    }
}
