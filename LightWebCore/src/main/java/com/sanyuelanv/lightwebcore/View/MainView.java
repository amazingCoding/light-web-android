package com.sanyuelanv.lightwebcore.View;

import android.content.Context;
import android.graphics.Color;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.Model.PageConfig;

/**
 * Create By songhang in 2020/8/13
 */
public class MainView extends RelativeLayout {
    private Context mContext;
    private FrameLayout frameLayout;
    private CapsuleBtn capsuleBtn;
    private int statusBarHeight;
    private NavBar navBar;
    private LinearLayout appView;

    private int capsuleBtnWidth = 95;
    private int capsuleBtnHeight = 32;
    private static int DesignNavBarHeight = 52;
    private int capsuleBtnTop;
    private int capsuleBtnRight;
    private int navBarHeight = 0;
    private OnClickListener backListener;

    public MainView(Context context) {
        this(context, null,0, 0);
    }
    public MainView(Context context, AttributeSet attrs) {
        this(context, attrs,0, 0);
    }
    public MainView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    public MainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initData();
    }

    public MainView(Context context, int statusBarHeight,CapsuleBtn.CapsuleBtnClickListener clickListener) {
        super(context);
        this.mContext = context;
        initData();
        this.statusBarHeight = statusBarHeight;
        setCapsuleBtn(statusBarHeight,clickListener);
    }

    private void initData(){
        // 主要内容
        frameLayout = new FrameLayout(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(frameLayout,layoutParams);
    }
    public void initApp(PageConfig pageConfig, LightWebView webView,View.OnClickListener listener){
        // 隐藏
        frameLayout.setVisibility(GONE);
        // 创建 linearLayout
        appView = new LinearLayout(mContext);
        appView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams appViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        addView(appView,appViewLayoutParams);
        // 添加 navBar
        backListener = listener;
        LinearLayout.LayoutParams navBarLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        navBar = new NavBar(mContext);
        if(!pageConfig.isHideNav()){  createNavBar(pageConfig); }
        appView.addView(navBar,navBarLayoutParams);
        // 添加 webview
        LinearLayout.LayoutParams webLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        appView.addView(webView,webLayoutParams);
        webView.setBackgroundColor(Color.parseColor(pageConfig.getBackgroundColor()));
        // 250 ms 动画显示
        Fade fade = new Fade();
        fade.setDuration(250);
        TransitionManager.beginDelayedTransition(frameLayout,fade);
        frameLayout.setVisibility(View.VISIBLE);
    }
    private void createNavBar(PageConfig pageConfig){
        int height =  UIHelper.dp2px(mContext,DesignNavBarHeight);
        navBarHeight = height + statusBarHeight;
        navBar.setUpView(height,navBarHeight,pageConfig,backListener);
    }
    public void changeApp(PageConfig pageConfig){
        if(pageConfig.isHideNav()){
            navBarHeight = 0;
            // 隐藏
            // 没有创建 - 不处理
            // 创建了 - 改变高度
            if(navBar.isInit()){  navBar.changeView(navBarHeight,pageConfig);  }
        }
        else{
            int height =  UIHelper.dp2px(mContext,DesignNavBarHeight);
            navBarHeight = height + statusBarHeight;
            // 展示
            // 没有创建 - 创建
            // 创建了 - 改变高度
            if(navBar.isInit()){  navBar.changeView(navBarHeight,pageConfig);  }
            else {  createNavBar(pageConfig);  }
        }

    }

    public void setCapsuleBtn(int statusBarHeight,CapsuleBtn.CapsuleBtnClickListener clickListener) {
        if(capsuleBtn == null){
            capsuleBtn = new CapsuleBtn(mContext);
            int width = UIHelper.dp2px(mContext,capsuleBtnWidth);
            int height =  UIHelper.dp2px(mContext,capsuleBtnHeight);
            // statusBarHeight + （44 - 32） / 2
            int top = statusBarHeight + UIHelper.dp2px(mContext,8);
            int right = UIHelper.dp2px(mContext,12);

            capsuleBtnTop = UIHelper.px2dp(mContext,top);
            capsuleBtnRight = UIHelper.px2dp(mContext,right);

            RelativeLayout.LayoutParams layoutParams  = new RelativeLayout.LayoutParams(width,height);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            layoutParams.setMargins(0,top,right,0);
            capsuleBtn.setClickListener(clickListener);
            addView(capsuleBtn,layoutParams);
        }
    }
    public void addView(View view){
        frameLayout.removeAllViews();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(view,layoutParams);
    }

    public void addView(View view, LinearLayout.LayoutParams layoutParams) {
        frameLayout.removeAllViews();
        frameLayout.addView(view,layoutParams);
    }

    public int getCapsuleBtnWidth() {
        return capsuleBtnWidth;
    }

    public int getCapsuleBtnHeight() {
        return capsuleBtnHeight;
    }

    public int getCapsuleBtnTop() {
        return capsuleBtnTop;
    }

    public int getCapsuleBtnRight() {
        return capsuleBtnRight;
    }

    public int getNavBarHeight() {
        return navBarHeight;
    }

}
