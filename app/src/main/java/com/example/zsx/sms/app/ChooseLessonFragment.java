package com.example.zsx.sms.app;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.example.zsx.sms.R;
import com.example.zsx.sms.controller.ChooseLessonAdapter;
import com.example.zsx.sms.controller.ChooseLessonAtapterNew;
import com.example.zsx.sms.controller.MySQLiteHelper;
import com.example.zsx.sms.modle.ChooseLessonCard;
import com.example.zsx.sms.modle.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by zsx on 2015/5/14.
 */
public class ChooseLessonFragment extends Fragment {

    private Context context;
    private View mView;
    ListView listView_chooesnlesson;
    ListView listView_unchoosedlesson;

    String sqlchoosen = "select * from lesson natural join ls where sid = ?";
    String sqlunchoosed = "select * from lesson where lesson.lid not in ( select lid from lesson natural join ls where sid = ?)";
    int countchoosen;
    int countunchoosed;

    BootstrapButton btn_selectall;
    BootstrapButton btn_invertall;
    BootstrapButton btn_cancelall;
    BootstrapButton btn_unchoosed;
    BootstrapButton btn2_selectall;
    BootstrapButton btn2_invertall;
    BootstrapButton btn2_cancelall;
    BootstrapButton btn2_commit;
    BootstrapButton btn_done;
    TextView tv_name;

    String sid;
    String sname;


    SQLiteDatabase db;
    MySQLiteHelper mySQLiteHelper;
    Cursor cursorchoosen;
    Cursor cursorunchoosed;

    boolean isChoosennull = true;
    boolean isUnchoosednull = true;

    private ArrayList<HashMap<String, Object>> choosenlessondata;
    private ArrayList<HashMap<String,Object>> unchoosedlessondata;

    ChooseLessonAdapter choosenadapter;
    ChooseLessonAtapterNew unchoosedadapter;

    public ChooseLessonFragment(){};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        mySQLiteHelper = new MySQLiteHelper(context,"SMS.db",null,1);
        db = mySQLiteHelper.getWritableDatabase();
        Bundle bundle = getArguments();
        sid = bundle.getString("sid");
        sname = bundle.getString("sname");

        choosenlessondata = new ArrayList<HashMap<String, Object>>();
        unchoosedlessondata = new ArrayList<HashMap<String, Object>>();

        cursorchoosen = db.rawQuery(sqlchoosen,new String[]{sid});
        if(cursorchoosen.moveToFirst()){
            isChoosennull = false;
            do{
                String lid = cursorchoosen.getString(cursorchoosen.getColumnIndex("lid"));
                String lname = cursorchoosen.getString(cursorchoosen.getColumnIndex("lname"));
                int credit = cursorchoosen.getInt(cursorchoosen.getColumnIndex("lcredit"));
                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                Log.d("chooselesson",lid);
                Log.d("chooselesson",lname);
                Log.d("chooselesson",credit+"");
                tempHashMap.put("lid",lid);
                tempHashMap.put("lname", lname);
                tempHashMap.put("lcredit",credit+"");
                choosenlessondata.add(tempHashMap);
            }while (cursorchoosen.moveToNext());
        }
        cursorchoosen.close();
        choosenadapter = new ChooseLessonAdapter(context,choosenlessondata);

        cursorunchoosed = db.rawQuery(sqlunchoosed,new String[]{sid});
        if(cursorunchoosed.moveToFirst()){
            isUnchoosednull = false;
            do{
                String lid = cursorunchoosed.getString(cursorunchoosed.getColumnIndex("lid"));
                String lname = cursorunchoosed.getString(cursorunchoosed.getColumnIndex("lname"));
                int credit = cursorunchoosed.getInt(cursorunchoosed.getColumnIndex("lcredit"));
                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                Log.d("unchooselesson",lid);
                Log.d("unchooselesson",lname);
                Log.d("unchooselesson",credit+"");
                tempHashMap.put("lid",lid);
                tempHashMap.put("lname", lname);
                tempHashMap.put("lcredit",credit+"");
                unchoosedlessondata.add(tempHashMap);
            }while(cursorunchoosed.moveToNext());
        }
        cursorunchoosed.close();
        unchoosedadapter = new ChooseLessonAtapterNew(context,unchoosedlessondata);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_chooselesson, container, false);

        listView_chooesnlesson = (ListView)mView.findViewById(R.id.lv_chosenlesson_chooselesson);
        listView_unchoosedlesson = (ListView)mView.findViewById(R.id.lv_unchoosedlesson_chooselesson);
        btn_selectall = (BootstrapButton) mView.findViewById(R.id.btn_selectall_chooselesson);
        btn_invertall = (BootstrapButton) mView.findViewById(R.id.btn_invertall_chooselesson);
        btn_cancelall = (BootstrapButton) mView.findViewById(R.id.btn_cancelall_chooselesson);
        btn_unchoosed = (BootstrapButton) mView.findViewById(R.id.btn_unchoose_chooselesson);
        btn2_selectall = (BootstrapButton) mView.findViewById(R.id.btn2_selectall_chooselesson);
        btn2_invertall = (BootstrapButton) mView.findViewById(R.id.btn2_invertall_chooselesson);
        btn2_cancelall = (BootstrapButton) mView.findViewById(R.id.btn2_cancelall_chooselesson);
        btn2_commit = (BootstrapButton) mView.findViewById(R.id.btn2_commit_chooselesson);
        btn_done = (BootstrapButton) mView.findViewById(R.id.btn_done_chooselesson);
        tv_name = (TextView) mView.findViewById(R.id.tv_name_chooselesson);


        tv_name.setText(sname);

        listView_chooesnlesson.setAdapter(choosenadapter);
        listView_chooesnlesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ChooseLessonCard holder = (ChooseLessonCard) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb_selected.toggle();
                // 将CheckBox的选中状况记录下来
                ChooseLessonAdapter.getIsSelected().put(arg2, holder.cb_selected.isChecked());
                // 调整选定条目
            }
        });

        listView_unchoosedlesson.setAdapter(unchoosedadapter);
        listView_unchoosedlesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ChooseLessonCard holder = (ChooseLessonCard) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb_selected.toggle();
                // 将CheckBox的选中状况记录下来
                ChooseLessonAtapterNew.getIsSelectedNew().put(arg2, holder.cb_selected.isChecked());
                // 调整选定条目
            }
        });

        btn_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < choosenlessondata.size(); i++) {
                    ChooseLessonAdapter.getIsSelected().put(i, true);
                }
                dataChangedChoosen();
            }
        });
        btn_invertall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < choosenlessondata.size(); i++) {
                    if (ChooseLessonAdapter.getIsSelected().get(i)) {
                        ChooseLessonAdapter.getIsSelected().put(i, false);
                    } else {
                        ChooseLessonAdapter.getIsSelected().put(i, true);
                    }
                }
                // 刷新listview和TextView的显示
                dataChangedChoosen();
            }
        });
        btn_cancelall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < choosenlessondata.size(); i++) {
                    if (ChooseLessonAdapter.getIsSelected().get(i)) {
                        ChooseLessonAdapter.getIsSelected().put(i, false);
                    }
                }
                // 刷新listview和TextView的显示
                dataChangedChoosen();
            }
        });
        btn_unchoosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * tuixuan data operate
                 * */
                 if(isChoosennull)
                     return;
                 AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("退选课程");
                dialog.setMessage("确定退选吗？");
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        Log.d("delete", "haha");
                        db = mySQLiteHelper.getWritableDatabase();
                        countchoosen = 0;
                        for (int i = 0; i < choosenlessondata.size(); i++) {
                            if (ChooseLessonAdapter.getIsSelected().get(i)) {
                                String lid = choosenlessondata.get(i-countchoosen).get("lid").toString();
                                db.delete("ls","sid = ? and lid = ?",new String[]{sid,lid});

                                HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                                tempHashMap = choosenlessondata.get(i-countchoosen);
                                choosenlessondata.remove(i-countchoosen);
                                unchoosedlessondata.add(tempHashMap);
                                countchoosen++;
                            }
                        }
                        dataChanged();
                        Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.create().show();
               }
        });

        btn2_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < unchoosedlessondata.size(); i++) {
                    ChooseLessonAtapterNew.getIsSelectedNew().put(i, true);
                }
                dataChangedUnchoosed();
            }
        });
        btn2_invertall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < unchoosedlessondata.size(); i++) {
                    if (ChooseLessonAtapterNew.getIsSelectedNew().get(i)) {
                        ChooseLessonAtapterNew.getIsSelectedNew().put(i, false);
                    } else {
                        ChooseLessonAtapterNew.getIsSelectedNew().put(i, true);
                    }
                }
                // 刷新listview和TextView的显示
                dataChangedUnchoosed();
            }
        });
        btn2_cancelall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < unchoosedlessondata.size(); i++) {
                    if (ChooseLessonAtapterNew.getIsSelectedNew().get(i)) {
                        ChooseLessonAtapterNew.getIsSelectedNew().put(i, false);
                    }
                }
                // 刷新listview和TextView的显示
                dataChangedUnchoosed();
            }
        });
        btn2_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countunchoosed = 0;
                db = mySQLiteHelper.getWritableDatabase();
                for (int i = 0; i < unchoosedlessondata.size(); i++) {
                    if (ChooseLessonAtapterNew.getIsSelectedNew().get(i)) {
                        String lid = unchoosedlessondata.get(i-countunchoosed).get("lid").toString();
                        ContentValues cv = new ContentValues();
                        cv.put("lid",lid);
                        cv.put("sid",sid);
                        cv.put("score",0);
                        db.insert("ls",null,cv);
                        HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
                        tempHashMap = unchoosedlessondata.get(i-countunchoosed);
                        unchoosedlessondata.remove(i-countunchoosed);
                        choosenlessondata.add(tempHashMap);
                        countunchoosed++;
                    }
                }
                dataChanged();
                Toast.makeText(context,"选课成功",Toast.LENGTH_SHORT).show();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        return mView;
    }

    private void dataChangedChoosen() {
        ChooseLessonAdapter.setRefresh(false);
        choosenadapter.notifyDataSetChanged();
    }

    private void dataChangedUnchoosed(){
        ChooseLessonAtapterNew.setRefresh(false);
        unchoosedadapter.notifyDataSetChanged();
    }

    private void dataChanged(){
        ChooseLessonAtapterNew.setRefresh(true);
        ChooseLessonAdapter.setRefresh(true);
        choosenadapter.notifyDataSetChanged();
        unchoosedadapter.notifyDataSetChanged();
        ChooseLessonAtapterNew.setRefresh(false);
        ChooseLessonAdapter.setRefresh(false);
    }
}
