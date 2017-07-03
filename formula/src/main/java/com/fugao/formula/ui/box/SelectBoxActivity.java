package com.fugao.formula.ui.box;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.BoxListEntity;
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
import com.fugao.formula.utils.dialog.SingleBtnDialog;
import com.fugao.formula.utils.dialog.TwoBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class SelectBoxActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.recycler_box)
    RecyclerView recyclerView;
    @BindView(R.id.tv_select_box_count)
    TextView tv_count;
    @BindView(R.id.tv_select_box_name)
    TextView tv_name;
    @BindView(R.id.tv_select_box_division)
    TextView tv_division;
    @BindView(R.id.refresh_box)
    SwipeRefreshLayout refresh_box;
    private SelectBoxAdapter adapter;
    private List<BoxListEntity> mList;
    private SingleBtnDialog singleBtnDialog;
    private TwoBtnDialog twoBtnDialog;
    private int count = 0;
    private int checkCount = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_box);
    }

    @Override
    public void initView() {
        refresh_box.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        for (AdviceEntity bean : Constant.ADVICE_BOX_LIST) {
            count = count + Integer.parseInt(bean.Quantity);
        }
        tv_count.setText("待装箱瓶数：" + count);
        tv_name.setText("装箱人：" + XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userName", ""));
        tv_division.setText("病区：" + XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionName", ""));

    }

    @Override
    public void initData() {
        twoBtnDialog = new TwoBtnDialog(SelectBoxActivity.this);
        singleBtnDialog = new SingleBtnDialog(SelectBoxActivity.this);
        mList = new ArrayList<>();
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectBoxAdapter(R.layout.item_select_box, mList);
        recyclerView.setAdapter(adapter);
        checkNetWork();
    }

    //检查网络情况
    private void checkNetWork() {
        recyclerView.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(SelectBoxActivity.this)) {
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
        DialogUtils.showProgressDialog(SelectBoxActivity.this, "正在加载数据...");
        String divisionCode = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionCode", "");
        String url = FormulaApi.getBoxData("5", divisionCode, "", "");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(SelectBoxActivity.this, "没有数据");
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            mList = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                            adapter.setNewData(mList);
                        }
                    }
                } else {
                    ToastUtils.showShort(SelectBoxActivity.this, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(SelectBoxActivity.this, "获取失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.get(url, callback);
    }

    @Override
    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (count == 0) {
                    ToastUtils.showShort(SelectBoxActivity.this, "没有需要装箱的奶瓶");
                } else {
                    showTwoDialog(mList.get(position));
                }
            }
        });
        //下拉刷新数据
        refresh_box.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_box.setRefreshing(false);
                checkNetWork();
            }
        });
    }

    //提示是否装在此箱
    private void showTwoDialog(final BoxListEntity box) {
        twoBtnDialog.setDialogCloseImageView(View.GONE);
        twoBtnDialog.setDialogTitleTextView("温馨提示！");
        twoBtnDialog.setDialogContentTextView("是否要装在" + box.MilkBoxID + "箱子？");
        twoBtnDialog.setLeftTextView("否");
        twoBtnDialog.setRightTextView("是");
        twoBtnDialog.setCanceledOnTouchOutside(false);
        twoBtnDialog.setDialogBtnClickListener(new TwoBtnDialog.DialogBtnClickListener() {
            @Override
            public void leftBtnClick() {
                twoBtnDialog.dismiss();
            }

            @Override
            public void rightBtnClick() {
                twoBtnDialog.dismiss();
                Boxing(box);

            }
        });
        twoBtnDialog.show();

    }

    private void Boxing(final BoxListEntity bean) {
        List<PostEntity> postData = new ArrayList<>();
        DialogUtils.showProgressDialog(SelectBoxActivity.this, "装箱中...");
        PostEntity postBean = new PostEntity();
        postBean.MilkBoxID = bean.MilkBoxID;
        postBean.MilkBoxStatus = bean.MilkBoxStatus;
        postBean.MilkBoxSpec = bean.MilkBoxSpec;
        postBean.HZID = bean.HZID;
        postBean.MilkQuantity = Integer.parseInt(bean.MilkQuantity) + checkCount + "";
        postBean.WardName = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionName", "");
        postBean.WardCode = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionCode", "");
        postBean.OperatorName = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        postBean.NPID = getAdviceIDs();
        postBean.CurOperation = "装箱";
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
                            ToastUtils.showShort(SelectBoxActivity.this, "没有数据");
                        } else {
                            List<BoxListEntity> beans = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                            if (beans.size() > 0) {
                                mList.remove(bean);
                                mList.add(0, beans.get(0));
                                adapter.setNewData(mList);
                                ToastUtils.showShort(SelectBoxActivity.this, "装箱成功");
                                XmlDB.getInstance(SelectBoxActivity.this).saveKey("boxing", "yes");
                                count = 0;
                                Constant.ADVICE_BOX_LIST = new ArrayList<>();
                                tv_count.setText("待装箱瓶数：" + count);
                            }
                        }
                    }
                } else {
                    ToastUtils.showShort(SelectBoxActivity.this, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                ToastUtils.showShort(SelectBoxActivity.this, "装箱失败");
            }
        };
        OkHttpUtils.post(url, callback, json);
    }

    private String getAdviceIDs() {
        String adviceIds = "";
        if (Constant.ADVICE_BOX_LIST.size() > 0) {
            for (int i = 0; i < Constant.ADVICE_BOX_LIST.size(); i++) {
                for (int j = 0; j < Constant.ADVICE_BOX_LIST.get(i).FormulaMilkDetail.size(); j++) {
                    adviceIds = adviceIds + Constant.ADVICE_BOX_LIST.get(i).FormulaMilkDetail.get(j).MilkNo + ";";
                }
            }
        }
        if (!StringUtils.StringIsEmpty(adviceIds)) {
            adviceIds = adviceIds.substring(0, adviceIds.length() - 1);
        }
        return adviceIds;
    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        checkCount = Integer.parseInt(intent.getStringExtra("count"));
    }

    @Override
    public void receiveBoxCode(String result) {
        super.receiveBoxCode(result);
    }
}
