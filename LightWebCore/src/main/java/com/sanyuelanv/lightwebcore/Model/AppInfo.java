package com.sanyuelanv.lightwebcore.Model;

import android.content.Context;
import android.os.Build;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;

/**
 * Create By songhang in 2020/8/20
 */
public class AppInfo {
//    phoneName: 'electron',
//    system: 'electronOS',
//    systemVersion: '0.1',
//    screenWidth: app.rect.width,
//    screenHeight: app.rect.height,
//    webWidth: app.rect.width,
//    webHeight: appRect.height,
//    statusBarHeight: 20,
//    capsule: { width: 65, height: 28, x: app.rect.width - 12 - 65, y: 28 },
    private String phoneName;
    private String system;
    private String systemVersion;
    private int screenWidth;
    private int screenHeight;
    private int webWidth;
    private int webHeight;
    private int statusBarHeight;
    private int capsuleWidth;
    private int capsuleHeight;
    private int capsuleX;
    private int capsuleY;

    private int maxRouters;
    private int currentPos;
    private int currentTheme;
    private Context mContext;

    public AppInfo(Context context) {
        mContext = context;
        phoneName = getDeviceName();
        system = "android";
        systemVersion = Build.VERSION.RELEASE;
        screenWidth = UIHelper.px2dp(context,UIHelper.getScreenWidth(context));
        screenHeight = UIHelper.px2dp(context,UIHelper.getRealHeight(context));
        webWidth = screenWidth;
    }
    public void setCapsuleRect(int width,int height,int top,int right){
        capsuleWidth = width;
        capsuleHeight = height;
        capsuleY = top;
        capsuleX = screenWidth - width - right;
    }
    public void setRouter(int maxRouters,int currentPos){
        this.maxRouters = maxRouters;
        this.currentPos = currentPos;
    }
    public void setHeight(int navBarHeight){
        int webHeight = UIHelper.getScreenContentHeight(mContext) - navBarHeight;
        this.webHeight = UIHelper.px2dp(mContext,webHeight);
    }
    public String toPageString(){
        return "{" +
                "data:{" +
                    "webWidth:" + webWidth + "," +
                    "webHeight:" + webHeight + "," +
                    "currentTheme:"+ currentTheme +
                "}," +
                "state:0" +
                "}";
    }
    public String toString(String extra) {
        String myExtra = extra == null ?  "null" : "'" +extra+"'";
        return "{" +
                    "data:{" +
                        "appInfo:{" +
                        "phoneName:'" + phoneName + "'," +
                        "system:'" + system + "'," +
                        "systemVersion:'" + system + "'," +
                        "screenWidth:" + screenWidth + "," +
                        "screenHeight:" + screenHeight + "," +
                        "webWidth:" + webWidth + "," +
                        "webHeight:" + webHeight + "," +
                        "statusBarHeight:" + statusBarHeight + "," +
                        "capsule:{"+
                        "width:" + capsuleWidth + "," +
                        "height:" + capsuleHeight + "," +
                        "x:" + capsuleX + "," +
                        "y:" + capsuleY + "," +
                        "},"+
                        "},"+
                        "routerInfo:{"+
                        "maxRouters:" + maxRouters + "," +
                        "currentPos:" + currentPos + "," +
                        "},"+
                        "extra:" + myExtra + ","+
                        "currentTheme:"+ currentTheme +
                    "}," +
                    "state:0" +
                "}";
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getWebWidth() {
        return webWidth;
    }

    public void setWebWidth(int webWidth) {
        this.webWidth = webWidth;
    }

    public int getWebHeight() {
        return webHeight;
    }

    public void setWebHeight(int webHeight) {
        this.webHeight = webHeight;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = UIHelper.px2dp(mContext,statusBarHeight);
    }

    public int getCapsuleWidth() {
        return capsuleWidth;
    }

    public void setCapsuleWidth(int capsuleWidth) {
        this.capsuleWidth = capsuleWidth;
    }

    public int getCapsuleHeight() {
        return capsuleHeight;
    }

    public void setCapsuleHeight(int capsuleHeight) {
        this.capsuleHeight = capsuleHeight;
    }

    public int getCapsuleX() {
        return capsuleX;
    }

    public void setCapsuleX(int capsuleX) {
        this.capsuleX = capsuleX;
    }

    public int getCapsuleY() {
        return capsuleY;
    }

    public void setCapsuleY(int capsuleY) {
        this.capsuleY = capsuleY;
    }

    public int getMaxRouters() {
        return maxRouters;
    }

    public void setMaxRouters(int maxRouters) {
        this.maxRouters = maxRouters;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
}
