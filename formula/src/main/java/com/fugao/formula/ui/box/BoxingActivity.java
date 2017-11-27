package com.fugao.formula.ui.box;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fugao.formula.MessageEvent;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.db.dao.MilkNameDao;
import com.fugao.formula.db.dao.TimeListDao;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.XmlDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class BoxingActivity extends BaseActivity {
    @BindView(R.id.tl)
    SegmentTabLayout tl;
    @BindView(R.id.tv_division)
    TextView division;
    @BindView(R.id.tv_all_sure)
    TextView tv_all_sure;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private CheckAdviceFragment1 fragment1;
    private BoxingFragment fragment2;
    private BoxingFragment1 fragment3;
    private String[] mTitles = {"医嘱核对", "今日装箱"};
    private String time;
    public MilkNameDao milkNameDao;
    private TimeListDao timeListDao;
    private String[] times = {"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private List<TimeEntity> mTimes;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_boxing);
    }

    @Override
    public void initView() {
        XmlDB.getInstance(BoxingActivity.this).saveKey("milkCode", "");
        timeListDao = new TimeListDao(DataBaseInfo.getInstance(BoxingActivity.this));
        XmlDB.getInstance(BoxingActivity.this).saveKey("time", "");
        mTimes = new ArrayList<>();
        timeListDao.deleteAllInfo();
        for (int i = 0; i < times.length; i++) {
            TimeEntity time = new TimeEntity();
            time.Name = times[i];
            time.State = "0";
            mTimes.add(time);
        }
        timeListDao.saveToTimeList(mTimes);
        //注册事件
        EventBus.getDefault().register(this);
        Constant.SELECT_PLACE="未核对";
    }

    @Override
    public void initData() {
        milkNameDao = new MilkNameDao(DataBaseInfo.getInstance(BoxingActivity.this));
        fragment1 = new CheckAdviceFragment1();
        mFragments.add(fragment1);
        fragment2 = new BoxingFragment();
        fragment3 = new BoxingFragment1();
        mFragments.add(fragment3);
        division.setVisibility(View.VISIBLE);
        division.setText(XmlDB.getInstance(BoxingActivity.this).getKeyStringValue("divisionName", ""));
        tl.setTabData(mTitles, this, R.id.fl_change, mFragments);
    }

    @Override
    public void initListener() {
        tv_all_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment1.checkYZ(tv_all_sure, "");
            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BoxingActivity.this, SelectActivity.class);
                startActivityForResult(intent, 501);
            }
        });
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                tl.setCurrentTab(i);
                if ("医嘱核对".equals(mTitles[i])) {
                    division.setVisibility(View.VISIBLE);
                    if (!StringUtils.StringIsEmpty(time) && !time.contains(";")) {
                        tv_all_sure.setVisibility(View.VISIBLE);
                    } else {
                        tv_all_sure.setVisibility(View.GONE);
                    }
                } else {
                    //今日装箱隐藏全部核对和病区两个按钮
                    division.setVisibility(View.GONE);
                    tv_all_sure.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselect(int i) {

            }
        });

    }

    @Override
    public void initIntent() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            BoxingActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //判断是否显示全部核对按钮,支持1-3个时间点
    public void showAllSure(boolean hasData) {
        if (hasData) {
            time = XmlDB.getInstance(BoxingActivity.this).getKeyString("time", "");
            if (!StringUtils.StringIsEmpty(time)) {
                String[] times = time.split(";");
                if (times.length <= 3) {
                    tv_all_sure.setVisibility(View.VISIBLE);
                } else {
                    tv_all_sure.setVisibility(View.GONE);
                }
            } else {
                tv_all_sure.setVisibility(View.GONE);
            }
        } else {
            tv_all_sure.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("yes".equals(XmlDB.getInstance(BoxingActivity.this).getKeyString("boxing", ""))) {
            fragment1.getData();
            XmlDB.getInstance(BoxingActivity.this).saveKey("boxing", "no");
        }
    }

    //扫描奶瓶
    @Override
    public void receiveMilkCode(String result) {
        super.receiveMilkCode(result);
        fragment1.checkYZ(tv_all_sure, result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 502:
                division.setText(XmlDB.getInstance(BoxingActivity.this).getKeyStringValue("divisionName", ""));
                fragment1.getData();
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent){
        if("refresh".equals(messageEvent.getMessage())){
            fragment1.refreshCheckFragment();
        }
    }
}
