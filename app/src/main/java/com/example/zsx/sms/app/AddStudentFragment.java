package com.example.zsx.sms.app;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.ChooseLessonAdapter;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.modle.ChooseLessonCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zsx on 2015/5/10.
 */
public class AddStudentFragment extends Fragment {

    private Context context;
    private View mView;
    BootstrapEditText et_id;
    BootstrapEditText et_name;
    BootstrapEditText et_phone;
    BootstrapEditText et_age;
    BootstrapEditText et_class;
    Spinner spinner;
    ListView listView;
    BootstrapButton btn_selectall;
    BootstrapButton btn_invertall;
    BootstrapButton btn_cancelall;
    BootstrapButton btn_commit;

    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    Cursor cursor;

    private ArrayList<HashMap<String, Object>> data;
    ChooseLessonAdapter adapter;


    String id;
    String name;
    String phone;
    String gender = "男";
    String classid;

    int age;
    private int checkNum; // 记录选中的条目数量

    public AddStudentFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();

        data = new ArrayList<HashMap<String,Object>>();
            cursor = db.query("lesson",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex("lname"));
                    String id = cursor.getString(cursor.getColumnIndex("lid"));
                    int credit = cursor.getInt(cursor.getColumnIndex("lcredit"));
                    Log.d("qqname", name);
                    Log.d("qqid",id);
                    Log.d("qqenglish",credit+"");
                    HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                    tempHashMap.put("lid",id);
                    tempHashMap.put("lname", name);
                    tempHashMap.put("lcredit",credit+"");
                    data.add(tempHashMap);
                }while(cursor.moveToNext());
            }
        cursor.close();
        Log.d("datahh",data.toString());
        adapter = new ChooseLessonAdapter(context,data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_addstudent, container, false);

        /**
         * basic intfo part
         */

        et_id = (BootstrapEditText) mView.findViewById(R.id.et_addid_addstufrg);
        et_name = (BootstrapEditText) mView.findViewById(R.id.et_addname_stufrg);
        et_phone = (BootstrapEditText) mView.findViewById(R.id.et_addphone_addstufrg);
        et_age = (BootstrapEditText) mView.findViewById(R.id.et_addage_addstufrg);
        et_class = (BootstrapEditText) mView.findViewById(R.id.et_addclass_addstufrg);
        spinner = (Spinner) mView.findViewById(R.id.sp_addgender_addstufrg);
        listView = (ListView) mView.findViewById(R.id.lv_chooselesson);
        btn_selectall = (BootstrapButton) mView.findViewById(R.id.btn_chooseall_addstufrg);
        btn_invertall = (BootstrapButton) mView.findViewById(R.id.btn_invertall_addstufrg);
        btn_cancelall = (BootstrapButton) mView.findViewById(R.id.btn_cancelall_addstufrg);
        btn_commit = (BootstrapButton) mView.findViewById(R.id.btn_commit_addstufrg);

        String array[] = {"男","女"};
        final ArrayAdapter<String> spinadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,array);
        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(spinadapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        /**
         * choose lesson part
         */

        listView.setAdapter(adapter);

        btn_selectall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < data.size(); i++) {
                    ChooseLessonAdapter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                checkNum = data.size();
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });

        btn_cancelall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < data.size(); i++) {
                    if (ChooseLessonAdapter.getIsSelected().get(i)) {
                        ChooseLessonAdapter.getIsSelected().put(i, false);
                        checkNum--;// 数量减1
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });

        btn_invertall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的设为未选，未选的设为已选
                for (int i = 0; i < data.size(); i++) {
                    if (ChooseLessonAdapter.getIsSelected().get(i)) {
                        ChooseLessonAdapter.getIsSelected().put(i, false);
                        checkNum--;
                    } else {
                        ChooseLessonAdapter.getIsSelected().put(i, true);
                        checkNum++;
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  need to add check if there id the same id in the table student
                 * */

                id = et_id.getText().toString();
                name = et_name.getText().toString();
                phone = et_phone.getText().toString();
                String getage = et_age.getText().toString();
                classid = et_class.getText().toString();
                if(getage.equals(""))
                    age = 0;
                else
                    age = Integer.parseInt(getage);
                Log.d("testage",(age==0)+"");

                if(!isInfoValid(id,name,gender,phone,classid,age)){
                    Toast.makeText(context,"基本信息不完整请重新录入",Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursornew = db.rawQuery("select sid from student where sid = ?",new String[]{id});
                if(cursornew.moveToFirst()){
                    Toast.makeText(context,"该学生已存在或信息错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                cursornew.close();
                //insert into student table
                ContentValues cv = new ContentValues();
                cv.put("sid",id);
                cv.put("sname",name);
                cv.put("sphone",phone);
                cv.put("sgender",gender);
                cv.put("sage",age);
                cv.put("sclass",classid);
                db.insert("student",null,cv);
                db.close();

                db = mySQLiteHelper.getWritableDatabase();
                /**
                 * l_s表插数据
                 * */
                for (int i = 0; i < data.size(); i++) {
                    if (ChooseLessonAdapter.getIsSelected().get(i)) {
                        Log.d("testcomit",data.get(i).get("lid").toString());
                        String lid = data.get(i).get("lid").toString();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("sid",id);
                        contentValues.put("lid",lid);
                        contentValues.put("score",0);
                        db.insert("ls",null,contentValues);
                        Log.d("addsid",id);
                        Log.d("addlid",lid);
                        //Log.d("score",score+"");
                    }
                }
                Toast.makeText(context,"新建成功",Toast.LENGTH_SHORT).show();
                //更新布局
                reFreshView();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ChooseLessonCard holder = (ChooseLessonCard) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb_selected.toggle();
                // 将CheckBox的选中状况记录下来
                ChooseLessonAdapter.getIsSelected().put(arg2, holder.cb_selected.isChecked());
                // 调整选定条目
                if (holder.cb_selected.isChecked() == true) {
                    checkNum++;
                } else {
                    checkNum--;
                }
                // 用TextView显示
                Log.d("itemchoose","已选中"+checkNum+"项");
            }
        });


        return mView;
    }

    private void dataChanged() {
        // 通知listView刷新
        adapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        Log.d("datachanged","已选中" + checkNum + "项");
    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

             public void onItemSelected(AdapterView<?> adapterView, View arg1, int position,long id) {
                  gender =adapterView.getItemAtPosition(position).toString();  //
                 Log.d("testspinner",gender);
             }
             public void onNothingSelected(AdapterView<?> arg0) {
                  gender = "男";
             }
    }

    boolean isInfoValid(String id,String name,String gender,String phone,String classid,int age){
        if(!gender.equals("男") && !gender.equals("女"))
            return false;
        if(name.equals("") || id.equals("") || phone.equals("") || classid.equals("") || age == 0)
            return false;
        return true;
    }

    public void reFreshView(){
        et_id.setText("");
        et_name.setText("");
        et_phone.setText("");
        et_age.setText("");
        et_class.setText("");
        for (int i = 0; i < data.size(); i++) {
            if (ChooseLessonAdapter.getIsSelected().get(i)) {
                ChooseLessonAdapter.getIsSelected().put(i, false);
            }
        }
        dataChanged();
    }


}
