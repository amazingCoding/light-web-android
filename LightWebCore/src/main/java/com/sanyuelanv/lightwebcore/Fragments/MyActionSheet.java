package com.sanyuelanv.lightwebcore.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;
import com.sanyuelanv.lightwebcore.R;

import java.util.Objects;

/**
 * Create By songhang in 2020/8/25
 */
public class MyActionSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    protected OnControlBtnListener listener;
    private boolean isDev;
    private ThemeTypes nowTheme;
    private ThemeConfig theme;
    private LinearLayout linearLayout;
    private  boolean isInit = false;

    public MyActionSheet(boolean isDev, ThemeConfig theme, ThemeTypes nowTheme) {
        this.isDev = isDev;
        this.theme = theme;
        // auto 的话采用传过来的nowTheme ，如果不是 auto 就直接写死
        this.nowTheme = theme == ThemeConfig.auto ? nowTheme : ThemeTypes.compare(theme);
    }

    public void setListener(OnControlBtnListener listener) {
        this.listener = listener;
    }

    public interface OnControlBtnListener {
        void changeDevItem();
        void reloadItem();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(createView());
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        assert dialog != null;
        dialog.setCanceledOnTouchOutside(false);
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        bottomSheet.setBackgroundResource(android.R.color.transparent);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        bottomSheet.setFitsSystemWindows(false);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        behavior.setHideable(false);
        // 初始为展开状态
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public  void changStyle(ThemeTypes nowTheme){
        Context context = getContext();
        assert context != null;
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
                        if (isDev) textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radii));
                        else textView.setBackground(createStateDrawable(itemBgColor,itemBgPressColor,radiiNone));
                    }
                    break;
                }
            }
        }
        linearLayout.setBackground(UIHelper.getGradientDrawable(radii,0,0,mainBgColor));
        linearLayout.setClipToOutline(true);
    };
    protected  View createView(){
        //
        Context context = getContext();
        assert context != null;
        LinearLayout mainBox = new LinearLayout(context);
        mainBox.setOrientation(LinearLayout.VERTICAL);
        mainBox.setGravity(Gravity.BOTTOM);

        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams mainBoxParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.BOTTOM);
        mainBox.addView(linearLayout,mainBoxParams);
        if(isDev){
            createTextView(1,"打开调试台");
            addLineInView(false);
        }
        createTextView(0,"重新下载");
        addLineInView(true);
        createTextView(-1,"取消");
        changStyle(this.nowTheme);
        isInit = true;
        return mainBox;
    };
    private void createTextView(int tag, String text){
        Context context = getContext();
        assert context != null;
        int h = UIHelper.dp2px(context,55);
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
    }
    private void addLineInView(boolean isBold){
        Context context = getContext();
        assert context != null;
        int height = UIHelper.dp2px(context,isBold ? 8 : 1) ;
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
    }
}
