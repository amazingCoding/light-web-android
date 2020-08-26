package com.sanyuelanv.lightwebcore.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sanyuelanv.lightwebcore.Helper.FileHelper;
import com.sanyuelanv.lightwebcore.Helper.JavaScriptHelper;
import com.sanyuelanv.lightwebcore.Helper.JsonHelper;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/19
 */
public class LightWebView extends WebView {
    public static String VERSION = "webApp/android/0.1";
    private Context mContext;
    private String globalName;
    private String debugJs;

    public LightWebView(Context context) {
        this(context,null,0,0);
    }

    public LightWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0,0);
    }

    public LightWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public LightWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr,  defStyleRes);
        mContext = context;
        setConfig();
    }

    public  void  setConfig(){
        WebSettings webSettings = getSettings();
        // 内容布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 布局算法
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 文件缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(false); // Application Cache缓存机制： manifest文件去确定是否更新，不推荐使用了，标准也不会再支持。
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(false); //这里的 Database 是 webSQL 需要 原生SQL语句，而并非  IndexDB， 而 IndexDB 开启 JS 即可用
        webSettings.setAllowFileAccess(true); // 安卓5-10 测试过图片，只能是 HTML 根目录内部使用，出了根目录就无法访问
        // js 设置： 可用 & 不能打开新窗口
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
        // 字体
        webSettings.setDefaultFontSize(16);
        webSettings.setTextZoom(100); // WebView里的字体就不会随系统字体大小设置发生变化
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        // UA
        webSettings.setUserAgentString(VERSION);
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);

        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    public void loadUrl(String url,JavaScriptHelper.onMessageListener listener,Boolean debug) {
        JavaScriptHelper javaScriptHelper = new JavaScriptHelper(mContext,listener);
        addJavascriptInterface(javaScriptHelper,"AndroidNative");
        if (debug){  debugJs = FileHelper.getAssetsData(mContext,"vconsole.js");  }
        super.loadUrl(url);
    }
    public  void openDebug(){
        if (debugJs != null){
            evaluateJavascript(debugJs, null);
            debugJs = null;
        }
    }

    public void evaluateJsByID(String id, JSONObject success,JSONObject error,boolean notRemoveLister){
        String flag = notRemoveLister ? "true" : "false";
        String successStr = success == null ? "null" : success.toString();
        String errorStr = error == null ? "null" : error.toString();
        evaluateJsByID(id,successStr,errorStr,flag);
    }

    public void evaluateJsByID(String id, String success,String error,String flag){
        String str = "window["+ globalName +"].exec("+id+","+ success +","+ error +","+ flag +")";
        Log.d("Javascript",str);
        evaluateJavascript(str,null);
    }
    public void pub(String name, String res){
        String myRes = JsonHelper.getNormalRes(res,0);
        String str = "window["+ globalName +"].pub('"+ name +"',"+ myRes +")";
        Log.d("Javascript",str);
        evaluateJavascript(str,null);
    }

    public void setGlobalName(String globalName) {
        this.globalName = globalName;
    }
}
