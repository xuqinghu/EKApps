package com.fugao.breast.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fugao.breast.constant.Constant;

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
        initIntent();
        initView();
        initData();
        initListener();
        initScanBroadCast();
    }

    public abstract void setContentView();


    public abstract void initView();

    public abstract void initData();


    public abstract void initListener();

    public abstract void initIntent();

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
                if (result.contains("A") && result.length() == 9) {
                    receiverCode(result);
                } else {
                    receiverPlaceCode(result);
                }
            }

        }
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

    public void receiverCode(String result) {

    }

    public void receiverPlaceCode(String result) {

    }

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
