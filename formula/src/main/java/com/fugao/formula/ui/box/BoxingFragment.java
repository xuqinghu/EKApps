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
import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.DialogUtils;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.FileHelper;
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
 * Created by Administrator on 2017/5/31 0031.
 */

public class BoxingFragment extends BaseFragment {
    @BindView(R.id.recycler_boxing)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_box)
    SwipeRefreshLayout refresh_box;
    @BindView(R.id.tv_boxing_userName)
    TextView userName;
    @BindView(R.id.tv_boxing_deptName)
    TextView deptName;
    @BindView(R.id.tv_boxing_count)
    TextView count;
    private List<BoxListEntity> mList;
    private TodayBoxingAdapter adapter;
    private SingleBtnDialog singleBtnDialog;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_boxing, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        refresh_box.setColorSchemeColors(getResources().getIntArray(R.array.color_array));
        userName.setText("操作人:" + XmlDB.getInstance(fatherActivity).getKeyStringValue("userName", ""));
        deptName.setText("科室:" + XmlDB.getInstance(fatherActivity).getKeyString("deptName", ""));
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(fatherActivity);
        mList = new ArrayList<>();
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(fatherActivity, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(fatherActivity));
        adapter = new TodayBoxingAdapter(R.layout.fragment_boxing_item, mList);
        recyclerView.setAdapter(adapter);
        getData();

    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(fatherActivity, BoxDetailActivity.class);
                intent.putExtra("boxlist", mList.get(i));
                startActivityForResult(intent, 101);
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

    //检查网络情况
    private void checkNetWork() {
        recyclerView.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(fatherActivity)) {
            refreshData();
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

    //刷新数据
    private void refreshData() {
        DialogUtils.showProgressDialog(fatherActivity, "正在加载数据...");
        String userCode = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        String date = DateUtils.getCurrentDate();
        String url = FormulaApi.getBoxData("3", "", date, userCode);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {
                            count.setText("已装:0个");
                            ToastUtils.showShort(fatherActivity, "没有数据");
                            mList.clear();
                            adapter.setNewData(mList);
                        } else {
                            mList = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                            recyclerView.setVisibility(View.VISIBLE);
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

    private void getData() {
        String userCode = XmlDB.getInstance(fatherActivity).getKeyString("userCode", "");
        String date = DateUtils.getCurrentDate();
        String url = FormulaApi.getBoxData("3", "", date, userCode);
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                if (code == 200) {
                    if (response != null) {
                        if (!"[]".equals(response)) {
                            mList = FastJsonUtils.getBeanList(response, BoxListEntity.class);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.setNewData(mList);
                            count.setText("已装:" + mList.size() + "个");
                        } else {
                            count.setText("已装:0个");
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

    //取消装箱后刷新数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 102:
                refreshData();
                Constant.CANCEL_BOXING = false;
                break;
            default:
                break;
        }
    }
}
