package com.example.zsx.sms.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.ActivityCollector;
import com.example.zsx.sms.controller.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends BaseActivity {


    BootstrapButton bbtn_info;
    BootstrapButton bbtn_exit;

    BootstrapButton bbtn_student;
    BootstrapButton bbtn_lesson;
    BootstrapButton bbtn_grade;

    BootstrapThumbnail tb_lesson;
    BootstrapThumbnail tb_student;
    BootstrapThumbnail tb_grade;

    FontAwesomeText at_user;
    FontAwesomeText at_cog;
    FontAwesomeText at_clock;
    FontAwesomeText at_trash;

    TextView tv_date;
    TextView tv_week;
    TextView tv_loginnumber;
    TextView tv_logintime;

    SharedPreferences countfile;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countfile = getSharedPreferences("loginfile",0);
        boolean islogin = countfile.getBoolean("login",false);
        if(!islogin){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        mySQLiteHelper = new MySQLiteHelper(MainActivity.this,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();



        bbtn_info = (BootstrapButton)findViewById(R.id.bbtn_web);
        bbtn_exit = (BootstrapButton)findViewById(R.id.bbtn_exit);
        bbtn_student = (BootstrapButton)findViewById(R.id.bbtn_student_main);
        bbtn_lesson = (BootstrapButton)findViewById(R.id.bbtn_lesson_main);
        bbtn_grade = (BootstrapButton)findViewById(R.id.bbtn_grade_main);

        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_week = (TextView) findViewById(R.id.tv_week_number);
        tv_loginnumber = (TextView)findViewById(R.id.tv_loginnumber);
        tv_logintime = (TextView) findViewById(R.id.tv_logintime);

        tb_lesson = (BootstrapThumbnail)findViewById(R.id.tb_lesson);
        tb_student = (BootstrapThumbnail)findViewById(R.id.tb_student);
        tb_grade = (BootstrapThumbnail)findViewById(R.id.tb_grade);

        at_user = (FontAwesomeText)findViewById(R.id.at_user);
        at_cog = (FontAwesomeText)findViewById(R.id.at_cog);
        at_clock = (FontAwesomeText)findViewById(R.id.at_clock);
        at_trash = (FontAwesomeText)findViewById(R.id.at_trash);

        at_user.startFlashing(MainActivity.this,true, FontAwesomeText.AnimationSpeed.MEDIUM);
        at_cog.startRotate(MainActivity.this,true, FontAwesomeText.AnimationSpeed.SLOW);

        at_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this).setTitle("just for fun");
                dialog.setMessage("wo can add some great operate");
                dialog.setPositiveButton("sure",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                    }
                });
                dialog.create().show();
            }
        });

        Cursor cursor = db.query("log",null,null,null,null,null,null,null);
        cursor.moveToLast();
        if(cursor.moveToPrevious()){
            int number = cursor.getInt(cursor.getColumnIndex("number"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String strnumber = number+"次";
            date = forMat(date);
            tv_loginnumber.setText(strnumber);
            tv_logintime.setText(date);
            Log.d("test date",date);
        }
        else{
            tv_loginnumber.setText(0+"次");
            tv_logintime.setText("无");
        }

        tb_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LessonActivity.class);
                startActivity(intent);
            }
        });
        tb_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StudentActivity.class);
                startActivity(intent);
            }
        });
        tb_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GradeActivity.class);
                startActivity(intent);
            }
        });

        bbtn_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LessonActivity.class);
                startActivity(intent);
            }
        });

        bbtn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StudentActivity.class);
                startActivity(intent);
            }
        });

        bbtn_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GradeActivity.class);
                startActivity(intent);
            }
        });

        bbtn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://cc.jlu.edu.cn/G2S/ShowSystem/Index.aspx"));
                startActivity(intent);
            }
        });

        bbtn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
            }
        });

        SimpleDateFormat formatter  = new SimpleDateFormat ("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_date.setText(str);
        int weekNumber = getWeekNumber(10,curDate);
        String showweknumber = "第"+weekNumber+"周";
        tv_week.setText(showweknumber);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onDestroy(){
        super.onDestroy();
        SharedPreferences.Editor editor = countfile.edit();
        editor.putBoolean("login",false);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getWeekNumber(int startNumber,Date date){
        return getWeekOfYear(date) - startNumber;
    }

    public static int getWeekOfYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }

    public void onBackPressed(){
        super.onBackPressed();
        SharedPreferences.Editor editor = countfile.edit();
        editor.putBoolean("login",false);
        editor.commit();
    }

    public String forMat(String str){
        String year = str.substring(0,4);
        String month = str.substring(4,6);
        String day = str.substring(6,8);
        String hour = str.substring(8,10);
        String minute = str.substring(10,12);
        String second = str.substring(12,14);
        return year+"年"+month+"月"+day+"日"+hour+"时"+minute+"分"+second+"秒";
    }
}
