package com.fugao.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.breast.ui.BreastActivity;
import com.fugao.breast.utils.StringUtils;
import com.fugao.breast.utils.ToastUtils;
import com.fugao.breast.utils.common.DialogUtils;
import com.fugao.breast.utils.common.FastJsonUtils;
import com.fugao.breast.utils.common.OkHttpUtils;
import com.fugao.breast.utils.dialog.ListDialog;
import com.fugao.test1.wifi.WiFiService;

import java.util.List;


/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username, et_password;
    private TextView tv_setting, tv_login;
    private String port;
    private String ip;
    private String userName, userPws;
    private WardAdapter adapter;
    private ListDialog listDialog;
    private String name;
    private String gh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //wifi 管理service
        startService(new Intent(this, WiFiService.class));
        initView();
        initData();
        initSetting();
        initListener();
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_login.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        et_username.setText(XmlDB.getInstance(LoginActivity.this).getKeyStringValue("name", ""));
    }

    private void initData() {
        listDialog = new ListDialog(LoginActivity.this);
        adapter = new WardAdapter(R.layout.ward_list_item, LoginActivity.this);
    }

    private void initListener() {

    }

    private void initSetting() {
        ip = XmlDB.getInstance(LoginActivity.this).getKeyString("ip", "");
        port = XmlDB.getInstance(LoginActivity.this).getKeyString("port", "");
    }

    private void Login() {
        userName = et_username.getText().toString();
        userPws = et_password.getText().toString();
        String validateResult = validateLogin(userName, userPws);
        String url = "http://" + ip + ":" + port + "/api/User";
        if ("ok".equals(validateResult)) {
            DialogUtils.showProgressDialog(LoginActivity.this, "正在登录中...");
            NurseBean bean = new NurseBean();
            bean.GH = userName;
            bean.PWD = userPws;
            String json = JSON.toJSONString(bean);
            OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response, int code) {
                    DialogUtils.dissmissProgressDialog();
                    if (code == 200) {
                        if (response != null) {
                            NurseBean nurseBean = FastJsonUtils.getBean(response, NurseBean.class);
                            if (nurseBean != null && "登录成功".equals(nurseBean.Mark)) {
                                name = nurseBean.XM;
                                gh = nurseBean.GH;
                                if (nurseBean.BQInfo != null && nurseBean.BQInfo.size() > 1) {
                                    selectWard(nurseBean.BQInfo);
                                } else if (nurseBean.BQInfo != null) {
                                    goToBreastActivity(nurseBean.BQInfo.get(0).BQDM, nurseBean.BQInfo.get(0).BQMC);
                                }
                                XmlDB.getInstance(LoginActivity.this).saveKey("name", nurseBean.GH);
                                ToastUtils.showShort(LoginActivity.this, nurseBean.Mark);
                            } else {
                                ToastUtils.showShort(LoginActivity.this, nurseBean.Mark);
                            }
                        }
                    } else {
                        ToastUtils.showShort(LoginActivity.this, "服务器异常");
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    DialogUtils.dissmissProgressDialog();
                    ToastUtils.showShort(LoginActivity.this, "登录失败");
                }
            };
            OkHttpUtils.post(url, callback, json);
        } else {
            ToastUtils.showShort(LoginActivity.this, validateResult);
        }
    }

    private void goToBreastActivity(String wardId, String wardName) {
        Intent intent1 = new Intent();
        intent1.putExtra("nurseCode", gh);
        intent1.putExtra("nurseName", name);
        intent1.putExtra("deptId", "1111");
        intent1.putExtra("wardId", wardId);
        intent1.putExtra("wardName", wardName);
        intent1.putExtra("ip", ip);
        intent1.putExtra("port", port);
        intent1.setClass(LoginActivity.this, BreastActivity.class);
        startActivity(intent1);
        LoginActivity.this.finish();
    }

    //选择所属病区
    private void selectWard(List<WardBean> beens) {
        adapter.setDatas(beens);
        listDialog.setAdapter(adapter);
        listDialog.setTitleTextView("管辖病区");
        listDialog.setDataReturnListener(new ListDialog.RequestReturnListener<WardBean>() {
            @Override
            public void returnResult(WardBean result) {
                goToBreastActivity(result.BQDM, result.BQMC);
            }
        });
        listDialog.show();
    }

    /*
    * 验证登陆条件
    */
    public String validateLogin(String username, String password) {

        String respone = "ok";
        if (StringUtils.StringIsEmpty(username)) {
            respone = "账号不能为空";
        } else if (StringUtils.StringIsEmpty(password)) {
            respone = "密码不能为空";
        }
        return respone;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSetting();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                Login();
                break;
            case R.id.tv_setting:
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, LoginDialogActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
