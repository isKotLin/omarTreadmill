package com.vigorchip.omatreadmill.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wr-app1 on 2018/6/13.
 * 本地的运动记录，用于用户没登录的状态下listview显示机器的跑步记录
 */

public class MySQLiteHelper extends SQLiteOpenHelper{
    public static final String CREAT_RECORD = "create table record(" +
            "id integer primary key autoincrement," +
            "starttime long," +
            "alltime long," +
            "distance double," +
            "calories double)";

    public MySQLiteHelper(Context context) {
        super(context,"run_record.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAT_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
