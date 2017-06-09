package com.fugao.formula.ui.box;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BoxingFragment extends BaseFragment {
    private String mTitle;

    public static BoxingFragment getInstance(String title) {
        BoxingFragment boxingFragment = new BoxingFragment();
        boxingFragment.mTitle = title;
        return boxingFragment;
    }


    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_boxing, container, false);
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
