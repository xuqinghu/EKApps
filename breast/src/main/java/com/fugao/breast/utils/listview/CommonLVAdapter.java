package com.fugao.breast.utils.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public abstract class CommonLVAdapter<T> extends BaseAdapter {
    private int mLayoutId;
    private List<T> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public CommonLVAdapter(int layoutId, Context context)
    {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mInflater = LayoutInflater.from(context);
    }

    public CommonLVAdapter(Context context, int layoutId, List<T> datas)
    {
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<T> getDatas()
    {
        return mDatas;
    }

    public void setDatas(List<T> datas)
    {
        this.mDatas = datas;
    }

    public void addLoadMoreData(List<T> datas)
    {
        if(mDatas != null && datas != null && datas.size() > 0)
        {
            mDatas.addAll(datas);
        }
    }

    public void remove(int index)
    {
        if(mDatas != null && mDatas.size() > index)
        {
            mDatas.remove(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 清理数据
     */
    public void clearDatas()
    {
        if(mDatas != null)
        {
            mDatas.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount()
    {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas==null?null:mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext,convertView,mLayoutId,parent,position);
        convert(viewHolder,mDatas.get(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);

}
