package com.fugao.formula;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.ui.box.BoxingActivity;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.ll_boxing)
    LinearLayout ll_boxing;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView() {
        ll_boxing.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initIntent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_boxing:
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, BoxingActivity.class);
                startActivity(intent1);
        }

    }
}
