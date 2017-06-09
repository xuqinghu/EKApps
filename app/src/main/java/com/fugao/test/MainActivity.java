package com.fugao.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fugao.breast.ui.BreastActivity;
import com.fugao.breast.ui.put.PutList;
import com.fugao.breast.ui.thaw.ThawList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_main_breast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_main_breast = (TextView) findViewById(R.id.tv_main_breast);
        tv_main_breast.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
            case R.id.tv_main_breast:
                Intent intent1 = new Intent();
                intent1.putExtra("nurseCode", "6666");
                intent1.putExtra("nurseName", "高晓");
                intent1.putExtra("deptId", "1111");
                intent1.putExtra("wardId", "1230100");
                intent1.putExtra("wardName", "二病区");
                intent1.putExtra("ip", "192.168.10.125");
                intent1.putExtra("port", "9998");
                intent1.setClass(MainActivity.this, BreastActivity.class);
                startActivity(intent1);
                break;
        }

    }
}
