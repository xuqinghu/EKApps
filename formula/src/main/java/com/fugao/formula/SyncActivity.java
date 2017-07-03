package com.fugao.formula;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fugao.formula.constant.Constant;
import com.fugao.formula.constant.FormulaApi;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.db.dao.MilkNameDao;
import com.fugao.formula.db.dao.TimeListDao;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.ui.box.SelectActivity;
import com.fugao.formula.utils.FastJsonUtils;
import com.fugao.formula.utils.OkHttpUtils;
import com.fugao.formula.utils.ToastUtils;
import com.fugao.formula.utils.ViewUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class SyncActivity extends FragmentActivity {
    /**
     * 准备要访问数据的链接
     */
    public ArrayList<String> isPrepareConnectString;
    /**
     * 进度条
     */
    private ProgressBar updateInfoProgressBar;
    /**
     * 控制按钮
     */
    private Button sync_dialog_re_connect, sync_dialog_close;
    /**
     * 加载显示信息
     */
    private TextView textVersionMsg;
    /**
     * progressbar 当前长度
     */
    private int currentBarLength = 0;
    /**
     * 加载处理Handler
     */
    private Handler updateProgressHandler;

    /**
     * 加载成功标志
     */
    public static final int UPDATE_SUCCESS = 1;
    private DataBaseInfo dataBaseInfo;
    private MilkNameDao milkNameDao;

    /**
     * 添加执行的方法名称
     */
    private void setMethodName() {
//        isPrepareConnectString.add(Constant.GETDIVISION); //获取病区
        isPrepareConnectString.add(Constant.GETMILKNAMES); //获取奶名

    }

    private void initHandler() {
        // TODO Auto-generated method stub
        updateProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_SUCCESS:
                        currentBarLength++;
                        updateInfoProgressBar.setProgress(currentBarLength);
                        getDataFromServer();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        initView();
        initData();
        initListener();
        initIntent();
    }

    private void initView() {
        setTitle("数据加载中!");
        updateInfoProgressBar = (ProgressBar) findViewById(R.id.updateInfoProgressBar);
        sync_dialog_re_connect = (Button) findViewById(R.id.sync_dialog_re_connect);
        sync_dialog_close = (Button) findViewById(R.id.sync_dialog_close);
        textVersionMsg = (TextView) findViewById(R.id.textVersionMsg);
        ViewUtils.setGone(sync_dialog_close);
        ViewUtils.setGone(sync_dialog_re_connect);
        ViewUtils.setVisible(updateInfoProgressBar);
        ViewUtils.setVisible(textVersionMsg);
    }

    private void initData() {
        dataBaseInfo = DataBaseInfo.getInstance(SyncActivity.this);
        milkNameDao = new MilkNameDao(dataBaseInfo);
        isPrepareConnectString = new ArrayList<String>();
        setMethodName();
        updateInfoProgressBar.setMax(isPrepareConnectString.size());
        updateInfoProgressBar.setProgress(0);
        initHandler();
        getDataFromServer();
    }

    private void initListener() {
        sync_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sync_dialog_re_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reconnect();
            }
        });
    }

    private void initIntent() {

    }

    public void getDivision() {
        textVersionMsg.setText("正在加载病区数据...");
        String url = "http://192.168.10.125:9998/api/BreastMilk?patId=&deptid=1111&WardId=1230100&date=20170626&type=2";
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response, int code) {
                requestSuccess(Constant.GETDIVISION);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        OkHttpUtils.get(url, callback);
    }

    public void getMilkNames() {
        textVersionMsg.setText("正在加载奶名数据...");
        String url = FormulaApi.getAdviceData("2", "", "", "");
        OkHttpUtils.ResultCallback<String> callback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response, int code) {
                if (code == 200) {
                    if (response != null) {
                        if ("[]".equals(response)) {

                        } else {
                            milkNameDao.deleteAllInfo();
                            List<MilkBean> milkList = FastJsonUtils.getBeanList(response, MilkBean.class);
                            milkNameDao.saveToMilkName(milkList);
                        }
                        requestSuccess(Constant.GETMILKNAMES);
                    }
                } else {
                    ToastUtils.showShort(SyncActivity.this, "服务器异常");
                }

            }

            @Override
            public void onFailure(Exception e) {
                requestFail();
            }
        };
        OkHttpUtils.get(url, callback);
    }

    /**
     * 从服务端获取数据
     *
     * @Title: getDataFromServer
     * @Description: TODO
     * @return: void
     */
    public void getDataFromServer() {
        if (isPrepareConnectString.size() > 0) {
            invokeMethod(isPrepareConnectString.get(0));
        } else {
            finish();
            LoginActivity.loginToMainActivity.sendEmptyMessage(1);
        }
    }

    /**
     * 获取数据失败
     *
     * @Title: requestFail
     * @Description: TODO
     * @return: void
     */
    public void requestFail() {
        ViewUtils.setVisible(sync_dialog_close);
        ViewUtils.setVisible(sync_dialog_re_connect);
        ViewUtils.setGone(updateInfoProgressBar);
        ViewUtils.setVisible(textVersionMsg);
    }

    /**
     * 获取数据成功
     *
     * @param string
     * @Title: requestSuccess
     * @Description: TODO
     * @return: void
     */
    public void requestSuccess(String string) {
        Log.i("sync", string + "--->requestSuccess");
        isPrepareConnectString.remove(string);
        updateProgressHandler.sendEmptyMessage(UPDATE_SUCCESS);
    }

    /**
     * 反射执行方法
     *
     * @param methodName
     * @Title: invokeMethod
     * @Description: TODO
     * @return: void
     */
    public void invokeMethod(String methodName) {
        Class[] cargs = null;
        Object[] inArgs = null;
        Method m;
        try {
            m = SyncActivity.class.getMethod(methodName, cargs);
            m.invoke(this, inArgs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重试 获取服务器端的数据
     */
    public void reconnect() {
        updateInfoProgressBar.setProgress(currentBarLength);
        getDataFromServer();
        ViewUtils.setGone(sync_dialog_close);
        ViewUtils.setGone(sync_dialog_re_connect);
        ViewUtils.setVisible(updateInfoProgressBar);
        ViewUtils.setVisible(textVersionMsg);
    }
}
