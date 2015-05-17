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

import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.LessonAdapter;
import com.example.zsx.sms.controller.MySQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/9.
 */
public class QueryLessonFragment extends Fragment {

    private View mView;
    private Context mContext;
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private ListView listView;

    ArrayList<HashMap<String, Object>> data;
    LessonAdapter adapter;

    String name;
    String id;
    Cursor cursor;

    public QueryLessonFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mySQLiteHelper = new MySQLiteHelper(mContext,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();



        Bundle bundle = getArguments();
        name = bundle.getString("name");
        id = bundle.getString("id");
        Log.d("queryfrg name",name);
        Log.d("queryfrg id",id);

        data = new ArrayList<HashMap<String,Object>>();

        if(name.equals("") && id.equals("")){   //default query all
            cursor = db.query("lesson",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{

                    String name = cursor.getString(cursor.getColumnIndex("lname"));
                    String id = cursor.getString(cursor.getColumnIndex("lid"));
                    int credit = cursor.getInt(cursor.getColumnIndex("lcredit"));
                    Log.d("qqname", name);
                    Log.d("qqid",id);
                    Log.d("qqenglish",credit+"");
                    HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                    tempHashMap.put("lid",id);
                    tempHashMap.put("lname", name);
                    tempHashMap.put("lcredit",credit+"");
                    data.add(tempHashMap);
                }while(cursor.moveToNext());
            }
        }
        else{                                                      //query in some conditions
            cursor = db.query("lesson",null,"lid like ? or lname like ?",new String[]{id,name},null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex("lname"));
                    String id = cursor.getString(cursor.getColumnIndex("lid"));
                    int credit = cursor.getInt(cursor.getColumnIndex("lcredit"));
                    Log.d("qqname", name);
                    Log.d("qqid",id);
                    Log.d("qqenglish",credit+"");
                    HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                    tempHashMap.put("lid",id);
                    tempHashMap.put("lname", name);
                    tempHashMap.put("lcredit",credit+"");
                    data.add(tempHashMap);
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        adapter = new LessonAdapter(mContext, data);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_querylesson, container, false);
        listView = (ListView) mView.findViewById(R.id.lv_querylesson);
        listView.setAdapter(adapter);
        return mView;
    }

    public void changed(){

    }

}
