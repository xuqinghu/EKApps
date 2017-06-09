package com.fugao.breast.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.entity.BreastMilk;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class BreastListDao {
    public String TAG = "BreastListDAO";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;
    private BreastDetailDao breastDetailDao;

    public BreastListDao(DataBaseInfo dataBaseInfoCurrent) {
        dataBaseInfo = dataBaseInfoCurrent;
        sqlDB = dataBaseInfo.getSqlDB();
    }

    public boolean isEmpty() {
        return dataBaseInfo.tableIsEmpty(Constant.BREAST_LIST);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        dataBaseInfo.closeDB();
    }

    public int deleteAllInfo() {
        return sqlDB.delete(Constant.BREAST_LIST, null, null);
    }


    public void saveToBreastList(List<BreastMilk> breastMilks) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        //先删除明细数据
        breastDetailDao.deleteAllInfo();
        for (BreastMilk breastMilk : breastMilks) {
            contentValues = new ContentValues();
            contentValues.put("Id", StringUtils.getString(breastMilk.Id));
            contentValues.put("Name", StringUtils.getString(breastMilk.Name));
            contentValues.put("Sex", StringUtils.getString(breastMilk.Sex));
            contentValues.put("Pid", StringUtils.getString(breastMilk.Pid));
            contentValues.put("BedNo", StringUtils.getString(breastMilk.BedNo));
            contentValues.put("DepartmentId", StringUtils.getString(breastMilk.DepartmentId));
            contentValues.put("WardId", StringUtils.getString(breastMilk.WardId));
            contentValues.put("DepartmentName", StringUtils.getString(breastMilk.DepartmentName));
            contentValues.put("WardName", StringUtils.getString(breastMilk.WardName));
            contentValues.put("YZTime", StringUtils.getString(breastMilk.YZTime));
            contentValues.put("YZdosis", StringUtils.getString(breastMilk.YZdosis));
            contentValues.put("ThawAmount", StringUtils.getString(breastMilk.ThawAmount));
            contentValues.put("ThawAccount", StringUtils.getString(breastMilk.ThawAccount));
            contentValues.put("YZZL", StringUtils.getString(breastMilk.YZZL));
            contentValues.put("RoomNo", StringUtils.getString(breastMilk.RoomNo));
            if (breastMilk.BreastMilkItems != null && breastMilk.BreastMilkItems.size() > 0) {
                breastDetailDao.saveToBreastDetail(breastMilk.BreastMilkItems, breastMilk.Pid, "1");
            }
            sqlDB.insert(Constant.BREAST_LIST, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存解冻列表数据到本地数据库 完成");
    }

    public void updateData(BreastMilk breastMilk, String pid) {
        String sql = "update " + Constant.BREAST_LIST + " set ThawAccount='"
                + breastMilk.ThawAccount + "',ThawAmount='" + breastMilk.ThawAmount
                + "' where Pid='" + pid + "'";
        sqlDB.execSQL(sql);
    }

    public List<BreastMilk> getBreastList() {
        Cursor cursor = sqlDB.query(Constant.BREAST_LIST, null, null,
                null, null, null, null);
        return getBreastListByCursor(cursor);
    }

    public List<BreastMilk> getBreastListByCursor(Cursor cursor) {
        List<BreastMilk> breastMilks = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreastMilk breastMilk = new BreastMilk();
            breastMilk.Id = cursor.getString(cursor.getColumnIndex("Id"));
            breastMilk.Name = cursor.getString(cursor.getColumnIndex("Name"));
            breastMilk.Sex = cursor.getString(cursor.getColumnIndex("Sex"));
            breastMilk.Pid = cursor.getString(cursor.getColumnIndex("Pid"));
            breastMilk.BedNo = cursor.getString(cursor.getColumnIndex("BedNo"));
            breastMilk.DepartmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
            breastMilk.WardId = cursor.getString(cursor.getColumnIndex("WardId"));
            breastMilk.DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            breastMilk.WardName = cursor.getString(cursor.getColumnIndex("WardName"));
            breastMilk.YZTime = cursor.getString(cursor.getColumnIndex("YZTime"));
            breastMilk.YZdosis = cursor.getString(cursor.getColumnIndex("YZdosis"));
            breastMilk.ThawAmount = cursor.getString(cursor.getColumnIndex("ThawAmount"));
            breastMilk.ThawAccount = cursor.getString(cursor.getColumnIndex("ThawAccount"));
            breastMilk.YZZL = cursor.getString(cursor.getColumnIndex("YZZL"));
            breastMilk.RoomNo = cursor.getString(cursor.getColumnIndex("RoomNo"));
            breastMilks.add(breastMilk);
        }
        cursor.close();
        return breastMilks;
    }

}
