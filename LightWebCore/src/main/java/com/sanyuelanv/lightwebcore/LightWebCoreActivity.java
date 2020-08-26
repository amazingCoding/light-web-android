package com.sanyuelanv.lightwebcore;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleObserver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sanyuelanv.lightwebcore.Fragments.ActionSheet;
import com.sanyuelanv.lightwebcore.Fragments.MyActionSheet;
import com.sanyuelanv.lightwebcore.Fragments.BaseFragment;
import com.sanyuelanv.lightwebcore.Helper.AndroidBug5497Workaround;
import com.sanyuelanv.lightwebcore.Helper.DownLoadHelper;
import com.sanyuelanv.lightwebcore.Model.Enum.FragmentLife;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;

import java.io.File;

public class LightWebCoreActivity extends FragmentActivity implements  DownLoadHelper.OnDownloadListener, LifecycleObserver {
    private  int maxRouter;
    private boolean isReady = false;
    private boolean isDev;
    private DownLoadHelper manager;
    private String downLoadURL;
    private Class fragmentClass = BaseFragment.class;
    private int currentNightMode;
    private int lifeCode = 0;
    private ActionSheet actionSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_web_core_main);
        AndroidBug5497Workaround.assistActivity(this);
        // get data
        Intent intent = getIntent();
        downLoadURL = intent.getStringExtra("url");
        maxRouter = intent.getIntExtra("maxRouter",3);
        isDev = intent.getBooleanExtra("isDev",false);
        currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // init
        initData();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void finish() {
        super.finish();
        lifeCode = -1;
        overridePendingTransition(R.anim.light_web_anim_hide_in,R.anim.light_web_anim_hide);
    }
    @Override
    public void onBackPressed() {
        FragmentManager manager =  this.getSupportFragmentManager();
        int size = manager.getFragments().size();
        if (size <= 1){
            finish();
        }
        else {
            if(actionSheet != null){
                manager.popBackStackImmediate("lightWeb_actionSheet",1);
                actionSheet = null;
                return;
            }
            popRouter(size - 2,null);
        }
    }
    @Override
    protected void onDestroy() {
        manager.cancelDownLoad();
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(lifeCode == 1){
            Log.d("MyLife","onStart");
            lifeCode = 0;
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(lifeCode >= 0){
            lifeCode = 1;
            Log.d("MyLife","onStop");
        }

    }

    private void initData(){
        // show UI
        try {
            // 首个创建的 popStack 确保为 空: 防止 onCreate 多次执行
            FragmentManager manager = this.getSupportFragmentManager();
            if(manager.getFragments().size() > 0){
                manager.popBackStackImmediate("ID_0" ,1);
            }
            pushRouter("index",null);
        }
        catch (Exception e){}
        // load zip - unzip - make fragment show app
        if(manager == null){
            manager = new DownLoadHelper(downLoadURL,this);
            manager.setDownloadListener(this);
            manager.download(false);
        }
        else {
            manager.download(true);
        }

    }
    private BaseFragment createNewFragment(int index, String name,String extra) throws InstantiationException, IllegalAccessException {
        BaseFragment baseFragment = (BaseFragment) fragmentClass.newInstance();
        baseFragment.initData(index,name,extra,isDev);
        return baseFragment;
    }

    // DownLoadHelper.OnDownloadListener 下载相关
    @Override
    public void onDownloadSuccess(final File file) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        final LightWebCoreActivity that = this;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = that.getSupportFragmentManager();
                BaseFragment fragment = (BaseFragment)manager.getFragments().get(0);
                if(fragment != null)  fragment.loadHTML();
                isReady = true;
            }
        });
    }
    @Override
    public void onDownloading(double progress) {

    }
    @Override
    public void onDownloadFailed(final Exception e) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        final LightWebCoreActivity that = this;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = that.getSupportFragmentManager();
                BaseFragment fragment = (BaseFragment)manager.getFragments().get(0);
                if(fragment != null)  fragment.setError(e.getMessage());
            }
        });
    }
    public void reDownLoad(){
        isReady = false;
        initData();
    }

    // 路由相关
    public boolean pushRouter(String name,String extra) throws IllegalAccessException, InstantiationException {
        FragmentManager manager =  this.getSupportFragmentManager();
        int index = manager.getBackStackEntryCount();
        if(index > maxRouter - 1) return false;
        String ID = "ID_" + index;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        BaseFragment fragment = createNewFragment(index,name,extra);
        if(index == 0) {
            transaction.add(R.id.light_web_core_main,fragment,ID);
        }
        else {
            BaseFragment baseFragment = (BaseFragment)manager.getFragments().get(index - 1);
            // baseFragment 设置状态
            baseFragment.setLife(FragmentLife.willHide);
            transaction.hide(baseFragment);
            transaction.add(R.id.light_web_core_main,fragment,ID);
        }
        transaction.addToBackStack(ID);
        transaction.commitAllowingStateLoss();
        return  true;
    }
    public void popRouter(int index,String extra){
        FragmentManager manager = this.getSupportFragmentManager();
        // 栈内，最后一个 will dead ,中间直接 dead ，目标 fragment willShow
        int lastIndex = manager.getFragments().size() - 1;
        String myExtra = extra;
        for (int i = lastIndex;i >= index;i--){
            BaseFragment fragment = (BaseFragment)manager.getFragments().get(i);
            // 即将出现的
            if(i == index) {
                if(myExtra != null) fragment.setExtra(myExtra);
                fragment.setLife(FragmentLife.willShow);
            }
            // 需要销毁的
            else{
                if (i == lastIndex){
                    myExtra = myExtra == null ? fragment.getPopExtra() :myExtra;
                    fragment.setLife(FragmentLife.willDead);
                }
                else fragment.setLife(FragmentLife.dead);
            }
        }
        manager.popBackStackImmediate("ID_" + index,0);
    }
    public void replaceRouter(int index,String name,String extra) throws IllegalAccessException, InstantiationException {
        // 清空 >= index 的，再加入一个新的 init
        FragmentManager manager = this.getSupportFragmentManager();
        int lastIndex = manager.getFragments().size() - 1;
        for (int i = lastIndex;i >= index;i--){
            BaseFragment baseFragment = (BaseFragment)manager.getFragments().get(i);
            baseFragment.setLife(FragmentLife.dead);
        }
        manager.popBackStackImmediate("ID_" + index,1);
        // 创建一个新的
        BaseFragment fragment = createNewFragment(index,name,extra);
        fragment.setLife(FragmentLife.replaceWillShow);
        String ID = "ID_" + index;
        FragmentTransaction transaction = manager.beginTransaction();

        if(index > 0){
            BaseFragment baseFragment = (BaseFragment)manager.getFragments().get(index - 1);
            baseFragment.setLife(FragmentLife.hide);
            transaction.hide(baseFragment);
        }
        transaction.add(R.id.light_web_core_main,fragment,ID);
        transaction.addToBackStack(ID);
        transaction.commitAllowingStateLoss();
    }

    // get
    public int getMaxRouter() {
        return maxRouter;
    }
    public DownLoadHelper getManager() {
        return manager;
    }
    public boolean isReady() {
        return isReady;
    }

    // open about
    public void openAbout(){
        actionSheet = new ActionSheet();
        String ID = "lightWeb_actionSheet";
        FragmentManager manager =  this.getSupportFragmentManager();
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.light_web_core_main, actionSheet,ID);
        transaction.addToBackStack(ID);
        transaction.commitAllowingStateLoss();
//        MyActionSheet myActionSheet = new MyActionSheet(isDev, ThemeConfig.auto, ThemeTypes.light);
//        myActionSheet.setListener(new MyActionSheet.OnControlBtnListener() {
//            @Override
//            public void changeDevItem() {
//
//            }
//
//            @Override
//            public void reloadItem() {
//
//            }
//        });
//        myActionSheet.show(getSupportFragmentManager(),"lightWeb_actionSheet");
    }


    // 静态方法——创建页面
    public static void createLightWebView(Activity activity, String url, int maxRouter,boolean isDev){
        Context myContext = activity.getBaseContext();
        Intent intent = new Intent(myContext , LightWebCoreActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("maxRouter",maxRouter);
        intent.putExtra("isDev",isDev);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivityForResult(intent,200);
        activity.overridePendingTransition(R.anim.light_web_anim_show,R.anim.light_web_anim_show_out);
    }
}