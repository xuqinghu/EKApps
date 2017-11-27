package com.fugao.formula.ui.box;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.MessageEvent;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.ViewUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.SingleBtnDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 未核对界面
 * Created by Administrator on 2017/8/4 0004.
 */
@SuppressLint("ValidFragment")
public class NotCheckFragment extends BaseFragment {
    @BindView(R.id.recyclerView_advice)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_advice)
    SwipeRefreshLayout refresh_advice;
    private SingleBtnDialog singleBtnDialog;
    private NotCheckAdapter mAdapter;
    private List<AdviceEntity> mList;
    private TextView mPersonCount;
    private TextView mMilkCount;
    private Button mBoxing;
    private String milk_code = "";
    private BoxingActivity activity;

    public NotCheckFragment(TextView personCount, TextView milkCount, Button boxing) {
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
        mAdapter = new NotCheckAdapter(R.layout.not_check_item, mList);
        recyclerView.setAdapter(mAdapter);
        checkNetWork();
    }

    public void checkNetWork() {
        ViewUtils.setGone(mBoxing);
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
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String time = XmlDB.getInstance(fatherActivity).getKeyString("time", "");
        String divisionCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
        String url = FormulaApi.getAdviceData("4", divisionCode, milk_code, time);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if ("[]".equals(response)) {
                        ToastUtils.showShort(fatherActivity, "没有数据");
                        mMilkCount.setText("瓶数:0");
                        mPersonCount.setText("人数:0");
                        activity.showAllSure(false);
                    } else {
                        mList.clear();
                        mList = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                        recyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setNewData(mList);
                        showCount(mPersonCount, mMilkCount);
                        activity.showAllSure(true);
                    }
                } else {
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                    activity.showAllSure(false);
                }

            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(fatherActivity, "获取失败");
                activity.showAllSure(false);
            }
        };
        OkHttpUtils.get(url, callback);
    }

    //核对医嘱
    public void checkYZ(final TextView all_sure, final String bottleId) {
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.OperatorName = XmlDB.getInstance(fatherActivity).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.ExecFrequency = XmlDB.getInstance(fatherActivity).getKeyString("time", "");
        postBean.NPID = bottleId;
        postBean.MilkName = XmlDB.getInstance(fatherActivity).getKeyString("milkCode", "");
        postBean.WardCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
        postBean.CurOperation = "核对医嘱";
        postData.add(postBean);
        final String json = JSON.toJSONString(postData);
        DialogUtils.showProgressDialog(fatherActivity, "正在核对医嘱...");
        final String url = FormulaApi.FORMULA_POST;
        final OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            mList.clear();
                            mAdapter.setNewData(mList);
                            showCount(mPersonCount, mMilkCount);
                            ToastUtils.showShort(fatherActivity, "核对成功");
                            all_sure.setVisibility(View.GONE);
                            if("已核对".equals(Constant.SELECT_PLACE)){
                                EventBus.getDefault().post(new MessageEvent("refresh"));
                            }
                        } else {
                            List<AdviceEntity> beans = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            if (StringUtils.StringIsEmpty(beans.get(0).ShowMsg)) {
                                mList.clear();
                                mList = beans;
                                mAdapter.setNewData(mList);
                                showCount(mPersonCount, mMilkCount);
                                ToastUtils.showShort(fatherActivity, "核对成功");
                                all_sure.setVisibility(View.GONE);
                                if("已核对".equals(Constant.SELECT_PLACE)){
                                    EventBus.getDefault().post(new MessageEvent("refresh"));
                                }
                            } else {
                                String msg = beans.get(0).ShowMsg;
                                if ("未找到相应的医嘱".equals(msg)) {
                                    ToastUtils.showShort(fatherActivity, "核对失败，未找到相应的医嘱");
                                } else if ("医嘱变更".equals(msg)) {
                                    showWarnDialog("医嘱有变更，请打印新医嘱条码");
                                } else if ("该牛奶未在执行时间内找到".equals(msg)) {
                                    showWarnDialog("请扫描所选时间内的奶瓶条码");
                                } else if ("牛奶已核对".equals(msg)) {
                                    if (StringUtils.StringIsEmpty(bottleId)) {
                                        ToastUtils.showLong(fatherActivity, "该时间点内没有可核对的医嘱，请先打印瓶贴条码");
                                    } else {
                                        ToastUtils.showShort(fatherActivity, "已核对，无需重复核对");
                                    }
                                }
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(fatherActivity, "核对失败");
            }
        };
        OkHttpUtils.post(url, callback, json);
    }

    private void showCount(TextView personCount, TextView milkCount) {
        personCount.setText("医嘱条数:" + mList.size());
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
                intent.putExtra("state", "未核对");
                intent.setClass(fatherActivity, AdviceDetailActivity.class);
                fatherActivity.startActivity(intent);
            }
        });
    }
}
