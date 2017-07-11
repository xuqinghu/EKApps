package com.fugao.formula.ui.box;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.entity.BoxListEntity;
import com.fugao.formula.entity.PersonEntity;
import com.fugao.formula.utils.RecyclerViewDivider;
import com.fugao.formula.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class PersonDetailActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.recycler_person_detail_advice)
    RecyclerView recyclerView;
    @BindView(R.id.tv_person_detail_code)
    TextView code;
    @BindView(R.id.tv_person_detail_count)
    TextView count;
    @BindView(R.id.tv_person_detail_size)
    TextView size;
    @BindView(R.id.tv_person_detail_division)
    TextView division;
    @BindView(R.id.tv_person_detail_binningPerson)
    TextView binningPerson;
    @BindView(R.id.tv_person_detail_binningDate)
    TextView binningDate;
    @BindView(R.id.tv_person_detail_deliveryPerson)
    TextView deliveryPerson;
    @BindView(R.id.tv_person_detail_deliveryDate)
    TextView deliveryDate;
    @BindView(R.id.tv_person_detail_acceptPerson)
    TextView acceptPerson;
    @BindView(R.id.tv_person_detail_acceptDate)
    TextView acceptDate;
    private PersonEntity personEntity;
    private PersonDetailAdviceAdapter adapter;
    private List<PersonEntity> mList;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_person_detail);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        code.setText("奶箱：" + personEntity.MilkBoxID);
        count.setText("奶瓶总数：" + personEntity.MilkQuantity);
        size.setText("规格：" + personEntity.MilkBoxSpec);
        if (!StringUtils.StringIsEmpty(personEntity.LoadTime)) {
            binningDate.setText("装箱时间：" + personEntity.LoadTime.substring(11, personEntity.LoadTime.length()));
        }
        division.setText("病区：" + personEntity.WardName);
        binningPerson.setText("装箱人：" + personEntity.LoadName);
        deliveryPerson.setText("配送员：" + StringUtils.getString(personEntity.TransGH));
        if (!StringUtils.StringIsEmpty(personEntity.TransTime)) {
            deliveryDate.setText("配送时间：" + personEntity.TransTime.substring(11, personEntity.LoadTime.length()));
        } else {
            deliveryDate.setText("配送时间：");
        }
        acceptPerson.setText("接收人：" + StringUtils.getString(personEntity.RevName));
        if (!StringUtils.StringIsEmpty(personEntity.RevTime)) {
            acceptDate.setText("接收时间：" + personEntity.RevTime.substring(11, personEntity.LoadTime.length()));
        } else {
            acceptDate.setText("接收时间：");
        }
        mList = new ArrayList<>();
        adapter = new PersonDetailAdviceAdapter(R.layout.activity_box_detail_item, mList);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(PersonDetailActivity.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(PersonDetailActivity.this));
        recyclerView.setAdapter(adapter);
        mList.add(personEntity);
        adapter.setNewData(mList);
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
        personEntity = new PersonEntity();
        personEntity = (PersonEntity) intent.getSerializableExtra("list");

    }
}
