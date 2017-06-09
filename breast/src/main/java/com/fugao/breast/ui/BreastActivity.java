package com.fugao.breast.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.constant.BreastApi;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.db.dao.BreastDetailDao;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.ui.put.PutList;
import com.fugao.breast.ui.thaw.ThawList;
import com.fugao.breast.utils.ToastUtils;
import com.fugao.breast.utils.common.DialogUtils;
import com.fugao.breast.utils.common.OkHttpUtils;
import com.fugao.breast.utils.common.XmlDB;
import com.fugao.breast.utils.dialog.SingleBtnDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class BreastActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_back, ll_updata, put, thaw;
    private TextView tv_updata_count;
    private DataBaseInfo dataBaseInfo;
    private BreastDetailDao breastDetailDao;
    private List<BreastMilkDetial> uploadData;
    //判断是否有数据未上传  0是没有 1是有
    private int hasData = 0;
    private SingleBtnDialog singleBtnDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    hasUpData();
                    break;
            }
        }
    };

    @Override
    public void setContentView() {
        setContentView(R.layout.main);
    }

    @Override
    public void initView() {
        put = (LinearLayout) findViewById(R.id.ll_put);
        thaw = (LinearLayout) findViewById(R.id.ll_thaw);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_updata = (LinearLayout) findViewById(R.id.ll_updata);
        tv_updata_count = (TextView) findViewById(R.id.tv_updata_count);
        put.setOnClickListener(this);
        thaw.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_updata.setOnClickListener(this);

    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(BreastActivity.this);
        dataBaseInfo = DataBaseInfo.getInstance(BreastActivity.this);
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        uploadData = new ArrayList<>();
        hasUpData();

    }

    private void showDialog() {
        singleBtnDialog.setDialogCloseImageView(View.GONE);
        singleBtnDialog.setDialogTitleTextView("温馨提示！");
        singleBtnDialog.setDialogContentTextView("您有数据没有上传，请先上传数据");
        singleBtnDialog.setSureTextView("上传");
        singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
            @Override
            public void sureBtnClick() {
                upData(uploadData);
            }
        });
        singleBtnDialog.show();
    }

    //上传数据
    private void upData(List<BreastMilkDetial> data) {
        String json = JSON.toJSONString(data);
        String url = BreastApi.IP + BreastApi.BREAST;
        DialogUtils.showProgressDialog(BreastActivity.this, "正在上传数据...");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    Message msg = new Message();
                    msg.arg1 = 1;
                    handler.sendMessage(msg);
                    breastDetailDao.deleteAllInfo();
                    ToastUtils.showShort(BreastActivity.this, "上传成功");
                }
            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(BreastActivity.this, "上传失败");
            }
        };
        OkHttpUtils.post(url, callback, json);
    }

    private void hasUpData() {
        uploadData = breastDetailDao.getBreastMilkDetialByUpload("0");
        if (uploadData == null || uploadData.size() == 0) {
            ll_updata.setVisibility(View.GONE);
            hasData = 0;
        } else {
            ll_updata.setVisibility(View.VISIBLE);
            tv_updata_count.setText("您有" + uploadData.size() + "条数据需要上传");
            hasData = 1;
            upData(uploadData);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        hasUpData();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        String nurseCode = intent.getStringExtra("nurseCode");
        String nurseName = intent.getStringExtra("nurseName");
        String deptId = intent.getStringExtra("deptId");
        String wardId = intent.getStringExtra("wardId");
        String wardName = intent.getStringExtra("wardName");
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        BreastApi.IP = "http://" + ip + ":" + port + "/";
        XmlDB.getInstance(BreastActivity.this).saveKey("nCode", nurseCode);
        XmlDB.getInstance(BreastActivity.this).saveKey("nName", nurseName);
        XmlDB.getInstance(BreastActivity.this).saveKey("deptId", deptId);
        XmlDB.getInstance(BreastActivity.this).saveKey("wardId", wardId);
        XmlDB.getInstance(BreastActivity.this).saveKey("wardName", wardName);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_put) {
            if (hasData == 0) {
                Intent intent1 = new Intent();
                intent1.setClass(BreastActivity.this, PutList.class);
                startActivity(intent1);
            } else {
                showDialog();
            }
        } else if (i == R.id.ll_thaw) {
            if (hasData == 0) {
                Intent intent2 = new Intent();
                intent2.setClass(BreastActivity.this, ThawList.class);
                startActivity(intent2);
            } else {
                showDialog();
            }
        } else if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.ll_updata) {
            upData(uploadData);
        }
    }
}
