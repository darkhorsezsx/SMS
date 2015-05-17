package com.example.zsx.sms.app;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.ActivityCollector;
import com.example.zsx.sms.controller.MySQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsx on 2015/5/7.
 */
public class LoginActivity extends BaseActivity {

    BootstrapEditText et_id;
    BootstrapEditText et_passward;
    BootstrapButton btn_log;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mySQLiteHelper = new MySQLiteHelper(LoginActivity.this,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();

        et_id = (BootstrapEditText)findViewById(R.id.et_id_login);
        et_passward = (BootstrapEditText)findViewById(R.id.et_passward_login);
        btn_log = (BootstrapButton)findViewById(R.id.btn_log_login);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String passward = et_passward.getText().toString();
                if(!id.equals("admin") || !passward.equals("123456")){
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences countfile = getSharedPreferences("loginfile",0);         //countfile to count how many files should be there
                SharedPreferences.Editor editor = countfile.edit();
                editor.putBoolean("login",true);
                editor.commit();
                ActivityCollector.removeActivity(LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);

                SimpleDateFormat formatter  = new SimpleDateFormat ("yyyyMMddHHmmss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                Log.d("haha",curDate.toString());
                String str = formatter.format(curDate);
                Log.d("haha",str);
                ContentValues cv = new ContentValues();
                cv.put("date",str);
                db.insert("log",null,cv);
                db.close();

                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void onBackPressed(){
        //super.onBackPressed();
        ActivityCollector.finishAll();
    }
}
