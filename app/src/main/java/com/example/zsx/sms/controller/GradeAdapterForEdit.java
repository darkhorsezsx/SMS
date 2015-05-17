package com.example.zsx.sms.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.modle.ChooseLessonCardForGrade;
import com.example.zsx.sms.modle.EditGradeCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/15.
 */
public class GradeAdapterForEdit extends BaseAdapter {
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private ArrayList<HashMap<String, Object>> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    String lid;
    String sid;

    // 构造器
    public GradeAdapterForEdit(Context context,ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EditGradeCard holder = null;

        final String lid = list.get(position).get("lid").toString();
        String lname = list.get(position).get("lname").toString();
        final String score = list.get(position).get("score").toString();
        final String sid  =  list.get(position).get("sid").toString();


        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new EditGradeCard();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.card_editgrade, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_editgradecard);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id_editgradecard);
            holder.et_score = (BootstrapEditText) convertView.findViewById(R.id.et_score_editgradecard);
            holder.btn_commit = (BootstrapButton) convertView.findViewById(R.id.btn_commit_editgradecard);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (EditGradeCard) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv_id.setText(lid);
        holder.tv_name.setText(lname);
        holder.et_score.setText(score);

        final EditGradeCard finalHolder = holder;
        holder.btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newscore = finalHolder.et_score.getText().toString();
                if(newscore.equals(score))
                    return;
                if(newscore.equals("")){
                    Toast.makeText(context,"成绩有误",Toast.LENGTH_SHORT).show();
                    newscore = score;
                }
                ContentValues cv = new ContentValues();
                cv.put("lid",lid);
                cv.put("sid",sid);
                cv.put("score",newscore);
                db.update("ls",cv,"lid = ? and sid = ?",new String[]{lid,sid});
                Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
