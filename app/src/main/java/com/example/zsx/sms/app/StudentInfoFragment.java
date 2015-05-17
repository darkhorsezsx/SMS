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

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.controller.StudentInfoAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/12.
 */
public class StudentInfoFragment extends Fragment {

    Context context;
    private View mView;
    private BootstrapEditText et_id;
    private BootstrapEditText et_name;
    private BootstrapButton btn_query;
    String name;
    String id;
    int credit = 0;
    Context mContext;
    ListView listView;

    ArrayList<HashMap<String, Object>> data;
    StudentInfoAdapter adapter;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    Cursor cursor;

    public StudentInfoFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();



        data = new ArrayList<HashMap<String,Object>>();
        Cursor cursor = db.query("student",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("sname"));
                String id = cursor.getString(cursor.getColumnIndex("sid"));
                String phone = cursor.getString(cursor.getColumnIndex("sphone"));
                int age = cursor.getInt(cursor.getColumnIndex("sage"));
                String gender = cursor.getString(cursor.getColumnIndex("sgender"));
                String classid = cursor.getString(cursor.getColumnIndex("sclass"));
                Log.d("studentname", name);
                Log.d("studentid",id);
                Log.d("studentphone",phone);
                Log.d("studentage", age+"");
                Log.d("studentgender",gender);
                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                tempHashMap.put("sid",id);
                tempHashMap.put("sname", name);
                tempHashMap.put("sphone",phone);
                tempHashMap.put("sgender",gender);
                tempHashMap.put("sclass", classid);
                tempHashMap.put("sage",age);
                data.add(tempHashMap);
            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter = new StudentInfoAdapter(context,data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_editinfo, container, false);

        et_id = (BootstrapEditText)mView.findViewById(R.id.et_queryid_studentinfofrg);
        et_name = (BootstrapEditText)mView.findViewById(R.id.et_queryname_studentinfofrg);
        btn_query = (BootstrapButton)mView.findViewById(R.id.btn_query_studentinfofrg);
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_id.getText().toString();
                name = et_name.getText().toString();

                data.clear();

                if(name.equals("") && id.equals("")){   //default query all
                    cursor = db.query("student",null,null,null,null,null,null);
                    if(cursor.moveToFirst()){
                        do{

                            String name = cursor.getString(cursor.getColumnIndex("sname"));
                            String id = cursor.getString(cursor.getColumnIndex("sid"));
                            String phone = cursor.getString(cursor.getColumnIndex("sphone"));
                            int age = cursor.getInt(cursor.getColumnIndex("sage"));
                            String gender = cursor.getString(cursor.getColumnIndex("sgender"));
                            String classid = cursor.getString(cursor.getColumnIndex("sclass"));
                            Log.d("studentname", name);
                            Log.d("studentid",id);
                            Log.d("studentphone",phone);
                            Log.d("studentage", age+"");
                            Log.d("studentgender",gender);
                            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                            tempHashMap.put("sid",id);
                            tempHashMap.put("sname", name);
                            tempHashMap.put("sphone",phone);
                            tempHashMap.put("sgender",gender);
                            tempHashMap.put("sclass", classid);
                            tempHashMap.put("sage",age);
                            data.add(tempHashMap);
                        }while(cursor.moveToNext());
                    }
                }
                else{                                                      //query in some conditions
                    cursor = db.query("student",null,"sid like ? or sname like ?",new String[]{id,name},null,null,null);
                    if(cursor.moveToFirst()){
                        do{
                            String name = cursor.getString(cursor.getColumnIndex("sname"));
                            String id = cursor.getString(cursor.getColumnIndex("sid"));
                            String phone = cursor.getString(cursor.getColumnIndex("sphone"));
                            int age = cursor.getInt(cursor.getColumnIndex("sage"));
                            String gender = cursor.getString(cursor.getColumnIndex("sgender"));
                            String classid = cursor.getString(cursor.getColumnIndex("sclass"));
                            Log.d("studentname", name);
                            Log.d("studentid",id);
                            Log.d("studentphone",phone);
                            Log.d("studentage", age+"");
                            Log.d("studentgender",gender);
                            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                            tempHashMap.put("sid",id);
                            tempHashMap.put("sname", name);
                            tempHashMap.put("sphone",phone);
                            tempHashMap.put("sgender",gender);
                            tempHashMap.put("sclass", classid);
                            tempHashMap.put("sage",age);
                            data.add(tempHashMap);
                        }while(cursor.moveToNext());
                    }
                }
                cursor.close();
                adapter.notifyDataSetChanged();
            }
        });
        listView = (ListView) mView.findViewById(R.id.lv_editinfo);
        listView.setAdapter(adapter);
        return mView;
    }
}
