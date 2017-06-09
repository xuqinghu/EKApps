package com.fugao.breast.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fugao.breast.R;
import com.fugao.breast.utils.common.DisplayUtil;

/**
 * Created by HOME on 2016/9/19.
 */
public class SingleBtnDialog extends Dialog implements View.OnClickListener {

    private ImageView dialogCloseImageView;
    private TextView dialogTitleTextView;
    private TextView dialogContentTextView;
    private TextView sureTextView;
    private LinearLayout contentViewLayout;
    private RelativeLayout titleLayout;
    private Context mContext;
    private SingleBtnDialogClickListener singleBtnDialogClickListener;

    public SingleBtnDialog(Context context) {
        super(context, R.style.MyBaseDialog);
        this.mContext = context;
        initView();
        initListener();
    }

    /**
     * 初始化组件view
     */
    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.single_btn_dialog, null);
        int width = DisplayUtil.getScreenWH(mContext).get("width") - DisplayUtil.dip2px(mContext, 30);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view, layoutParams);
//        setContentView(R.layout.single_btn_dialog);
        sureTextView = (TextView) view.findViewById(R.id.sureTextView);
        titleLayout = (RelativeLayout) view.findViewById(R.id.titleLayout);
        contentViewLayout = (LinearLayout) view.findViewById(R.id.contentViewLayout);
        dialogTitleTextView = (TextView) view.findViewById(R.id.dialogTitleTextView);
        dialogCloseImageView = (ImageView) view.findViewById(R.id.dialogCloseImageView);
        dialogContentTextView = (TextView) view.findViewById(R.id.dialogContentTextView);
    }

    /**
     * 初始化组件点击事件
     */
    private void initListener() {
        sureTextView.setOnClickListener(this);
        dialogCloseImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sureTextView) {
            dismiss();
            if (singleBtnDialogClickListener != null) {
                singleBtnDialogClickListener.sureBtnClick();
            }

        } else if (i == R.id.dialogCloseImageView) {
            dismiss();

        }
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
     * 设置sure按钮文本
     *
     * @param sureText
     */
    public void setSureTextView(String sureText) {
        sureTextView.setText(sureText);
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
     * 设置标题的可见性
     *
     * @param visible
     */
    public void setTitleVisible(int visible) {
        titleLayout.setVisibility(visible);
    }

    /**
     * 设置按钮的可见性
     *
     * @param visible
     */
    public void setBtnVisible(int visible) {
        sureTextView.setVisibility(visible);
    }
    public void setDialogCloseImageView(int isVisible) {
        dialogCloseImageView.setVisibility(isVisible);
    }

    public interface SingleBtnDialogClickListener {
        void sureBtnClick();
    }

    public SingleBtnDialogClickListener getSingleBtnDialogClickListener() {
        return singleBtnDialogClickListener;
    }

    public void setSingleBtnDialogClickListener(SingleBtnDialogClickListener singleBtnDialogClickListener) {
        this.singleBtnDialogClickListener = singleBtnDialogClickListener;
    }
}
