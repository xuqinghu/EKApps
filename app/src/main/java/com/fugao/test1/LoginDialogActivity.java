package com.fugao.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.fugao.breast.utils.StringUtils;
import com.fugao.breast.utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class LoginDialogActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_close;
    private Button btn_login;
    private EditText mPwd;
    private AutoCompleteTextView mAccount;
    private InputMethodManager imm;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Intent intent = new Intent(LoginDialogActivity.this, SettingsActivity1.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 0) {
                btn_close.setVisibility(View.VISIBLE);
                ToastUtils.showShort(LoginDialogActivity.this, "登陆失败");
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        initView();
        initData();

    }

    private void initView() {
        mAccount = (AutoCompleteTextView) findViewById(R.id.login_account);
        mPwd = (EditText) findViewById(R.id.login_password);
        btn_login = (Button) findViewById(R.id.login_btn_login);
        btn_close = (ImageButton) findViewById(R.id.login_close_button);
        btn_login.setOnClickListener(this);
        btn_close.setOnClickListener(this);

    }

    private void initData() {
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    /*
    * 验证登陆条件
    */
    public String validateLogin(String username, String password) {
        String respone = "ok";
        if (StringUtils.StringIsEmpty(username)) {
            respone = "账号不能为空";
        }
        if (StringUtils.StringIsEmpty(password)) {
            respone = "密码不能为空";
        }
        return respone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn_login:
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String account = mAccount.getText().toString();
                String pwd = mPwd.getText().toString();
                //判断输入
                if (StringUtils.StringIsEmpty(account)) {
                    ToastUtils.showShort(LoginDialogActivity.this, "用户名不能为空!");
                    return;
                }
                if (StringUtils.StringIsEmpty(pwd)) {
                    ToastUtils.showShort(LoginDialogActivity.this, "密码不能为空!");
                    return;
                }
                btn_close.setVisibility(View.GONE);
                String local_username = "0000";
                String local_password = "1";
                Message msg = new Message();
                if (local_username.equals(account) && local_password.equals(pwd)) {
                    msg.what = 1;//登陆成功
                } else {
                    msg.what = 0;//登陆失败
                }
                handler.sendMessage(msg);
                break;
            case R.id.login_close_button:
                finish();
                break;
            default:
                break;
        }

    }
}
