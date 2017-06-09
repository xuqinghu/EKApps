package com.fugao.breast.ui.thaw;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.constant.BreastApi;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.db.dao.BreastListDao;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.ui.put.PutList;
import com.fugao.breast.utils.common.DateUtils;
import com.fugao.breast.utils.common.DialogUtils;
import com.fugao.breast.utils.common.FastJsonUtils;
import com.fugao.breast.utils.common.FileHelper;
import com.fugao.breast.utils.common.NetWorkUtils;
import com.fugao.breast.utils.common.OkHttpUtils;
import com.fugao.breast.utils.common.RecyclerViewDivider;
import com.fugao.breast.utils.common.XmlDB;
import com.fugao.breast.utils.dialog.SingleBtnDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxq on 2017/5/22 0022.
 */

public class ThawList extends BaseActivity {
    private ThawListAdapter adapter;
    private List<BreastMilk> data;
    private RecyclerView recyclerView;
    private LinearLayout back, search;
    private TextView division, name, count, tv_updata_count;
    private DataBaseInfo dataBaseInfo;
    private BreastListDao breastListDao;
    private SingleBtnDialog singleBtnDialog;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_breast_list);

    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_breast_list);
        back = (LinearLayout) findViewById(R.id.ll_breast_list_back);
        search = (LinearLayout) findViewById(R.id.ll_thaw_list_search);
        division = (TextView) findViewById(R.id.tv_breast_list_division);
        name = (TextView) findViewById(R.id.tv_breast_list_name);
        count = (TextView) findViewById(R.id.tv_breast_list_count);
        tv_updata_count = (TextView) findViewById(R.id.tv_updata_count);
        division.setText("病区:" + XmlDB.getInstance(ThawList.this).getKeyStringValue("wardName", ""));
        name.setText("解冻人:" + XmlDB.getInstance(ThawList.this).getKeyStringValue("nName", ""));
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(ThawList.this);
        dataBaseInfo = DataBaseInfo.getInstance(ThawList.this);
        breastListDao = new BreastListDao(dataBaseInfo);
        data = new ArrayList<>();
        recyclerView.addItemDecoration(new RecyclerViewDivider(ThawList.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThawListAdapter(R.layout.breast_list_item, data);
        recyclerView.setAdapter(adapter);
        checkNetWork();
    }

    //设置已解冻瓶数
    private void thawCount(List<BreastMilk> beans) {
        int c = 0;
        for (BreastMilk bean : beans) {
            c = c + Integer.parseInt(bean.ThawAccount);
        }
        count.setText("已解冻：" + c);
    }


    @Override
    public void initListener() {
        //list点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(ThawList.this, ThawDetail.class);
                intent.putExtra("thawlist", data.get(i));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchActivity.class);
            }
        });
    }

    @Override
    public void initIntent() {

    }

    private void checkNetWork() {
        recyclerView.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(ThawList.this)) {
            getData();
        } else {
            singleBtnDialog.setDialogCloseImageView(View.GONE);
            singleBtnDialog.setDialogTitleTextView("温馨提示！");
            singleBtnDialog.setDialogContentTextView("没有可用的网络");
            singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
                @Override
                public void sureBtnClick() {
                    finish();
                }
            });
            singleBtnDialog.show();
        }
    }

    private void getData() {
        String deptId = XmlDB.getInstance(ThawList.this).getKeyString("deptId", "");
        String wardId = XmlDB.getInstance(ThawList.this).getKeyString("wardId", "");
        String url = BreastApi.getBreastData("", deptId, wardId, DateUtils.getBeforeDate(), "1");
        DialogUtils.showProgressDialog(ThawList.this, "正在加载数据...");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                DialogUtils.dissmissProgressDialog();
                recyclerView.setVisibility(View.VISIBLE);
                if (response != null) {
                    data = FastJsonUtils.getBeanList(response, BreastMilk.class);
                    breastListDao.deleteAllInfo();
                    breastListDao.saveToBreastList(data);
                    adapter.setNewData(data);
                    thawCount(data);
                }

            }

            @Override
            public void onFailure(Exception e) {
                DialogUtils.dissmissProgressDialog();
                getSecondData();
            }
        };
        OkHttpUtils.get(url, callback);
    }

    //重新从网络上获取数据
    private void getSecondData() {
        singleBtnDialog.setDialogCloseImageView(View.GONE);
        singleBtnDialog.setDialogTitleTextView("温馨提示！");
        singleBtnDialog.setDialogContentTextView("数据加载失败");
        singleBtnDialog.setSureTextView("重试");
        singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
            @Override
            public void sureBtnClick() {
                checkNetWork();
            }
        });
        singleBtnDialog.show();
    }

    //刷新列表
    private void refreshList() {
        data.clear();
        data = breastListDao.getBreastList();
        adapter.setNewData(data);
        thawCount(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}
