package com.fugao.formula.ui.box;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
    private int selectCount = 0;
    private List<AdviceEntity> selectList;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_box);
    }

    @Override
    public void initView() {
        refresh_box.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        getCount();
        tv_count.setText("待装箱瓶数:" + count);
        tv_name.setText("装箱人:" + XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userName", ""));
        tv_division.setText("病区:" + XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionName", ""));

    }

    private void getCount() {
        for (AdviceEntity bean : Constant.ADVICE_BOX_LIST) {
            count = count + Integer.parseInt(bean.Quantity);
        }
    }

    @Override
    public void initData() {
        twoBtnDialog = new TwoBtnDialog(SelectBoxActivity.this);
        singleBtnDialog = new SingleBtnDialog(SelectBoxActivity.this);
        mList = new ArrayList<>();
        selectList = new ArrayList<>();
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
                            mList.clear();
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
        tv_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountDialog();
            }
        });
    }

    private void showCountDialog() {
        LayoutInflater factory = LayoutInflater.from(SelectBoxActivity.this);//提示框
        final View view = factory.inflate(R.layout.dialog_count, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.et_count);//获得输入框对象
        new AlertDialog.Builder(SelectBoxActivity.this)
                .setTitle("设置人数")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (!StringUtils.StringIsEmpty(edit.getText().toString()) &&
                                        !edit.getText().toString().equals("0")) {
                                    getSelectList(Integer.parseInt(edit.getText().toString()));
                                }
                            }
                        }).setNegativeButton("取消", null).create().show();
    }

    //获取设置的待装箱瓶数
    private void getSelectList(int c) {
        if (Constant.ADVICE_BOX_LIST.size() >= c) {
            selectCount = 0;
            selectList.clear();
            for (int i = 0; i < c; i++) {
                AdviceEntity bean = Constant.ADVICE_BOX_LIST.get(i);
                selectList.add(bean);
            }
            for (AdviceEntity bean : selectList) {
                selectCount = selectCount + Integer.parseInt(bean.Quantity);
            }
            tv_count.setText("待装箱瓶数:" + selectCount);
        } else {
            showWarnDialog("设置的人数比需要装箱的人数还大");
            tv_count.setText("待装箱瓶数:" + count);
            selectCount = 0;
        }

    }

    //提示是否装在此箱
    private void showTwoDialog(final BoxListEntity box) {
        twoBtnDialog.setDialogCloseImageView(View.GONE);
        twoBtnDialog.setDialogTitleTextView("温馨提示！");
        twoBtnDialog.setDialogContentTextView("是否要装在" + box.MilkBoxID + "号箱子？");
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
        if (selectCount == 0) {
            postBean.MilkQuantity = Integer.parseInt(bean.MilkQuantity) + count + "";
        } else {
            postBean.MilkQuantity = Integer.parseInt(bean.MilkQuantity) + selectCount + "";
        }
        postBean.WardName = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionName", "");
        postBean.WardCode = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("divisionCode", "");
        postBean.OperatorName = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userName", "");
        postBean.OperatorGH = XmlDB.getInstance(SelectBoxActivity.this).getKeyString("userCode", "");
        postBean.OperatorDate = DateUtils.getCurrentDate();
        postBean.OperatorTime = DateUtils.getCurrentTime();
        if (selectCount == 0) {
            postBean.NPID = getAdviceIDs(Constant.ADVICE_BOX_LIST);
        } else {
            postBean.NPID = getAdviceIDs(selectList);
        }
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
                            if ("签发".equals(beans.get(0).MilkBoxStatus) && StringUtils.StringIsEmpty(beans.get(0).HZID)) {
                                ToastUtils.showShort(SelectBoxActivity.this, "这个箱子已经被签发了");
                                checkNetWork();
                            } else if ("签收".equals(beans.get(0).MilkBoxStatus) && StringUtils.StringIsEmpty(beans.get(0).HZID)) {
                                ToastUtils.showShort(SelectBoxActivity.this, "这个箱子已经被签收了");
                                checkNetWork();
                            } else if ("已被其他病区装箱".equals(beans.get(0).MilkBoxStatus)) {
                                ToastUtils.showShort(SelectBoxActivity.this, "这个箱子已经被其他病区使用");
                                checkNetWork();
                            } else {
                                mList.remove(bean);
                                mList.add(0, beans.get(0));
                                adapter.setNewData(mList);
                                ToastUtils.showShort(SelectBoxActivity.this, "装箱成功");
                                XmlDB.getInstance(SelectBoxActivity.this).saveKey("boxing", "yes");
                                if (selectCount == 0) {
                                    count = 0;
                                    Constant.ADVICE_BOX_LIST = new ArrayList<>();
                                } else {
                                    count = count - selectCount;
                                    Constant.ADVICE_BOX_LIST.removeAll(selectList);
                                    selectCount = 0;
                                }
                                tv_count.setText("待装箱瓶数：" + count);
                                checkNetWork();
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

    private String getAdviceIDs(List<AdviceEntity> beans) {
        String adviceIds = "";
        if (beans.size() > 0) {
            for (int i = 0; i < beans.size(); i++) {
                for (int j = 0; j < beans.get(i).FormulaMilkDetail.size(); j++) {
                    adviceIds = adviceIds + beans.get(i).FormulaMilkDetail.get(j).MilkNo + ";";
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

    }

    @Override
    public void receiveBoxCode(String result) {
        super.receiveBoxCode(result);
    }
}
