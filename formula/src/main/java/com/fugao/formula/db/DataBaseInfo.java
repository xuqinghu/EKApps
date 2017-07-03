package com.fugao.formula.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fugao.formula.constant.Constant;

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
            String timeList = "CREATE TABLE " + Constant.TIME_LIST
                    + " (Name varchar(30)," + "State varchar(50))";
            db.execSQL(timeList);
            String milkName = "CREATE TABLE " + Constant.MILK_NAME
                    + " (MilkCode varchar(30)," + "MilkName varchar(50))";
            db.execSQL(milkName);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                // TODO 更新数据库版本的时候 要保存以前的数据的操作
                db.execSQL("drop table if exists " + Constant.TIME_LIST);
                db.execSQL("drop table if exists " + Constant.MILK_NAME);
            }
            onCreate(db);
        }
    }

}
