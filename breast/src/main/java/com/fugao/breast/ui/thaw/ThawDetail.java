package com.fugao.breast.ui.thaw;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.db.dao.BreastDetailDao;
import com.fugao.breast.db.dao.BreastListDao;
import com.fugao.breast.entity.BreastInfo;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.Status;
import com.fugao.breast.entity.ThawListBean;
import com.fugao.breast.utils.StringUtils;
import com.fugao.breast.utils.ToastUtils;
import com.fugao.breast.utils.common.DateUtils;
import com.fugao.breast.utils.common.FloatUtil;
import com.fugao.breast.utils.common.RecyclerViewDivider;
import com.fugao.breast.utils.common.XmlDB;
import com.fugao.breast.utils.dialog.SingleBtnDialog;
import com.fugao.breast.utils.dialog.TwoBtnDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxq on 2017/5/22 0022.
 */

public class ThawDetail extends BaseActivity {
    private LinearLayout back;
    private RecyclerView recyclerView1, recyclerView2;
    private TextView dose, replenish, flag;
    private ThawDetailAdapter adapter1;
    private PatientAdapter adapter2;
    private BreastMilk breastMilk;
    private List<BreastMilk> breastMilkList;
    private List<BreastMilkDetial> breastMilkDetials;
    private TwoBtnDialog twoBtnDialog;
    private SingleBtnDialog singleBtnDialog;
    private boolean today = true;
    private DataBaseInfo dataBaseInfo;
    private BreastDetailDao breastDetailDao;
    private BreastListDao breastListDao;
    private String coorDinateID;
    private String milkBoxNo;
    private String remarks;
    private String twinsCode;
    private float fyzzl = 0;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_breast_detail);
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.ll_breast_detail_back);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycler_breast_detail);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_breast_patient);
        dose = (TextView) findViewById(R.id.tv_thaw_detail_dose);
        replenish = (TextView) findViewById(R.id.tv_thaw_detail_replenish);
        flag = (TextView) findViewById(R.id.tv_replenish_flag);
    }

    @Override
    public void initData() {
        breastMilkList = new ArrayList<>();
        singleBtnDialog = new SingleBtnDialog(ThawDetail.this);
        twoBtnDialog = new TwoBtnDialog(ThawDetail.this);
        dataBaseInfo = DataBaseInfo.getInstance(ThawDetail.this);
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        breastListDao = new BreastListDao(dataBaseInfo);
        breastMilkDetials = new ArrayList<>();
        if (StringUtils.StringIsEmpty(twinsCode)) {
            breastMilkDetials = breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getCurrentDate(), "2", breastMilk.Pid);
        } else {
            breastMilkDetials = breastDetailDao.getBreastMilkDetialByDateAndStateAndTwinsCode(DateUtils.getCurrentDate(), "2", twinsCode);
        }
        recyclerView2.setNestedScrollingEnabled(false);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter2 = new PatientAdapter(R.layout.patient_item, breastMilkList);
        recyclerView2.setAdapter(adapter2);
        initPatientInfo();
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView1.addItemDecoration(new RecyclerViewDivider(ThawDetail.this, LinearLayoutManager.HORIZONTAL));
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new ThawDetailAdapter(R.layout.breast_detail_item, breastMilkDetials);
        recyclerView1.setAdapter(adapter1);
        //全局设置已解冻奶量
        Constant.THAW_AMOUNT = getThawAmount(DateUtils.getCurrentDate());
        Constant.PUT_AMOUNT = Integer.parseInt(breastMilk.CFAmount);
        //设置最下方已解冻奶量
        updateAmountValue();
    }

    @Override
    public void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter1.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if (breastMilkDetials.get(position).State.equals("3")) {
                    if (breastMilkDetials.get(position).MilkBoxState.equals("2")) {
                        twoBtnDialog.setDialogCloseImageView(View.GONE);
                        twoBtnDialog.setDialogTitleTextView("温馨提示！");
                        twoBtnDialog.setDialogContentTextView("是否重新解冻" + breastMilkDetials.get(position).MilkBoxNo
                                + breastMilkDetials.get(position).Remarks + "处的奶");
                        twoBtnDialog.setLeftTextView("否");
                        twoBtnDialog.setRightTextView("是");
                        twoBtnDialog.setCanceledOnTouchOutside(false);
                        twoBtnDialog.setDialogBtnClickListener(new TwoBtnDialog.DialogBtnClickListener() {
                            @Override
                            public void leftBtnClick() {
                                twoBtnDialog.dismiss();
                            }

                            @Override
                            public void rightBtnClick() {
                                twoBtnDialog.dismiss();
                                coorDinateID = breastMilkDetials.get(position).CoorDinateID;
                                milkBoxNo = breastMilkDetials.get(position).MilkBoxNo;
                                remarks = breastMilkDetials.get(position).Remarks;
                                refreshData(
                                        breastMilkDetials.get(position).Amount,
                                        breastMilkDetials.get(position).QRcode
                                );
                                ToastUtils.showShort(ThawDetail.this, "重新解冻成功");
                            }
                        });
                        twoBtnDialog.show();
                    } else {
                        singleBtnDialog.setDialogCloseImageView(View.GONE);
                        singleBtnDialog.setDialogTitleTextView("温馨提示！");
                        singleBtnDialog.setDialogContentTextView("这个位置已经被释放掉了，不允许重新解冻");
                        singleBtnDialog.show();
                    }
                }
                return false;
            }
        });
        replenish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (today) {
                    today = false;
                    breastMilkDetials.clear();
                    breastMilkDetials = getReplenishData();
                    adapter1.setNewData(breastMilkDetials);
                    flag.setVisibility(View.VISIBLE);
                    flag.setText("现在是在备今日母乳");
                    replenish.setText("备明日");
                    Constant.THAW_AMOUNT = getThawAmount(DateUtils.getBeforeDate());
                    updateAmountValue();
                } else {
                    today = true;
                    breastMilkDetials.clear();
                    breastMilkDetials = getTodayData();
                    adapter1.setNewData(breastMilkDetials);
                    flag.setVisibility(View.GONE);
                    replenish.setText("备今日");
                    Constant.THAW_AMOUNT = getThawAmount(DateUtils.getCurrentDate());
                    updateAmountValue();
                }
            }
        });

    }

    //重新解冻数据
    private void refreshData(String amount, String qrCode) {
        BreastMilkDetial bDetail = new BreastMilkDetial();
        bDetail.QRcode = qrCode;
        bDetail.State = "2";
        bDetail.ThawDate = "";
        bDetail.ThawTime = "";
        bDetail.CFGH = XmlDB.getInstance(ThawDetail.this).getKeyString("nCode", "");
        bDetail.CFDate = DateUtils.getCurrentDate();
        bDetail.ThawGH = "";
        if (breastDetailDao.getState(bDetail).equals("1")) {
            breastDetailDao.updateData(bDetail, "0");
        } else {
            breastDetailDao.updateData(bDetail, "1");
        }
        updateList(amount, "refreshData");
    }

    //获取补充解冻数据
    private List<BreastMilkDetial> getReplenishData() {
        List<BreastMilkDetial> mDetails1 = new ArrayList<>();
        if (StringUtils.StringIsEmpty(twinsCode)) {
            mDetails1 = breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getBeforeDate(), "2", breastMilk.Pid);
        } else {
            mDetails1 = breastDetailDao.getBreastMilkDetialByDateAndStateAndTwinsCode(DateUtils.getBeforeDate(), "2", twinsCode);
        }
        return mDetails1;
    }

    //获取今日解冻数据
    private List<BreastMilkDetial> getTodayData() {
        List<BreastMilkDetial> milkDetials2 = new ArrayList<>();
        if (StringUtils.StringIsEmpty(twinsCode)) {
            milkDetials2 = breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getCurrentDate(), "2", breastMilk.Pid);
        } else {
            milkDetials2 = breastDetailDao.getBreastMilkDetialByDateAndStateAndTwinsCode(DateUtils.getCurrentDate(), "2", twinsCode);
        }
        return milkDetials2;
    }

    //获取解冻奶量
    private int getThawAmount(String date) {
        int amount = 0;
        List<BreastMilkDetial> mBeans = new ArrayList<>();
        if (StringUtils.StringIsEmpty(twinsCode)) {
            mBeans = breastDetailDao.getBreastMilkDetialByDate(date, breastMilk.Pid);
        } else {
            mBeans = breastDetailDao.getBreastMilkDetialByDateAndTwinsCode(date, twinsCode);
        }
        //获取解冻瓶数
        Constant.THAW_ACCOUNT = mBeans.size();
        for (BreastMilkDetial bean : mBeans) {
            amount = (int) (amount + Float.parseFloat(bean.Amount));
        }
        return amount;
    }


    private void initPatientInfo() {
        breastMilkList.clear();
        if (StringUtils.StringIsEmpty(twinsCode)) {
            breastMilkList.add(breastMilk);
        } else {
            List<BreastMilk> breastMilks = breastListDao.getBreastListByTwinsCode(twinsCode);
            if (breastMilks != null && breastMilks.size() > 0) {
                breastMilkList.addAll(breastMilks);
                for (BreastMilk bean : breastMilks) {
                    fyzzl = fyzzl + Float.parseFloat(bean.YZZL);
                }
            }

        }
        adapter2.setNewData(breastMilkList);

    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        breastMilk = new BreastMilk();
        breastMilk = (BreastMilk) intent.getSerializableExtra("thawlist");
        twinsCode = breastMilk.TwinsCode;
    }

    @Override
    public void receiverCode(String result) {
        super.receiverCode(result);
        boolean ishas = false;
        if ("0".equals(breastMilk.YZdosis)) {
            singleBtnDialog.setDialogCloseImageView(View.GONE);
            singleBtnDialog.setDialogTitleTextView("温馨提示！");
            singleBtnDialog.setDialogContentTextView("这个病人暂时没有母乳医嘱，不能进行解冻操作");
            singleBtnDialog.show();
        } else {
            for (int i = 0; i < breastMilkDetials.size(); i++) {
                if (result.equals(breastMilkDetials.get(i).QRcode)) {
                    ishas = true;
                    List<BreastMilkDetial> bDetails = new ArrayList<>();
                    bDetails = breastDetailDao.getBreastMilkDetialByPidAndQRcode(breastMilkDetials.get(i).Pid, breastMilkDetials.get(i).QRcode);
                    if (bDetails.size() > 0 && bDetails.get(0).State.equals("3")) {
                        singleBtnDialog.setDialogCloseImageView(View.GONE);
                        singleBtnDialog.setDialogTitleTextView("温馨提示！");
                        singleBtnDialog.setDialogContentTextView("这瓶奶已经被解冻过了");
                        singleBtnDialog.show();
                        break;
                    } else {
                        BreastMilkDetial breastMilkDetial = new BreastMilkDetial();
                        breastMilkDetial.QRcode = breastMilkDetials.get(i).QRcode;
                        breastMilkDetial.CFDate = breastMilkDetials.get(i).CFDate;
                        breastMilkDetial.CFGH = breastMilkDetials.get(i).CFGH;
                        breastMilkDetial.State = "3";
                        //补充解冻设置昨天的日期，今日解冻设置今天的日期
                        if (today) {
                            breastMilkDetial.ThawDate = DateUtils.getCurrentDate();
                        } else {
                            breastMilkDetial.ThawDate = DateUtils.getBeforeDate();
                        }
                        breastMilkDetial.ThawTime = DateUtils.getCurrentTime();
                        breastMilkDetial.MilkBoxState = "2";
                        breastMilkDetial.ThawGH = XmlDB.getInstance(ThawDetail.this).getKeyStringValue("nCode", "");
                        coorDinateID = breastMilkDetials.get(i).CoorDinateID;
                        milkBoxNo = breastMilkDetials.get(i).MilkBoxNo;
                        remarks = breastMilkDetials.get(i).Remarks;
                        saveData(breastMilkDetial, breastMilkDetials.get(i).Amount);
                        ToastUtils.showShort(ThawDetail.this, "解冻成功");
                        break;
                    }
                }
            }
            if (!ishas) {
                singleBtnDialog.setDialogCloseImageView(View.GONE);
                singleBtnDialog.setDialogTitleTextView("温馨提示！");
                singleBtnDialog.setDialogContentTextView("在列表中找不到这瓶奶");
                singleBtnDialog.show();
            }
        }
    }


    //保存解冻数据
    private void saveData(BreastMilkDetial bean, String str_Amount) {
        breastDetailDao.updateData(bean, "0");
        updateList(str_Amount, "saveData");
    }

    //更新界面
    private void updateList(String amount, String flag) {
        if ("refreshData".equals(flag)) {
            Constant.THAW_AMOUNT = (int) (Constant.THAW_AMOUNT - Float.parseFloat(amount));
            Constant.THAW_ACCOUNT = Constant.THAW_ACCOUNT - 1;
            Constant.PUT_AMOUNT = (int) (Constant.PUT_AMOUNT + Float.parseFloat(amount));
        } else {
            Constant.THAW_AMOUNT = (int) (Constant.THAW_AMOUNT + Float.parseFloat(amount));
            Constant.THAW_ACCOUNT = Constant.THAW_ACCOUNT + 1;
            Constant.PUT_AMOUNT = (int) (Constant.PUT_AMOUNT - Float.parseFloat(amount));
        }
        updateAmountValue();
        breastMilkDetials.clear();
        if (today) {
            breastMilkDetials = getTodayData();
        } else {
            breastMilkDetials = getReplenishData();
        }
        adapter1.setNewData(breastMilkDetials);
//        if (isRelease(breastMilkDetials, coorDinateID)) {
//            showReleaseDialog();
//        }
        BreastMilk bm = new BreastMilk();
        bm.ThawAccount = Constant.THAW_ACCOUNT + "";
        bm.ThawAmount = getThawAmount(DateUtils.getCurrentDate()) + "";
        bm.CFAmount = Constant.PUT_AMOUNT + "";
        if (StringUtils.StringIsEmpty(twinsCode)) {
            breastListDao.updateData(bm, breastMilk.Pid);
        } else {
            breastListDao.updateDataByTwinsCode(bm, twinsCode);
        }

    }

    //判断是否释放位置
    private boolean isRelease(List<BreastMilkDetial> beans, String coorDinateID) {
        boolean release = true;
        for (int i = 0; i < beans.size(); i++) {
            if (coorDinateID.equals(beans.get(i).CoorDinateID)) {
                if ("2".equals(beans.get(i).State)) {
                    release = false;
                    break;
                }
            }
        }
        return release;
    }

    //显示释放位置提示
    private void showReleaseDialog() {
        twoBtnDialog.setDialogCloseImageView(View.GONE);
        twoBtnDialog.setDialogTitleTextView("温馨提示！");
        twoBtnDialog.setDialogContentTextView("是否释放" + milkBoxNo + remarks + "这个位置");
        twoBtnDialog.setLeftTextView("否");
        twoBtnDialog.setRightTextView("是");
        twoBtnDialog.setCanceledOnTouchOutside(false);
        twoBtnDialog.setDialogBtnClickListener(new TwoBtnDialog.DialogBtnClickListener() {
            @Override
            public void leftBtnClick() {
                singleBtnDialog.dismiss();
            }

            @Override
            public void rightBtnClick() {
                breastDetailDao.updateMilkBoxState(coorDinateID, "1");
                singleBtnDialog.dismiss();
                ToastUtils.showShort(ThawDetail.this, "释放成功");
                breastMilkDetials.clear();
                if (today) {
                    breastMilkDetials = getTodayData();
                } else {
                    breastMilkDetials = getReplenishData();
                }
            }
        });
        twoBtnDialog.show();
    }


    //更新已解冻奶量
    private void updateAmountValue() {
        String yzzl = "";
        if (StringUtils.StringIsEmpty(twinsCode)) {
            yzzl = breastMilk.YZZL;
        } else {
            yzzl = fyzzl + "";
        }
        if (Constant.THAW_AMOUNT >= Float.parseFloat(yzzl) && today) {
            dose.setText(Constant.THAW_AMOUNT + "ml，达到剂量标准！");
        } else {
            dose.setText(Constant.THAW_AMOUNT + "ml");
        }

    }

}
