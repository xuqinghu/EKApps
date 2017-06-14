package com.fugao.breast.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fugao.breast.R;

/**
 * 列表对话框
 * Created by Administrator on 2017/6/14 0014.
 */

public class ListDialog<T> extends Dialog implements AdapterView.OnItemClickListener {
    private TextView titleTextView;
    private TextView promptTextView;
    private ListView listView;
    private BaseAdapter adapter;
    private RequestReturnListener<T> requestReturnListener;

    public ListDialog(Context context) {
        super(context, R.style.MyBaseDialog);
        initView();
    }

    public ListDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        setContentView(R.layout.list_dialog_layout);
        promptTextView = (TextView) findViewById(R.id.promptTextView);
        listView = (ListView) findViewById(R.id.listView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        setCanceledOnTouchOutside(true);
        setListener();
    }

    private void setListener() {
        listView.setOnItemClickListener(this);
    }

    public void setTitleTextView(String titleText) {
        titleTextView.setText(titleText);
    }

    /**
     * 设置提示语
     *
     * @param promptText
     */
    public void setPromptTextView(String promptText) {
        listView.setVisibility(View.GONE);
        promptTextView.setVisibility(View.VISIBLE);
        promptTextView.setText(promptText);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        listView.setVisibility(View.VISIBLE);
        promptTextView.setVisibility(View.GONE);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (requestReturnListener != null) {
            requestReturnListener.returnResult((T) adapter.getItem(position));
        }
        dismiss();
    }

    public void setDataReturnListener(RequestReturnListener<T> requestReturnListener) {
        this.requestReturnListener = requestReturnListener;

    }

    public interface RequestReturnListener<T> {
        void returnResult(T result);
    }
}
