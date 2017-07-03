package com.fugao.formula.ui.box;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.entity.AdviceEntity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class AdviceDetailActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.tv_advice_detail_name)
    TextView name;
    @BindView(R.id.tv_advice_detail_pid)
    TextView pid;
    @BindView(R.id.tv_advice_detail_division)
    TextView division;
    @BindView(R.id.tv_advice_detail_roomNo)
    TextView roomNo;
    @BindView(R.id.tv_advice_detail_status)
    TextView status;
    @BindView(R.id.tv_advice_detail_bedNo)
    TextView bedNo;
    @BindView(R.id.tv_advice_detail_milkName)
    TextView milkName;
    @BindView(R.id.tv_advice_detail_doctor)
    TextView doctor;
    @BindView(R.id.tv_advice_detail_Frequency)
    TextView frequency;
    @BindView(R.id.tv_advice_detail_count)
    TextView count;
    @BindView(R.id.tv_advice_detail_reguTime)
    TextView reguTime;
    private AdviceEntity mData;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_advice_detail);
    }

    @Override
    public void initView() {
        name.setText("姓名：" + mData.PatName);
        pid.setText("住院号：" + mData.HosptNo);
        division.setText("病区：" + mData.WardName);
        roomNo.setText("房间号：" + mData.RoomNo);
        if ("0".equals(mData.AdviceStatus)) {
            status.setText("未核对");
            status.setTextColor(getResources().getColor(R.color.text_green));
        } else if ("1".equals(mData.AdviceStatus)) {
            status.setText("已核对");
            status.setTextColor(getResources().getColor(R.color.text_blue));
        } else {
            status.setText("已装箱");
        }
        bedNo.setText("床位号：" + mData.BedNo);
        if (mData.MilkName.length() > 10) {
            milkName.setText("奶名：" + mData.MilkName.substring(0, 10));
        } else {
            milkName.setText("奶名：" + mData.MilkName);
        }
        doctor.setText("所属医生：" + mData.DocCode);
        frequency.setText("频次：" + mData.Frequency);
        count.setText("瓶数：" + mData.Quantity);
        reguTime.setText("执行时间：" + mData.ReguTime);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        mData = (AdviceEntity) intent.getSerializableExtra("advice");
    }
}
