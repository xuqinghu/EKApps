package com.fugao.formula.ui.box;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;


/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class NotBoxingFragment extends BaseFragment {
    private String mTitle;

    public static NotBoxingFragment getInstance(String title) {
        NotBoxingFragment notBoxingFragment = new NotBoxingFragment();
        notBoxingFragment.mTitle = title;
        return notBoxingFragment;
    }

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_notboxing, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
