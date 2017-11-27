package com.fugao.formula.ui.signrReceive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.ui.box.AdviceDetailActivity;
import com.fugao.formula.ui.box.BoxingActivity;
import com.fugao.formula.ui.box.NotCheckAdapter;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 未签收
 * Created by Administrator on 2017/8/4 0004.
 */

public class NotReceiveFragment extends BaseFragment {
    @BindView(R.id.refresh_advice)
    SwipeRefreshLayout refresh_advice;
    @BindView(R.id.recyclerView_advice)
    RecyclerView recyclerView;
    private NotReceiveAdapter mAdapter;
    private SingleBtnDialog singleBtnDialog;
    private List<AdviceEntity> mList;
    private SignReceiveActivity1 activity;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_not_receive, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        refresh_advice.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        activity = (SignReceiveActivity1) fatherActivity;
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        singleBtnDialog = new SingleBtnDialog(fatherActivity);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        mAdapter = new NotReceiveAdapter(R.layout.not_receive_item, mList);
        recyclerView.setAdapter(mAdapter);
        checkNetWork();
    }

    public void checkNetWork() {
        recyclerView.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(fatherActivity)) {
            getData();
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

    private void getData() {
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String divisionCode = XmlDB.getInstance(fatherActivity).getKeyString("code", "");
        String time = XmlDB.getInstance(fatherActivity).getKeyString("receiveTime", "");
        String milk = XmlDB.getInstance(fatherActivity).getKeyString("milk", "");
        String url = FormulaApi.getAdviceData("6", divisionCode, milk, time);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if ("[]".equals(response)) {
                        ToastUtils.showShort(fatherActivity, "没有数据");
                        activity.showCount(3, mList);
                    } else {
                        mList.clear();
                        mList = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                        activity.showCount(0, mList);
                        recyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setNewData(mList);
                    }

                } else {
                    activity.showCount(3, mList);
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                }

            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                activity.showCount(3, mList);
                ToastUtils.showShort(fatherActivity, "获取失败");
            }
        };
        OkHttpUtils.get(url, callback);
    }

    @Override
    public void initListener() {
        //下拉刷新数据
        refresh_advice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_advice.setRefreshing(false);
                checkNetWork();
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("advice", mList.get(position));
                intent.putExtra("state", "未签收");
                intent.setClass(fatherActivity, AdviceDetailActivity.class);
                fatherActivity.startActivity(intent);
            }
        });
    }

    public void sureReceive(String hzId) {
        DialogUtils.showProgressDialog(fatherActivity, "正在签收...");
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.HZID = hzId;
        postBean.CurOperation = "签收";
        postBean.OperatorName = XmlDB.getInstance(fatherActivity).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.WardCode = XmlDB.getInstance(fatherActivity).getKeyString("code", "");
        postBean.ExecFrequency = XmlDB.getInstance(fatherActivity).getKeyString("receiveTime", "");
        postBean.MilkName = XmlDB.getInstance(fatherActivity).getKeyString("milk", "");
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
                            ToastUtils.showShort(fatherActivity, "没有数据");
                        } else {
                            List<AdviceEntity> beans = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            if (StringUtils.StringIsEmpty(beans.get(0).ShowMsg)) {
                                mList.clear();
                                mList = beans;
                                activity.showCount(0, mList);
                                mAdapter.setNewData(mList);
                                ToastUtils.showShort(fatherActivity, "签收成功");
                            } else {
                                ToastUtils.showShort(fatherActivity, beans.get(0).ShowMsg);
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(fatherActivity, "签收失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.post(url, callback, json);
    }
}
