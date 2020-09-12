package com.sanyuelanv.lightwebdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.sanyuelanv.lightwebcore.LightWebCoreActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
        final  Switch btn = (Switch) findViewById(R.id.changeDev);
        findViewById(R.id.goWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LightWebCoreActivity.createLightWebView(activity,"http://192.168.50.222:3000/app.zip?fdsfdsfds",5,btn.isChecked());
            }
        });
    }

}