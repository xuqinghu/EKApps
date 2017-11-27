package com.fugao.formula.ui.box;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fugao.formula.MessageEvent;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.ViewUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;
import com.fugao.formula.utils.swipe.util.Attributes;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 核对界面
 * Created by Administrator on 2017/8/4 0004.
 */
@SuppressLint("ValidFragment")
public class CheckFragment extends BaseFragment {
    @BindView(R.id.recyclerView_advice)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_advice)
    SwipeRefreshLayout refresh_advice;
    private SingleBtnDialog singleBtnDialog;
    private List<AdviceEntity> mList;
    private CheckAdviceAdapter mAdapter;
    private TextView mPersonCount;
    private TextView mMilkCount;
    private Button mBoxing;
    private String milk_code = "";
    private int checkCount = 0;
    private BoxingActivity activity;

    public CheckFragment(TextView personCount, TextView milkCount, Button boxing) {
        this.mPersonCount = personCount;
        this.mMilkCount = milkCount;
        this.mBoxing = boxing;
    }

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_not_check, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        activity = (BoxingActivity) fatherActivity;
        refresh_advice.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        singleBtnDialog = new SingleBtnDialog(fatherActivity);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        mAdapter = new CheckAdviceAdapter(fatherActivity, mList, mPersonCount, mMilkCount);
        mAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
    }

    public void checkNetWork() {
        ViewUtils.setVisible(mBoxing);
        milk_code = XmlDB.getInstance(fatherActivity).getKeyString("milkCode", "");
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
        activity.showAllSure(false);
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String time = XmlDB.getInstance(fatherActivity).getKeyString("time", "");
        String divisionCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
        String url = FormulaApi.getAdviceData("5", divisionCode, milk_code, time);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if ("[]".equals(response)) {
                        ToastUtils.showShort(fatherActivity, "没有数据");
                        mMilkCount.setText("瓶数:0");
                        mPersonCount.setText("人数:0");
                    } else {
                        mList.clear();
                        mList = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                        recyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setData(mList, milk_code);
                        showCount(mPersonCount, mMilkCount);
                    }
                } else {
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(fatherActivity, "获取失败");
            }
        };
        OkHttpUtils.get(url, callback);
    }

    private void showCount(TextView personCount, TextView milkCount) {
        int count = 0;
        List<AdviceEntity> beans2 = new ArrayList<>();
        //筛选已核对数据
        for (AdviceEntity bean : mList) {
            if ("1".equals(bean.AdviceStatus)) {
                beans2.add(bean);
            }
        }
        Constant.ADVICE_BOX_LIST = beans2;
        //计算已核对的瓶数
        for (AdviceEntity bean : beans2) {
            count = count + Integer.parseInt(bean.Quantity);
        }
        milkCount.setText("瓶数:" + count);
        checkCount = count;
        XmlDB.getInstance(fatherActivity).saveKey("checkCount", checkCount + "");
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
    }
}
