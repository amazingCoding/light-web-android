package com.sanyuelanv.lightwebdemo;

import android.app.Application;
import android.util.Log;

/**
 * Create By songhang in 2020/8/25
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyLife","init");
    }
}
