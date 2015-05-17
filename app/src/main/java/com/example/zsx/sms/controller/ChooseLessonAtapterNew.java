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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/15.
 */
public class ChooseLessonAtapterNew extends BaseAdapter {
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private static boolean isRefresh = false;

    private ArrayList<HashMap<String, Object>> list;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelectedNew;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public ChooseLessonAtapterNew(Context context,ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        isSelectedNew = new HashMap<Integer, Boolean>();
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
        // 初始化数据
        initDate();
        Log.d("init adapter", "tata");
    }

    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelectedNew().put(i,false);
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
        ChooseLessonCard holder = null;

        String id = list.get(position).get("lid").toString();
        String name = list.get(position).get("lname").toString();
        String credit = list.get(position).get("lcredit").toString();

        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ChooseLessonCard();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.card_chooselesson, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_chooselessoncard);
            holder.tv_credit = (TextView) convertView.findViewById(R.id.tv_credit_chooselessoncard);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id_chooselessoncard);
            holder.cb_selected = (CheckBox) convertView.findViewById(R.id.cb_chooselessoncard);

            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ChooseLessonCard) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv_id.setText(id);
        holder.tv_name.setText(name);
        holder.tv_credit.setText(credit);

        // 根据isSelected来设置checkbox的选中状况
        holder.cb_selected.setChecked(getIsSelectedNew().get(position));
        return convertView;
    }

    public static HashMap<Integer,Boolean> getIsSelectedNew() {
        return isSelectedNew;
    }

    public static void setIsSelectedNew(HashMap<Integer,Boolean> isSelected) {
        ChooseLessonAtapterNew.isSelectedNew = isSelected;
    }

    public static boolean getRefresh(){
        return isRefresh;
    }

    public static void setRefresh(boolean value){
        isRefresh = value;
    }

    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        if(getRefresh())
            initDate();
        super.notifyDataSetChanged();

    }
}
