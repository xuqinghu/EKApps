package com.fugao.breast.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.fugao.breast.R;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BaseProgressDialog extends Dialog{
    private TextView mLoadText;

    public BaseProgressDialog(Context context) {
        super(context, R.style.MyBaseDialog);
        initView();
    }

    public BaseProgressDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    /**
     * 初始化组件
     */
    private void initView() {
        setContentView(R.layout.base_progress_dialog_view);
        mLoadText = (TextView) findViewById(R.id.loadText);
    }

    public void setLoadText(String loadText) {
        mLoadText.setText(loadText);
    }
}
