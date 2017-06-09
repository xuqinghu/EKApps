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
 * Created by Administrator on 2017/5/24 0024.
 */

public class BreastDetailDao {
    public String TAG = "BreastDetailDao";
    public DataBaseInfo dataBaseInfo;
    public SQLiteDatabase sqlDB;

    public BreastDetailDao(DataBaseInfo dataBaseInfoCurrent) {
        dataBaseInfo = dataBaseInfoCurrent;
        sqlDB = dataBaseInfo.getSqlDB();
    }

    public boolean isEmpty() {
        return dataBaseInfo.tableIsEmpty(Constant.BEAST_DETAIL);
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        dataBaseInfo.closeDB();
    }

    public int deleteAllInfo() {
        return sqlDB.delete(Constant.BEAST_DETAIL, null, null);
    }

    public void saveToBreastDetail(List<BreastMilkDetial> breastMilkDetials, String pid, String upload) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        // 数据插入操作循环
        for (BreastMilkDetial breastMilkDetial : breastMilkDetials) {
            contentValues = new ContentValues();
            contentValues.put("Pid", StringUtils.getString(pid));
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
            contentValues.put("MilkBoxState", StringUtils.getString(breastMilkDetial.MilkBoxState));
            contentValues.put("Upload", upload);
            sqlDB.insert(Constant.BEAST_DETAIL, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存数据到本地数据库 完成");
    }

    public void saveToPutBreastDetail(List<BreastMilkDetial> breastMilkDetials, String pid, String milkBoxState,
                                      String upload) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        // 数据插入操作循环
        for (BreastMilkDetial breastMilkDetial : breastMilkDetials) {
            contentValues = new ContentValues();
            contentValues.put("Pid", StringUtils.getString(pid));
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
            contentValues.put("MilkBoxState", StringUtils.getString(milkBoxState));
            contentValues.put("Upload", upload);
            sqlDB.insert(Constant.BEAST_DETAIL, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存数据到本地数据库 完成");
    }

    public void updateData(BreastMilkDetial breastMilkDetial, String upload, String pid) {
        String sql = "update " + Constant.BEAST_DETAIL + " set State='"
                + breastMilkDetial.State + "',ThawDate='" + breastMilkDetial.ThawDate
                + "',ThawTime='" + breastMilkDetial.ThawTime + "',ThawGH='"
                + breastMilkDetial.ThawGH + "',Upload='" + upload
                + "' where Pid='" + pid + "' and QRcode='" + breastMilkDetial.QRcode + "'";
        sqlDB.execSQL(sql);
    }

    //通过分格id修改位置的状态
    public void updateMilkBoxState(String coorDinateID, String milkBoxState) {
        String sql = "update " + Constant.BEAST_DETAIL + " set MilkBoxState='" + milkBoxState
                + "' where CoorDinateID='" + coorDinateID + "'";
        sqlDB.execSQL(sql);
    }

    //根据奶的二维码修改位置信息
    public void updatePlaceByQRcode(BreastMilkDetial breastMilkDetial, String upload) {
        String sql = "update " + Constant.BEAST_DETAIL + " set MilkBoxId='"
                + breastMilkDetial.MilkBoxId + "',MilkBoxNo='" + breastMilkDetial.MilkBoxNo
                + "',CoorDinateID='" + breastMilkDetial.CoorDinateID + "',CoorDinate='"
                + breastMilkDetial.CoorDinate + "',Remarks='" + breastMilkDetial.Remarks
                + "',ThawDate='" + breastMilkDetial.ThawDate + "',ThawGH='" + breastMilkDetial.ThawGH
                + "',Upload='" + upload + "' where QRcode='" + breastMilkDetial.QRcode + "'";
        sqlDB.execSQL(sql);
    }

    //获取这条数据的状态，看upload是0还是1
    public String getState(BreastMilkDetial breastMilkDetial, String pid) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Pid='" + pid + "' and QRcode='"
                + breastMilkDetial.QRcode + "'", null, null, null, null);
        String upload = "";
        while (cursor.moveToNext()) {
            upload = cursor.getString(cursor.getColumnIndex("Upload"));
        }
        cursor.close();
        return upload;
    }

    /**
     * 根据上传状态获取数据
     *
     * @param upload
     * @return
     */
    public List<BreastMilkDetial> getBreastMilkDetialByUpload(String upload) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Upload='" + upload + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    /**
     * 根据住院号和二维码获取数据
     *
     * @param pid
     * @param qrCode
     * @return
     */
    public List<BreastMilkDetial> getBreastMilkDetialByPidAndQRcode(String pid, String qrCode) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Pid='" + pid + "' and QRcode='" + qrCode + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    /**
     * 根据二维码获取数据
     *
     * @param qrCode
     * @return
     */
    public List<BreastMilkDetial> getBreastMilkDetialByQRcode(String qrCode) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "QRcode='" + qrCode + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    //获取今日解冻or补充解冻数据
    public List<BreastMilkDetial> getBreastMilkDetialByDateAndState(String date, String state, String pid) {
        List<BreastMilkDetial> breastMilkDetials = new ArrayList<>();
        breastMilkDetials.addAll(getBreastMilkDetialByState(state, pid));
        breastMilkDetials.addAll(getBreastMilkDetialByDate(date, pid));
        return breastMilkDetials;
    }

    public List<BreastMilkDetial> getBreastMilkDetialByState(String state, String pid) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Pid='" + pid + "' and State='" + state + "'", null, null, null, "CoorDinateID");
        return getBreastDetailByCursor(cursor);
    }

    public List<BreastMilkDetial> getBreastMilkDetialByDate(String date, String pid) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Pid='" + pid + "' and ThawDate='" + date + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    /**
     * 根据住院号获取数据
     *
     * @param pid
     * @return
     */
    public List<BreastMilkDetial> getBreastMilkDetialByPid(String pid) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "Pid='" + pid + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    /**
     * 根据分格ID删除母乳信息
     */
    public void deleteDetailByCoorDinateID(String coorDinateID) {
        String whereClause = "CoorDinateID=?";
        String[] whereArgs = new String[]{coorDinateID};
        sqlDB.delete(Constant.BEAST_DETAIL, whereClause, whereArgs);
    }

    /**
     * 根据存放位置查找存放详情
     *
     * @param coorDinateID
     * @param state
     * @return
     */
    public List<BreastMilkDetial> getPutDetailByCoorDinateID(String coorDinateID, String state) {
        Cursor cursor = sqlDB.query(Constant.BEAST_DETAIL, null, "CoorDinateID='" + coorDinateID + "' and State='" + state + "'", null, null, null, null);
        return getBreastDetailByCursor(cursor);
    }

    /**
     * 母乳存放扫描插入数据
     *
     * @param breastMilkDetial
     * @param upload
     */
    public void insertBreastMilk(BreastMilkDetial breastMilkDetial, String upload) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Pid", breastMilkDetial.Pid);
        contentValues.put("Amount", breastMilkDetial.Amount);
        contentValues.put("MilkBoxId", breastMilkDetial.MilkBoxId);
        contentValues.put("MilkBoxNo", breastMilkDetial.MilkBoxNo);
        contentValues.put("CoorDinateID", breastMilkDetial.CoorDinateID);
        contentValues.put("CoorDinate", breastMilkDetial.CoorDinate);
        contentValues.put("Remarks", breastMilkDetial.Remarks);
        contentValues.put("State", breastMilkDetial.State);
        contentValues.put("SummaryId", breastMilkDetial.SummaryId);
        contentValues.put("PrintState", breastMilkDetial.PrintState);
        contentValues.put("ThawDate", breastMilkDetial.ThawDate);
        contentValues.put("ThawTime", breastMilkDetial.ThawTime);
        contentValues.put("ThawGH", breastMilkDetial.ThawGH);
        contentValues.put("MilkPumpDate", breastMilkDetial.MilkPumpDate);
        contentValues.put("MilkPumpTime", breastMilkDetial.MilkPumpTime);
        contentValues.put("QRcode", breastMilkDetial.QRcode);
        contentValues.put("YXQ", breastMilkDetial.YXQ);
        contentValues.put("MilkBoxState", breastMilkDetial.MilkBoxState);
        contentValues.put("Upload", upload);
        sqlDB.insert(Constant.BEAST_DETAIL, null, contentValues);
    }

    //转换位置时需要用到
    public void insertBreastMilkByCoorDinateID(String pid, List<BreastMilkDetial> breastMilkDetials, PutBreastMilk putBreastMilk) {
        sqlDB.beginTransaction(); // 手动设置开始事务
        ContentValues contentValues;
        // 数据插入操作循环
        for (BreastMilkDetial breastMilkDetial : breastMilkDetials) {
            contentValues = new ContentValues();
            contentValues.put("Pid", StringUtils.getString(pid));
            contentValues.put("Amount", StringUtils.getString(breastMilkDetial.Amount));
            contentValues.put("MilkBoxId", StringUtils.getString(putBreastMilk.MilkBoxId));
            contentValues.put("MilkBoxNo", StringUtils.getString(putBreastMilk.MilkBoxNo));
            contentValues.put("CoorDinateID", StringUtils.getString(putBreastMilk.CoorDinateID));
            contentValues.put("CoorDinate", StringUtils.getString(putBreastMilk.CoorDinate));
            contentValues.put("Remarks", StringUtils.getString(putBreastMilk.Remarks));
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
            contentValues.put("MilkBoxState", StringUtils.getString(breastMilkDetial.MilkBoxState));
            contentValues.put("Upload", 0);
            sqlDB.insert(Constant.BEAST_DETAIL, null, contentValues);
        }
        sqlDB.setTransactionSuccessful();
        sqlDB.endTransaction();
        Log.d(TAG, "保存数据到本地数据库 完成");

    }


    public List<BreastMilkDetial> getBreastDetailByCursor(Cursor cursor) {
        List<BreastMilkDetial> breastMilkDetials = new ArrayList<>();
        while (cursor.moveToNext()) {
            BreastMilkDetial breastMilkDetial = new BreastMilkDetial();
            breastMilkDetial.Pid = cursor.getString(cursor.getColumnIndex("Pid"));
            breastMilkDetial.Amount = cursor.getString(cursor.getColumnIndex("Amount"));
            breastMilkDetial.MilkBoxId = cursor.getString(cursor.getColumnIndex("MilkBoxId"));
            breastMilkDetial.MilkBoxNo = cursor.getString(cursor.getColumnIndex("MilkBoxNo"));
            breastMilkDetial.CoorDinateID = cursor.getString(cursor.getColumnIndex("CoorDinateID"));
            breastMilkDetial.CoorDinate = cursor.getString(cursor.getColumnIndex("CoorDinate"));
            breastMilkDetial.Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));
            breastMilkDetial.State = cursor.getString(cursor.getColumnIndex("State"));
            breastMilkDetial.SummaryId = cursor.getString(cursor.getColumnIndex("SummaryId"));
            breastMilkDetial.PrintState = cursor.getString(cursor.getColumnIndex("PrintState"));
            breastMilkDetial.ThawDate = cursor.getString(cursor.getColumnIndex("ThawDate"));
            breastMilkDetial.ThawTime = cursor.getString(cursor.getColumnIndex("ThawTime"));
            breastMilkDetial.ThawGH = cursor.getString(cursor.getColumnIndex("ThawGH"));
            breastMilkDetial.MilkPumpDate = cursor.getString(cursor.getColumnIndex("MilkPumpDate"));
            breastMilkDetial.MilkPumpTime = cursor.getString(cursor.getColumnIndex("MilkPumpTime"));
            breastMilkDetial.QRcode = cursor.getString(cursor.getColumnIndex("QRcode"));
            breastMilkDetial.YXQ = cursor.getString(cursor.getColumnIndex("YXQ"));
            breastMilkDetial.MilkBoxState = cursor.getString(cursor.getColumnIndex("MilkBoxState"));
            breastMilkDetials.add(breastMilkDetial);
        }
        cursor.close();
        return breastMilkDetials;
    }
}
