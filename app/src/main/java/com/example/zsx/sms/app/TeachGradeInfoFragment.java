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
import android.widget.ListView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.GradeAdapter;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.controller.TeachGradeAdapter;
import com.example.zsx.sms.modle.Grade;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/9.
 */
public class TeachGradeInfoFragment extends Fragment {
    private View mView;
    private Context mContext;
    private ListView listView;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    ArrayList<Grade> list;
    Cursor idcursor;

    FontAwesomeText at_share;
    FontAwesomeText at_link;

    public TeachGradeInfoFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mySQLiteHelper = new MySQLiteHelper(mContext,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();



        list = new ArrayList<Grade>();
        idcursor = db.query("lesson",null,null,null,null,null,null);
        if(idcursor.moveToFirst()){
            do{
                String name = idcursor.getString(idcursor.getColumnIndex("lname"));
                String id = idcursor.getString(idcursor.getColumnIndex("lid"));
                int credit = idcursor.getInt(idcursor.getColumnIndex("lcredit"));
                int sum = getSum(id);
                int great = getGreat(id);
                int normal = getNormal(id);
                int fail = getFail(id);
                Grade grade = new Grade(name,id,credit,sum,great,normal,fail);
                //Grade grade = new Grade("name","22",2,20,3,11,5);
                list.add(grade);
                Log.d("qqname", name);
                Log.d("qqid",id);
                Log.d("qqenglish",credit+"");
            }while(idcursor.moveToNext());
        }
        idcursor.close();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_teachgradeinfo, container, false);
        TeachGradeAdapter gradeAdapter = new TeachGradeAdapter(mContext,R.layout.card_teachgrade,list);
        listView = (ListView)mView.findViewById(R.id.lv_gradeinfo);
        listView.setAdapter(gradeAdapter);
        at_share = (FontAwesomeText) mView.findViewById(R.id.at_share_gradeinfofrg);
        at_link = (FontAwesomeText) mView.findViewById(R.id.at_link_gradeinfofrg);

        at_share.startFlashing(mContext,true, FontAwesomeText.AnimationSpeed.MEDIUM);
        at_link.startRotate(mContext,true, FontAwesomeText.AnimationSpeed.MEDIUM);
        return mView;
    }

    public int getSum(String lid){
        Cursor cursor = db.rawQuery("select count(*) from lesson natural join ls where ls.lid = ?",new String[]{lid});
        cursor.moveToFirst();
        int number = cursor.getInt(0);
        return number;
    }

    public int getGreat(String lid){
        Cursor cursor = db.rawQuery("select count(*) from lesson natural join ls where ls.lid = ? and score >89",new String[]{lid});
        cursor.moveToFirst();
        int number = cursor.getInt(0);
        cursor.close();
        return number;
    }

    public int getNormal(String lid){
        Cursor cursor = db.rawQuery("select count(*) from lesson natural join ls where ls.lid = ? and score <90 and score >59",new String[]{lid});
        cursor.moveToFirst();
        int number = cursor.getInt(0);
        cursor.close();
        return number;
    }

    public int getFail(String lid){
        Cursor cursor = db.rawQuery("select count(*) from lesson natural join ls where ls.lid = ? and score <60",new String[]{lid});
        cursor.moveToFirst();
        int number = cursor.getInt(0);
        cursor.close();
        return number;
    }
}
