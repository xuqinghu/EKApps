package com.fugao.breast.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fugao.breast.constant.Constant;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class DataBaseInfo {
    private String DB_NAME = "breast.db";

    private int DB_VERSION = 30;

    private SQLiteDatabase sqlDB;
    public static DataBaseInfo dataBaseInfoCurrent;

    /**
     * 初始化数据库
     *
     * @param context
     */
    private DataBaseInfo(Context context) {
        if (sqlDB == null || (!sqlDB.isOpen())) {
            sqlDB = new DataBaseHelper(context).getWritableDatabase();
        }

    }

    public static DataBaseInfo getInstance(Context context) {
        if (dataBaseInfoCurrent == null) {
            dataBaseInfoCurrent = new DataBaseInfo(context.getApplicationContext());
        }
        return dataBaseInfoCurrent;
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (sqlDB != null) {
            sqlDB.close();
            sqlDB = null;
        }
        if (dataBaseInfoCurrent != null) {
            dataBaseInfoCurrent = null;
        }
    }

    public SQLiteDatabase getSqlDB() {
        return sqlDB;
    }

    public void setSqlDB(SQLiteDatabase sqlDB) {
        this.sqlDB = sqlDB;
    }

    /**
     * 查询表是否为空
     *
     * @param tablename tableAdviceCategory
     * @return
     */
    public boolean tableIsEmpty(String tablename) {
        // 返回true为空，
        boolean flag = false;
        Cursor cursor = sqlDB.query(tablename, null, null, null, null, null,
                null);
        int x = cursor.getCount();
        cursor.close();
        if (x == 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public DataBaseHelper(Context context) {
            this(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String breastList_Sql = "CREATE TABLE " + Constant.BREAST_LIST
                    + " (Id varchar(30)," + "Name varchar(100),"
                    + "Sex varchar(50)," + "Pid varchar(50)," + "BedNo varchar(50),"
                    + "DepartmentId varchar(50)," + "WardId varchar(50),"
                    + "DepartmentName varchar(100)," + "WardName varchar(100),"
                    + "YZTime varchar(100)," + "YZdosis varchar(100),"
                    + "ThawAmount  varchar(50)," + "ThawAccount varchar(50),"
                    + "YZZL varchar(50)," + "RoomNo varchar(50))";
            db.execSQL(breastList_Sql);
            String breastDetail_Sql = "CREATE TABLE " + Constant.BEAST_DETAIL
                    + " (Pid varchar(50)," + "Amount varchar(30)," + "MilkBoxId varchar(100),"
                    + "MilkBoxNo varchar(50)," + "CoorDinateID varchar(50)," + "CoorDinate varchar(50),"
                    + "Remarks varchar(50)," + "State varchar(50),"
                    + "SummaryId varchar(100)," + "PrintState varchar(100),"
                    + "ThawDate varchar(100)," + "ThawTime varchar(100),"
                    + "ThawGH varchar(100)," + "MilkPumpDate varchar(100),"
                    + "MilkPumpTime varchar(100)," + "QRcode varchar(100),"
                    + "YXQ varchar(50)," + "MilkBoxState varchar(50)," + "Upload varchar(50))";
            db.execSQL(breastDetail_Sql);
            String putList_Sql = "CREATE TABLE " + Constant.PUT_LIST
                    + " (Id varchar(30)," + "Name varchar(100),"
                    + "Sex varchar(50)," + "Pid varchar(50)," + "BedNo varchar(50),"
                    + "DepartmentId varchar(50)," + "WardId varchar(50),"
                    + "DepartmentName varchar(100)," + "WardName varchar(100),"
                    + "YZTime varchar(100)," + "YZdosis varchar(100),"
                    + "ThawAmount  varchar(50)," + "ThawAccount varchar(50),"
                    + "CFAccount varchar(50)," + "CFAmount varchar(50),"
                    + "CoorDinateID  varchar(50)," + "RoomNo varchar(50),"
                    + "CoorDinate  varchar(50)," + "Remarks varchar(50),"
                    + "MilkBoxId  varchar(50)," + "MilkBoxNo varchar(50),"+ "MilkBoxState varchar(50),"
                    + "YZZL varchar(50))";
            db.execSQL(putList_Sql);
            String regist_Sql = "CREATE TABLE " + Constant.BREAST_REGIST
                    + " (Pid varchar(50)," + "Amount varchar(30)," + "MilkBoxId varchar(100),"
                    + "MilkBoxNo varchar(50)," + "CoorDinateID varchar(50)," + "CoorDinate varchar(50),"
                    + "Remarks varchar(50)," + "State varchar(50),"
                    + "SummaryId varchar(100)," + "PrintState varchar(100),"
                    + "ThawDate varchar(100)," + "ThawTime varchar(100),"
                    + "ThawGH varchar(50)," + "MilkPumpDate varchar(100),"
                    + "MilkPumpTime varchar(100)," + "QRcode varchar(50)," + "YXQ varchar(50),"
                    + "Name varchar(100)," + "BedNo varchar(100)," + "RoomNo varchar(50))";
            db.execSQL(regist_Sql);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                // TODO 更新数据库版本的时候 要保存以前的数据的操作
                db.execSQL("drop table if exists " + Constant.BREAST_LIST);
                db.execSQL("drop table if exists " + Constant.BEAST_DETAIL);
                db.execSQL("drop table if exists " + Constant.PUT_LIST);
                db.execSQL("drop table if exists " + Constant.BREAST_REGIST);
            }
            onCreate(db);
        }
    }

}
