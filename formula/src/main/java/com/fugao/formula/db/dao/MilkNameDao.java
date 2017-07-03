package com.fugao.formula.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.entity.MilkBean;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class MilkNameDao {
    public String TAG = "MilkNameDao";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;

    public MilkNameDao(DataBaseInfo dataBaseInfoCurrent) {
        dataBaseInfo = dataBaseInfoCurrent;
        sqlDB = dataBaseInfo.getSqlDB();
    }

    public boolean isEmpty() {
        return dataBaseInfo.tableIsEmpty(Constant.TIME_LIST);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        dataBaseInfo.closeDB();
    }

    public int deleteAllInfo() {
        return sqlDB.delete(Constant.MILK_NAME, null, null);
    }

    public void saveToMilkName(List<MilkBean> beans) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        for (MilkBean bean : beans) {
            contentValues = new ContentValues();
            contentValues.put("MilkCode", StringUtils.getString(bean.MilkCode));
            contentValues.put("MilkName", StringUtils.getString(bean.MilkName));
            sqlDB.insert(Constant.MILK_NAME, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存存放列表数据到本地数据库完成");
    }

    public List<MilkBean> getMilkName() {
        Cursor cursor = sqlDB.query(Constant.MILK_NAME, null, null,
                null, null, null, null);
        return getMilkNameByCursor(cursor);
    }

    public List<MilkBean> getMilkNameByCursor(Cursor cursor) {
        List<MilkBean> beans = new ArrayList<>();
        while (cursor.moveToNext()) {
            MilkBean bean = new MilkBean();
            bean.MilkCode = cursor.getString(cursor.getColumnIndex("MilkCode"));
            bean.MilkName = cursor.getString(cursor.getColumnIndex("MilkName"));
            beans.add(bean);
        }
        return beans;
    }
}
