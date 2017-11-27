package com.fugao.formula.ui.signrReceive;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseActivity;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.db.dao.MilkNameDao;
import com.fugao.formula.db.dao.TimeListDao;
import com.fugao.formula.entity.AdviceEntity;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.XmlDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 签收界面（跟）
 * Created by Administrator on 2017/8/4 0004.
 */

public class SignReceiveActivity1 extends BaseActivity {
    private String[] mTitles = {"未签收", "已签收"};
    @BindView(R.id.tl)
    SegmentTabLayout tl;
    @BindView(R.id.division)
    TextView division;
    @BindView(R.id.tv_receive_select_milk)
    TextView tv_receive_select_milk;
    @BindView(R.id.tv_receive_name)
    TextView tv_receive_name;
    @BindView(R.id.tv_receive_personCount)
    TextView tv_receive_personCount;
    @BindView(R.id.tv_receive_milkCount)
    TextView tv_receive_milkCount;
    @BindView(R.id.tv_receive_boxNo)
    TextView tv_receive_boxNo;
    @BindView(R.id.tv_receive_bedNo)
    TextView tv_receive_bedNo;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private NotReceiveFragment fragment1;
    private ReceiveFragment fragment2;
    private int selectTab = 0;
    private List<MilkBean> milks;
    private MilkNameDao milkNameDao;
    private boolean[] isChecked;
    private boolean checkFlag = false;
    private String milk_code = "";
    private TimeListDao timeListDao;
    private String[] times = {"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
    private List<TimeEntity> mTimes;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sign_receive1);
    }

    @Override
    public void initView() {
        tv_receive_name.setText("签收人:" + XmlDB.getInstance(SignReceiveActivity1.this).getKeyString("userName", ""));
        String code = XmlDB.getInstance(SignReceiveActivity1.this).getKeyString("divisionCode", "");
        String name = XmlDB.getInstance(SignReceiveActivity1.this).getKeyString("divisionName", "");
        division.setText(name);
        XmlDB.getInstance(SignReceiveActivity1.this).saveKey("code", code);
        XmlDB.getInstance(SignReceiveActivity1.this).saveKey("name", name);
        //清空选择的奶名数据
        XmlDB.getInstance(SignReceiveActivity1.this).saveKey("milk", "");
        Constant.SELECT_MILK_NAME.clear();
        //清空选择的时间点
        XmlDB.getInstance(SignReceiveActivity1.this).saveKey("receiveTime", "");
        timeListDao = new TimeListDao(DataBaseInfo.getInstance(SignReceiveActivity1.this));
        mTimes = new ArrayList<>();
        timeListDao.deleteAllInfo();
        for (int i = 0; i < times.length; i++) {
            TimeEntity time = new TimeEntity();
            time.Name = times[i];
            time.State = "0";
            mTimes.add(time);
        }
        timeListDao.saveToTimeList(mTimes);
        tv_receive_boxNo.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 3.0f);
        lp.setMargins(2, 0, 0, 0);
        tv_receive_select_milk.setLayoutParams(lp);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        tv_receive_bedNo.setLayoutParams(lp1);
    }

    @Override
    public void initData() {
        milks = new ArrayList<>();
        milkNameDao = new MilkNameDao(DataBaseInfo.getInstance(SignReceiveActivity1.this));
        getMilkName();
        fragment1 = new NotReceiveFragment();
        fragment2 = new ReceiveFragment();
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        tl.setTabData(mTitles, this, R.id.fl_change, mFragments);
    }

    //获取所选病区的奶名
    private void getMilkName() {
        milks.clear();
        String divisionCode = XmlDB.getInstance(SignReceiveActivity1.this).getKeyString("code", "");
        milks = milkNameDao.getMilkNameByWardCode(divisionCode);
    }

    //选择奶名
    private void selectMilkName() {
        isChecked = new boolean[milks.size()];
        for (int i = 0; i < milks.size(); i++) {
            for (int j = 0; j < Constant.SELECT_MILK_NAME.size(); j++) {
                if (Constant.SELECT_MILK_NAME.get(j).MilkCode.equals(milks.get(i).MilkCode)) {
                    isChecked[i] = true;
                    checkFlag = true;
                }
            }
            if (checkFlag = false) {
                isChecked[i] = false;
            }
            checkFlag = false;
        }
        final String[] milkName = new String[milks.size()];
        final String[] milkCode = new String[milks.size()];
        final List<MilkBean> milkBeans = new ArrayList<>();
        milkBeans.addAll(Constant.SELECT_MILK_NAME);
        for (int i = 0; i < milks.size(); i++) {
            milkName[i] = milks.get(i).MilkName;
            milkCode[i] = milks.get(i).MilkCode;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(SignReceiveActivity1.this);
        builder.setIcon(R.mipmap.milk);
        builder.setTitle("选择奶名");
        builder.setMultiChoiceItems(milkName, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    MilkBean milkBean = new MilkBean();
                    milkBean.MilkName = milkName[which];
                    milkBean.MilkCode = milkCode[which];
                    milkBeans.add(milkBean);
                } else {
                    //移除这一项
                    for (int i = 0; i < milkBeans.size(); i++) {
                        if (milkCode[which].equals(milkBeans.get(i).MilkCode)) {
                            milkBeans.remove(i);
                            i--;
                        }
                    }
                }
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Constant.SELECT_MILK_NAME = milkBeans;
                milk_code = "";
                String milk_name = "";
                for (int i = 0; i < Constant.SELECT_MILK_NAME.size(); i++) {
                    milk_code = milk_code + Constant.SELECT_MILK_NAME.get(i).MilkCode + ";";
                    milk_name = milk_name + Constant.SELECT_MILK_NAME.get(i).MilkName + ";";
                }
                if (!StringUtils.StringIsEmpty(milk_code)) {
                    milk_code = milk_code.substring(0, milk_code.length() - 1);
                    milk_name = milk_name.substring(0, milk_name.length() - 1);
                    XmlDB.getInstance(SignReceiveActivity1.this).saveKey("milk", milk_code);
                } else {
                    XmlDB.getInstance(SignReceiveActivity1.this).saveKey("milk", "");
                }
                if ("未签收".equals(mTitles[selectTab])) {
                    fragment1.checkNetWork();
                } else {
                    fragment2.checkNetWork();
                }
                if ("".equals(milk_code)) {
                    tv_receive_select_milk.setText("奶名");
                } else {
                    tv_receive_select_milk.setText(milk_name);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void initListener() {
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tl.setCurrentTab(position);
                selectTab = position;
                if ("未签收".equals(mTitles[position])) {
                    fragment1.checkNetWork();
                    tv_receive_boxNo.setVisibility(View.GONE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 3.0f);
                    lp.setMargins(2, 0, 0, 0);
                    tv_receive_select_milk.setLayoutParams(lp);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    tv_receive_bedNo.setLayoutParams(lp1);
                } else {
                    fragment2.checkNetWork();
                    tv_receive_boxNo.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 2.0f);
                    lp.setMargins(2, 0, 0, 0);
                    tv_receive_select_milk.setLayoutParams(lp);
                    LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                    lp1.setMargins(2, 0, 0, 0);
                    tv_receive_bedNo.setLayoutParams(lp1);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SignReceiveActivity1.this, ReceiveSelectActivity.class);
                startActivityForResult(intent, 503);
            }
        });
        tv_receive_select_milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMilkName();
            }
        });

    }

    public void showCount(int selectTab, List<AdviceEntity> bean) {
        //1 未签收 2 已签收 3 没有数据或者获取数据失败或者服务器异常
        tv_receive_milkCount.setVisibility(View.GONE);
        if (selectTab == 3) {
            tv_receive_personCount.setText("医嘱条数:0");
        } else {
            tv_receive_personCount.setText("医嘱条数:" + bean.size());
        }
    }

    @Override
    public void initIntent() {

    }

    @Override
    public void receiveBoxCode(String result) {
        super.receiveBoxCode(result);
        fragment1.sureReceive(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 504:
                division.setText(XmlDB.getInstance(SignReceiveActivity1.this).getKeyStringValue("name", ""));
                String milk = XmlDB.getInstance(SignReceiveActivity1.this).getKeyString("milk", "");
                if (StringUtils.StringIsEmpty(milk)) {
                    tv_receive_select_milk.setText("奶名");
                    getMilkName();
                }
                if (selectTab == 0) {
                    fragment1.checkNetWork();
                } else {
                    fragment2.checkNetWork();
                }
                break;
            default:
                break;
        }
    }
}
