package com.fugao.formula.ui.box;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
    private String milk_code = "";
    private AdviceEntity checkBean;
    private List<MilkDetail> adviceIDList;
    private int checkCount = 0;
    private boolean[] isChecked;
    private boolean checkFlag = false;

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
        Constant.SELECT_MILK_NAME.clear();
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
        milks = milkNameDao.getMilkNameByWardCode(XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", ""));
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        mAdapter = new CheckAdviceAdapter(fatherActivity, mList, personCount, milkCount);
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
                    milk_code = "";
                } else {
                    select_milk.setText(result.MilkName);
                    milk_code = result.MilkCode;
                }
                checkNetWork();
            }
        });
        listDialog.show();
    }

    //选择奶名，多选
    private void selectMilkName1() {
        isChecked = new boolean[milks.size()];
        for (int i = 0; i < milks.size(); i++) {
            for (int j = 0; j < Constant.SELECT_MILK_NAME.size(); j++) {
                if (Constant.SELECT_MILK_NAME.get(j).MilkCode.equals(milks.get(i).MilkCode)) {
                    isChecked[i] = true;
                    checkFlag = true;
                }
            }
            if (checkFlag = false) {
                isChecked[i] = false;
            }
            checkFlag = false;
        }
        final String[] milkName = new String[milks.size()];
        final String[] milkCode = new String[milks.size()];
        final List<MilkBean> milkBeans = new ArrayList<>();
        milkBeans.addAll(Constant.SELECT_MILK_NAME);
        for (int i = 0; i < milks.size(); i++) {
            milkName[i] = milks.get(i).MilkName;
            milkCode[i] = milks.get(i).MilkCode;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
        builder.setIcon(R.mipmap.milk);
        builder.setTitle("选择奶名");
        builder.setMultiChoiceItems(milkName, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    MilkBean milkBean = new MilkBean();
                    milkBean.MilkName = milkName[which];
                    milkBean.MilkCode = milkCode[which];
                    milkBeans.add(milkBean);
                } else {
                    //移除这一项
                    for (int i = 0; i < milkBeans.size(); i++) {
                        if (milkCode[which].equals(milkBeans.get(i).MilkCode)) {
                            milkBeans.remove(i);
                            i--;
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Constant.SELECT_MILK_NAME = milkBeans;
                milk_code = "";
                String milk_name = "";
                for (int i = 0; i < Constant.SELECT_MILK_NAME.size(); i++) {
                    milk_code = milk_code + Constant.SELECT_MILK_NAME.get(i).MilkCode + ";";
                    milk_name = milk_name + Constant.SELECT_MILK_NAME.get(i).MilkName + ";";
                }
                if (!StringUtils.StringIsEmpty(milk_code)) {
                    milk_code = milk_code.substring(0, milk_code.length() - 1);
                    milk_name = milk_name.substring(0, milk_name.length() - 1);
                }
                checkNetWork();
                if ("".equals(milk_code)) {
                    select_milk.setText("奶名");
                } else {
                    select_milk.setText(milk_name);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

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
        String url = FormulaApi.getAdviceData("4", divisionCode, milk_code, time);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(fatherActivity, "没有数据");
                            milkCount.setText("瓶数:0");
                            personCount.setText("人数:0");
                        } else {
                            mList.clear();
                            mList = FastJsonUtils.getBeanList(response, AdviceEntity.class);
                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter.setData(mList, milk_code);
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
    public void checkYZ(final TextView all_sure, String bottleId) {
        List<PostEntity> postData = new ArrayList<>();
        PostEntity postBean = new PostEntity();
        postBean.OperatorName = XmlDB.getInstance(fatherActivity).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.ExecFrequency = XmlDB.getInstance(fatherActivity).getKeyString("time", "");
        postBean.NPID = bottleId;
        postBean.MilkName = milk_code;
        postBean.WardCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
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
                            if (StringUtils.StringIsEmpty(beans.get(0).ShowMsg)) {
                                mList.clear();
                                mList = beans;
                                mAdapter.setData(mList, milk_code);
                                showCount();
                                ToastUtils.showShort(fatherActivity, "核对成功");
                            } else {
                                String msg = beans.get(0).ShowMsg;
                                if ("未找到相应的医嘱".equals(msg)) {
                                    ToastUtils.showShort(fatherActivity, "核对失败，未找到相应的医嘱");
                                } else if ("医嘱变更".equals(msg)) {
                                    showWarnDialog("医嘱有变更，请打印新医嘱条码");
                                } else if ("该牛奶未在执行时间内找到".equals(msg)) {
                                    showWarnDialog("请扫描所选时间内的奶瓶条码");
                                } else if ("牛奶已核对".equals(msg)) {
                                    ToastUtils.showShort(fatherActivity, "已核对，无需重复核对");
                                }
                            }
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
                selectMilkName1();

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
        milks.clear();
        milks = milkNameDao.getMilkNameByWardCode(XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", ""));
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
