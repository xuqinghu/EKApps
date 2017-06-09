package com.fugao.formula.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/4 0004.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * 当前activity
     */
    public Activity fatherActivity;
    /**
     * 当前视图
     */
    public View currentView;
    public DisplayMetrics displayMetrics = new DisplayMetrics();
    public int phoneWidth;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.fatherActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        fatherActivity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        phoneWidth = displayMetrics.widthPixels;
        phoneWidth = displayMetrics.widthPixels;
        currentView = setContentView(inflater, container,
                savedInstanceState);
        ButterKnife.bind(this, currentView);
        initView(currentView);
        initData();
        initListener();
        return currentView;
    }

    public abstract View setContentView(LayoutInflater inflater, ViewGroup container,
                                        Bundle savedInstanceState);

    public abstract void initView(View currentView);

    public abstract void initData();

    public abstract void initListener();
}
