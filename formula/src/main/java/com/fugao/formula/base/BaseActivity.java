package com.fugao.formula.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fugao.formula.constant.Constant;
import com.fugao.formula.utils.StringUtils;

import butterknife.ButterKnife;

/**
 * Created by huxq on 2017/5/21 0021.
 * 模板Activity
 */

public abstract class BaseActivity extends AppCompatActivity {
    public Context context;
    private ReceivePDAScan receivePDAScan;
    private IntentFilter filter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
        initIntent();
        initView();
        initData();
        initListener();
        initScanBroadCast();
    }

    public void initScanBroadCast() {
        receivePDAScan = new ReceivePDAScan();
        filter = new IntentFilter();
        filter.addAction(Constant.LACH_SIS);
        registerReceiver(receivePDAScan, filter);
    }

    public class ReceivePDAScan extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receiver_action = intent.getAction();
            if (receiver_action.equals(Constant.LACH_SIS)) {
                String result = intent.getStringExtra
                        ("lachesis_barcode_value_notice_broadcast_data_string");
                if (!StringUtils.StringIsEmpty(result) && result.contains("PF")) {
                    receiveBoxCode(result);
                } else if (!StringUtils.StringIsEmpty(result) && result.contains("S")) {
                    receiveMilkCode(result);
                } else if (!StringUtils.StringIsEmpty(result) && result.contains("BY")) {
                    receiveBoxCode(result);
                } else {
                    receivePersonCode(result);
                }

            }

        }
    }

    //扫描箱子
    public void receiveBoxCode(String result) {

    }

    //扫描奶瓶
    public void receiveMilkCode(String result) {

    }

    public void receivePersonCode(String result) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receivePDAScan);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receivePDAScan, filter);
    }

    public abstract void setContentView();


    public abstract void initView();

    public abstract void initData();


    public abstract void initListener();

    public abstract void initIntent();


}
