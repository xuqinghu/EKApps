package com.fugao.formula.ui.box;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.entity.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BoxingActivity extends BaseActivity {
    @BindView(R.id.tl)
    CommonTabLayout tl;
    @BindView(R.id.tv_activity_boxing_title)
    TextView title;
    private ArrayList<CustomTabEntity> mTabEntities;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"未装箱", "已装箱"};

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_boxing);

    }

    @Override
    public void initView() {
        title.setText(mTitles[0]);
    }

    @Override
    public void initData() {
        mTabEntities = new ArrayList<>();
        for (String title : mTitles) {
            if ("已装箱".equals(title)) {
                mFragments.add(BoxingFragment.getInstance(title));
            } else if ("未装箱".equals(title)) {
                mFragments.add(NotBoxingFragment.getInstance(title));
            }

        }
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        tl.setTabData(mTabEntities, this, R.id.fl_change, mFragments);
    }

    @Override
    public void initListener() {
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                title.setText(mTitles[i]);
            }

            @Override
            public void onTabReselect(int i) {

            }
        });

    }

    @Override
    public void initIntent() {

    }

}
