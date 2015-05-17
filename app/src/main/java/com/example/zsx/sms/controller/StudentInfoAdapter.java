package com.example.zsx.sms.controller;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.app.StudentActivity;
import com.example.zsx.sms.modle.ChooseLessonCard;
import com.example.zsx.sms.modle.Student;
import com.example.zsx.sms.modle.StudentInfoCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/12.
 */
public class StudentInfoAdapter extends BaseAdapter {
    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    private ArrayList<HashMap<String, Object>> list;
    private Context context;
    private LayoutInflater inflater = null;

    // 构造器
    public StudentInfoAdapter(Context context,ArrayList<HashMap<String, Object>> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        StudentInfoCard holder = null;

        final String id = list.get(position).get("sid").toString();
        final String name = list.get(position).get("sname").toString();
        final String phone = list.get(position).get("sphone").toString();
        final String classid = list.get(position).get("sclass").toString();
        final String age = list.get(position).get("sage").toString();
        final String gender = list.get(position).get("sgender").toString();

        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new StudentInfoCard();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.card_student, null);
            holder.et_id = (BootstrapEditText) convertView.findViewById(R.id.et_id_studentcard);
            holder.et_name = (BootstrapEditText) convertView.findViewById(R.id.et_name_studentcard);
            holder.et_phone = (BootstrapEditText) convertView.findViewById(R.id.et_phone_studentcard);
            holder.et_age = (BootstrapEditText) convertView.findViewById(R.id.et_age_studentcard);
            holder.et_gender = (BootstrapEditText) convertView.findViewById(R.id.et_gender_studentcard);
            holder.et_class = (BootstrapEditText) convertView.findViewById(R.id.et_class_studentcard);
            holder.btn_editbasic = (BootstrapButton) convertView.findViewById(R.id.btn_editbasic_studentcard);
            holder.btn_editsure = (BootstrapButton) convertView.findViewById(R.id.btn_editsure_studentcard);
            holder.btn_editgrade = (BootstrapButton) convertView.findViewById(R.id.btn_editgrade_studentcard);
            holder.btn_delete = (BootstrapButton) convertView.findViewById(R.id.btn_delete_studentcard);
            holder.btn_chooselesson = (BootstrapButton) convertView.findViewById(R.id.btn_chooselesson_studentcard);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (StudentInfoCard) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.et_id.setText(id);
        holder.et_name.setText(name);
        holder.et_phone.setText(phone);
        holder.et_gender.setText(gender);
        holder.et_age.setText(age);
        holder.et_class.setText(classid);
        holder.et_id.setEnabled(false);
        holder.et_name.setEnabled(false);
        holder.et_phone.setEnabled(false);
        holder.et_gender.setEnabled(false);
        holder.et_age.setEnabled(false);
        holder.et_class.setEnabled(false);
        holder.btn_editsure.setEnabled(false);

        final StudentInfoCard finalHolder = holder;

        holder.btn_editbasic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.et_name.setEnabled(true);
                finalHolder.et_phone.setEnabled(true);
                finalHolder.et_gender.setEnabled(true);
                finalHolder.et_age.setEnabled(true);
                finalHolder.et_class.setEnabled(true);
                finalHolder.btn_editsure.setEnabled(true);
                finalHolder.et_name.requestFocus();
            }
        });
        holder.btn_editgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send broadcast for activity to start a new editgradefragment
                Intent intent = new Intent("start.fragment.action");
                intent.putExtra("sid",id);
                intent.putExtra("sname",name);
                intent.putExtra("type","edit");
                context.sendBroadcast(intent);

            }
        });
        holder.btn_editsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = finalHolder.et_id.getText().toString();
                String newname = finalHolder.et_name.getText().toString();
                String newphone = finalHolder.et_phone.getText().toString();
                String newgender = finalHolder.et_gender.getText().toString();
                String newage = finalHolder.et_age.getText().toString();
                String newclass = finalHolder.et_class.getText().toString();
                finalHolder.et_id.setEnabled(false);
                finalHolder.et_name.setEnabled(false);
                finalHolder.et_phone.setEnabled(false);
                finalHolder.et_gender.setEnabled(false);
                finalHolder.et_age.setEnabled(false);
                finalHolder.et_class.setEnabled(false);
                finalHolder.btn_editsure.setEnabled(false);
                boolean isValid = isInfoValid(newname,newclass,newphone,newgender,newage);
                if(!isValid){
                    Toast.makeText(context,"信息有误",Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean isSame = isInfoSame(name,newname,classid,newclass,phone,newphone,gender,newgender,age,newage);
                if(isSame){
                    Toast.makeText(context,"信息未变化",Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues cv = new ContentValues();
                cv.put("sid",id);
                cv.put("sname",newname);
                cv.put("sphone",newphone);
                cv.put("sclass",newclass);
                cv.put("sage",newage);
                cv.put("sgender",newgender);
                db.update("student",cv,"sid = ?",new String[]{id});
                Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show();
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("删除学生");
                dialog.setMessage("删除后不可恢复，确定删除学生吗？");
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        Log.d("delete", "haha");
                        String newid =  finalHolder.et_id.getText().toString();
                        db = mySQLiteHelper.getWritableDatabase();
                        db.delete("student","sid = ?",new String[]{id});
                        db.delete("ls","sid = ?",new String[]{id});
                        db.close();
                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.create().show();
            }
        });
        holder.btn_chooselesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * add broadcast
                 * */
                Intent intent = new Intent("start.fragment.action");
                intent.putExtra("sid",id);
                intent.putExtra("sname",name);
                intent.putExtra("type","choose");
                context.sendBroadcast(intent);
             }
        });

        return convertView;
    }

    private boolean isInfoValid(String name,String classid,String phone,String gender,String age){
        if(age.equals(""))
            return false;
        if(!gender.equals("男") && !gender.equals("女"))
            return false;
        if(name.equals("") || classid.equals("") || phone.equals(""))
            return false;
        return true;
    }

    private boolean isInfoSame(String name,String newname,String classid,String newclassid,String phone,String newphone,String gender,String newgender,String age,String newage){
        if(name.equals(newname) && classid.equals(newclassid) && phone.equals(newphone) && gender.equals(newgender) && age.equals(newage))
            return true;
        else
            return false;
    }

    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        super.notifyDataSetChanged();
    }
}
