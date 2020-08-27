package com.sanyuelanv.lightwebcore.View;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;

/**
 * Create By songhang in 8/26/20
 */
public class ActionSheetView extends LinearLayout implements  View.OnClickListener,View.OnTouchListener {
    public interface OnControlBtnListener {
        void changeDevItem();
        void reloadItem();
        void closeItem();
    }
    private static String devItemTitle = "打开调试台";
    private static String reloadItemTitle = "重新下载";
    private static String cancelItemTitle = "取消";
    protected OnControlBtnListener listener;
    private Context mContext;
    private boolean isDev;
    private ThemeTypes nowTheme;
    private ThemeConfig theme;
    private LinearLayout linearLayout;
    private int contentHeight;
    private  boolean isInit = false;
    protected float downX, downY,moveY,moveX;

    public void setListener(ActionSheetView.OnControlBtnListener listener) {
        this.listener = listener;
    }
    public ActionSheetView(Context context, boolean isDev, ThemeTypes nowTheme, ThemeConfig theme) {
        super(context);
        mContext = context;
        this.isDev = isDev;
        this.nowTheme = nowTheme;
        this.theme = theme;
        createView();
    }

    public  void changStyle(ThemeTypes nowTheme){
        Context context = mContext;
        // 不是 auto 而且isInit = true   || 相同主题 则无需修改主题
        if(this.theme != ThemeConfig.auto && isInit) return;
        if (this.nowTheme == nowTheme && isInit) return;
        this.nowTheme = nowTheme;

        int itemBgColor;
        int itemBgPressColor;
        int itemTextColor;
        int cancelTextColor;
        int mainBgColor;
        int lineColor;
        float radius = UIHelper.dp2px(context,12);
        float[] radii = {radius,radius,radius,radius,0,0,0,0};
        float[] radiiNone = {0,0,0,0,0,0,0,0};
        if (nowTheme == ThemeTypes.light){
            mainBgColor = Color.rgb(245,245,245);
            itemBgColor = Color.WHITE;
            itemBgPressColor = Color.argb(25,50,50,50);
            lineColor = Color.rgb(239,239,239);
            itemTextColor = Color.parseColor("#353535");
            cancelTextColor = Color.parseColor("#e64340");
        }
        else {
            mainBgColor = Color.rgb(24,24,24);
            itemBgColor = Color.rgb(35,35,35);
            itemBgPressColor = Color.argb(50,153,153,153);
            lineColor = Color.rgb(30,30,30);
            itemTextColor = Color.parseColor("#BBBBBB");
            cancelTextColor = Color.parseColor("#CD5C5C");
        }
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            int tag = (int) view.getTag();
            switch (tag){
                // line
                case -4:{
                    view.setBackgroundColor(lineColor);
                    break;
                }
                // 取消
                case -1:{
                    TextView textView = (TextView)view;
                    textView.setTextColor(cancelTextColor);
                    textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor, radiiNone));
                    break;
                }
                default:{
                    TextView textView = (TextView)view;
                    textView.setTextColor(itemTextColor);
                    if(tag == 1) textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radii));
                    else {
                        if (!isDev) textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radii));
                        else textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radiiNone));
                    }
                    break;
                }
            }
        }
        linearLayout.setBackground(UIHelper.getGradientDrawable(radii,0,0,mainBgColor));
        linearLayout.setClipToOutline(true);
    };
    private  void createView(){
        Context context = mContext;
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.BOTTOM);
        setClickable(true);
        LinearLayout topView = new LinearLayout(context);
        topView.setTag(-2);
        LinearLayout.LayoutParams topViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        addView(topView,topViewParams);
        topView.setOnClickListener(this);
        topView.setOnTouchListener(this);

        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams mainBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.BOTTOM);
        addView(linearLayout,mainBoxParams);
        if(isDev){
            createTextView(1,devItemTitle);
            addLineInView(UIHelper.dp2px(context,1) );
        }
        createTextView(0,reloadItemTitle);
        addLineInView(UIHelper.dp2px(context,8) );
        createTextView(-1,cancelItemTitle);

        if(UIHelper.isEdgeToEdgeEnabled(mContext) == 2){
            int bottom = UIHelper.getRealHeight(mContext) - UIHelper.getScreenHeight(mContext);
            addLineInView(bottom);
        }
        changStyle(this.nowTheme);
        // 展示动画
        Log.d("contentHeight",contentHeight+"");
        showView();
        isInit = true;
    };
    public void showView(){
        // 背景色渐变
        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", 0x000f0000, 0x8C000000);
        animator.setDuration(150);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
        // 底部上升
        Animation translateAnimation = new TranslateAnimation(0, 0, contentHeight, 0);//设置平移的起点和终点
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        translateAnimation.setDuration(250);  //设置动画时间
        linearLayout.setAnimation(translateAnimation);
        translateAnimation.start(); //启动
    }
    public void hideView(Animation.AnimationListener listener){
        // 背景色渐变
        ValueAnimator animator = ObjectAnimator.ofInt(this, "backgroundColor", 0x8C000000,0x000f0000);
        animator.setDuration(250);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
        // 底部下降
        Animation translateAnimation = new TranslateAnimation(0, 0, 0, contentHeight);//设置平移的起点和终点
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        translateAnimation.setDuration(250);  //设置动画时间
        linearLayout.setAnimation(translateAnimation);
        translateAnimation.setAnimationListener(listener);
        translateAnimation.start(); //启动
    }
    private void createTextView(int tag, String text){
        Context context = mContext;
        int h = UIHelper.dp2px(context,55);
        contentHeight += h;
        int paddingH = UIHelper.dp2px(context,24);
        int textSize = 17;
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(paddingH,0,paddingH,0);
        textView.setLines(1);
        textView.setTag(tag);
        textView.setText(text);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams titleViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,h);
        linearLayout.addView(textView,titleViewParams);
        textView.setOnClickListener(this);
        textView.setOnTouchListener(this);
    }
    private void addLineInView(int height){
        contentHeight += height;
        Context context = mContext;
        assert context != null;
        View line = new View(context);
        line.setTag(-4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        linearLayout.addView(line,params);
    }
    private StateListDrawable createStateDrawable(int normalColor, int pressColor, float[] radii){
        StateListDrawable bg = new StateListDrawable();
        Drawable pressed  = UIHelper.getGradientDrawable(radii,0,0,pressColor);
        Drawable normal = UIHelper.getGradientDrawable(radii,0,0,normalColor);
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);
        bg.addState(new int[] {  }, normal);
        return bg;
    }

    @Override
    public void onClick(View v) {
        int index = (int)v.getTag();
        if (index == 0){  if(listener != null) listener.reloadItem(); }
        else if (index == 1){  if(listener != null) listener.changeDevItem(); }
        else{  if(listener != null) listener.closeItem(); }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                moveX = 0;
                moveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX += Math.abs(event.getX() - downX);
                moveY += Math.abs(event.getY() - downY);
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                //判断是否继续传递信号
                if(moveX>20||moveY>20){
                    return true;
                }
                break;
        }
        return false;//继续执行后面的代码:点击
    }

}
