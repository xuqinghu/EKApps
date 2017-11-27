package com.fugao.breast.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.PutBreastMilk;
import com.fugao.breast.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huxq on 2017/5/27 0027.
 */

public class PutListDao {
    public String TAG = "PutListDao";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;
    private BreastDetailDao breastDetailDao;

    public PutListDao(DataBaseInfo dataBaseInfoCurrent) {
        dataBaseInfo = dataBaseInfoCurrent;
        sqlDB = dataBaseInfo.getSqlDB();
    }

    public boolean isEmpty() {
        return dataBaseInfo.tableIsEmpty(Constant.PUT_LIST);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        dataBaseInfo.closeDB();
    }

    public int deleteAllInfo() {
        return sqlDB.delete(Constant.PUT_LIST, null, null);
    }

    //更新存放列表
    public void updateData(PutBreastMilk putBreastMilk, String coorDinateID) {
        String sql = "update " + Constant.PUT_LIST + " set Name='"
                + putBreastMilk.Name + "',Sex='" + putBreastMilk.Sex
                + "',Pid='" + putBreastMilk.Pid + "',BedNo='"
                + putBreastMilk.BedNo + "',CFAccount='" + putBreastMilk.CFAccount + "',MilkBoxState='" + putBreastMilk.MilkBoxState
                + "',CFAmount='" + putBreastMilk.CFAmount + "',RoomNo='" + putBreastMilk.RoomNo
                + "' where CoorDinateID='" + coorDinateID + "'";
        sqlDB.execSQL(sql);
    }

    public void saveToPutList(List<PutBreastMilk> putBreastMilks) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        breastDetailDao = new BreastDetailDao(dataBaseInfo);
        //先删除明细数据
        breastDetailDao.deleteAllInfo();
        for (PutBreastMilk putBreastMilk : putBreastMilks) {
            contentValues = new ContentValues();
            contentValues.put("Id", StringUtils.getString(putBreastMilk.Id));
            contentValues.put("Name", StringUtils.getString(putBreastMilk.Name));
            contentValues.put("Sex", StringUtils.getString(putBreastMilk.Sex));
            contentValues.put("Pid", StringUtils.getString(putBreastMilk.Pid));
            contentValues.put("BedNo", StringUtils.getString(putBreastMilk.BedNo));
            contentValues.put("DepartmentId", StringUtils.getString(putBreastMilk.DepartmentId));
            contentValues.put("WardId", StringUtils.getString(putBreastMilk.WardId));
            contentValues.put("DepartmentName", StringUtils.getString(putBreastMilk.DepartmentName));
            contentValues.put("WardName", StringUtils.getString(putBreastMilk.WardName));
            contentValues.put("YZTime", StringUtils.getString(putBreastMilk.YZTime));
            contentValues.put("YZdosis", StringUtils.getString(putBreastMilk.YZdosis));
            contentValues.put("ThawAmount", StringUtils.getString(putBreastMilk.ThawAmount));
            contentValues.put("ThawAccount", StringUtils.getString(putBreastMilk.ThawAccount));
            contentValues.put("YZZL", StringUtils.getString(putBreastMilk.YZZL));
            contentValues.put("CoorDinateID", StringUtils.getString(putBreastMilk.CoorDinateID));
            contentValues.put("CoorDinate", StringUtils.getString(putBreastMilk.CoorDinate));
            contentValues.put("Remarks", StringUtils.getString(putBreastMilk.Remarks));
            contentValues.put("MilkBoxId", StringUtils.getString(putBreastMilk.MilkBoxId));
            contentValues.put("MilkBoxNo", StringUtils.getString(putBreastMilk.MilkBoxNo));
            contentValues.put("RoomNo", StringUtils.getString(putBreastMilk.RoomNo));
            contentValues.put("CFAccount", StringUtils.getString(putBreastMilk.CFAccount));
            contentValues.put("CFAmount", StringUtils.getString(putBreastMilk.CFAmount));
            contentValues.put("MilkBoxState", StringUtils.getString(putBreastMilk.MilkBoxState));
            contentValues.put("MilkBoxOutherState", StringUtils.getString(putBreastMilk.MilkBoxOutherState));
            if (putBreastMilk.BreastMilkItems != null && putBreastMilk.BreastMilkItems.size() > 0) {
                breastDetailDao.saveToPutBreastDetail(putBreastMilk.BreastMilkItems, putBreastMilk.Pid,
                        putBreastMilk.MilkBoxState,putBreastMilk.TwinsCode,"1");
            }
            sqlDB.insert(Constant.PUT_LIST, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存存放列表数据到本地数据库 完成");
    }

    public List<PutBreastMilk> getPutList() {
        Cursor cursor = sqlDB.query(Constant.PUT_LIST, null, null,
                null, null, null, null);
        return getPutListByCursor(cursor);
    }

    /**
     * 根据病人住院号获取数据
     *
     * @param pid
     * @return
     */
    public List<PutBreastMilk> getPutListByPid(String pid) {
        Cursor cursor = sqlDB.query(Constant.PUT_LIST, null, "Pid='" + pid + "'",
                null, null, null, null);
        return getPutListByCursor(cursor);
    }

    /**
     * 根据位置二维码获取数据
     *
     * @param coorDinateID
     * @return
     */
    public List<PutBreastMilk> getPutListByCoorDinateID(String coorDinateID) {
        Cursor cursor = sqlDB.query(Constant.PUT_LIST, null, "CoorDinateID='" + coorDinateID + "'", null, null, null, null);
        return getPutListByCursor(cursor);
    }

    public List<PutBreastMilk> getPutListByCursor(Cursor cursor) {
        List<PutBreastMilk> putBreastMilks = new ArrayList<>();
        while (cursor.moveToNext()) {
            PutBreastMilk putBreastMilk = new PutBreastMilk();
            putBreastMilk.Id = cursor.getString(cursor.getColumnIndex("Id"));
            putBreastMilk.Name = cursor.getString(cursor.getColumnIndex("Name"));
            putBreastMilk.Sex = cursor.getString(cursor.getColumnIndex("Sex"));
            putBreastMilk.Pid = cursor.getString(cursor.getColumnIndex("Pid"));
            putBreastMilk.BedNo = cursor.getString(cursor.getColumnIndex("BedNo"));
            putBreastMilk.DepartmentId = cursor.getString(cursor.getColumnIndex("DepartmentId"));
            putBreastMilk.WardId = cursor.getString(cursor.getColumnIndex("WardId"));
            putBreastMilk.DepartmentName = cursor.getString(cursor.getColumnIndex("DepartmentName"));
            putBreastMilk.WardName = cursor.getString(cursor.getColumnIndex("WardName"));
            putBreastMilk.YZTime = cursor.getString(cursor.getColumnIndex("YZTime"));
            putBreastMilk.YZdosis = cursor.getString(cursor.getColumnIndex("YZdosis"));
            putBreastMilk.ThawAmount = cursor.getString(cursor.getColumnIndex("ThawAmount"));
            putBreastMilk.ThawAccount = cursor.getString(cursor.getColumnIndex("ThawAccount"));
            putBreastMilk.YZZL = cursor.getString(cursor.getColumnIndex("YZZL"));
            putBreastMilk.CoorDinateID = cursor.getString(cursor.getColumnIndex("CoorDinateID"));
            putBreastMilk.CoorDinate = cursor.getString(cursor.getColumnIndex("CoorDinate"));
            putBreastMilk.Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));
            putBreastMilk.MilkBoxId = cursor.getString(cursor.getColumnIndex("MilkBoxId"));
            putBreastMilk.MilkBoxNo = cursor.getString(cursor.getColumnIndex("MilkBoxNo"));
            putBreastMilk.RoomNo = cursor.getString(cursor.getColumnIndex("RoomNo"));
            putBreastMilk.CFAccount = cursor.getString(cursor.getColumnIndex("CFAccount"));
            putBreastMilk.CFAmount = cursor.getString(cursor.getColumnIndex("CFAmount"));
            putBreastMilk.MilkBoxState = cursor.getString(cursor.getColumnIndex("MilkBoxState"));
            putBreastMilk.MilkBoxOutherState = cursor.getString(cursor.getColumnIndex("MilkBoxOutherState"));
            putBreastMilks.add(putBreastMilk);
        }
        cursor.close();
        return putBreastMilks;
    }

}
