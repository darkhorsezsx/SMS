package com.example.zsx.sms.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.ActivityCollector;
import com.example.zsx.sms.controller.ChooseLessonAdapter;
import com.example.zsx.sms.controller.ChooseLessonAdapterForGrade;
import com.example.zsx.sms.controller.GradeAdapter;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.modle.ChooseLessonCard;
import com.example.zsx.sms.modle.ChooseLessonCardForGrade;
import com.example.zsx.sms.modle.GradeCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/13.
 */
public class GradeActivity extends Activity{

    ListView list_class;
    ListView list_grade;
    RadioGroup rg_select;
    BootstrapButton btn_query;
    BootstrapButton btn_selectall;
    BootstrapButton btn_invertall;
    BootstrapButton btn_cancelall;
    BootstrapButton btn_delete;
    TextView tv_shownofinding;

    String sid = null;
    String lid;
    private int checkNum; // 记录选中的条目数量

    String orderMode = "asc";
    boolean isAsc = false;
    String sqlasc = "select * from student natural join (select * from lesson natural join (select * from ls where ls.lid = ?)) order by score asc";
    String sqldesc = "select * from student natural join (select * from lesson natural join (select * from ls where ls.lid = ?)) order by score desc";

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    Cursor cursorclass;
    Cursor cursorgrade;

    boolean isclassnull = true;
    boolean isgradenull = true;

    private ArrayList<HashMap<String, Object>> dataclass;
    ChooseLessonAdapterForGrade adapterclass;

    private ArrayList<HashMap<String, Object>> datagrade;
    GradeAdapter adaptergrade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        list_class = (ListView)findViewById(R.id.lv_chooselesson_gradeactivity);
        list_grade = (ListView)findViewById(R.id.lv_showgrade_gradeactivity);
        rg_select = (RadioGroup)findViewById(R.id.rg_gradeactivity);
        btn_query = (BootstrapButton) findViewById(R.id.btn_query_gradeactivity);
        btn_selectall = (BootstrapButton) findViewById(R.id.btn_selectall_gradeactivity);
        btn_invertall = (BootstrapButton) findViewById(R.id.btn_invertall_gradeactivity);
        btn_cancelall = (BootstrapButton) findViewById(R.id.btn_cancelall_gradeactivity);
        btn_delete = (BootstrapButton) findViewById(R.id.btn_delete_gradeactivity);
        tv_shownofinding = (TextView) findViewById(R.id.tv_shownofinding_gradeactivity);

        tv_shownofinding.setVisibility(View.INVISIBLE);

        mySQLiteHelper = new MySQLiteHelper(GradeActivity.this,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
        dataclass = new ArrayList<HashMap<String,Object>>();
        datagrade = new ArrayList<HashMap<String,Object>>();

        cursorclass = db.query("lesson",null,null,null,null,null,null);

        if(cursorclass.moveToFirst()){
            isclassnull = false;
            do{
                String name = cursorclass.getString(cursorclass.getColumnIndex("lname"));
                String id = cursorclass.getString(cursorclass.getColumnIndex("lid"));
                int credit = cursorclass.getInt(cursorclass.getColumnIndex("lcredit"));
                Log.d("qqname", name);
                Log.d("qqid",id);
                Log.d("qqenglish",credit+"");
                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                tempHashMap.put("lid",id);
                tempHashMap.put("lname", name);
                tempHashMap.put("lcredit",credit+"");
                dataclass.add(tempHashMap);
            }while(cursorclass.moveToNext());
        }
        cursorclass.close();

        Log.d("datahh",dataclass.toString());
        adapterclass = new ChooseLessonAdapterForGrade(GradeActivity.this,dataclass);
        list_class.setAdapter(adapterclass);

        list_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseLessonCardForGrade holder = (ChooseLessonCardForGrade) view.getTag();
                holder.rb_selected.toggle();
                ChooseLessonAdapterForGrade.getIsSelectedForGrade().put(position, holder.rb_selected.isChecked());
                for (int i = 0; i < dataclass.size(); i++) {
                    ChooseLessonAdapterForGrade.getIsSelectedForGrade().put(i, false);
                }
                ChooseLessonAdapterForGrade.getIsSelectedForGrade().put(position, true);
                adapterclass.notifyDataSetChanged();
                Log.d("testgradle",dataclass.get(position).get("lname").toString());
                lid = dataclass.get(position).get("lid").toString();
            }
        });

        list_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GradeCard holder = (GradeCard) view.getTag();
                holder.cb_selected.toggle();
                GradeAdapter.getIsSelected().put(position, holder.cb_selected.isChecked());

                //adapterclass.notifyDataSetChanged();
                Log.d("testgradle",dataclass.get(position).get("lname").toString());
                lid = dataclass.get(position).get("lid").toString();
            }
        });

        rg_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rg_select.getCheckedRadioButtonId()){
                    case R.id.rb_inc:  orderMode = "asc";isAsc = true;break;
                    case R.id.rb_dec: orderMode = "desc";isAsc = false;break;
                    default: orderMode = "asc";
                }
            }
        });




        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isclassnull){
                    Toast.makeText(GradeActivity.this,"无课程，请新建课程！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(lid == null){
                    Toast.makeText(GradeActivity.this,"未选择课程！",Toast.LENGTH_SHORT).show();
                    return;
                }
                // clear data earlier and add new data
                datagrade.clear();
                if(isAsc)
                    cursorgrade = db.rawQuery(sqlasc,new String[]{lid});
                else
                    cursorgrade = db.rawQuery(sqldesc,new String[]{lid});

                if(!cursorgrade.moveToFirst()){
                    tv_shownofinding.setVisibility(View.VISIBLE);
                }
                if(cursorgrade.moveToFirst()){
                    tv_shownofinding.setVisibility(View.GONE);
                    isgradenull = false;
                    do{
                        //can't query anything why?!!!
                        String lname = cursorgrade.getString(cursorgrade.getColumnIndex("lname"));
                        String lid = cursorgrade.getString(cursorgrade.getColumnIndex("lid"));
                        int lcredit = cursorgrade.getInt(cursorgrade.getColumnIndex("lcredit"));
                        String sid = cursorgrade.getString(cursorgrade.getColumnIndex("sid"));
                        String sname = cursorgrade.getString(cursorgrade.getColumnIndex("sname"));
                        int sscore = cursorgrade.getInt(cursorgrade.getColumnIndex("score"));
                        Log.d("qqlname", lname);
                        Log.d("qqlid",lid);
                        Log.d("qqcredit",lcredit+"");
                        Log.d("qqsname", sname);
                        Log.d("qqsid",sid);
                        Log.d("qqscore",sscore+"");
                        HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                        tempHashMap.put("lid",lid);
                        tempHashMap.put("lname", lname);
                        tempHashMap.put("lcredit",lcredit);
                        tempHashMap.put("sid",sid);
                        tempHashMap.put("sname", sname);
                        tempHashMap.put("sscore",sscore+"");
                        datagrade.add(tempHashMap);
                    }while(cursorgrade.moveToNext());
                }
                cursorgrade.close();
                adaptergrade = new GradeAdapter(GradeActivity.this,datagrade);
                list_grade.setAdapter(adaptergrade);
            }
        });

        btn_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isgradenull)
                    return;
                for (int i = 0; i < datagrade.size(); i++) {
                    GradeAdapter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                checkNum = datagrade.size();
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isgradenull)
                    return;
                for (int i = 0; i < datagrade.size(); i++) {
                    if (GradeAdapter.getIsSelected().get(i)) {
                        Log.d("testcomit",datagrade.get(i).get("lid").toString());
                        String lid = datagrade.get(i).get("lid").toString();
                        String sid = datagrade.get(i).get("sid").toString();
                        db.delete("ls","lid = ? and sid = ?",new String[]{lid,sid});
                    }
                }
                Toast.makeText(GradeActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            }
        });
        btn_invertall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isgradenull)
                    return;
                // 遍历list的长度，将已选的设为未选，未选的设为已选
                for (int i = 0; i < datagrade.size(); i++) {
                    if (GradeAdapter.getIsSelected().get(i)) {
                        GradeAdapter.getIsSelected().put(i, false);
                        checkNum--;
                    } else {
                        GradeAdapter.getIsSelected().put(i, true);
                        checkNum++;
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });
        btn_cancelall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isgradenull)
                    return;
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < datagrade.size(); i++) {
                    if (GradeAdapter.getIsSelected().get(i)) {
                        GradeAdapter.getIsSelected().put(i, false);
                        checkNum--;// 数量减1
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
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

    private void dataChanged() {
        // 通知listView刷新
        adaptergrade.notifyDataSetChanged();
        // TextView显示最新的选中数目
        Log.d("datachanged","已选中" + checkNum + "项");
    }
}
