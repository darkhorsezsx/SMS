package com.example.zsx.sms.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zsx on 2015/5/9.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    String CREATE_LESSON = "create table lesson(" +
            "lid char(4) primary key not null," +
            "lname varchar(10)," +
            "lcredit integer)";

    String CREATE_STUDENT = "create table student(" +
            "sid char(8) primary key not null," +
            "sname varchar(6)," +
            "sgender char(2)," +
            "sage integer," +
            "sphone char(12)," +
            "sclass char(6))";

    String CREATE_LS = "create table ls(" +
            "sid char(8)," +
            "lid char(4)," +
            "score integer ," +
            "primary key(sid,lid))";

    String CREATE_LOG = "create table log(number integer primary key autoincrement," +
            "date not null)";


    Context mContext;

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context,name,cursorFactory,version);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_STUDENT);
        db.execSQL(CREATE_LESSON);
        db.execSQL(CREATE_LS);
        db.execSQL(CREATE_LOG);
        Toast.makeText(mContext,"succeed", Toast.LENGTH_SHORT).show();
        Log.d("databaseinit","初始化成功");
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if(!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

}
