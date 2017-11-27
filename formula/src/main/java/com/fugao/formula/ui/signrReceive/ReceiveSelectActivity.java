package com.fugao.formula.ui.signrReceive;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.FormulaApplication;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.db.dao.TimeListDao;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.entity.WardBean;
import com.fugao.formula.ui.box.DivisionAdapter;
import com.fugao.formula.ui.box.TimeAdapter;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.ListDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReceiveSelectActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.tv_receive_division)
    TextView tv_receive_division;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.btn_receive_sure)
    Button btn_receive_sure;
    private DataBaseInfo dataBaseInfo;
    private TimeListDao timeListDao;
    private List<TimeEntity> mTimes;
    private String[] times = {"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private ListDialog listDialog;
    private DivisionAdapter adapter1;
    private TimeAdapter adapter2;
    private FormulaApplication application;
    private List<WardBean> mList;
    private String divisionName = "";
    private String divisionCode = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_receive_select);
    }

    @Override
    public void initView() {
        tv_receive_division.setText(XmlDB.getInstance(ReceiveSelectActivity.this).getKeyStringValue("name", ""));
    }

    @Override
    public void initData() {
        application = (FormulaApplication) getApplication();
        mList = application.getDivisionList();
        listDialog = new ListDialog(ReceiveSelectActivity.this);
        mTimes = new ArrayList<>();
        dataBaseInfo = DataBaseInfo.getInstance(ReceiveSelectActivity.this);
        timeListDao = new TimeListDao(dataBaseInfo);
        adapter1 = new DivisionAdapter(R.layout.division_list_item, ReceiveSelectActivity.this);
        recycler.setLayoutManager(new GridLayoutManager(this, 5));
        adapter2 = new TimeAdapter(R.layout.time_item, mTimes);
        recycler.setAdapter(adapter2);
        setData();
    }

    //选择所属病区
    private void selectDivision() {
        adapter1.setDatas(mList);
        listDialog.setAdapter(adapter1);
        listDialog.setTitleTextView("选择病区");
        listDialog.setDataReturnListener(new ListDialog.RequestReturnListener<WardBean>() {
            @Override
            public void returnResult(WardBean result) {
                tv_receive_division.setText(result.BQMC);
                divisionName = result.BQMC;
                divisionCode = result.BQDM;
            }
        });
        listDialog.show();
    }

    private void getSelectTime() {
        mTimes.clear();
        mTimes = timeListDao.getTimeList();
    }

    private void setData() {
        getSelectTime();
        adapter2.setNewData(mTimes);
    }

    @Override
    public void initListener() {
        btn_receive_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListDao.updateData(mTimes);
                List<TimeEntity> timeList = timeListDao.getTimeListByState("1");
                if (timeList != null && timeList.size() > 0) {
                    String time = "";
                    for (TimeEntity bean : timeList) {
                        time = time + bean.Name + ";";
                    }
                    time = time.substring(0, time.length() - 1);
                    XmlDB.getInstance(ReceiveSelectActivity.this).saveKey("receiveTime", time);
                } else {
                    XmlDB.getInstance(ReceiveSelectActivity.this).saveKey("receiveTime", "");
                }
                if (!StringUtils.StringIsEmpty(divisionName)) {
                    XmlDB.getInstance(ReceiveSelectActivity.this).saveKey("name", divisionName);
                    XmlDB.getInstance(ReceiveSelectActivity.this).saveKey("code", divisionCode);
                    //切换病区时需要清空奶名数据，因为每个病区的奶名是不一样的
                    XmlDB.getInstance(ReceiveSelectActivity.this).saveKey("milk", "");
                    Constant.SELECT_MILK_NAME.clear();
                }
                setResult(504);
                finish();
            }
        });
        tv_receive_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDivision();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("0".equals(mTimes.get(position).State)) {
                    mTimes.get(position).State = "1";
                } else {
                    mTimes.get(position).State = "0";
                }
                adapter2.setNewData(mTimes);
            }
        });

    }

    @Override
    public void initIntent() {

    }
}
