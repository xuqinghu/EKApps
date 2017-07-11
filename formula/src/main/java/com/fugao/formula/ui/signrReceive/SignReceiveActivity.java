package com.fugao.formula.ui.signrReceive;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.ui.box.SelectBoxActivity;
import com.fugao.formula.ui.signSend.SignSendActivity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class SignReceiveActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.tv_warn)
    TextView tv_warn;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.tv_size)
    TextView tv_size;
    @BindView(R.id.tv_division)
    TextView tv_division;
    @BindView(R.id.tv_count)
    TextView tv_count;
    @BindView(R.id.tv_binning_person)
    TextView tv_binning_person;
    @BindView(R.id.tv_binning_time)
    TextView tv_binning_time;
    @BindView(R.id.tv_delivery_person)
    TextView tv_delivery_person;
    @BindView(R.id.tv_delivery_time)
    TextView tv_delivery_time;
    @BindView(R.id.tv_milkName)
    TextView tv_milkName;
    private String code = "";
    private SingleBtnDialog singleBtnDialog;
    private List<BoxListEntity> mList;
    private String hzId = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_receive);

    }

    @Override
    public void initView() {
        ll_back.setOnClickListener(this);
        btn_sure.setOnClickListener(this);

    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(SignReceiveActivity.this);
        mList = new ArrayList<>();
        showSureButton();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initIntent() {

    }

    private void showSureButton() {
        code = tv_code.getText().toString();
        if (StringUtils.StringIsEmpty(code)) {
            btn_sure.setVisibility(View.GONE);
            tv_warn.setVisibility(View.GONE);
        } else {
            btn_sure.setVisibility(View.VISIBLE);
            tv_warn.setVisibility(View.VISIBLE);
        }
    }

    private void clearBoxInfo() {
        tv_code.setText("");
        tv_size.setText("");
        tv_division.setText("");
        tv_count.setText("");
        tv_binning_person.setText("");
        tv_binning_time.setText("");
        tv_delivery_person.setText("");
        tv_delivery_time.setText("");
        tv_milkName.setText("奶名:");
    }

    //扫描箱子二维码
    @Override
    public void receiveBoxCode(String result) {
        super.receiveBoxCode(result);
        hzId = result;
        checkNetWork();
    }

    //检查网络情况
    private void checkNetWork() {
        if (NetWorkUtils.isNetworkAvalible(SignReceiveActivity.this)) {
            getBoxInfo();
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

    //通过箱子编号获取箱子信息
    private void getBoxInfo() {
        DialogUtils.showProgressDialog(SignReceiveActivity.this, "正在获取箱子信息...");
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.HZID = hzId;
        postBean.CurOperation = "待签收";
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(SignReceiveActivity.this, "找不到这个箱子的信息");
                        } else {
                            mList = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                            if ("装箱".equals(mList.get(0).MilkBoxStatus)) {
                                ToastUtils.showShort(SignReceiveActivity.this, "这个箱子还没有被签发");
                            } else if ("签收".equals(mList.get(0).MilkBoxStatus)) {
                                ToastUtils.showShort(SignReceiveActivity.this, "这个箱子已经被签收了");
                            } else if ("取消".equals(mList.get(0).MilkBoxStatus)) {
                                ToastUtils.showShort(SignReceiveActivity.this, "此箱贴无效，可能是取消装箱导致的");
                            } else {
                                tv_code.setText(mList.get(0).MilkBoxID);
                                tv_size.setText(mList.get(0).MilkBoxSpec);
                                tv_division.setText(mList.get(0).WardName);
                                tv_count.setText(mList.get(0).MilkQuantity);
                                tv_binning_person.setText(mList.get(0).LoadName);
                                tv_binning_time.setText(mList.get(0).LoadTime);
                                tv_delivery_person.setText(mList.get(0).TransGH);
                                tv_delivery_time.setText(mList.get(0).TransTime);
                                String milkName = "";
                                if (mList.get(0).MilkDetail.size() > 0) {
                                    for (int i = 0; i < mList.get(0).MilkDetail.size(); i++) {
                                        milkName = milkName + mList.get(0).MilkDetail.get(i).MilkName +
                                                "/" + mList.get(0).MilkDetail.get(i).Quantity + ";";

                                    }
                                    tv_milkName.setText(milkName);
                                }

                                showSureButton();
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(SignReceiveActivity.this, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(SignReceiveActivity.this, "获取失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.post(url, callback, json);
    }

    private void sureReceive() {
        DialogUtils.showProgressDialog(SignReceiveActivity.this, "正在签收...");
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.HZID = hzId;
        postBean.CurOperation = "签收";
        postBean.OperatorName = XmlDB.getInstance(SignReceiveActivity.this).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(SignReceiveActivity.this).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        clearBoxInfo();
                        showSureButton();
                        ToastUtils.showShort(SignReceiveActivity.this, "签收成功");
                    }
                } else {
                    ToastUtils.showShort(SignReceiveActivity.this, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(SignReceiveActivity.this, "签收失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.post(url, callback, json);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.btn_sure:
                sureReceive();
                break;
            default:
                break;
        }

    }
}
