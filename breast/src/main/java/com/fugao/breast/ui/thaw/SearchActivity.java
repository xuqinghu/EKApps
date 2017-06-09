package com.fugao.breast.ui.thaw;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.entity.ThawListBean;
import com.fugao.breast.utils.common.FastJsonUtils;
import com.fugao.breast.utils.common.FileHelper;
import com.fugao.breast.utils.common.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询界面
 * Created by Administrator on 2017/5/23 0023.
 */

public class SearchActivity extends BaseActivity {
    private LinearLayout back;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private List<ThawListBean> data;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.ll_search_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_search);

    }

    @Override
    public void initData() {
        data = new ArrayList<>();
        data = getData();
        recyclerView.addItemDecoration(new RecyclerViewDivider(SearchActivity.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchAdapter(R.layout.search_item, data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //list点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, ThawDetail.class);
                intent.putExtra("thawlist", data.get(i));
                startActivity(intent);
            }
        });

    }

    @Override
    public void initIntent() {

    }

    private List<ThawListBean> getData() {
        List<ThawListBean> beans = new ArrayList<>();
        String breastJson = FileHelper.getFromAssets("thawlist", SearchActivity.this);
        beans = FastJsonUtils.getBeanList(breastJson, ThawListBean.class);
        return beans;
    }
}
