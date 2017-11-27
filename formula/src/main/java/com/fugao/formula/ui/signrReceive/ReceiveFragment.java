package com.fugao.formula.ui.signrReceive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.ui.box.AdviceDetailActivity;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 已签收
 * Created by Administrator on 2017/8/4 0004.
 */

public class ReceiveFragment extends BaseFragment {
    @BindView(R.id.refresh_advice)
    SwipeRefreshLayout refresh_advice;
    @BindView(R.id.recyclerView_advice)
    RecyclerView recyclerView;
    private ReceiveAdapter mAdapter;
    private List<AdviceEntity> mList;
    private SingleBtnDialog singleBtnDialog;
    private SignReceiveActivity1 activity;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_receive, container, false);
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
        mAdapter = new ReceiveAdapter(R.layout.receive_item, mList);
        recyclerView.setAdapter(mAdapter);
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
        final String milk = XmlDB.getInstance(fatherActivity).getKeyString("milk", "");
        String url = FormulaApi.getAdviceData("7", divisionCode, milk, time);
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
                        activity.showCount(1, mList);
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
                intent.putExtra("state", "已签收");
                intent.setClass(fatherActivity, AdviceDetailActivity.class);
                fatherActivity.startActivity(intent);
            }
        });
    }
}
