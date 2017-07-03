package com.fugao.formula.ui.box;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.formula.FormulaApplication;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.db.dao.TimeListDao;
import com.fugao.formula.entity.DivisionEntity;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.entity.WardBean;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.FileHelper;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.XmlDB;
import com.fugao.formula.utils.dialog.ListDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 条件选择
 * Created by Administrator on 2017/6/23 0023.
 */

public class SelectActivity extends BaseActivity {
    @BindView(R.id.ll_back)
    LinearLayout back;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_select_division)
    TextView division;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    private DataBaseInfo dataBaseInfo;
    private TimeListDao timeListDao;
    private TimeAdapter adapter2;
    private List<TimeEntity> mTimes;
    private String[] times = {"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private ListDialog listDialog;
    private DivisionAdapter adapter1;
    private FormulaApplication application;
    private List<WardBean> mList;
    private String divisionName = "";
    private String divisionCode = "";

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select);
    }

    @Override
    public void initView() {
        division.setText(XmlDB.getInstance(SelectActivity.this).getKeyStringValue("divisionName", ""));
    }

    @Override
    public void initData() {
        application = (FormulaApplication) getApplication();
        mList = application.getDivisionList();
        listDialog = new ListDialog(SelectActivity.this);
        mTimes = new ArrayList<>();
        dataBaseInfo = DataBaseInfo.getInstance(SelectActivity.this);
        timeListDao = new TimeListDao(dataBaseInfo);
        adapter1 = new DivisionAdapter(R.layout.division_list_item, SelectActivity.this);
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
                division.setText(result.BQMC);
                divisionName = result.BQMC;
                divisionCode = result.BQDM;
            }
        });
        listDialog.show();
    }

    private void getSelectTime() {
        timeListDao.deleteAllInfo();
        for (int i = 0; i < times.length; i++) {
            TimeEntity time = new TimeEntity();
            time.Name = times[i];
            time.State = "0";
            mTimes.add(time);
        }
        timeListDao.saveToTimeList(mTimes);
    }

    private void getSelectTimeByDb() {
        mTimes.clear();
        mTimes = timeListDao.getTimeList();
    }

    private void setData() {
        String state = XmlDB.getInstance(SelectActivity.this).getKeyString("login", "");
        if ("yes".equals(state)) {
            getSelectTime();
            XmlDB.getInstance(SelectActivity.this).saveKey("login", "no");
        } else {
            getSelectTimeByDb();
        }
        adapter2.setNewData(mTimes);
    }

    @Override
    public void initListener() {
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeListDao.updateData(mTimes);
                String time = "";
                List<TimeEntity> timeList = timeListDao.getTimeListByState("1");
                if (timeList != null && timeList.size() > 0) {
                    for (TimeEntity bean : timeList) {
                        time = time + bean.Name + ";";
                    }
                    time = time.substring(0, time.length() - 1);
                    XmlDB.getInstance(SelectActivity.this).saveKey("time", time);
                } else {
                    XmlDB.getInstance(SelectActivity.this).saveKey("time", "");
                }
                if (!StringUtils.StringIsEmpty(divisionName)) {
                    XmlDB.getInstance(SelectActivity.this).saveKey("divisionName", divisionName);
                    XmlDB.getInstance(SelectActivity.this).saveKey("divisionCode", divisionCode);
                }
                setResult(502);
                finish();
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
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
