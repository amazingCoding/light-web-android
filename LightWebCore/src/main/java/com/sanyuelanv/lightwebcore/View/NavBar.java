package com.sanyuelanv.lightwebcore.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.telephony.UiccCardInfo;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.Model.PageConfig;
import com.sanyuelanv.lightwebcore.R;

/**
 * Create By songhang in 2020/8/14
 */
public class NavBar extends LinearLayout {
    private Context mContext;
    private LinearLayout backBtn;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private TextView titleView;
    private LinearLayout container;
    private boolean isInit;

    public NavBar(Context context) {
        this(context,null,0,0);
    }
    public NavBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0,0);
    }
    public NavBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    public NavBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
    }
    public  void setUpView(int height,int navBarHeight, PageConfig pageConfig, View.OnClickListener listener){
        isInit = true;
        container = new LinearLayout(mContext);
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navBarHeight);
        addView(container,containerLayoutParams);
        container.setGravity(Gravity.BOTTOM);

        linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        container.addView(linearLayout,layoutParams);
        linearLayout.setOrientation(HORIZONTAL);
        // back
        if(pageConfig.isNeedBack()){
            createBtn(height,UIHelper.isDeepColor(pageConfig.getNavBackgroundColor()));
            backBtn.setOnClickListener(listener);
        }
        // title,title color
        createTitle(height,pageConfig.isNeedBack(),pageConfig.getTitle(),Color.parseColor(pageConfig.getTitleColor()));
        // back color
        setBackgroundColor(Color.parseColor(pageConfig.getNavBackgroundColor()));
    }
    public void changeView(int navBarHeight,PageConfig pageConfig){
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, navBarHeight);
        container.setLayoutParams(containerLayoutParams);
        // back color
        setBackgroundColor(Color.parseColor(pageConfig.getNavBackgroundColor()));
        // title
        titleView.setTextColor(Color.parseColor(pageConfig.getTitleColor()));
        titleView.setText(pageConfig.getTitle());
        // back
        if(imageView != null){
            boolean isDeepColor = UIHelper.isDeepColor(pageConfig.getNavBackgroundColor());
            Drawable blackImage = getResources().getDrawable(isDeepColor ? R.drawable.light_web_back_w : R.drawable.light_web_back);
            imageView.setImageDrawable(blackImage);
        }
    }
    private void createBtn(int width,boolean isDeepColor){
        backBtn = new LinearLayout(mContext);
        int imageWidth = UIHelper.dp2px(mContext,20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,width);
        LinearLayout.LayoutParams ImageLayoutParams = new LinearLayout.LayoutParams(imageWidth,imageWidth);
        backBtn.setGravity(Gravity.CENTER);
        imageView = new ImageView(mContext);
        Drawable blackImage = getResources().getDrawable(isDeepColor ? R.drawable.light_web_back_w : R.drawable.light_web_back);
        imageView.setImageDrawable(blackImage);
        backBtn.addView(imageView,ImageLayoutParams);
        linearLayout.addView(backBtn,layoutParams);
    }
    private void createTitle(int height,boolean isNeedBack,String text,int textColor){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, height,1);
        // 95 + 12 + 10
        int right = UIHelper.dp2px(mContext,117);
        int left = isNeedBack ?  UIHelper.dp2px(mContext,107) - height : right;
        layoutParams.setMargins(left,0,right,0);
        titleView = new TextView(mContext);
        titleView.setMaxLines(1);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        titleView.setTextColor(textColor);
        titleView.setText(text);
        linearLayout.addView(titleView,layoutParams);
    }

    public boolean isInit() {
        return isInit;
    }
}
