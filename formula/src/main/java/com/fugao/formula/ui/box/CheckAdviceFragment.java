package com.fugao.formula.ui.box;

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
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.db.dao.MilkNameDao;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.entity.MilkDetail;
import com.fugao.formula.entity.PostEntity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.NetWorkUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.ListDialog;
import com.fugao.formula.utils.dialog.SingleBtnDialog;
import com.fugao.formula.utils.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class CheckAdviceFragment extends BaseFragment {
    @BindView(R.id.btn_boxing)
    Button boxing;
    @BindView(R.id.tv_select_milk)
    TextView select_milk;
    @BindView(R.id.recyclerView_advice)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_advice)
    SwipeRefreshLayout refresh_advice;
    @BindView(R.id.tv_check_advice_personCount)
    TextView personCount;
    @BindView(R.id.tv_check_advice_milkCount)
    TextView milkCount;
    @BindView(R.id.tv_check_advice_name)
    TextView name;
    private List<AdviceEntity> mList;
    private List<MilkBean> milks;
    private CheckAdviceAdapter mAdapter;
    private MilkNameAdapter adapter;
    private SingleBtnDialog singleBtnDialog;
    private ListDialog listDialog;
    private MilkNameDao milkNameDao;
    private BoxingActivity activity;
    private String milkCode = "";
    private AdviceEntity checkBean;
    private List<MilkDetail> adviceIDList;
    private int checkCount = 0;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_check_advice, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        activity = (BoxingActivity) fatherActivity;
        refresh_advice.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        name.setText("核对人:" + XmlDB.getInstance(fatherActivity).getKeyString("userName", ""));

    }

    @Override
    public void initData() {
        milkNameDao = activity.milkNameDao;
        listDialog = new ListDialog(fatherActivity);
        singleBtnDialog = new SingleBtnDialog(fatherActivity);
        checkBean = new AdviceEntity();
        mList = new ArrayList<>();
        milks = new ArrayList<>();
        adviceIDList = new ArrayList<>();
        milks = milkNameDao.getMilkName();
        MilkBean milkBean = new MilkBean();
        milkBean.MilkName = "全部";
        milkBean.MilkCode = "";
        milks.add(0, milkBean);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        mAdapter = new CheckAdviceAdapter(fatherActivity, mList);
        adapter = new MilkNameAdapter(R.layout.milk_name_item, fatherActivity);
        mAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);
        checkNetWork();
    }

    //选择奶名
    private void selectMilkName() {
        adapter.setDatas(milks);
        listDialog.setAdapter(adapter);
        listDialog.setTitleTextView("选择奶名");
        listDialog.setDataReturnListener(new ListDialog.RequestReturnListener<MilkBean>() {
            @Override
            public void returnResult(MilkBean result) {
                if ("全部".equals(result.MilkName)) {
                    select_milk.setText("奶名");
                    milkCode = "";
                } else {
                    select_milk.setText(result.MilkName);
                    milkCode = result.MilkCode;
                }
                checkNetWork();
            }
        });
        listDialog.show();
    }

    //检查网络情况
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

    //获取医嘱数据
    private void getData() {
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String time = XmlDB.getInstance(fatherActivity).getKeyString("time", "");
        String divisionCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
        String url = FormulaApi.getAdviceData("4", divisionCode, milkCode, time);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(fatherActivity, "没有数据");
                        } else {
                            mList = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter.setData(mList);
                            showCount();
                        }
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

    //核对医嘱
    public void checkYZ(final TextView all_sure) {
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.OperatorName = XmlDB.getInstance(fatherActivity).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        if (Constant.CHECK_ALL) {
            postBean.NPID = getAllMilkNo();
        } else {
            postBean.NPID = getAdviceIDs();
        }
        postBean.CurOperation = "核对医嘱";
        postData.add(postBean);
        String json = JSON.toJSONString(postData);
        DialogUtils.showProgressDialog(fatherActivity, "正在核对医嘱...");
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
                            if (beans != null && !Constant.CHECK_ALL) {
                                mList.remove(checkBean);
                                mList.add(0, beans.get(0));
                            } else if (beans != null && Constant.CHECK_ALL) {
                                mList = beans;
                            }
                            Constant.CHECK_ALL = false;
                            mAdapter.setData(mList);
                            showCount();
                            ToastUtils.showShort(fatherActivity, "核对成功");
                            all_sure.setVisibility(View.GONE);
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

    @Override
    public void initListener() {
        //装箱
        boxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fatherActivity, SelectBoxActivity.class);
                intent.putExtra("count", checkCount + "");
                startActivity(intent);
                XmlDB.getInstance(fatherActivity).saveKey("boxing", "no");
            }
        });
        //选择奶名
        select_milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMilkName();

            }
        });
        //下拉刷新数据
        refresh_advice.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_advice.setRefreshing(false);
                checkNetWork();
            }
        });

    }

    private void showCount() {
        int count = 0;
        List<AdviceEntity> beans1 = new ArrayList<>();
        List<AdviceEntity> beans2 = new ArrayList<>();
        //筛选未核对数据
        for (AdviceEntity bean : mList) {
            if ("0".equals(bean.AdviceStatus)) {
                beans1.add(bean);
            }
        }
        personCount.setText("人数:" + beans1.size());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("yes".equals(XmlDB.getInstance(fatherActivity).getKeyString("boxing", ""))) {
            checkNetWork();
        }
        XmlDB.getInstance(fatherActivity).saveKey("boxing", "no");
    }

    public void getAdviceIDList(String milkNo) {
        for (int i = 0; i < mList.size(); i++) {
            for (int j = 0; j < mList.get(i).FormulaMilkDetail.size(); j++) {
                if (milkNo.equals(mList.get(i).FormulaMilkDetail.get(j).MilkNo)) {
                    adviceIDList = mList.get(i).FormulaMilkDetail;
                    checkBean = mList.get(i);
                    return;
                }
            }
        }
    }

    //全部核对
    public String getAllMilkNo() {
        String adviceIds = "";
        for (int i = 0; i < mList.size(); i++) {
            if ("0".equals(mList.get(i).AdviceStatus)) {
                for (int j = 0; j < mList.get(i).FormulaMilkDetail.size(); j++) {
                    adviceIds = adviceIds + mList.get(i).FormulaMilkDetail.get(j).MilkNo + ";";
                }
            }

        }
        if (!StringUtils.StringIsEmpty(adviceIds)) {
            adviceIds = adviceIds.substring(0, adviceIds.length() - 1);
        }
        return adviceIds;
    }

    public String getAdviceIDs() {
        String adviceIds = "";
        for (MilkDetail bean : adviceIDList) {
            adviceIds = adviceIds + bean.MilkNo + ";";
        }
        if (!StringUtils.StringIsEmpty(adviceIds)) {
            adviceIds = adviceIds.substring(0, adviceIds.length() - 1);
        }
        return adviceIds;
    }
}
