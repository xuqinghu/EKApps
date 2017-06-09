package com.fugao.breast.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fugao.breast.constant.Constant;
import com.fugao.breast.db.DataBaseInfo;
import com.fugao.breast.entity.BreastMilkDetial;
import com.fugao.breast.entity.BreastRegist;
import com.fugao.breast.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class BreastRegistDao {
    public String TAG = "BreastRegistDao";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;

    public BreastRegistDao(DataBaseInfo dataBaseInfoCurrent) {
        dataBaseInfo = dataBaseInfoCurrent;
        sqlDB = dataBaseInfo.getSqlDB();
    }

    public boolean isEmpty() {
        return dataBaseInfo.tableIsEmpty(Constant.BREAST_REGIST);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        dataBaseInfo.closeDB();
    }

    public int deleteAllInfo() {
        return sqlDB.delete(Constant.BREAST_REGIST, null, null);
    }

    /**
     * 根据二维码获取数据
     *
     * @param qrCode
     * @return
     */
    public List<BreastRegist> getBreastMilkDetialByQRCode(String qrCode) {
        Cursor cursor = sqlDB.query(Constant.BREAST_REGIST, null, "QRcode='" + qrCode + "'", null, null, null, null);
        return getBreastRegistByCursor(cursor);
    }

    public void saveToBreastRegist(List<BreastMilkDetial> breastMilkDetials, String name, String bedNo, String roomNo) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        // 数据插入操作循环
        for (BreastMilkDetial breastMilkDetial : breastMilkDetials) {
            contentValues = new ContentValues();
            contentValues.put("Pid", StringUtils.getString(breastMilkDetial.Pid));
            contentValues.put("Name", StringUtils.getString(name));
            contentValues.put("BedNo", StringUtils.getString(bedNo));
            contentValues.put("Amount", StringUtils.getString(breastMilkDetial.Amount));
            contentValues.put("MilkBoxId", StringUtils.getString(breastMilkDetial.MilkBoxId));
            contentValues.put("MilkBoxNo", StringUtils.getString(breastMilkDetial.MilkBoxNo));
            contentValues.put("CoorDinateID", StringUtils.getString(breastMilkDetial.CoorDinateID));
            contentValues.put("CoorDinate", StringUtils.getString(breastMilkDetial.CoorDinate));
            contentValues.put("Remarks", StringUtils.getString(breastMilkDetial.Remarks));
            contentValues.put("State", StringUtils.getString(breastMilkDetial.State));
            contentValues.put("SummaryId", StringUtils.getString(breastMilkDetial.SummaryId));
            contentValues.put("PrintState", StringUtils.getString(breastMilkDetial.PrintState));
            contentValues.put("ThawDate", StringUtils.getString(breastMilkDetial.ThawDate));
            contentValues.put("ThawTime", StringUtils.getString(breastMilkDetial.ThawTime));
            contentValues.put("ThawGH", StringUtils.getString(breastMilkDetial.ThawGH));
            contentValues.put("MilkPumpDate", StringUtils.getString(breastMilkDetial.MilkPumpDate));
            contentValues.put("MilkPumpTime", StringUtils.getString(breastMilkDetial.MilkPumpTime));
            contentValues.put("QRcode", StringUtils.getString(breastMilkDetial.QRcode));
            contentValues.put("YXQ", StringUtils.getString(breastMilkDetial.YXQ));
            contentValues.put("RoomNo", StringUtils.getString(roomNo));
            sqlDB.insert(Constant.BREAST_REGIST, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存数据到本地数据库 完成");
    }

    public List<BreastRegist> getBreastRegistByCursor(Cursor cursor) {
        List<BreastRegist> breastRegists = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreastRegist breastRegist = new BreastRegist();
            breastRegist.Pid = cursor.getString(cursor.getColumnIndex("Pid"));
            breastRegist.Name = cursor.getString(cursor.getColumnIndex("Name"));
            breastRegist.BedNo = cursor.getString(cursor.getColumnIndex("BedNo"));
            breastRegist.Amount = cursor.getString(cursor.getColumnIndex("Amount"));
            breastRegist.MilkBoxId = cursor.getString(cursor.getColumnIndex("MilkBoxId"));
            breastRegist.MilkBoxNo = cursor.getString(cursor.getColumnIndex("MilkBoxNo"));
            breastRegist.CoorDinateID = cursor.getString(cursor.getColumnIndex("CoorDinateID"));
            breastRegist.CoorDinate = cursor.getString(cursor.getColumnIndex("CoorDinate"));
            breastRegist.Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));
            breastRegist.State = cursor.getString(cursor.getColumnIndex("State"));
            breastRegist.SummaryId = cursor.getString(cursor.getColumnIndex("SummaryId"));
            breastRegist.PrintState = cursor.getString(cursor.getColumnIndex("PrintState"));
            breastRegist.ThawDate = cursor.getString(cursor.getColumnIndex("ThawDate"));
            breastRegist.ThawTime = cursor.getString(cursor.getColumnIndex("ThawTime"));
            breastRegist.ThawGH = cursor.getString(cursor.getColumnIndex("ThawGH"));
            breastRegist.MilkPumpDate = cursor.getString(cursor.getColumnIndex("MilkPumpDate"));
            breastRegist.MilkPumpTime = cursor.getString(cursor.getColumnIndex("MilkPumpTime"));
            breastRegist.QRcode = cursor.getString(cursor.getColumnIndex("QRcode"));
            breastRegist.YXQ = cursor.getString(cursor.getColumnIndex("YXQ"));
            breastRegist.RoomNo = cursor.getString(cursor.getColumnIndex("RoomNo"));
            breastRegists.add(breastRegist);
        }
        cursor.close();
        return breastRegists;
    }
}
