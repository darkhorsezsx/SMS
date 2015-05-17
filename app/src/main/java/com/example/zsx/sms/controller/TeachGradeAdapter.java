package com.example.zsx.sms.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.zsx.sms.R;
import com.example.zsx.sms.modle.Grade;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by zsx on 2015/5/13.
 */
public class TeachGradeAdapter extends ArrayAdapter<Grade> {

    private int resourceId;

    public TeachGradeAdapter(Context context,int viewId,List<Grade> objects){
        super(context,viewId,objects);
        resourceId = viewId;
    }

    public View getView(int positon,View convertView,ViewGroup parent){
        Grade grade = getItem(positon);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        TextView tv_id = (TextView) view.findViewById(R.id.tv_id_teachgradecard);
        TextView tv_name= (TextView) view.findViewById(R.id.tv_name_teachgradecard);
        TextView tv_credit= (TextView) view.findViewById(R.id.tv_credit_teachgradecard);
        TextView tv_great= (TextView) view.findViewById(R.id.tv_great_teachgradecard);
        TextView tv_sum= (TextView) view.findViewById(R.id.tv_sum_teachgradecard);
        TextView tv_normal= (TextView) view.findViewById(R.id.tv_normal_teachgradecard);
        TextView tv_fail= (TextView) view.findViewById(R.id.tv_fail_teachgradecard);

        tv_id.setText(grade.getId());
        tv_name.setText(grade.getName());
        tv_credit.setText(grade.getCredit()+"");
        tv_sum.setText(grade.getCount()+"");
        tv_great.setText(grade.getGreat()+"");
        tv_normal.setText(grade.getNormal()+"");
        tv_fail.setText(grade.getFail()+"");

        return view;
    }
}
