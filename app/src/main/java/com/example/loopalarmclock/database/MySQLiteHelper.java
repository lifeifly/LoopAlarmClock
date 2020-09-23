package com.example.loopalarmclock.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table Loop(" +
            "id Integer primary key autoincrement," +
            "type text," +
            "tag Integer," +
            "week text," +
            "year Integer," +
            "month Integer," +
            "day Integer," +
            "hour Integer," +
            "minute Integer," +
            "dd Integer," +
            "dh Integer," +
            "dm Integer," +
            "label text," +
            "vabrate Integer," +
            "enable Integer," +
            "time Integer," +
            "times Integer," +
            "interval Integer)";

    public MySQLiteHelper(Context context) {
        super(context, "alarm.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
