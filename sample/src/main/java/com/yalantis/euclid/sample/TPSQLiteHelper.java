package com.yalantis.euclid.sample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yalantis.euclid.global.GlobalConfig;

/**
 * Created by bbwang on 2016/8/14.
 */
public class TPSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteDB";

    public TPSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,  factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ GlobalConfig.TABLE_TASK_LIST_UC
                +"(id int,name varchar(32),uc long,create_time int, update_time TimeStamp NOT NULL DEFAULT CURRENT_TIMESTAMP)";
        //输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
        //execSQL函数用于执行SQL语句
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }
}
