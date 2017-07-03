package com.fugao.formula.ui.signSend;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class SignSendActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.recyclerView_sign_send)
    RecyclerView recyclerView;
    @BindView(R.id.tv_userGH)
    TextView tv_userGH;
    private SignSendAdapter adapter;
    private List<BoxListEntity> mList;
    //上传List
    private List<BoxListEntity> upList;
    private String userGH = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_send);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        upList = new ArrayList<>();
        adapter = new SignSendAdapter(SignSendActivity.this, mList);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(SignSendActivity.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(SignSendActivity.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void initIntent() {

    }

    @Override
    public void receivePersonCode(String result) {
        super.receivePersonCode(result);
        userGH = result;
        tv_userGH.setText(result);
    }

    @Override
    public void receiveBoxCode(String result) {
        super.receiveBoxCode(result);
        if (StringUtils.StringIsEmpty(userGH)) {
            ToastUtils.showShort(SignSendActivity.this, "请先扫描员工卡");
        } else {
            postData(result);
        }
    }

    private void postData(String hzId) {
        DialogUtils.showProgressDialog(SignSendActivity.this, "签发中...");
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.HZID = hzId;
        postBean.OperatorName = XmlDB.getInstance(SignSendActivity.this).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(SignSendActivity.this).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.CurOperation = "签发";
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        String url = FormulaApi.FORMULA_POST;
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if ("[]".equals(response)) {
                        ToastUtils.showShort(SignSendActivity.this, "没有数据");
                    } else {
                        List<BoxListEntity> beans = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                        upList.add(beans.get(0));
                        adapter.setData(upList);
                        ToastUtils.showShort(SignSendActivity.this, "签发成功");
                        tv_userGH.setText("请先扫描员工卡");
                    }

                } else {
                    ToastUtils.showShort(SignSendActivity.this, "服务器异常");
                }

            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(SignSendActivity.this, "签发失败");
            }
        };
        OkHttpUtils.post(url, callback, json);
    }
}
