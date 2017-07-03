package com.fugao.formula.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.fugao.formula.R;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/25 0025.
 */

public class PopMenu implements AdapterView.OnItemClickListener {
    public interface OnItemClickListener {
        public void onItemClick(int index);
    }

    private ArrayList<String> itemList;
    private Context context;
    protected PopupWindow popupWindow;
    private ListView listView;
    private OnItemClickListener listener;
    private LayoutInflater inflater;

    public PopMenu(Context context) {
        this.context = context;

        itemList = new ArrayList<String>();

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popmenu, null);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PopAdapter());
        listView.setOnItemClickListener(this);

        popupWindow = new PopupWindow(view, context.getResources()
                .getDimensionPixelSize(R.dimen.popmenu_width), // 这里宽度需要自己指定，使用
                // WRAP_CONTENT
                // 会很大
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (listener != null) {
            listener.onItemClick(position);
        }
        dismiss();
    }

    // 设置菜单项点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // 批量添加菜单项
    public void addItems(String[] items) {
        for (String s : items)
            itemList.add(s);
    }
    public PopupWindow getPopupWindow(){
        return popupWindow;
    }
    public void addItems(ArrayList<String> itemList) {
        this.itemList = itemList;
    }

    // 单个添加菜单项
    public void addItem(String item) {
        itemList.add(item);
    }

    // 下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
        popupWindow.showAsDropDown(parent,
                10,
                // 保证尺寸是根据屏幕像素密度来的
                context.getResources().getDimensionPixelSize(
                        R.dimen.popmenu_yoff));

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    public void showAsDropDown(View parent, int xoff, int yoff) {
        popupWindow.showAsDropDown(parent, xoff, yoff);
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    // 隐藏菜单
    public void dismiss() {
        popupWindow.dismiss();
    }

    // 适配器
    private final class PopAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.pomenu_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.groupItem = (TextView) convertView
                        .findViewById(R.id.textView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.groupItem.setText(itemList.get(position));

            return convertView;
        }

        private final class ViewHolder {
            TextView groupItem;
        }
    }

}
