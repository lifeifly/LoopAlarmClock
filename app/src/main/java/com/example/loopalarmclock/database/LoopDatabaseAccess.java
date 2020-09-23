package com.example.loopalarmclock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.loopalarmclock.bean.Constants;
import com.example.loopalarmclock.bean.LoopAlarmBean;
import com.example.loopalarmclock.utils.StartCloseAlarm;

import java.util.ArrayList;
import java.util.List;

public class LoopDatabaseAccess {

    private static SQLiteDatabase database;
    private static StartCloseAlarm startCloseAlarm;
    public static void initDB(Context context) {
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(context);
        database = mySQLiteHelper.getWritableDatabase();
        startCloseAlarm=StartCloseAlarm.getInstance(context);
    }

    public static void insertData(String tableName, ContentValues values) {
        database.insert(tableName, null, values);
    }

    public static void deleteItemData(String tableName, String tag) {
        LoopAlarmBean bean =queryItemData(Integer.parseInt(tag));
        startCloseAlarm.cancelAlarm(bean);
        database.delete(tableName, "tag=?", new String[]{tag});
    }

    public static List<LoopAlarmBean> queryAllData(String tableName) {
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        List<LoopAlarmBean> data = new ArrayList<>();
        //将查询到的所有数据封装成对象，添加到集合并返回
        while (cursor.moveToNext()) {
            String label = cursor.getString(cursor.getColumnIndex(Constants.LABEL));
            String type = cursor.getString(cursor.getColumnIndex(Constants.TYPE));
            String week = cursor.getString(cursor.getColumnIndex(Constants.WEEK));
            int year = cursor.getInt(cursor.getColumnIndex(Constants.YEAR));
            int month = cursor.getInt(cursor.getColumnIndex(Constants.MONTH));
            int day = cursor.getInt(cursor.getColumnIndex(Constants.DAY));
            int hour = cursor.getInt(cursor.getColumnIndex(Constants.HOUR));
            int minute = cursor.getInt(cursor.getColumnIndex(Constants.MINUTE));
            int dd = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_DAY));
            int dh = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_HOUR));
            int dm = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_MINUTE));
            int vabrate = cursor.getInt(cursor.getColumnIndex(Constants.VABRATE));
            int enable = cursor.getInt(cursor.getColumnIndex(Constants.ENABLE));
            int tag = cursor.getInt(cursor.getColumnIndex(Constants.TAG));
            int time = cursor.getInt(cursor.getColumnIndex(Constants.TIME));
            int times = cursor.getInt(cursor.getColumnIndex(Constants.TIMES));
            int interval = cursor.getInt(cursor.getColumnIndex(Constants.INTERVAL));
            LoopAlarmBean bean = new LoopAlarmBean(type, week, tag, year, month, day, hour, minute, dd, dh, dm, vabrate, label, enable, time, times, interval);
            //开启闹钟
            if (enable==1){
                startCloseAlarm.startAlarm(bean);
            }
            data.add(bean);
        }
        return data;
    }

    public static int updateData(String tableName, ContentValues values, int tag) {
        int itemPosition = database.update(tableName, values, "tag=?", new String[]{String.valueOf(tag)});
        LoopAlarmBean bean=queryItemData(tag);
        if (bean.getEnable()==1){
            startCloseAlarm.startAlarm(bean);
        }else if (bean.getEnable()==0){
            startCloseAlarm.cancelAlarm(bean);
        }
        return itemPosition;
    }

    public static LoopAlarmBean queryItemData(int tag) {
        LoopAlarmBean bean=null;
        Cursor cursor=database.query("Loop",null,"tag=?",new String[]{String.valueOf(tag)},null,null,null);
        while (cursor.moveToNext()){
            String label = cursor.getString(cursor.getColumnIndex(Constants.LABEL));
            String type = cursor.getString(cursor.getColumnIndex(Constants.TYPE));
            String week = cursor.getString(cursor.getColumnIndex(Constants.WEEK));
            int year = cursor.getInt(cursor.getColumnIndex(Constants.YEAR));
            int month = cursor.getInt(cursor.getColumnIndex(Constants.MONTH));
            int day = cursor.getInt(cursor.getColumnIndex(Constants.DAY));
            int hour = cursor.getInt(cursor.getColumnIndex(Constants.HOUR));
            int minute = cursor.getInt(cursor.getColumnIndex(Constants.MINUTE));
            int dd = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_DAY));
            int dh = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_HOUR));
            int dm = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_MINUTE));
            int vabrate = cursor.getInt(cursor.getColumnIndex(Constants.VABRATE));
            int enable = cursor.getInt(cursor.getColumnIndex(Constants.ENABLE));
            int time = cursor.getInt(cursor.getColumnIndex(Constants.TIME));
            int times = cursor.getInt(cursor.getColumnIndex(Constants.TIMES));
            int interval = cursor.getInt(cursor.getColumnIndex(Constants.INTERVAL));
            bean = new LoopAlarmBean(type, week, tag, year, month, day, hour, minute, dd, dh, dm, vabrate, label, enable, time, times, interval);
        }
        return bean;
    }

}
