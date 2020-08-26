package com.sanyuelanv.lightwebcore.Model;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

import com.sanyuelanv.lightwebcore.Helper.JsonHelper;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/24
 */
public class Vibrator {
    private  boolean isLong;

    public Vibrator(JSONObject json) {
        isLong =  JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isLong",-1));
    }

    public boolean vibrator(Context context){
        android.os.Vibrator vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator != null && vibrator.hasVibrator()){
            // 存在震动器
            int s =  isLong ? 200 : 50;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect vibrationEffect = VibrationEffect.createOneShot(s,100);
                vibrator.vibrate(vibrationEffect);
            }
            else {
                vibrator.vibrate(s);
            }
            return  true;
        }
        else {
            return  false;
        }
    }
}
