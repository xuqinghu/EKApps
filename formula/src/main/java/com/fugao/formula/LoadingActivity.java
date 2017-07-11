package com.fugao.formula;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.fugao.formula.utils.XmlDB;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class LoadingActivity extends AppCompatActivity {
    private ImageView splash_loading_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        splash_loading_item = (ImageView) findViewById(R.id.splash_loading_item);
        initData();
        inintSettings();
    }

    private void initData() {
        Animation translate = AnimationUtils.loadAnimation(this,
                R.anim.splash_loading);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
                intent.setClass(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.splash_push_left_in,
                        R.anim.splash_push_left_out);
                LoadingActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_loading_item.setAnimation(translate);
    }

    private void inintSettings() {
        boolean firstCome = XmlDB.getInstance(this).getKeyBooleanValue("firstCome", true);
        if (firstCome) {
            XmlDB.getInstance(this).saveKey("ip", "192.168.50.177");
            XmlDB.getInstance(this).saveKey("port", "8002");
            XmlDB.getInstance(this).saveKey("firstCome", false);
        }

    }
}
