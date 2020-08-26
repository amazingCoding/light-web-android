package com.sanyuelanv.lightwebcore.Helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;
import com.sanyuelanv.lightwebcore.R;

import java.lang.reflect.Method;

/**
 * Create By songhang in 2020/8/12
 */
public class UIHelper {
    // color 相关
    public static int getColor(int colorId, Context context){
        return context.getResources().getColor(colorId);
    }
    public static boolean isDeepColor(final String hexColor) {
        boolean flag = true;
        if (hexColor.startsWith("#") && hexColor.length() == 7){
            final int[] ret = new int[3];
            for (int i = 0; i < 3; i++) {
                ret[i] = Integer.parseInt(hexColor.substring(i * 2 + 1, i * 2 + 3), 16);
            }
            if(ret[0]*0.299 + ret[0]*0.578 + ret[0]*0.114 >= 192){  flag = false;  }
        }
        return flag;
    }
    // drawable 相关
    public static GradientDrawable getGradientDrawable(int radius, int strokeWidth, int strokeColor, int bgColor){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        if (radius > 0){  drawable.setCornerRadius(radius);  }
        if (strokeWidth > 0){  drawable.setStroke(strokeWidth,strokeColor);  }
        drawable.setColor(bgColor);
        return drawable;
    }
    public static GradientDrawable getGradientDrawable(float[] radii,int strokeWidth,int strokeColor,int bgColor){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radii);
        if (strokeWidth > 0){  drawable.setStroke(strokeWidth,strokeColor);  }
        drawable.setColor(bgColor);
        return drawable;
    }
    public static StateListDrawable getStateListDrawable(int bgColor){
        StateListDrawable bg = new StateListDrawable();
        Drawable pressed = getGradientDrawable(0,0,0,bgColor);
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        return bg;
    }
    // screen
    public static int getScreenWidth(Context context) {
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    public static int getScreenHeight(Context context) {
        // 该高度不包括 底部虚拟键的高度，但包括状态栏。
        Resources resource = context.getResources();
        DisplayMetrics displayMetrics = resource.getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        return height;
    }
    public static int getScreenContentHeight(Context context) {
        // 该高度应该是指内容高度
        // 导航手势模式 & 安卓 5.0 以上 = 整个屏幕高度 => getRealHeight
        // 非导航手势模式 & 安卓 5.0 以上 = 整个屏幕高度 - 虚拟按钮高度  => getScreenHeight
        // 获取高度 在 安卓 5.0  height - 状态栏高度
        int height = 0;
        if(isEdgeToEdgeEnabled(context) != 2){
            height = getScreenHeight(context);
        }
        else {
            height = getRealHeight(context);
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
            height -= getStatusBarHeight(context);
        }
        return height;
    }
    public static int getRealHeight(Context context) {
        // 获取屏幕实际高度（也包含虚拟导航栏）
        DisplayMetrics displayMetrics = null;
        displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    public static int isEdgeToEdgeEnabled(Context context) {
        // return 2 is EdgeToEdge
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android");
        if (resourceId > 0) {
            return resources.getInteger(resourceId);
        }
        return 0;
    }
    // dp & px 相关
    public static int dp2px(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.9);
    }
    public static int px2dp(Context context,int px){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.9);
    }
    // 状态栏相关
    public  static int  initStatusBar(Context context, Window window){
        // 状态栏
        // LOLLIPOP 的设置成黑底白字，并且不支持后续改变字体颜色 -> status height = 0
        // LOLLIPOP 以上的 白底（隐藏了状态栏）黑字，后续可以改变颜色
        // 最后返回：获取 status height 高度，以供设置胶囊按钮位置
        int statusBarHeight = 0;
        View decorView = window.getDecorView();
        int option =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if(isEdgeToEdgeEnabled(context) == 2){
            option = option  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ;
        }
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(Color.rgb(21,24,27));
        }
        else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            statusBarHeight = UIHelper.getStatusBarHeight(context);
        }
        Log.d("MyUI",option + "");
        decorView.setSystemUiVisibility(option);
        return  statusBarHeight;
    }
    public static void changeStatusBar(Context context,Window window, ThemeTypes type){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = window.getDecorView();
            int option =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if(isEdgeToEdgeEnabled(context) == 2) option = option  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ;
            if(type.equals(ThemeTypes.dark)){
                option =option |  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(option);
        }
    }
    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        Resources resource = context.getResources();
        int result = 0;
        int resourceId = resource.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 ) {  result = resource.getDimensionPixelSize(resourceId);  }
        return result;
    }
}
