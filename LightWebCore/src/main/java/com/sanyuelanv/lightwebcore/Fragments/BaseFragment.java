package com.sanyuelanv.lightwebcore.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanyuelanv.lightwebcore.Helper.AndroidHelper;
import com.sanyuelanv.lightwebcore.Helper.JavaScriptHelper;
import com.sanyuelanv.lightwebcore.Helper.JsonHelper;
import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.LightWebCoreActivity;
import com.sanyuelanv.lightwebcore.Model.AppInfo;
import com.sanyuelanv.lightwebcore.Model.Enum.BridgeEvents;
import com.sanyuelanv.lightwebcore.Model.Enum.BridgeMethods;
import com.sanyuelanv.lightwebcore.Model.Enum.FragmentLife;
import com.sanyuelanv.lightwebcore.Model.Enum.FragmentStatus;
import com.sanyuelanv.lightwebcore.Model.PageConfig;
import com.sanyuelanv.lightwebcore.Model.RouterSystem;
import com.sanyuelanv.lightwebcore.Model.Vibrator;
import com.sanyuelanv.lightwebcore.R;
import com.sanyuelanv.lightwebcore.View.CapsuleBtn;
import com.sanyuelanv.lightwebcore.View.ErrorView;
import com.sanyuelanv.lightwebcore.View.LightWebView;
import com.sanyuelanv.lightwebcore.View.LoadView;
import com.sanyuelanv.lightwebcore.View.MainView;

import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

/**
 * Create By songhang in 2020/8/14
 */
public class BaseFragment extends Fragment implements CapsuleBtn.CapsuleBtnClickListener, JavaScriptHelper.onMessageListener {
    private int currentIndex;
    private  String urlName;
    private  String extra;

    private FragmentLife life = FragmentLife.willInit;
    private  MainView mainView;
    private LightWebView mWebView;

    private AppInfo appInfo;
    private PageConfig pageConfig;
    private String popExtra;
    private boolean isDev;
    private LightWebCoreActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // status 设置,修改 status
        LightWebCoreActivity activity = (LightWebCoreActivity)getActivity();
        assert activity != null;
        mActivity = activity;
        int statusBarHeight = UIHelper.initStatusBar(mActivity,mActivity.getWindow());
        if(mainView == null){
            mainView = new MainView(activity,statusBarHeight,this);
            mainView.addView(setLoadView());
            // 设置 appInfo
            appInfo = new AppInfo(activity);
            appInfo.setCapsuleRect(mainView.getCapsuleBtnWidth(),mainView.getCapsuleBtnHeight(),mainView.getCapsuleBtnTop(),mainView.getCapsuleBtnRight());
            appInfo.setRouter(activity.getMaxRouter(),currentIndex);
            appInfo.setStatusBarHeight(statusBarHeight);
        }
        if(activity.isReady()){ loadHTML();  }
        return mainView;
    }
    @Override
    public void onDestroy() {
        removeHTML();
        super.onDestroy();
    }
    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        Log.d("动画","urlName:"+urlName + "--- index ---"+currentIndex + "" + "-- nextAnim:" + nextAnim);
        switch (life){
            case willInit:{
                setLife(FragmentLife.show);
                if(currentIndex != 0) return AnimationUtils.loadAnimation(mActivity,R.anim.light_web_anim_right_in);
                return super.onCreateAnimation(transit, enter, nextAnim);
            }
            case willShow:{
                setLife(FragmentLife.show);
                return AnimationUtils.loadAnimation(mActivity,R.anim.light_web_anim_page_in);
            }
            case replaceWillShow:{
                setLife(FragmentLife.show);
                return AnimationUtils.loadAnimation(mActivity,R.anim.light_web_anim_replace_in);
            }
            case willHide:{
                setLife(FragmentLife.hide);
                return AnimationUtils.loadAnimation(mActivity,R.anim.light_web_anim_page_out);
            }
            case willDead:{
                setLife(FragmentLife.dead);
                return AnimationUtils.loadAnimation(mActivity,R.anim.light_web_anim_right_out);
            }
            default:{
                return super.onCreateAnimation(transit, enter, nextAnim);
            }
        }
    }

    public void  initData(int currentIndex, String urlName,String extra,boolean isDev){
        this.currentIndex = currentIndex;
        this.urlName = urlName;
        this.extra = extra;
        this.isDev = isDev;
    }

    // region  webView
    private void removeHTML(){
        if (mWebView != null){
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ViewGroup viewGroup = ((ViewGroup) mWebView.getParent());
            if (viewGroup != null) viewGroup.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
    public void loadHTML(){
        // 判断 HTML 是否存在
        if(checkFileExists(urlName)){
            mWebView = new LightWebView(this.getActivity());
            String url =  "file://" + mActivity.getManager().getAppFileDir().getAbsolutePath() + "/" + urlName + ".html";
            mWebView.loadUrl(url,this,isDev);
        }
        else {  setError("missing index file");  }

    }
    // endregion webView

    // region helper
    private boolean checkFileExists(String name){
        File appFile = mActivity.getManager().getAppFileDir();
        File htmlFile = new File(appFile,name + ".html");
        return htmlFile.exists();
    }
    // endregion helper

    // region GET/SET
    // life 去掌控路由动画事件  & show/hide 事件 js 推送
    public void setLife(FragmentLife life) {
        this.life = life;
        if(mWebView != null){
            if (life == FragmentLife.willHide) mWebView.pub(BridgeEvents.hide,null);
            else if (life == FragmentLife.willShow){
                mWebView.pub(BridgeEvents.show,extra);
                extra = null;
                // 状态栏调整
                UIHelper.changeStatusBar(mActivity,mActivity.getWindow(), pageConfig.getStatusStyle());
            }
        }
    }
    // 对外层负责的错误状态切换
    public void setError(String error) {
        mainView.addView(setErrorView(error));
    }
    public String getPopExtra() {
        return popExtra;
    }
    public void setPopExtra(String popExtra) {
        this.popExtra = popExtra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    // endregion GET/SET

    // region customFunction
    // 自定义 view
    protected View setLoadView(){
        return  new LoadView(this.getActivity());
    }
    protected View setErrorView(String error){
        ErrorView view =  new ErrorView(this.getActivity());
        view.setErrText(error);
        view.setCloseBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBtnEvent();
            }
        });
        view.setReloadBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainView.addView(setLoadView());
                mActivity.reDownLoad();
            }
        });
        return view;
    }
    // 自定义 CapsuleBtn 事件
    protected void aboutBtnEvent(){
        mActivity.openAbout();
    }
    protected void closeBtnEvent(){
        mActivity.finish();
    }
    // endregion customFunction

    // CapsuleBtn.CapsuleBtnClickListener
    @Override
    public void capsuleBtnClick(int viewID) {
        if (viewID == 0) aboutBtnEvent();
        else closeBtnEvent();
    }
    // region JavaScriptHelper.onMessageListener
    @Override
    public void onMessage(final String name,final String id,final JSONObject jsonObject) {
        Log.d("Javascript",name);
        Log.d("Javascript",id);
        Log.d("Javascript",jsonObject.toString());
    }
    @Override
    public void lightWebInit(final String callBackID,final JSONObject jsonObject) {
        // 生产 页面配置
        pageConfig = new PageConfig(jsonObject);
        pageConfig.setNeedBack(currentIndex > 0);
        mWebView.openDebug();
        UIHelper.changeStatusBar(mActivity,mActivity.getWindow(), pageConfig.getStatusStyle());
        mWebView.setGlobalName(pageConfig.getGlobal());
        // 创建 UI
        mainView.initApp(pageConfig, mWebView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.popRouter(currentIndex - 1,null);
            }
        });
        // 构造回调参数
        appInfo.setHeight(mainView.getNavBarHeight());
        // 回调 js
        mWebView.evaluateJsByID(callBackID,appInfo.toString(extra),"null","false");
        // 用完  extra 要清空
        extra = null;
    }
    @Override
    public void lightWebPageConfig(String callBackID, JSONObject jsonObject) {
        pageConfig.update(jsonObject);
        // 导航栏
        mainView.changeApp(pageConfig);
        // 状态栏
        UIHelper.changeStatusBar(mActivity,mActivity.getWindow(), pageConfig.getStatusStyle());
        // 页面背景色
        mWebView.setBackgroundColor(Color.parseColor(pageConfig.getBackgroundColor()));
        appInfo.setHeight(mainView.getNavBarHeight());
        // 回调 js
        mWebView.evaluateJsByID(callBackID,appInfo.toPageString(),"null","false");
    }
    @Override
    public void lightWebRouter(String callBackID, JSONObject jsonObject) {
        RouterSystem routerSystem = new RouterSystem(jsonObject);
        String err  = null;
        switch (routerSystem.getAction()){
            case push:{
                // 判断 文件是否存在，调用 activity push 接口打开新页面
                if (checkFileExists(routerSystem.getName())){
                    try {
                        boolean resFlag =  mActivity.pushRouter(routerSystem.getName(),routerSystem.getExtra());
                        if(resFlag){
                            String res = JsonHelper.getNormalRes(null,0);
                            mWebView.evaluateJsByID(callBackID,res,"null","false");
                        }
                        else err = "max router limit";
                    }
                    catch (IllegalAccessException | java.lang.InstantiationException e) { err = e.getMessage(); }
                }
                else {  err = "missing file"; }
                break;
            }
            case pop:{
                if(currentIndex == 0){  err = "it is the first page";  }
                else {
                    if(routerSystem.getPos() > -1 && routerSystem.getPos() >= currentIndex){
                        err = "it is error pos to pop";
                    }
                    else {
                        int pos = currentIndex - 1;
                        if(routerSystem.getPos() > -1) pos = routerSystem.getPos();
                        mActivity.popRouter(pos,routerSystem.getExtra());
                    }
                }
                break;
            }
            case replace:{
                // pos 从 0 开始算
                // 比 当前 page 位置大的话，返回报错
                // 不设置的时候，相当于 当前 page pos
                if (checkFileExists(routerSystem.getName())){
                    if(routerSystem.getPos() > -1 && routerSystem.getPos() >= currentIndex){
                        err = "it is error pos to replace";
                    }
                    else {
                        int pos = currentIndex;
                        if(routerSystem.getPos() > -1) pos = routerSystem.getPos();
                        try {
                            mActivity.replaceRouter(pos,routerSystem.getName(),routerSystem.getExtra());
                        } catch (IllegalAccessException | java.lang.InstantiationException e) {
                            err = e.getMessage();
                        }
                    }
                }
                else {  err = "missing file"; }
                break;
            }
            case setPopExtra:{
                setPopExtra(routerSystem.getExtra());
                String res = JsonHelper.getNormalRes(null,0);
                mWebView.evaluateJsByID(callBackID,res,"null","false");
                break;
            }
            case restart:{
                mActivity.reDownLoad();
                break;
            }
        }
        if(err != null){
            String res = JsonHelper.getNormalRes(err,-1);
            mWebView.evaluateJsByID(callBackID,"null",res,"false");
        }
    }
    @Override
    public void vibrate(String callBackID, JSONObject jsonObject) {
        Vibrator vibrator = new Vibrator(jsonObject);
        String res;
        if(vibrator.vibrator(mActivity)){
            res = JsonHelper.getNormalRes(null,0);
            mWebView.evaluateJsByID(callBackID,res,"null","false");
        }
        else {
            // 没有震动器
            res = JsonHelper.getNormalRes("Vibrator missing",-1);
            mWebView.evaluateJsByID(callBackID,"null",res,"false");
        }
    }
    @Override
    public void setClipboard(String callBackID, JSONObject jsonObject) {
        String content = JsonHelper.getStringInJson(jsonObject,"text","");
        AndroidHelper.setClipContent(mActivity,content);
        String res = JsonHelper.getNormalRes(null,0);
        mWebView.evaluateJsByID(callBackID,res,"null","false");
    }
    @Override
    public void getClipboard(String callBackID, JSONObject jsonObject) {
        String content =  AndroidHelper.getClipContent(mActivity);
        String res = JsonHelper.getNormalRes(content,0);
        mWebView.evaluateJsByID(callBackID,res,"null","false");
    }
    // endregion JavaScriptHelper.onMessageListener
}
