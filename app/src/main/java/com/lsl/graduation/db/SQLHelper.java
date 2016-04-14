package com.lsl.graduation.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Forrest on 16/4/13.
 */
public class SQLHelper extends SQLiteOpenHelper{
    public static final String DB_NAME="database.db";
    public static final int VERSION=1;
    public static final String TABLE_CHANNEL="ChannelItem";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String ORDERID="orderId";
    public static final String SELECTED="selected";
    private  Context context;

    public SQLHelper(Context context){
        super(context,DB_NAME,null,VERSION);
        this.context=context;
    }
    public Context getContext(){
        return context;
    }
    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        //创建 数据库后，对数据库的操作
//        String sql="create table if not exists"+TABLE_CHANNEL+
//                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                ID+"INTEGER,"+
//                NAME+"TEXT,"+
//                ORDERID+"INTEGER,"+
//                SELECTED+"TEXT)";
//        db.execSQL(sql);
//  创建数据库后，对数据库的操作 如果不存在就创建，并将id作为自增长属性
        String sql = "create table if not exists " + TABLE_CHANNEL +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID + " INTEGER , " +
                NAME + " TEXT , " +
                ORDERID + " INTEGER , " +
                SELECTED + " TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更改数据库版本操作
        onCreate(db);
    }
}
