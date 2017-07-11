package com.fugao.formula.ui.box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.PersonEntity;
import com.fugao.formula.utils.DateUtils;
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
 * 装箱数据，按人来查的
 * Created by huxq on 2017/7/8 0008.
 */

public class PersonFragment extends BaseFragment {
    @BindView(R.id.refresh_person)
    SwipeRefreshLayout refresh_person;
    @BindView(R.id.recycler_person)
    RecyclerView recycler_person;
    @BindView(R.id.tv_boxing_userName)
    TextView userName;
    @BindView(R.id.tv_boxing_deptName)
    TextView deptName;
    @BindView(R.id.tv_boxing_count)
    TextView count;
    private List<PersonEntity> mList;
    private PersonAdapter adapter;
    private SingleBtnDialog singleBtnDialog;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_person, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        refresh_person.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        userName.setText("操作人:" + XmlDB.getInstance(fatherActivity).getKeyStringValue("userName", ""));
        deptName.setText("科室:" + XmlDB.getInstance(fatherActivity).getKeyString("deptName", ""));
    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        singleBtnDialog = new SingleBtnDialog(fatherActivity);
        //添加分割线
        recycler_person.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recycler_person.setLayoutManager(new LinearLayoutManager(fatherActivity));
        adapter = new PersonAdapter(R.layout.item_person, mList);
        recycler_person.setAdapter(adapter);
        getData();

    }

    private void checkNetWork() {
        recycler_person.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(fatherActivity)) {
            refreshData();
        } else {
            showWarnDialog("没有可用的网络");
        }
    }

    private void getData() {
        String userCode = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        String date = DateUtils.getCurrentDate();
        String url = FormulaApi.getBoxData("6", "", date, userCode);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                if (code == 200) {
                    if (response != null) {
                        if (!"[]".equals(response)) {
                            mList = FastJsonUtils.getBeanList(response, PersonEntity.class);
                            recycler_person.setVisibility(View.VISIBLE);
                            adapter.setNewData(mList);
                            count.setText("已装:" + mList.size() + "个");
                        } else {
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        };
        OkHttpUtils.get(url, callback);
    }

    //刷新数据
    private void refreshData() {
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String userCode = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        String date = DateUtils.getCurrentDate();
        String divisionCode = XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", "");
        String url = FormulaApi.getBoxData("6", "", date, userCode);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            ToastUtils.showShort(fatherActivity, "没有数据");
                        } else {
                            mList = FastJsonUtils.getBeanList(response, PersonEntity.class);
                            recycler_person.setVisibility(View.VISIBLE);
                            adapter.setNewData(mList);
                            count.setText("已装:" + mList.size() + "个");
                        }
                    }
                } else {
                    ToastUtils.showShort(fatherActivity, "服务器异常");
                }
            }

            @Override
            public void onFailure(Exception e) {
                ToastUtils.showShort(fatherActivity, "获取失败");
                DialogUtils.dissmissProgressDialog();
            }
        };
        OkHttpUtils.get(url, callback);
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

    @Override
    public void initListener() {
        //下拉刷新数据
        refresh_person.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_person.setRefreshing(false);
                checkNetWork();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(fatherActivity, PersonDetailActivity.class);
                intent.putExtra("list", mList.get(i));
                startActivity(intent);
            }
        });

    }
}
