package com.example.zsx.sms.controller;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.modle.LessonCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/11.
 */
public class LessonAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> data;
    private LayoutInflater layoutInflater;
    private Context context;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;

    /*String id;
    String name;
    int credit;*/


    public LessonAdapter(Context context, ArrayList<HashMap<String, Object>> data) {

        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
    }

    /**
     *获取列数
     */
    public int getCount() {
        return data.size();
    }
    /**
     *获取某一位置的数据
     */
    public Object getItem(int position) {
        return data.get(position);
    }
    /**
     *获取唯一标识
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * android绘制每一列的时候，都会调用这个方法
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LessonCard lessonCard = null;

        final String id = data.get(position).get("lid").toString();
        final String name = data.get(position).get("lname").toString();
        final int credit = Integer.parseInt(data.get(position).get("lcredit").toString());

        if(convertView==null){
            lessonCard = new LessonCard();
            // 获取组件布局
            convertView = layoutInflater.inflate(R.layout.card_lesson, null);
            lessonCard.tv_id = (TextView) convertView.findViewById(R.id.tv_showid_lessoncard);
            lessonCard.tv_name = (TextView) convertView.findViewById(R.id.tv_showname_lessoncard);
            lessonCard.tv_credit = (TextView) convertView.findViewById(R.id.tv_showcredit_lessoncard);
            lessonCard.et_id = (BootstrapEditText) convertView.findViewById(R.id.et_id_lessoncard);
            lessonCard.et_name = (BootstrapEditText) convertView.findViewById(R.id.et_name_lessoncard);
            lessonCard.et_credit = (BootstrapEditText) convertView.findViewById(R.id.et_credit_lessoncard);
            lessonCard.btn_edit = (BootstrapButton) convertView.findViewById(R.id.btn_edit_lessoncard);
            lessonCard.btn_editsure = (BootstrapButton) convertView.findViewById(R.id.btn_editsure_lessoncard);
            lessonCard.btn_delete = (BootstrapButton) convertView.findViewById(R.id.btn_delete_lessoncard);

            // 这里要注意，是使用的tag来存储数据的。
            convertView.setTag(lessonCard);
        }
        else {
            lessonCard = (LessonCard) convertView.getTag();
        }
        lessonCard.et_id.setText(id);
        lessonCard.et_name.setText(name);
        lessonCard.et_credit.setText(credit + "");

        lessonCard.et_id.setEnabled(false);
        lessonCard.et_name.setEnabled(false);
        lessonCard.et_credit.setEnabled(false);
        lessonCard.btn_editsure.setBootstrapButtonEnabled(false);

        final LessonCard finalLessonCard = lessonCard;
        lessonCard.btn_edit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                finalLessonCard.et_id.setEnabled(true);
                finalLessonCard.et_name.setEnabled(true);
                finalLessonCard.et_credit.setEnabled(true);
                finalLessonCard.btn_editsure.setBootstrapButtonEnabled(true);
            }
        });

        lessonCard.btn_editsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalLessonCard.et_id.setEnabled(false);
                finalLessonCard.et_name.setEnabled(false);
                finalLessonCard.et_credit.setEnabled(false);
                finalLessonCard.btn_editsure.setBootstrapButtonEnabled(false);

                String newid =  finalLessonCard.et_id.getText().toString();
                String newname =  finalLessonCard.et_name.getText().toString();
                int newcredit = Integer.parseInt( finalLessonCard.et_credit.getText().toString());
                /**
                 * if data same
                 * */
                if (newid.equals(id) && newname.equals(name) && newcredit == credit) {
                    Toast.makeText(context, "数据未发生变化未更新数据", Toast.LENGTH_SHORT).show();
                    Log.d("testid",id);
                    Log.d("testname",name);
                    Log.d("testcredit",credit+"");
                    Log.d("testid",newid);
                    Log.d("testname",newname);
                    Log.d("testcredit",newcredit+"");
                    return;
                }
                Log.d("testid",id);
                Log.d("testname",name);
                Log.d("testcredit",credit+"");
                Log.d("testid",newid);
                Log.d("testname",newname);
                Log.d("testcredit",newcredit+"");
                ContentValues cv = new ContentValues();
                cv.put("lid",newid);
                cv.put("lname",newname);
                cv.put("lcredit",newcredit);
                /*
                Log.d("testid",id);
                Log.d("testname",name);
                Log.d("testcredit",credit+"");
                Log.d("testnewid",newid);
                Log.d("testnewname",newname);
                Log.d("testnewcredit",newcredit+"");*/
                db.update("lesson",cv,"lid = ?",new String[]{newid});
                Toast.makeText(context,"更新成功",Toast.LENGTH_SHORT).show();
            }
        });

        lessonCard.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("删除课程");
                dialog.setMessage("删除后不可恢复，确定删除课程吗？");
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        Log.d("delete", "haha");
                        String newid =  finalLessonCard.et_id.getText().toString();
                        db.delete("lesson","lid = ?",new String[]{newid});
                        db.delete("ls","lid = ?",new String[]{newid});
                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.create().show();
            }
        });

        return convertView;
    }


    @Override
    public void notifyDataSetChanged() // Create this function in your adapter class
    {
        super.notifyDataSetChanged();
    }
    
}
