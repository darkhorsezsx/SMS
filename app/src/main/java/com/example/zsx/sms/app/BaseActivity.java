package com.example.zsx.sms.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.zsx.sms.controller.ActivityCollector;

/**
 * Created by zsx on 2015/5/7.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
