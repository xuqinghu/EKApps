package com.fugao.breast.ui.put;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.db.dao.BreastDetailDao;
import com.fugao.breast.db.dao.BreastRegistDao;
import com.fugao.breast.db.dao.PutListDao;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.BreastRegist;
import com.fugao.breast.entity.PutBreastMilk;
import com.fugao.breast.ui.BreastActivity;
import com.fugao.breast.ui.thaw.ThawDetail;
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

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class PutDetail extends BaseActivity {
    private LinearLayout back;
    private TextView place, name, pid, bedNo, roomNo, count, amount, transform;
    private PutDetailAdapter adapter;
    private RecyclerView recyclerView;
    private DataBaseInfo dataBaseInfo;
    private BreastDetailDao breastDetailDao;
    private PutListDao putListDao;
    private BreastRegistDao breastRegistDao;
    private List<BreastMilkDetial> breastMilkDetials;
    private PutBreastMilk putBreastMilk;
    private SingleBtnDialog singleBtnDialog;
    private TwoBtnDialog twoBtnDialog;
    private List<BreastRegist> breastRegists;
    private String coorDinateID;
    private boolean isTransform = false;
    private List<PutBreastMilk> putBeans;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_put_detail);
        Constant.PUT_AMOUNT = 0;
        Constant.THAW_ACCOUNT = 0;
    }

    @Override
    public void initView() {
        back = (LinearLayout) findViewById(R.id.ll_put_detail_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_put_detail);
        place = (TextView) findViewById(R.id.tv_put_detail_place);
        name = (TextView) findViewById(R.id.tv_put_detail_name);
        pid = (TextView) findViewById(R.id.tv_put_detail_pid);
        bedNo = (TextView) findViewById(R.id.tv_put_detail_bedNo);
        roomNo = (TextView) findViewById(R.id.tv_put_detail_roomNo);
        count = (TextView) findViewById(R.id.tv_put_detail_count);
        amount = (TextView) findViewById(R.id.tv_put_detail_amount);
        transform = (TextView) findViewById(R.id.tv_put_detail_transform);
        setPatientInfo();
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(PutDetail.this);
        twoBtnDialog = new TwoBtnDialog(PutDetail.this);
        dataBaseInfo = DataBaseInfo.getInstance(PutDetail.this);
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        putListDao = new PutListDao(dataBaseInfo);
        putBeans = new ArrayList<>();
        putBeans = putListDao.getPutList();
        breastRegistDao = new BreastRegistDao(dataBaseInfo);
        breastMilkDetials = new ArrayList<>();
        breastMilkDetials = breastDetailDao.getPutDetailByCoorDinateID(putBreastMilk.CoorDinateID, "2");
        //存放瓶数
        Constant.PUT_ACCOUNT = (int) Float.parseFloat(putBreastMilk.CFAccount);
        //存放奶量
        Constant.PUT_AMOUNT = (int) Float.parseFloat(putBreastMilk.CFAmount);
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(PutDetail.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PutDetailAdapter(R.layout.put_detail_item, breastMilkDetials);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void initListener() {
        //返回点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        transform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("转换位置".equals(transform.getText().toString())) {
                    transform.setText("取消转换位置");
                    isTransform = true;
                } else {
                    transform.setText("转换位置");
                    isTransform = false;
                }

            }
        });

    }

    @Override
    public void initIntent() {
        Intent intent = getIntent();
        putBreastMilk = new PutBreastMilk();
        putBreastMilk = (PutBreastMilk) intent.getSerializableExtra("putlist");
    }

    private void setPatientInfo() {
        place.setText(putBreastMilk.MilkBoxNo + putBreastMilk.Remarks);
        name.setText("姓名:" + putBreastMilk.Name);
        pid.setText("住院号:" + putBreastMilk.Pid);
        bedNo.setText("床位号:" + putBreastMilk.BedNo);
        roomNo.setText("房间号:" + putBreastMilk.RoomNo);
        count.setText("存放数量:" + putBreastMilk.CFAccount);
        amount.setText("存放奶量:" + FloatUtil.moveZero(putBreastMilk.CFAmount) + "ml");
    }

    private void updatePatientInfo(BreastRegist breastRegist) {
        name.setText("姓名:" + breastRegist.Name);
        pid.setText("住院号:" + breastRegist.Pid);
        bedNo.setText("床位号:" + breastRegist.BedNo);
        roomNo.setText("房间号:" + breastRegist.RoomNo);
        count.setText("存放数量:" + Constant.PUT_ACCOUNT);
        amount.setText("存放奶量:" + Constant.PUT_AMOUNT + "ml");
    }

    @Override
    public void receiverPlaceCode(String result) {

        for (final PutBreastMilk bean : putBeans) {
            if (bean.CoorDinateID.equals(result) && !bean.CoorDinateID.equals(putBreastMilk.CoorDinateID)) {
                if (isTransform) {
                    if (StringUtils.StringIsEmpty(bean.Name) || bean.Name.equals(putBreastMilk.Name)) {
                        twoBtnDialog.setDialogCloseImageView(View.GONE);
                        twoBtnDialog.setDialogTitleTextView("温馨提示！");
                        twoBtnDialog.setDialogContentTextView("是否将这个位置的奶全部转移到" + bean.MilkBoxNo + bean.Remarks
                                + "处");
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
                                name.setText("姓名:");
                                pid.setText("住院号:");
                                bedNo.setText("床位号:");
                                roomNo.setText("房间号:");
                                count.setText("存放数量:0");
                                amount.setText("存放奶量:0ml");
                                breastMilkDetials.clear();
                                //更新这个格子的基本信息
                                PutBreastMilk pbean3 = new PutBreastMilk();
                                pbean3.CFAccount = "0";
                                pbean3.CFAmount = "0.00";
                                pbean3.BedNo = "";
                                pbean3.Name = "";
                                pbean3.Pid = "";
                                pbean3.RoomNo = "";
                                pbean3.MilkBoxState = "1";
                                putListDao.updateData(pbean3, putBreastMilk.CoorDinateID);
                                //更新转换格子的信息
                                PutBreastMilk pbean4 = new PutBreastMilk();
                                pbean4.CFAccount = FloatUtil.moveZero(Float.parseFloat(putBreastMilk.CFAccount) + Float.parseFloat(bean.CFAccount) + "");
                                pbean4.CFAmount = FloatUtil.moveZero(Float.parseFloat(putBreastMilk.CFAmount) + Float.parseFloat(bean.CFAmount) + "");
                                pbean4.BedNo = putBreastMilk.BedNo;
                                pbean4.Name = putBreastMilk.Name;
                                pbean4.Pid = putBreastMilk.Pid;
                                pbean4.RoomNo = putBreastMilk.RoomNo;
                                pbean4.MilkBoxState = "2";
                                putListDao.updateData(pbean4, bean.CoorDinateID);
                                List<BreastMilkDetial> allData = new ArrayList<>();
                                //获取这个格子里面所有奶的信息
                                allData = breastDetailDao.getPutDetailByCoorDinateID(putBreastMilk.CoorDinateID, "2");
                                //将数据插入转换的格子
                                breastDetailDao.insertBreastMilkByCoorDinateID(putBreastMilk.Pid, allData, bean);
                                //删除这个格子里面的所有奶
                                breastDetailDao.deleteDetailByCoorDinateID(putBreastMilk.CoorDinateID);
                                adapter.setNewData(breastMilkDetials);
                                ToastUtils.showShort(PutDetail.this, "转移成功");
                            }
                        });
                        twoBtnDialog.show();
                    } else {
                        singleBtnDialog.setDialogCloseImageView(View.GONE);
                        singleBtnDialog.setDialogTitleTextView("温馨提示！");
                        singleBtnDialog.setDialogContentTextView("一个位置不能存两个病人的奶，不能转移");
                        singleBtnDialog.show();
                    }
                } else {
                    putBreastMilk = bean;
                    setPatientInfo();
                    //存放瓶数
                    Constant.PUT_ACCOUNT = (int) Float.parseFloat(putBreastMilk.CFAccount);
                    //存放奶量
                    Constant.PUT_AMOUNT = (int) Float.parseFloat(putBreastMilk.CFAmount);
                    breastMilkDetials.clear();
                    breastMilkDetials = breastDetailDao.getPutDetailByCoorDinateID(putBreastMilk.CoorDinateID, "2");
                    adapter.setNewData(breastMilkDetials);
                }
                break;
            } else if (bean.CoorDinateID.equals(result) && bean.CoorDinateID.equals(putBreastMilk.CoorDinateID)) {
                singleBtnDialog.setDialogCloseImageView(View.GONE);
                singleBtnDialog.setDialogTitleTextView("温馨提示！");
                singleBtnDialog.setDialogContentTextView("已经处于当前冰箱分格");
                singleBtnDialog.show();
            }
        }
    }

    @Override
    public void receiverCode(final String result) {
        super.receiverCode(result);
        if (isTransform) {
            singleBtnDialog.setDialogCloseImageView(View.GONE);
            singleBtnDialog.setDialogTitleTextView("温馨提示！");
            singleBtnDialog.setDialogContentTextView("正在进行转换位置操作，只能扫描位置二维码");
            singleBtnDialog.show();
        } else {
            breastRegists = new ArrayList<>();
            breastRegists = breastRegistDao.getBreastMilkDetialByQRCode(result);
            List<PutBreastMilk> putMilks = new ArrayList<>();
            putMilks = putListDao.getPutListByCoorDinateID(putBreastMilk.CoorDinateID);
            String pid = "";
            if (putMilks.size() > 0) {
                pid = putMilks.get(0).Pid;
            }
            if (breastRegists.size() > 0) {
                if (breastRegists.get(0).Pid.equals(pid) || "".equals(pid)) {
                    //检测这瓶奶是否已经被存放了
                    List<BreastMilkDetial> bmdBeans = breastDetailDao.
                            getBreastMilkDetialByQRcode(result);
                    if (bmdBeans != null && bmdBeans.size() > 0) {
                        coorDinateID = bmdBeans.get(0).CoorDinateID;
                    }
                    if (bmdBeans != null && bmdBeans.size() > 0) {
                        if (bmdBeans.get(0).CoorDinateID.equals(putBreastMilk.CoorDinateID)) {
                            singleBtnDialog.setDialogCloseImageView(View.GONE);
                            singleBtnDialog.setDialogTitleTextView("温馨提示！");
                            singleBtnDialog.setDialogContentTextView("这瓶奶已经存放到这里了");
                            singleBtnDialog.show();
                        } else {
                            twoBtnDialog.setDialogCloseImageView(View.GONE);
                            twoBtnDialog.setDialogTitleTextView("温馨提示！");
                            twoBtnDialog.setDialogContentTextView("是否将" + bmdBeans.get(0).MilkBoxNo
                                    + bmdBeans.get(0).Remarks + "位置的存放在这里");
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
                                    BreastMilkDetial bean = new BreastMilkDetial();
                                    bean.CoorDinateID = putBreastMilk.CoorDinateID;
                                    bean.CoorDinate = putBreastMilk.CoorDinate;
                                    bean.MilkBoxId = putBreastMilk.MilkBoxId;
                                    bean.MilkBoxNo = putBreastMilk.MilkBoxNo;
                                    bean.Remarks = putBreastMilk.Remarks;
                                    bean.QRcode = result;
                                    bean.ThawDate = DateUtils.getCurrentDate();
                                    bean.ThawGH = XmlDB.getInstance(PutDetail.this).getKeyStringValue("nCode", "");
                                    breastDetailDao.updatePlaceByQRcode(bean, "0");
                                    breastMilkDetials.clear();
                                    breastMilkDetials = breastDetailDao.getPutDetailByCoorDinateID(putBreastMilk.CoorDinateID, "2");
                                    adapter.setNewData(breastMilkDetials);
                                    PutBreastMilk pbean = new PutBreastMilk();
                                    pbean.BedNo = breastRegists.get(0).BedNo;
                                    pbean.Name = breastRegists.get(0).Name;
                                    pbean.Pid = breastRegists.get(0).Pid;
                                    pbean.RoomNo = breastRegists.get(0).RoomNo;
                                    Constant.PUT_ACCOUNT = Constant.PUT_ACCOUNT + 1;
                                    pbean.CFAccount = Constant.PUT_ACCOUNT + "";
                                    Constant.PUT_AMOUNT = (int) (Constant.PUT_AMOUNT + Float.parseFloat(breastRegists.get(0).Amount));
                                    pbean.CFAmount = Constant.PUT_AMOUNT + "";
                                    pbean.MilkBoxState = "2";
                                    putListDao.updateData(pbean, putBreastMilk.CoorDinateID);
                                    updatePatientInfo(breastRegists.get(0));
                                    ToastUtils.showShort(PutDetail.this, "存放成功");
                                    List<BreastMilkDetial> bean1 = breastDetailDao.
                                            getPutDetailByCoorDinateID(coorDinateID, "2");
                                    if (bean1 != null && bean1.size() > 0) {
                                        List<PutBreastMilk> putBreastMilks = putListDao.getPutListByCoorDinateID(coorDinateID);
                                        PutBreastMilk pbean1 = new PutBreastMilk();
                                        pbean1.CFAccount = (Integer.parseInt(putBreastMilks.get(0).CFAccount) - 1) + "";
                                        pbean1.CFAmount = (Float.parseFloat(putBreastMilks.get(0).CFAmount) - Float.parseFloat(breastRegists.get(0).Amount)) + "";
                                        pbean1.BedNo = breastRegists.get(0).BedNo;
                                        pbean.RoomNo = breastRegists.get(0).RoomNo;
                                        pbean1.Name = breastRegists.get(0).Name;
                                        pbean1.Pid = breastRegists.get(0).Pid;
                                        pbean1.MilkBoxState = "2";
                                        putListDao.updateData(pbean1, coorDinateID);
                                    } else {
                                        PutBreastMilk pbean2 = new PutBreastMilk();
                                        pbean2.CFAccount = "0";
                                        pbean2.CFAmount = "0.00";
                                        pbean2.BedNo = "";
                                        pbean2.Name = "";
                                        pbean2.Pid = "";
                                        pbean2.RoomNo = "";
                                        pbean2.MilkBoxState = "2";
                                        putListDao.updateData(pbean2, coorDinateID);
                                    }
                                }
                            });
                            twoBtnDialog.show();
                        }
                    } else {
                        BreastMilkDetial breastMilkDetial = new BreastMilkDetial();
                        breastMilkDetial.Pid = breastRegists.get(0).Pid;
                        breastMilkDetial.Amount = breastRegists.get(0).Amount;
                        breastMilkDetial.MilkBoxId = putBreastMilk.MilkBoxId;
                        breastMilkDetial.MilkBoxNo = putBreastMilk.MilkBoxNo;
                        breastMilkDetial.CoorDinateID = putBreastMilk.CoorDinateID;
                        breastMilkDetial.CoorDinate = putBreastMilk.CoorDinate;
                        breastMilkDetial.Remarks = putBreastMilk.Remarks;
                        breastMilkDetial.State = "2";
                        breastMilkDetial.SummaryId = breastRegists.get(0).SummaryId;
                        breastMilkDetial.PrintState = breastRegists.get(0).PrintState;
                        breastMilkDetial.ThawDate = breastRegists.get(0).ThawDate;
                        breastMilkDetial.ThawTime = breastRegists.get(0).ThawTime;
                        breastMilkDetial.ThawGH = XmlDB.getInstance(PutDetail.this).getKeyStringValue("nCode", "");
                        breastMilkDetial.MilkPumpDate = breastRegists.get(0).MilkPumpDate;
                        breastMilkDetial.MilkPumpTime = breastRegists.get(0).MilkPumpTime;
                        breastMilkDetial.QRcode = breastRegists.get(0).QRcode;
                        breastMilkDetial.YXQ = breastRegists.get(0).YXQ;
                        breastMilkDetial.ThawDate = DateUtils.getCurrentDate();
                        breastMilkDetial.MilkBoxState = "2";
                        breastDetailDao.insertBreastMilk(breastMilkDetial, "0");
                        breastMilkDetials.clear();
                        breastMilkDetials = breastDetailDao.getPutDetailByCoorDinateID(putBreastMilk.CoorDinateID, "2");
                        adapter.setNewData(breastMilkDetials);
                        PutBreastMilk bean = new PutBreastMilk();
                        bean.BedNo = breastRegists.get(0).BedNo;
                        bean.RoomNo = breastRegists.get(0).RoomNo;
                        bean.Name = breastRegists.get(0).Name;
                        bean.Pid = breastRegists.get(0).Pid;
                        Constant.PUT_ACCOUNT = Constant.PUT_ACCOUNT + 1;
                        bean.CFAccount = Constant.PUT_ACCOUNT + "";
                        Constant.PUT_AMOUNT = (int) (Constant.PUT_AMOUNT + Float.parseFloat(breastRegists.get(0).Amount));
                        bean.CFAmount = Constant.PUT_AMOUNT + "";
                        bean.MilkBoxState = "2";
                        putListDao.updateData(bean, putBreastMilk.CoorDinateID);
                        updatePatientInfo(breastRegists.get(0));
                    }
                } else {
                    singleBtnDialog.setDialogCloseImageView(View.GONE);
                    singleBtnDialog.setDialogTitleTextView("温馨提示！");
                    singleBtnDialog.setDialogContentTextView("一个位置不能存两个病人的奶");
                    singleBtnDialog.show();
                }

            }
        }

    }
}
