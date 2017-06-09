package com.fugao.formula.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by huxq on 2017/5/21 0021.
 * 模板Activity
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
        initIntent();
        initView();
        initData();
        initListener();
    }

    public abstract void setContentView();


    public abstract void initView();

    public abstract void initData();


    public abstract void initListener();

    public abstract void initIntent();

    /**
     * 通过Class跳转界面
     */
    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
