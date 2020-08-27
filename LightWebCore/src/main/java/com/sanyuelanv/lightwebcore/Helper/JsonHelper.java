package com.sanyuelanv.lightwebcore.Helper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/19
 */
public class JsonHelper {
    public  static  boolean IntToBoolean(int i){
        return i != 0;
    }
    public static int getIntInJson(JSONObject json,String name,int def){
        int res = def;
        try {  res = json.getInt(name);  }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String getStringInJson(JSONObject json,String name,String def){
        String res = def;
        try {  res = json.getString(name);  }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static JSONObject getJsonInJson(JSONObject json,String name){
        JSONObject res = null;
        try {  res = json.getJSONObject(name);  }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
    public static void setValueToJson(JSONObject json,String name,String value){
        try {  json.put(name,value);  }
        catch (JSONException ignored){}
    }
    public static void setValueToJson(JSONObject json,String name,int value){
        try {  json.put(name,value);  }
        catch (JSONException ignored){}
    }
    public static void setValueToJson(JSONObject json,String name,JSONObject value){
        try {  json.put(name,value);  }
        catch (JSONException ignored){}
    }
}
