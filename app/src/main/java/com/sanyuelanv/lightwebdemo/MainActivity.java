package com.sanyuelanv.lightwebdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.sanyuelanv.lightwebcore.LightWebCoreActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
        findViewById(R.id.goWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // http://192.168.50.222:3000/app.zip
                // http://172.16.31.137:3000/app.zip
                LightWebCoreActivity.createLightWebView(activity,"http://172.16.31.141:3000/app.zip?dfasfafasf",5,true);
            }
        });
    }

}