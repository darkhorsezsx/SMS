package com.example.zsx.sms.app;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.MySQLiteHelper;

/**
 * Created by zsx on 2015/5/8.
 */
public class LessonActivity extends BaseActivity {

    BootstrapButton bbtn_add;
    BootstrapButton bbtn_query;
    BootstrapButton bbtn_grade;

    TextView tv_showid;
    TextView tv_showname;
    BootstrapEditText et_id;
    BootstrapEditText et_name;
    BootstrapButton btn_query;

    MySQLiteHelper mySQLiteHelper;
    Cursor cursorgrade;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        bbtn_add = (BootstrapButton)findViewById(R.id.bbtn_addlesson_lesson);
        bbtn_query = (BootstrapButton)findViewById(R.id.bbtn_querylesson_lesson);
        bbtn_grade = (BootstrapButton)findViewById(R.id.bbtn_gradeinfo_lesson);
        et_id = (BootstrapEditText)findViewById(R.id.et_queryid_titlelesson);
        et_name = (BootstrapEditText)findViewById(R.id.et_queryname_titlelesson);
        btn_query = (BootstrapButton)findViewById(R.id.btn_query_titlelesson);
        tv_showid = (TextView)findViewById(R.id.tv_showid_titlelesson);
        tv_showname = (TextView)findViewById(R.id.tv_showname_titlelesson);

        et_id.setVisibility(View.GONE);
        et_name.setVisibility(View.GONE);
        btn_query.setVisibility(View.GONE);
        tv_showid.setVisibility(View.GONE);
        tv_showname.setVisibility(View.GONE);

        /**
         * when jump to the activity to display the add lesson fragment
         * */
        bbtn_grade.setBootstrapType("default");
        bbtn_query.setBootstrapType("default");
        bbtn_add.setBootstrapType("primary");
        AddLessonFragment addLessonFragment = new AddLessonFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout_lesson,addLessonFragment).commit();


        bbtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbtn_grade.setBootstrapType("default");
                bbtn_query.setBootstrapType("default");
                bbtn_add.setBootstrapType("primary");
                AddLessonFragment addLessonFragment = new AddLessonFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout_lesson,addLessonFragment).commit();
                et_id.setVisibility(View.GONE);
                et_name.setVisibility(View.GONE);
                btn_query.setVisibility(View.GONE);
                tv_showid.setVisibility(View.GONE);
                tv_showname.setVisibility(View.GONE);

            }
        });

        bbtn_query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bbtn_grade.setBootstrapType("default");
                bbtn_query.setBootstrapType("primary");
                bbtn_add.setBootstrapType("default");
                et_id.setVisibility(View.VISIBLE);
                et_name.setVisibility(View.VISIBLE);
                btn_query.setVisibility(View.VISIBLE);
                tv_showid.setVisibility(View.VISIBLE);
                tv_showname.setVisibility(View.VISIBLE);
                String id = et_id.getText().toString();
                String name = et_name.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                bundle.putString("name",name);

                Log.d("activity name",id.equals("")+"");
                Log.d("queryactivity id",name.equals("")+"");

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                QueryLessonFragment queryLessonFragment = new QueryLessonFragment();
                queryLessonFragment.setArguments(bundle);
                ft.replace(R.id.framelayout_lesson,queryLessonFragment).commit();
            }
        });

        bbtn_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbtn_grade.setBootstrapType("primary");
                bbtn_query.setBootstrapType("default");
                bbtn_add.setBootstrapType("default");

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                TeachGradeInfoFragment gradeInfoFragment = new TeachGradeInfoFragment();
                ft.replace(R.id.framelayout_lesson,gradeInfoFragment).commit();
                et_id.setVisibility(View.GONE);
                et_name.setVisibility(View.GONE);
                btn_query.setVisibility(View.GONE);
                tv_showid.setVisibility(View.GONE);
                tv_showname.setVisibility(View.GONE);
            }
        });

        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String name = et_name.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                bundle.putString("name",name);

                Log.d("activity name",id.equals("")+"");
                Log.d("queryactivity id",name.equals("")+"");

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                QueryLessonFragment queryLessonFragment = new QueryLessonFragment();
                queryLessonFragment.setArguments(bundle);
                ft.replace(R.id.framelayout_lesson,queryLessonFragment).commit();
            }
        });
    }

    public void changed(){

    }
}
