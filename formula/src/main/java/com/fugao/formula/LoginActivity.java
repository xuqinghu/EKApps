package com.fugao.formula;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.NurseBean;
import com.fugao.formula.ui.MainActivity;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;
import com.fugao.formula.wifi.WiFiService;
import butterknife.BindView;

/**
 * Created by huxq on 2017/6/6 0006.
 */

public class LoginActivity extends BaseActivity{
    @BindView(R.id.et_username)
    EditText et_username;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    private String port;
    private String ip;
    private String userName, userPws;
    private String currentHostName = "";
    public static Handler loginToMainActivity;
    private FormulaApplication application;
    private String divisionCode;
    private SingleBtnDialog singleBtnDialog;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
        //wifi 管理service
        startService(new Intent(this, WiFiService.class));
        loginToMainActivity = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }

        };
    }

    @Override
    public void initView() {
        et_username.setText(XmlDB.getInstance(LoginActivity.this).getKeyStringValue("userCode", ""));
        divisionCode = XmlDB.getInstance(LoginActivity.this).getKeyString("divisionCode", "");
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(LoginActivity.this);
        application = (FormulaApplication) getApplication();
        if (BuildConfig.DEBUG) {
            et_username.setText("0000");
            et_password.setText("fg19831993");
        }

    }

    private void initSetting() {
        ip = XmlDB.getInstance(LoginActivity.this).getKeyString("ip", "");
        port = XmlDB.getInstance(LoginActivity.this).getKeyString("port", "");
    }

    @Override
    public void initListener() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetWork();
            }
        });
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, LoginDialogActivity.class);
                startActivity(intent);

            }
        });
    }


    //检查网络情况
    private void checkNetWork() {
        if (NetWorkUtils.isNetworkAvalible(LoginActivity.this)) {
            Login();
        } else {
            showWarnDialog("没有可用的网络");
        }
    }

    private void showWarnDialog(String title) {
        singleBtnDialog.setDialogTitleTextView("温馨提示！");
        singleBtnDialog.setDialogCloseImageView(View.GONE);
        singleBtnDialog.setDialogContentTextView(title);
        singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
            @Override
            public void sureBtnClick() {

            }
        });
        singleBtnDialog.show();
    }

    private void Login() {
        userName = et_username.getText().toString();
        userPws = et_password.getText().toString();
        if ("".equals(ip)) {
            ToastUtils.showShort(LoginActivity.this, "ip地址不能为空！");
            return;
        }
        if ("".equals(port)) {
            ToastUtils.showShort(LoginActivity.this, "端口号不能为空！");
            return;
        }
        currentHostName = ip + ":" + port;
        String validateResult = validateLogin(userName, userPws);
        if ("ok".equals(validateResult)) {
            OkHttpUtils.BASE_URL = "http://" + currentHostName.trim() + "/";
            String url = FormulaApi.LOGIN_URL;
            NurseBean bean = new NurseBean();
            bean.GH = userName;
            bean.PWD = userPws;
            String json = JSON.toJSONString(bean);
            DialogUtils.showProgressDialog(LoginActivity.this, "正在登录中...");
            //重新登录后清空选择的时间点
            XmlDB.getInstance(LoginActivity.this).saveKey("time", "");
            OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response, int code) {
                    DialogUtils.dissmissProgressDialog();
                    if (code == 200) {
                        if (response != null) {
                            NurseBean nurseBean = FastJsonUtils.getBean(response, NurseBean.class);
                            if (nurseBean != null && "登录成功".equals(nurseBean.Mark)) {
                                if (nurseBean.BQInfo != null && nurseBean.BQInfo.size() > 0) {
                                    application.setDivisionList(nurseBean.BQInfo);
                                    if (StringUtils.StringIsEmpty(divisionCode)) {
                                        XmlDB.getInstance(LoginActivity.this).saveKey("divisionCode", nurseBean.BQInfo.get(0).BQDM);
                                        XmlDB.getInstance(LoginActivity.this).saveKey("divisionName", nurseBean.BQInfo.get(0).BQMC);
                                    }
                                    XmlDB.getInstance(LoginActivity.this).saveKey("userName", nurseBean.XM);
                                    XmlDB.getInstance(LoginActivity.this).saveKey("userCode", nurseBean.GH);
                                    XmlDB.getInstance(LoginActivity.this).saveKey("deptName", nurseBean.DepName);
                                    //重新登录后清空选择的时间点
                                    XmlDB.getInstance(LoginActivity.this).saveKey("time", "");
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, SyncActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtils.showShort(LoginActivity.this, "您没有管辖的病区");
                                }
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

    @Override
    public void initIntent() {

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

    /**
     * 服务器发现新版本
     */
    private void newVersion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("提示！").setMessage("检测到有新版本").setNegativeButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                UpdateDialgoFragment.newInstance(OkHttpUtils.BASE_URL + nurseAccountBean.AppInfo.Path,
//                        FileHelper.appSDPath + "/" + nurseAccountBean.AppInfo.Name).show(getFragmentManager(),
//                        "updateApp");
            }
        }).create().show();
    }
}
