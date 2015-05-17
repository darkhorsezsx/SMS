package com.example.zsx.sms.app;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.zsx.sms.R;

/**
 * Created by zsx on 2015/5/7.
 */
public class StudentActivity extends BaseActivity{

    BootstrapButton bttn_add;
    BootstrapButton bttn_myspace;
    BootstrapButton bttn_editinfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bttn_add = (BootstrapButton)findViewById(R.id.bttn_addstudent_student);
        bttn_myspace = (BootstrapButton)findViewById(R.id.bttn_myspace_student);
        bttn_editinfo = (BootstrapButton)findViewById(R.id.bttn_editinfo_student);
        /**
         * when jump to this activity display the studentinfofragment
         * */
        bttn_add.setBootstrapType("primary");
        bttn_myspace.setBootstrapType("default");
        bttn_editinfo.setBootstrapType("drfault");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        AddStudentFragment addStudentFragment = new AddStudentFragment();
        ft.replace(R.id.framelayout_student,addStudentFragment).commit();

        bttn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bttn_add.setBootstrapType("primary");
                bttn_myspace.setBootstrapType("default");
                bttn_editinfo.setBootstrapType("drfault");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                AddStudentFragment addStudentFragment = new AddStudentFragment();
                ft.replace(R.id.framelayout_student,addStudentFragment).commit();
            }
        });

        bttn_myspace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                bttn_add.setBootstrapType("drfault");
                bttn_myspace.setBootstrapType("primary");
                bttn_editinfo.setBootstrapType("drfault");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://zhangshixu.qzone.qq.com"));
                startActivity(intent);
            }
        });

        bttn_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bttn_add.setBootstrapType("default");
                bttn_myspace.setBootstrapType("default");
                bttn_editinfo.setBootstrapType("primary");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                StudentInfoFragment studentInfoFragment = new StudentInfoFragment();
                ft.replace(R.id.framelayout_student,studentInfoFragment).commit();
            }
        });

        BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String sid = intent.getStringExtra("sid");
                String sname = intent.getStringExtra("sname");
                String type = intent.getStringExtra("type");
                Bundle bundle = new Bundle();
                bundle.putString("sid",sid);
                bundle.putString("sname",sname);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if(type.equals("edit")){
                    EditGradeFragment editGradeFragment = new EditGradeFragment();
                    editGradeFragment.setArguments(bundle);
                    ft.replace(R.id.framelayout_student,editGradeFragment).addToBackStack(null).commit();
                }
                else{
                    ChooseLessonFragment chooseLessonFragment = new ChooseLessonFragment();
                    chooseLessonFragment.setArguments(bundle);
                    ft.replace(R.id.framelayout_student,chooseLessonFragment).addToBackStack(null).commit();
                }

            }
        };
        this.registerReceiver(mBroadcastReceiver, new IntentFilter("start.fragment.action"));

    }

    public  void StartFragment(String sid,String sname){

     }
}
