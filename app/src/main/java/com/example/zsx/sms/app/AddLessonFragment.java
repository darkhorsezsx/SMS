package com.example.zsx.sms.app;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.MySQLiteHelper;

/**
 * Created by zsx on 2015/5/8.
 */
public class AddLessonFragment extends Fragment {

    private View mView;
    private BootstrapEditText et_id;
    private BootstrapEditText et_name;
    private BootstrapEditText et_credit;
    private BootstrapButton btn_add;
    String name;
    String id;
    int credit = 0;
    Context mContext;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    public AddLessonFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mySQLiteHelper = new MySQLiteHelper(mContext,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_addlesson, container, false);

        et_id = (BootstrapEditText)mView.findViewById(R.id.et_addid_addlesfrg);
        et_name = (BootstrapEditText)mView.findViewById(R.id.et_addname_addlesfrg);
        et_credit = (BootstrapEditText)mView.findViewById(R.id.et_addcredit_addlesfrg);
        btn_add = (BootstrapButton)mView.findViewById(R.id.btn_addlesfrg);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                id = et_id.getText().toString();
                String innercredit = et_credit.getText().toString();

                db = mySQLiteHelper.getWritableDatabase();
                Cursor cursornew = db.rawQuery("select lid from lesson where lid = ? or lname = ?",new String[]{id,name});
                if(cursornew.moveToFirst()){
                    Toast.makeText(mContext,"该课程已存在或信息错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                cursornew.close();

                if(isNull(name,id,innercredit)){
                    Toast.makeText(mContext,"信息不完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                credit = Integer.parseInt(innercredit);
                //query database if exists   exist cancle else continue
                /**
                 * add database logic
                 * */
                ContentValues cv = new ContentValues();
                cv.put("lid",id);
                cv.put("lname",name);
                cv.put("lcredit",credit);
                db.insert("lesson",null,cv);
                db.close();
                Toast.makeText(mContext,"新建成功",Toast.LENGTH_SHORT).show();
                reFreshView();
            }
        });
        return mView;
    }
    boolean isNull(String id,String name,String credit){
        if(id.equals("") || name.equals("") || credit.equals(""))
            return true;
        return false;
    }
    public void reFreshView(){
        et_id.setText("");
        et_name.setText("");
        et_credit.setText("");
    }
}
