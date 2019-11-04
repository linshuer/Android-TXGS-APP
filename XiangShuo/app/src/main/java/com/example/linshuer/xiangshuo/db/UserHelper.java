package com.example.linshuer.xiangshuo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Linshuer on 2018/6/3.
 */

public class UserHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user_info";//数据库名字
    private static final String TABLE_NAME = "user";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号

    private static final String CREATE_TABLE = "create table if not exists user ("
            + "username varchar primary key ,"
            + "password varchar, "
            + "headimage varchar, "
            + "nickname varchar, "
            + "sex varchar, "
            + "address varchar, "
            + "autograph varchar,"//数据库里的表
            + "theme varchar)";
    public UserHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public UserHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}

