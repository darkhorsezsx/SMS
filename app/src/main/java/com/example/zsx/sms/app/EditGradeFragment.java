package com.example.zsx.sms.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.GradeAdapterForEdit;
import com.example.zsx.sms.controller.MySQLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/13.
 */
public class EditGradeFragment extends Fragment {
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    Cursor cursor;
    Context context;
    View mView;
    TextView tv_name;
    ListView listView;
    BootstrapButton btn_done;
    FontAwesomeText tv1 ;
    String sid;
    String sname;
    String lid;
    String lname;

    String sql = "select * from lesson natural join ls where sid = ?";

    private ArrayList<HashMap<String, Object>> data;

    GradeAdapterForEdit adapter;

    public EditGradeFragment(){}


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        Bundle bundle = getArguments();
        sid = bundle.getString("sid");
        sname = bundle.getString("sname");



        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();

        data = new ArrayList<HashMap<String,Object>>();
        cursor = db.rawQuery(sql,new String[]{sid});

        if(cursor.moveToFirst()){
            do{
                lid = cursor.getString(cursor.getColumnIndex("lid"));
                int score = cursor.getInt(cursor.getColumnIndex("score"));
                String lname = cursor.getString(cursor.getColumnIndex("lname"));
                Log.d("studentname", sname);
                Log.d("studentid",lid);
                Log.d("studentphone",score+"");
                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                tempHashMap.put("sid",sid);
                tempHashMap.put("sname", sname);
                tempHashMap.put("lid",lid);
                tempHashMap.put("score",score+"");
                tempHashMap.put("lname",lname);
                data.add(tempHashMap);
            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter = new GradeAdapterForEdit(context,data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_editgrade, container, false);
        listView = (ListView) mView.findViewById(R.id.lv_editgrade);
        btn_done = (BootstrapButton) mView.findViewById(R.id.btn_done_editgrade);
        tv_name = (TextView) mView.findViewById(R.id.tv_showname_editgrade);

        tv_name.setText(sname);
        FontAwesomeText tv1 = (FontAwesomeText) mView.findViewById(R.id.tv_awesome_chooselesson);
        tv1.startFlashing(context, true, FontAwesomeText.AnimationSpeed.FAST);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        listView.setAdapter(adapter);

        return mView;
    }

}
