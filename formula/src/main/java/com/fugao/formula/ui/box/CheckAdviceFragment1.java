package com.fugao.formula.ui.box;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.fugao.formula.R;
import com.fugao.formula.base.BaseFragment;
import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.dao.MilkNameDao;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.entity.TabEntity;
import com.fugao.formula.utils.StringUtils;
import com.fugao.formula.utils.XmlDB;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 医嘱核对界面（未核对和已核对）
 * Created by Administrator on 2017/8/4 0004.
 */

public class CheckAdviceFragment1 extends BaseFragment {
    @BindView(R.id.tl1)
    CommonTabLayout tl;
    @BindView(R.id.btn_boxing)
    Button boxing;
    @BindView(R.id.tv_select_milk)
    TextView select_milk;
    @BindView(R.id.tv_check_advice_name)
    TextView name;
    @BindView(R.id.tv_check_advice_personCount)
    TextView personCount;
    @BindView(R.id.tv_check_advice_milkCount)
    TextView milkCount;
    private NotCheckFragment fragment1;
    private CheckFragment fragment2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"未核对", "已核对"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private BoxingActivity activity;
    private boolean[] isChecked;
    private boolean checkFlag = false;
    private String milk_code = "";
    private List<MilkBean> milks;
    private MilkNameDao milkNameDao;
    //未核对或者已核对
    private int selectTab = 0;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_check_advice1, container, false);
        return currentView;
    }

    @Override
    public void initView(View currentView) {
        activity = (BoxingActivity) fatherActivity;
        name.setText("核对人:" + XmlDB.getInstance(fatherActivity).getKeyString("userName", ""));
        personCount.setVisibility(View.VISIBLE);
        milkCount.setVisibility(View.GONE);
        Constant.SELECT_MILK_NAME.clear();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
    }

    @Override
    public void initData() {
        milks = new ArrayList<>();
        milkNameDao = activity.milkNameDao;
        milks = milkNameDao.getMilkNameByWardCode(XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", ""));
        fragment1 = new NotCheckFragment(personCount, milkCount, boxing);
        fragment2 = new CheckFragment(personCount, milkCount, boxing);
        mFragments.add(fragment1);
        mFragments.add(fragment2);
        tl.setTabData(mTabEntities, getActivity(), R.id.fl_change2, mFragments);
    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
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
                    XmlDB.getInstance(fatherActivity).saveKey("milkCode", milk_code);
                } else {
                    XmlDB.getInstance(fatherActivity).saveKey("milkCode", "");
                }
                if ("未核对".equals(mTitles[selectTab])) {
                    fragment1.checkNetWork();
                } else {
                    fragment2.checkNetWork();
                }
                if ("".equals(milk_code)) {
                    select_milk.setText("奶名");
                } else {
                    select_milk.setText(milk_name);
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

    //核对医嘱
    public void checkYZ(final TextView all_sure, String bottleId) {
        fragment1.checkYZ(all_sure, bottleId);
    }

    public void refreshCheckFragment(){
        fragment2.checkNetWork();
    }

    public void getData() {
        String milk = XmlDB.getInstance(fatherActivity).getKeyString("milkCode", "");
        if (StringUtils.StringIsEmpty(milk)) {
            select_milk.setText("奶名");
        }
        if (selectTab == 0) {
            fragment1.checkNetWork();
        } else {
            fragment2.checkNetWork();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        milks.clear();
        milks = milkNameDao.getMilkNameByWardCode(XmlDB.getInstance(fatherActivity).getKeyString("divisionCode", ""));
    }

    @Override
    public void initListener() {
        //选择奶名
        select_milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMilkName();
            }
        });
        //装箱
        boxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fatherActivity, SelectBoxActivity.class);
                intent.putExtra("count", XmlDB.getInstance(fatherActivity).getKeyString("checkCount", "0"));
                startActivity(intent);
                XmlDB.getInstance(fatherActivity).saveKey("boxing", "no");
            }
        });
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                selectTab = position;
                tl.setCurrentTab(position);
                if ("未核对".equals(mTitles[position])) {
                    fragment1.checkNetWork();
                    personCount.setVisibility(View.VISIBLE);
                    milkCount.setVisibility(View.GONE);
                    Constant.SELECT_PLACE="未核对";
                } else {
                    fragment2.checkNetWork();
                    personCount.setVisibility(View.GONE);
                    milkCount.setVisibility(View.VISIBLE);
                    Constant.SELECT_PLACE="已核对";
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }
}
