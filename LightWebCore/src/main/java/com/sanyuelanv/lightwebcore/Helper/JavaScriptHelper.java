package com.sanyuelanv.lightwebcore.Helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.sanyuelanv.lightwebcore.Model.Enum.BridgeMethods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/19
 */
public class JavaScriptHelper {
    Context mContext;
    private onMessageListener messageListener;
    public interface onMessageListener{
        public void onMessage(String name,String callBackID, JSONObject jsonObject);
        // 五个基本方法
        public void lightWebInit(String callBackID, JSONObject jsonObject);
        public void lightWebPageConfig(String callBackID, JSONObject jsonObject);
        public void lightWebRouter(String callBackID, JSONObject jsonObject);
        public void vibrate(String callBackID, JSONObject jsonObject);
        public void setClipboard(String callBackID, JSONObject jsonObject);
        public void getClipboard(String callBackID, JSONObject jsonObject);
    }

    public JavaScriptHelper(Context mContext, onMessageListener messageListener) {
        this.mContext = mContext;
        this.messageListener = messageListener;
    }

    @JavascriptInterface
    public void postMessage(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        final String name = JsonHelper.getStringInJson(jsonObject,"name","");
        JSONObject para = JsonHelper.getJsonInJson(jsonObject,"data");
        final String id = JsonHelper.getStringInJson(para,"id","");
        final JSONObject data = JsonHelper.getJsonInJson(para,"data");
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                jsMethod(name,id,data);
            }
        });

    }

    public void jsMethod(String name,String id,JSONObject data){
        if(messageListener != null){
            switch (name){
                case BridgeMethods.init:{
                    messageListener.lightWebInit(id,data);
                    break;
                }
                case BridgeMethods.pageConfig:{
                    messageListener.lightWebPageConfig(id,data);
                    break;
                }
                case BridgeMethods.router:{
                    messageListener.lightWebRouter(id,data);
                    break;
                }
                case BridgeMethods.vibrate:{
                    messageListener.vibrate(id,data);
                    break;
                }
                case BridgeMethods.setClipboard:{
                    messageListener.setClipboard(id,data);
                    break;
                }
                case BridgeMethods.getClipboard:{
                    messageListener.getClipboard(id,data);
                    break;
                }
                default:{
                    messageListener.onMessage(name,id,data);
                    break;
                }
            }
        }
    }
}
