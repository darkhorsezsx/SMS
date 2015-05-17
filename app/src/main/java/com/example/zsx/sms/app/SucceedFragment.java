package com.example.zsx.sms.app;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.modle.Student;

/**
 * Created by zsx on 2015/5/8.
 */
public class SucceedFragment extends Fragment {

    View mView;
    Context mContext;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    public SucceedFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mySQLiteHelper = new MySQLiteHelper(mContext,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_succeed, container, false);

        Cursor cursor = db.query("lesson",null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("lname"));
                String id = cursor.getString(cursor.getColumnIndex("lid"));
                int credit = cursor.getInt(cursor.getColumnIndex("lcredit"));
                Log.d("qqname",name);
                Log.d("qqid",id);
                Log.d("qqenglish",credit+"");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return mView;
    }
}
