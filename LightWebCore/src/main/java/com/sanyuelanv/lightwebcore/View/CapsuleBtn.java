package com.sanyuelanv.lightwebcore.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.R;

/**
 * Create By songhang in 2020/8/13
 */
public class CapsuleBtn extends LinearLayout implements View.OnClickListener {
    public interface CapsuleBtnClickListener{
        public void capsuleBtnClick(int viewID);
    }
    private Context mContext;
    private CapsuleBtnClickListener clickListener;

    public CapsuleBtn(Context context) {
        this(context,null,0,0);
    }
    public CapsuleBtn(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0,0);
    }
    public CapsuleBtn(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }
    public CapsuleBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        this.initData();
    }

    private void initData(){
        int radius = UIHelper.dp2px(mContext,16);
        int strokeWidth = (int)(UIHelper.dp2px(mContext,1) * 0.5) ;
        int strokeColor = Color.parseColor("#59323232");
        int bgColor = Color.parseColor("#80FFFFFF");

        setOrientation(HORIZONTAL);
        Drawable moreBtnImage =  getResources().getDrawable(R.drawable.light_web_more);
        Drawable closeBtnImage = getResources().getDrawable(R.drawable.light_web_circular);
        setBackground(UIHelper.getGradientDrawable(radius,strokeWidth,strokeColor,bgColor));
        // 更多按钮
        LinearLayout morBtn = createBtn(moreBtnImage);
        morBtn.setTag(0);
        morBtn.setOnClickListener(this);
        // 中间分割线
        createLine(strokeWidth,strokeColor);
        // 关闭按钮
        LinearLayout closeBtn = createBtn(closeBtnImage);
        closeBtn.setTag(1);
        closeBtn.setOnClickListener(this);
        // 圆角
        setClipToOutline(true);
        setGravity(Gravity.CENTER);
    }
    private LinearLayout createBtn(Drawable image){
        int width = UIHelper.dp2px(mContext,47);
        int imageWidth = UIHelper.dp2px(mContext,20);
        Drawable bg = UIHelper.getStateListDrawable(Color.parseColor("#30323232"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setBackground(bg);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);

        // image
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(imageWidth,imageWidth );
        imageView.setImageDrawable(image);
        imageView.setLayoutParams(imageLayoutParams);
        linearLayout.addView(imageView);

        addView(linearLayout);
        return linearLayout;
    }
    private void createLine(int width,int color){
        View line = new View(mContext);
        int lineHeight = UIHelper.dp2px(mContext,20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, lineHeight);
        line.setBackgroundColor(color);
        line.setLayoutParams(layoutParams);
        addView(line);
    }

    public void setClickListener(CapsuleBtnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        int index = (int)v.getTag();
        if(clickListener != null) clickListener.capsuleBtnClick(index);
    }
}
