package com.sanyuelanv.lightwebcore.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;

/**
 * Create By songhang in 2020/8/19
 */
public class ErrorView extends LinearLayout {
    private TextView descTextView;
    private LinearLayout reloadBtn;
    private LinearLayout closeBtn;
    private Context mContext;

    public ErrorView(Context context) {
        this(context,null,0,0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0,0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initData();
    }
    private  void  initData(){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        int horizontal = UIHelper.dp2px(mContext,15);
        int bottom = UIHelper.dp2px(mContext,20);
        int width = UIHelper.dp2px(mContext,200);
        int height = UIHelper.dp2px(mContext,40);
        Drawable reloadBtnDrawable = createBtnDrawable(0);
        Drawable closeBtnDrawable = createBtnDrawable(1);
        addView(createTextView("Error",24, Color.parseColor("#000000"),horizontal,bottom));
        descTextView = createTextView("",16, Color.parseColor("#555555"),horizontal,0);
        addView(descTextView);
        reloadBtn = createBtn(reloadBtnDrawable,"重试",width,height,height);
        closeBtn = createBtn(closeBtnDrawable,"返回",width,height,bottom);
    }

    private Drawable createBtnDrawable(int type){
        StateListDrawable bg = new StateListDrawable();
        Drawable normal;
        Drawable pressed;
        int width = UIHelper.dp2px(mContext,20);
        int lineWidth = UIHelper.dp2px(mContext,1);
        int color = Color.parseColor("#87FFFFFF");
        if (type == 0){
            int lineColor = Color.parseColor("#FF00C06C");
            int touchLineColor = Color.parseColor("#8700C06C");
            pressed = UIHelper.getGradientDrawable( width,lineWidth,touchLineColor,color);
            normal = UIHelper.getGradientDrawable( width,lineWidth,lineColor,0);
        }
        else {
            int lineColor = Color.parseColor("#FFBBBBBB");
            int touchLineColor = Color.parseColor("#87BBBBBB");
            pressed = UIHelper.getGradientDrawable( width,lineWidth,touchLineColor,color);
            normal = UIHelper.getGradientDrawable( width,lineWidth,lineColor,0);
        }
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        bg.addState(new int[] {  }, normal);
        return bg;
    }
    private TextView createTextView(String text, int textSize, @ColorInt int color, int horizontal, int bottom){
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(horizontal,0,horizontal,bottom);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        textView.setTextColor(color);
        return textView;
    }
    private LinearLayout createBtn(Drawable drawable,String text,int width,int height,int top){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        LinearLayout linearLayout = new LinearLayout(mContext);
        layoutParams.setMargins(0,top,0,0);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setBackground(drawable);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(HORIZONTAL);

        linearLayout.addView(createTextView(text,17, Color.parseColor("#808080"),0,0));

        addView(linearLayout);
        return linearLayout;
    }
    public void setErrText(String text){
        descTextView.setText(text);
    }
    public void setReloadBtnOnClickListener(@Nullable OnClickListener l) {
        reloadBtn.setOnClickListener(l);
    }
    public void setCloseBtnOnClickListener(@Nullable OnClickListener l) {
        closeBtn.setOnClickListener(l);
    }
}
