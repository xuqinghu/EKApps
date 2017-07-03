package com.fugao.formula.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.ui.box.BoxingActivity;
import com.fugao.formula.ui.signSend.SignSendActivity;
import com.fugao.formula.ui.signrReceive.SignReceiveActivity;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.TwoBtnDialog;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_boxing)
    LinearLayout ll_boxing;
    @BindView(R.id.ll_sign_send)
    LinearLayout ll_sign_send;
    @BindView(R.id.ll_sign_receive)
    LinearLayout ll_sign_receive;
    private TwoBtnDialog twoBtnDialog;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        ll_boxing.setOnClickListener(this);
        ll_sign_send.setOnClickListener(this);
        ll_sign_receive.setOnClickListener(this);
    }

    @Override
    public void initData() {
        twoBtnDialog = new TwoBtnDialog(MainActivity.this);
        XmlDB.getInstance(MainActivity.this).saveKey("login", "yes");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initIntent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_boxing:
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, BoxingActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_sign_send:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, SignSendActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_sign_receive:
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this, SignReceiveActivity.class);
                startActivity(intent3);
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        twoBtnDialog.setDialogCloseImageView(View.GONE);
        twoBtnDialog.setDialogTitleTextView("温馨提示！");
        twoBtnDialog.setDialogContentTextView("是否要退出程序？");
        twoBtnDialog.setLeftTextView("否");
        twoBtnDialog.setRightTextView("是");
        twoBtnDialog.setCanceledOnTouchOutside(false);
        twoBtnDialog.setDialogBtnClickListener(new TwoBtnDialog.DialogBtnClickListener() {

                                                   @Override
                                                   public void leftBtnClick() {
                                                       twoBtnDialog.dismiss();
                                                   }

                                                   @Override
                                                   public void rightBtnClick() {
                                                       twoBtnDialog.dismiss();
                                                       try {
                                                           finish();
                                                           ActivityManager activityMgr = (ActivityManager) getSystemService(Context
                                                                   .ACTIVITY_SERVICE);
                                                           activityMgr.killBackgroundProcesses(getPackageName());
                                                           System.exit(0);
                                                       } catch (Exception e) {
                                                           e.printStackTrace();
                                                           System.exit(0);
                                                       }
                                                   }
                                               }
        );
        twoBtnDialog.show();

    }
}
