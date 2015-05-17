package com.example.zsx.sms.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.zsx.sms.R;
import com.example.zsx.sms.modle.ChooseLessonCard;
import com.example.zsx.sms.modle.ChooseLessonCardForGrade;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/13.
 */
public class ChooseLessonAdapterForGrade extends BaseAdapter{
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private ArrayList<HashMap<String, Object>> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelectedForGrade;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public ChooseLessonAdapterForGrade(Context context,ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        isSelectedForGrade = new HashMap<Integer, Boolean>();
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
        // 初始化数据
        initDate();
        Log.d("init adapter", "tata");
    }

    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelectedForGrade().put(i,false);
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
        ChooseLessonCardForGrade holder = null;

        String id = list.get(position).get("lid").toString();
        String name = list.get(position).get("lname").toString();
        String credit = list.get(position).get("lcredit").toString();

        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ChooseLessonCardForGrade();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.card_chooselessonforgrade, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_chooselessoncardforgrade);
            holder.tv_credit = (TextView) convertView.findViewById(R.id.tv_credit_chooselessoncardforgrade);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id_chooselessoncardforgrade);
            holder.rb_selected = (RadioButton) convertView.findViewById(R.id.rb_chooselessoncardforgrade);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ChooseLessonCardForGrade) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv_id.setText(id);
        holder.tv_name.setText(name);
        holder.tv_credit.setText(credit);
        // 根据isSelected来设置checkbox的选中状况
        holder.rb_selected.setChecked(getIsSelectedForGrade().get(position));
        return convertView;
    }

    public static HashMap<Integer,Boolean> getIsSelectedForGrade() {
        return isSelectedForGrade;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        ChooseLessonAdapterForGrade.isSelectedForGrade = isSelected;
    }
}
