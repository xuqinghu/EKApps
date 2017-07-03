package com.fugao.formula.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fugao.formula.R;

/**
 * 带确认、取消按钮的dialog
 * Created by huxq on 2017/5/24 0024.
 */

public class TwoBtnDialog extends Dialog implements View.OnClickListener {
    private ImageView dialogCloseImageView;
    private TextView dialogTitleTextView;
    private TextView dialogContentTextView;
    private LinearLayout contentViewLayout;
    private TextView leftTextView;
    private TextView rightTextView;
    private DialogBtnClickListener dialogBtnClickListener;

    public TwoBtnDialog(Context context) {
        super(context, R.style.MyBaseDialog);
        initView();
        initListener();
    }

    public TwoBtnDialog(Context context, int theme) {
        super(context, theme);
        initView();
        initListener();
    }

    /**
     * 初始化组件view
     */
    private void initView() {
        setContentView(R.layout.two_btn_dialog);
        leftTextView = (TextView) findViewById(R.id.leftTextView);
        rightTextView = (TextView) findViewById(R.id.rightTextView);
        contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
        dialogTitleTextView = (TextView) findViewById(R.id.dialogTitleTextView);
        dialogCloseImageView = (ImageView) findViewById(R.id.dialogCloseImageView);
        dialogContentTextView = (TextView) findViewById(R.id.dialogContentTextView);
    }

    /**
     * 初始化组件点击事件
     */
    private void initListener() {
        leftTextView.setOnClickListener(this);
        rightTextView.setOnClickListener(this);
        dialogCloseImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.leftTextView) {
            if (dialogBtnClickListener != null) {
                dismiss();
                dialogBtnClickListener.leftBtnClick();
            }

        } else if (i == R.id.rightTextView) {
            if (dialogBtnClickListener != null) {
                dismiss();
                dialogBtnClickListener.rightBtnClick();
            }

        } else if (i == R.id.dialogCloseImageView) {
            if (dialogBtnClickListener != null) {
                dismiss();
                dialogBtnClickListener.leftBtnClick();
            }

        }
    }

    /**
     * 填充自定义view
     *
     * @param view
     */
    public void setContentViewLayout(View view) {
        dialogContentTextView.setVisibility(View.GONE);
        contentViewLayout.setVisibility(View.VISIBLE);
        contentViewLayout.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 设置填充内容
     *
     * @param contentText 填充内容
     */
    public void setDialogContentTextView(String contentText) {
        dialogContentTextView.setText(contentText);
    }

    /**
     * 设置标题内容
     *
     * @param titleText
     */
    public void setDialogTitleTextView(String titleText) {
        dialogTitleTextView.setText(titleText);
    }

    /**
     * 设置left按钮文本
     *
     * @param leftText
     */
    public void setLeftTextView(String leftText) {
        leftTextView.setText(leftText);
    }

    /**
     * 设置right按钮文本
     *
     * @param rightText
     */
    public void setRightTextView(String rightText) {
        rightTextView.setText(rightText);
    }

    /**
     * 关闭按钮是否显示
     *
     * @param isVisible
     */
    public void setDialogCloseImageView(int isVisible) {
        dialogCloseImageView.setVisibility(isVisible);
    }

    public interface DialogBtnClickListener {
        void leftBtnClick();

        void rightBtnClick();
    }

    public DialogBtnClickListener getDialogBtnClickListener() {
        return dialogBtnClickListener;
    }

    public void setDialogBtnClickListener(DialogBtnClickListener dialogBtnClickListener) {
        this.dialogBtnClickListener = dialogBtnClickListener;
    }
}
