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
    private RecyclerView recyclerView;
    private TextView dose, name, patId, bedNo, roomNo, adviceSize, adviceFq, totalAmount, replenish, flag, division;
    private ThawDetailAdapter adapter;
    private BreastMilk breastMilk;
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

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_breast_detail);
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.ll_breast_detail_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_breast_detail);
        dose = (TextView) findViewById(R.id.tv_thaw_detail_dose);
        name = (TextView) findViewById(R.id.tv_thaw_detail_name);
        patId = (TextView) findViewById(R.id.tv_thaw_detail_patId);
        bedNo = (TextView) findViewById(R.id.tv_thaw_detail_bedNo);
        roomNo = (TextView) findViewById(R.id.tv_thaw_detail_roomNo);
        adviceSize = (TextView) findViewById(R.id.tv_thaw_detail_adviceSize);
        adviceFq = (TextView) findViewById(R.id.tv_thaw_detail_adviceFq);
        totalAmount = (TextView) findViewById(R.id.tv_thaw_detail_totalAmount);
        replenish = (TextView) findViewById(R.id.tv_thaw_detail_replenish);
        flag = (TextView) findViewById(R.id.tv_replenish_flag);
        division = (TextView) findViewById(R.id.tv_thaw_detail_division);
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(ThawDetail.this);
        twoBtnDialog = new TwoBtnDialog(ThawDetail.this);
        dataBaseInfo = DataBaseInfo.getInstance(ThawDetail.this);
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        breastListDao = new BreastListDao(dataBaseInfo);
        breastMilkDetials = new ArrayList<>();
        breastMilkDetials = breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getCurrentDate(), "2", breastMilk.Pid);
        initPatientInfo();
        recyclerView.addItemDecoration(new RecyclerViewDivider(ThawDetail.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThawDetailAdapter(R.layout.breast_detail_item, breastMilkDetials);
        recyclerView.setAdapter(adapter);
        //全局设置已解冻奶量
        Constant.THAW_AMOUNT = getThawAmount(DateUtils.getCurrentDate());
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
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
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
                    adapter.setNewData(breastMilkDetials);
                    flag.setVisibility(View.VISIBLE);
                    flag.setText("正在进行补充解冻");
                    replenish.setText("今日解冻");
                    Constant.THAW_AMOUNT = getThawAmount(DateUtils.getBeforeDate());
                    updateAmountValue();
                } else {
                    today = true;
                    breastMilkDetials.clear();
                    breastMilkDetials = getTodayData();
                    adapter.setNewData(breastMilkDetials);
                    flag.setVisibility(View.GONE);
                    replenish.setText("补充解冻");
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
        bDetail.ThawGH = XmlDB.getInstance(ThawDetail.this).getKeyStringValue("nCode", "");
        if (breastDetailDao.getState(bDetail, breastMilk.Pid).equals("1")) {
            breastDetailDao.updateData(bDetail, "0", breastMilk.Pid);
        } else {
            breastDetailDao.updateData(bDetail, "1", breastMilk.Pid);
        }
        updateList(amount, "refreshData");
    }

    //获取补充解冻数据
    private List<BreastMilkDetial> getReplenishData() {
        return breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getBeforeDate(), "2", breastMilk.Pid);
    }

    //获取今日解冻数据
    private List<BreastMilkDetial> getTodayData() {
        return breastDetailDao.getBreastMilkDetialByDateAndState(DateUtils.getCurrentDate(), "2", breastMilk.Pid);
    }

    //获取解冻奶量
    private int getThawAmount(String date) {
        int amount = 0;
        List<BreastMilkDetial> beans = new ArrayList<>();
        beans = breastDetailDao.getBreastMilkDetialByDate(date, breastMilk.Pid);
        //获取解冻瓶数
        Constant.THAW_ACCOUNT = beans.size();
        for (BreastMilkDetial bean : beans) {
            amount = (int) (amount + Float.parseFloat(bean.Amount));
        }
        return amount;
    }


    private void initPatientInfo() {
        name.setText("姓名:" + breastMilk.Name);
        patId.setText("住院号:" + breastMilk.Pid);
        bedNo.setText("床号:" + breastMilk.BedNo);
        roomNo.setText("房间号:" + breastMilk.RoomNo);
        division.setText("病区:" + breastMilk.WardName);
        adviceSize.setText("医嘱剂量:" + FloatUtil.moveZero(breastMilk.YZdosis) + "ml");
        adviceFq.setText("医嘱频次:" + breastMilk.YZTime);
        totalAmount.setText("总量:" + breastMilk.YZZL + "ml");
    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        breastMilk = new BreastMilk();
        breastMilk = (BreastMilk) intent.getSerializableExtra("thawlist");
    }

    @Override
    public void receiverCode(String result) {
        super.receiverCode(result);
        boolean ishas = false;
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


    //保存解冻数据
    private void saveData(BreastMilkDetial bean, String str_Amount) {
        breastDetailDao.updateData(bean, "0", breastMilk.Pid);
        updateList(str_Amount, "saveData");
    }

    //更新界面
    private void updateList(String amount, String flag) {
        if ("refreshData".equals(flag)) {
            Constant.THAW_AMOUNT = (int) (Constant.THAW_AMOUNT - Float.parseFloat(amount));
            Constant.THAW_ACCOUNT = Constant.THAW_ACCOUNT - 1;
        } else {
            Constant.THAW_AMOUNT = (int) (Constant.THAW_AMOUNT + Float.parseFloat(amount));
            Constant.THAW_ACCOUNT = Constant.THAW_ACCOUNT + 1;
        }
        updateAmountValue();
        breastMilkDetials.clear();
        if (today) {
            breastMilkDetials = getTodayData();
        } else {
            breastMilkDetials = getReplenishData();
        }
        adapter.setNewData(breastMilkDetials);
        if (isRelease(breastMilkDetials, coorDinateID)) {
            showReleaseDialog();
        }
        BreastMilk bm = new BreastMilk();
        bm.ThawAccount = Constant.THAW_ACCOUNT + "";
        bm.ThawAmount = Constant.THAW_AMOUNT + "";
        if (today) {
            breastListDao.updateData(bm, breastMilk.Pid);
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
        if (Constant.THAW_AMOUNT >= Float.parseFloat(breastMilk.YZZL) && today) {
            dose.setText(Constant.THAW_AMOUNT + "ml，达到剂量标准！");
        } else {
            dose.setText(Constant.THAW_AMOUNT + "ml");
        }

    }

}
