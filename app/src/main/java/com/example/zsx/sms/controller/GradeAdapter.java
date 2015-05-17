package com.example.zsx.sms.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zsx.sms.R;
import com.example.zsx.sms.modle.ChooseLessonCard;
import com.example.zsx.sms.modle.GradeCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/13.
 */
public class GradeAdapter extends BaseAdapter {

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private ArrayList<HashMap<String, Object>> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public GradeAdapter(Context context,ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
        // 初始化数据
        initDate();
        Log.d("init adapter", "tata");
    }

    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
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
        GradeCard holder = null;

        String lid = list.get(position).get("lid").toString();
        String lname = list.get(position).get("lname").toString();
        String credit = list.get(position).get("lcredit").toString();
        String sid = list.get(position).get("sid").toString();
        String sname = list.get(position).get("sname").toString();
        String score = list.get(position).get("sscore").toString();


        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new GradeCard();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.card_grade, null);
            holder.tv_sname = (TextView) convertView.findViewById(R.id.tv_studentname_gradecard);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_studentscore_gradecard);
            holder.tv_sid = (TextView) convertView.findViewById(R.id.tv_studentid_gradecard);
            holder.tv_lname = (TextView) convertView.findViewById(R.id.tv_lessonname_gradecard);
            holder.tv_credit = (TextView) convertView.findViewById(R.id.tv_credit_gradecard);
            holder.tv_lid = (TextView) convertView.findViewById(R.id.tv_lessonid_gradecard);
            holder.cb_selected = (CheckBox) convertView.findViewById(R.id.cb_grade_gradecard);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (GradeCard) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv_sid.setText(sid);
        holder.tv_sname.setText(sname);
        holder.tv_score.setText(score);
        holder.tv_lid.setText(lid);
        holder.tv_lname.setText(lname);
        holder.tv_credit.setText(credit);
        // 根据isSelected来设置checkbox的选中状况
        holder.cb_selected.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        GradeAdapter.isSelected = isSelected;
    }
}
