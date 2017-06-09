package com.fugao.breast.ui.put;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fugao.breast.R;
import com.fugao.breast.base.BaseActivity;
import com.fugao.breast.constant.BreastApi;
import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.db.dao.BreastDetailDao;
import com.fugao.breast.db.dao.BreastRegistDao;
import com.fugao.breast.db.dao.PutListDao;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.PutBreastMilk;
import com.fugao.breast.utils.common.DateUtils;
import com.fugao.breast.utils.common.DialogUtils;
import com.fugao.breast.utils.common.FastJsonUtils;
import com.fugao.breast.utils.common.NetWorkUtils;
import com.fugao.breast.utils.common.OkHttpUtils;
import com.fugao.breast.utils.common.RecyclerViewDivider;
import com.fugao.breast.utils.common.XmlDB;
import com.fugao.breast.utils.dialog.SingleBtnDialog;
import com.fugao.breast.utils.dialog.TwoBtnDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 母乳存放列表界面
 * Created by huxq on 2017/5/22 0022.
 */

public class PutList extends BaseActivity {
    private LinearLayout back;
    private TextView division, name, tv_count;
    private RecyclerView recyclerView;
    private List<PutBreastMilk> data;
    private PutListAdapter adapter;
    private DataBaseInfo dataBaseInfo;
    private PutListDao putListDao;
    private BreastRegistDao breastRegistDao;
    private BreastDetailDao breastDetailDao;
    private boolean flag1;
    private boolean flag2;
    private SingleBtnDialog singleBtnDialog;
    private TwoBtnDialog twoBtnDialog;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_put_list);

    }

    @Override
    public void initView() {
        flag1 = false;
        flag2 = false;
        back = (LinearLayout) findViewById(R.id.ll_put_list_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_put_list);
        division = (TextView) findViewById(R.id.tv_put_list_division);
        name = (TextView) findViewById(R.id.tv_put_list_name);
        tv_count = (TextView) findViewById(R.id.tv_put_list_count);
        division.setText("病区:" + XmlDB.getInstance(PutList.this).getKeyStringValue("wardName", ""));
        name.setText("存放人:" + XmlDB.getInstance(PutList.this).getKeyStringValue("nName", ""));
    }

    @Override
    public void initData() {
        singleBtnDialog = new SingleBtnDialog(PutList.this);
        twoBtnDialog = new TwoBtnDialog(PutList.this);
        dataBaseInfo = DataBaseInfo.getInstance(PutList.this);
        putListDao = new PutListDao(dataBaseInfo);
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        breastRegistDao = new BreastRegistDao(dataBaseInfo);
        data = new ArrayList<>();
        //添加分割线
        recyclerView.addItemDecoration(new RecyclerViewDivider(PutList.this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PutListAdapter(R.layout.put_list_item, data);
        recyclerView.setAdapter(adapter);
        checkNetWork();
    }

    @Override
    public void initListener() {
        //list点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent();
                intent.setClass(PutList.this, PutDetail.class);
                intent.putExtra("putlist", data.get(i));
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                if ("2".equals(data.get(position).MilkBoxState) && "0".equals(data.get(position).CFAccount)) {
                    twoBtnDialog.setDialogCloseImageView(View.GONE);
                    twoBtnDialog.setDialogTitleTextView("温馨提示！");
                    twoBtnDialog.setDialogContentTextView("是否释放" + data.get(position).MilkBoxNo
                            + data.get(position).Remarks + "这个位置");
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
                            PutBreastMilk pbean = new PutBreastMilk();
                            pbean.CFAccount = "0";
                            pbean.CFAmount = "0.00";
                            pbean.BedNo = "";
                            pbean.RoomNo = "";
                            pbean.Name = "";
                            pbean.Pid = "";
                            pbean.MilkBoxState = "1";
                            putListDao.updateData(pbean, data.get(position).CoorDinateID);
                            refreshList();
                            BreastMilkDetial breastMilkDetial = new BreastMilkDetial();
                            breastMilkDetial.Pid = "";
                            breastMilkDetial.Amount = "";
                            breastMilkDetial.MilkBoxId = data.get(position).MilkBoxId;
                            breastMilkDetial.MilkBoxNo = data.get(position).MilkBoxNo;
                            breastMilkDetial.CoorDinateID = data.get(position).CoorDinateID;
                            breastMilkDetial.CoorDinate = data.get(position).CoorDinate;
                            breastMilkDetial.Remarks = data.get(position).Remarks;
                            breastMilkDetial.State = "";
                            breastMilkDetial.SummaryId = "";
                            breastMilkDetial.PrintState = "";
                            breastMilkDetial.ThawDate = "";
                            breastMilkDetial.ThawTime = "";
                            breastMilkDetial.ThawGH = "";
                            breastMilkDetial.MilkPumpDate = "";
                            breastMilkDetial.MilkPumpTime = "";
                            breastMilkDetial.QRcode = "";
                            breastMilkDetial.YXQ = "";
                            breastMilkDetial.MilkBoxState = "1";
                            breastDetailDao.insertBreastMilk(breastMilkDetial, "0");
                        }
                    });
                    twoBtnDialog.show();
                } else if ("2".equals(data.get(position).MilkBoxState) && !"0".equals(data.get(position).CFAccount)) {
                    singleBtnDialog.setDialogCloseImageView(View.GONE);
                    singleBtnDialog.setDialogTitleTextView("温馨提示！");
                    singleBtnDialog.setDialogContentTextView("格子里面还有奶，不能释放这个位置");
                    singleBtnDialog.show();
                } else if ("1".equals(data.get(position).MilkBoxState)) {
                    singleBtnDialog.setDialogCloseImageView(View.GONE);
                    singleBtnDialog.setDialogTitleTextView("温馨提示！");
                    singleBtnDialog.setDialogContentTextView("这个位置没有被人占用，不用释放");
                    singleBtnDialog.show();
                }
                return false;
            }
        });
        //返回点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void initIntent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    //更新列表
    private void refreshList() {
        data.clear();
        data = putListDao.getPutList();
        adapter.setNewData(data);
        setCount(data);
    }

    //设置当前存放瓶数
    private void setCount(List<PutBreastMilk> putBreastMilks) {
        int count = 0;
        for (PutBreastMilk bean : putBreastMilks) {
            count = count + Integer.parseInt(bean.CFAccount);
        }
        tv_count.setText("当前已存:" + count);


    }

    private void checkNetWork() {
        recyclerView.setVisibility(View.GONE);
        if (NetWorkUtils.isNetworkAvalible(PutList.this)) {
            getData();
            saveRegistData();
        } else {
            singleBtnDialog.setDialogCloseImageView(View.GONE);
            singleBtnDialog.setDialogTitleTextView("温馨提示！");
            singleBtnDialog.setDialogContentTextView("没有可用的网络");
            singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
                @Override
                public void sureBtnClick() {
                    finish();
                }
            });
            singleBtnDialog.show();
        }
    }

    //获取存放列表并保存到数据库
    private void getData() {
        DialogUtils.showProgressDialog(PutList.this, "正在加载数据...");
        String deptId = XmlDB.getInstance(PutList.this).getKeyString("deptId", "");
        String wardId = XmlDB.getInstance(PutList.this).getKeyString("wardId", "");
        String url = BreastApi.getBreastData("", deptId, wardId, DateUtils.getCurrentDate(), "2");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                flag1 = true;
                if (flag1 && flag2) {
                    DialogUtils.dissmissProgressDialog();
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (response != null) {
                    data = FastJsonUtils.getBeanList(response, PutBreastMilk.class);
                    putListDao.deleteAllInfo();
                    putListDao.saveToPutList(data);
                    adapter.setNewData(data);
                    setCount(data);
                }
            }

            @Override
            public void onFailure(Exception e) {
                flag1 = true;
                if (flag1 && flag2) {
                    DialogUtils.dissmissProgressDialog();
                    getSecondData();
                }
            }
        };
        OkHttpUtils.get(url, callback);
    }

    //重新从网络上获取数据
    private void getSecondData() {
        singleBtnDialog.setDialogCloseImageView(View.GONE);
        singleBtnDialog.setDialogTitleTextView("温馨提示！");
        singleBtnDialog.setDialogContentTextView("数据加载失败");
        singleBtnDialog.setSureTextView("重试");
        singleBtnDialog.setSingleBtnDialogClickListener(new SingleBtnDialog.SingleBtnDialogClickListener() {
            @Override
            public void sureBtnClick() {
                checkNetWork();
            }
        });
        singleBtnDialog.show();
    }

    //获取登记数据并保存到数据库
    private void saveRegistData() {
        String deptId = XmlDB.getInstance(PutList.this).getKeyString("deptId", "");
        String wardId = XmlDB.getInstance(PutList.this).getKeyString("wardId", "");
        String url = BreastApi.getBreastData("", deptId, wardId, DateUtils.getCurrentDate(), "3");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                flag2 = true;
                if (flag1 && flag2) {
                    DialogUtils.dissmissProgressDialog();
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (response != null) {
                    List<PutBreastMilk> beans = new ArrayList<>();
                    beans = FastJsonUtils.getBeanList(response, PutBreastMilk.class);
                    breastRegistDao.deleteAllInfo();
                    //将state=1的数据存在本地，为了后面扫描奶瓶时查询病人信息
                    for (PutBreastMilk putBreastMilk : beans) {
                        if (putBreastMilk.BreastMilkItems != null && putBreastMilk.BreastMilkItems.size() > 0) {
                            breastRegistDao.saveToBreastRegist(putBreastMilk.BreastMilkItems, putBreastMilk.Name, putBreastMilk.BedNo, putBreastMilk.RoomNo);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                flag2 = true;
                if (flag1 && flag2) {
                    DialogUtils.dissmissProgressDialog();
                    getSecondData();
                }
            }
        };
        OkHttpUtils.get(url, callback);
    }


    @Override
    public void receiverPlaceCode(String result) {
        super.receiverCode(result);
        for (PutBreastMilk putBreastMilk : data) {
            if (putBreastMilk.CoorDinateID.equals(result)) {
                Intent intent = new Intent();
                intent.setClass(PutList.this, PutDetail.class);
                intent.putExtra("putlist", putBreastMilk);
                startActivity(intent);
                break;
            }
        }

    }
}
