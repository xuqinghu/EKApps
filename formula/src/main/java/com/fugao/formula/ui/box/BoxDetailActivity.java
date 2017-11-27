package com.fugao.formula.ui.box;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class BoxDetailActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.recycler_box_detail_advice)
    RecyclerView recyclerView;
    @BindView(R.id.tv_box_detail_code)
    TextView code;
    @BindView(R.id.tv_box_detail_count)
    TextView count;
    @BindView(R.id.tv_box_detail_size)
    TextView size;
    @BindView(R.id.tv_box_detail_division)
    TextView division;
    @BindView(R.id.tv_box_detail_binningPerson)
    TextView binningPerson;
    @BindView(R.id.tv_box_detail_binningDate)
    TextView binningDate;
    @BindView(R.id.tv_box_detail_deliveryPerson)
    TextView deliveryPerson;
    @BindView(R.id.tv_box_detail_deliveryDate)
    TextView deliveryDate;
    @BindView(R.id.tv_box_detail_acceptPerson)
    TextView acceptPerson;
    @BindView(R.id.tv_box_detail_acceptDate)
    TextView acceptDate;
    private BoxListEntity boxListEntity;
    //    private BoxDetaiAdviceAdapter adapter;
    private BoxDetailAdviceAdapter1 adapter;
    private List<AdviceEntity> mList;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_box_detail);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        if ("0".equals(boxListEntity.MilkBoxID)) {
            code.setText("备用奶");
            count.setText("奶瓶总数：" + boxListEntity.MilkQuantity + "/" + boxListEntity.MilkJL);
        } else {
            code.setText("奶箱：" + boxListEntity.MilkBoxID);
            count.setText("奶瓶总数：" + boxListEntity.MilkQuantity);
        }
        size.setText("规格：" + boxListEntity.MilkBoxSpec);
        if (!StringUtils.StringIsEmpty(boxListEntity.LoadTime)) {
            binningDate.setText("装箱时间：" + boxListEntity.LoadTime.substring(11, boxListEntity.LoadTime.length()));
        }
        division.setText("病区：" + boxListEntity.WardName);
        binningPerson.setText("装箱人：" + boxListEntity.LoadName);
        deliveryPerson.setText("配送员：" + StringUtils.getString(boxListEntity.TransGH));
        if (!StringUtils.StringIsEmpty(boxListEntity.TransTime)) {
            deliveryDate.setText("配送时间：" + boxListEntity.TransTime.substring(11, boxListEntity.LoadTime.length()));
        } else {
            deliveryDate.setText("配送时间：");
        }
        acceptPerson.setText("接收人：" + StringUtils.getString(boxListEntity.RevName));
        if (!StringUtils.StringIsEmpty(boxListEntity.RevTime)) {
            acceptDate.setText("接收时间：" + boxListEntity.RevTime.substring(11, boxListEntity.LoadTime.length()));
        } else {
            acceptDate.setText("接收时间：");
        }
        mList = new ArrayList<>();
        adapter = new BoxDetailAdviceAdapter1(BoxDetailActivity.this, mList);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(BoxDetailActivity.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(BoxDetailActivity.this));
        recyclerView.setAdapter(adapter);
        if (boxListEntity.MilkDetail != null && boxListEntity.MilkDetail.size() > 0) {
            mList = boxListEntity.MilkDetail;
            adapter.setData(mList, boxListEntity.MilkBoxStatus, boxListEntity.HZID, boxListEntity.MilkBoxID);
        }
    }

    @Override
    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.CANCEL_BOXING) {
                    setResult(102);
                }
                finish();
            }
        });

    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        boxListEntity = new BoxListEntity();
        boxListEntity = (BoxListEntity) intent.getSerializableExtra("boxlist");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (Constant.CANCEL_BOXING) {
                setResult(102);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
