package com.fugao.formula.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fugao.formula.constant.Constant;
import com.fugao.formula.db.DataBaseInfo;
import com.fugao.formula.entity.TimeEntity;
import com.fugao.formula.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class TimeListDao {
    public String TAG = "TimeListDao";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;

    public TimeListDao(DataBaseInfo dataBaseInfoCurrent) {
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
        return sqlDB.delete(Constant.TIME_LIST, null, null);
    }

    public void saveToTimeList(List<TimeEntity> mTimes) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        for (TimeEntity time : mTimes) {
            contentValues = new ContentValues();
            contentValues.put("Name", StringUtils.getString(time.Name));
            contentValues.put("State", StringUtils.getString(time.State));
            sqlDB.insert(Constant.TIME_LIST, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存存放列表数据到本地数据库完成");
    }

    //通过状态获取时间点
    public List<TimeEntity> getTimeListByState(String state) {
        Cursor cursor = sqlDB.query(Constant.TIME_LIST, null, "State='" + state + "'",
                null, null, null, null);
        return getTimeListByCursor(cursor);
    }

    public List<TimeEntity> getTimeList() {
        Cursor cursor = sqlDB.query(Constant.TIME_LIST, null, null,
                null, null, null, null);
        return getTimeListByCursor(cursor);
    }


    public void updateData(List<TimeEntity> beans) {
        for (TimeEntity bean : beans) {
            String sql = "update " + Constant.TIME_LIST + " set State='"
                    + bean.State + "' where Name='" + bean.Name + "'";
            sqlDB.execSQL(sql);
        }
    }

    public List<TimeEntity> getTimeListByCursor(Cursor cursor) {
        List<TimeEntity> timeList = new ArrayList<>();
        while (cursor.moveToNext()) {
            TimeEntity time = new TimeEntity();
            time.Name = cursor.getString(cursor.getColumnIndex("Name"));
            time.State = cursor.getString(cursor.getColumnIndex("State"));
            timeList.add(time);
        }
        return timeList;
    }
}
