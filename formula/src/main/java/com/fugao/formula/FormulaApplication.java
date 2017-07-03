package com.fugao.formula;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fugao.formula.entity.DivisionEntity;
import com.fugao.formula.entity.WardBean;
import com.fugao.formula.utils.CrashHandler;
import com.fugao.formula.utils.DateUtils;
import com.fugao.formula.utils.FileHelper;
import com.fugao.formula.utils.LogUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class FormulaApplication extends Application {
    private List<WardBean> wardBeans;
    private Context mContext;
    /**
     * 当前时间 包含
     */
    public String executeTime;
    /**
     * 当前日期格式 20130829
     */
    public String currentDate;
    /**
     * 输液当前的项目sdcard 路径
     */
    public String formulaPath = "";


    @Override
    public void onCreate() {
        super.onCreate();
        onInit();
    }

    public void onInit() {
        mContext = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(mContext);
        init();
        executeTime = DateUtils.getCurrentDateAndTime();
        this.currentDate = DateUtils.getCurrentDate();
    }

    public void init() {
        formulaPath = FileHelper.appSDPath;
        FileHelper.creatSDDir("logs");
        LogUtils.setPath(formulaPath + "/logs/"
                + DateUtils.getCurrentDateAndTime() + ".txt");
        LogUtils.setTag("formula");
        LogUtils.setEnabled(true);
    }

    public List<WardBean> getDivisionList() {
        return wardBeans;
    }

    public void setDivisionList(List<WardBean> wardBeans) {
        this.wardBeans = wardBeans;
    }


}
