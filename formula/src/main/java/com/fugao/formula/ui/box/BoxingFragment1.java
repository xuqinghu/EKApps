package com.fugao.formula.ui.box;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.entity.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/8 0008.
 */

public class BoxingFragment1 extends BaseFragment {
    @BindView(R.id.tl1)
    CommonTabLayout tl;
    private PersonFragment fragment1;
    private BoxingFragment fragment2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"按病人查看", "按箱号查看"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_boxing1, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
    }

    @Override
    public void initData() {
        fragment1 = new PersonFragment();
        mFragments.add(fragment1);
        fragment2 = new BoxingFragment();
        mFragments.add(fragment2);
        tl.setTabData(mTabEntities, getActivity(), R.id.fl_change1, mFragments);

    }

    @Override
    public void initListener() {
        tl.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelect(int position) {
                tl.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }
}
