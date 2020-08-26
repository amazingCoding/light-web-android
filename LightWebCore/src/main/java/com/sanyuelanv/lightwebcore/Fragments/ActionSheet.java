package com.sanyuelanv.lightwebcore.Fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.LightWebCoreActivity;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;
import com.sanyuelanv.lightwebcore.R;
import com.sanyuelanv.lightwebcore.View.ActionSheetView;

/**
 * Create By songhang in 8/26/20
 */
public class ActionSheet extends Fragment {
    private LightWebCoreActivity mActivity;
    private ActionSheetView actionSheetView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LightWebCoreActivity activity = (LightWebCoreActivity)getActivity();
        assert activity != null;
        mActivity = activity;
        UIHelper.initStatusBar(mActivity,mActivity.getWindow());
        actionSheetView = new ActionSheetView(mActivity,true,  ThemeTypes.light,ThemeConfig.auto);
        // 下至上的动画
        // 背景色渐现
        ValueAnimator animator = ObjectAnimator.ofInt(actionSheetView, "backgroundColor", 0x000f0000, 0x8C000000);
        animator.setDuration(500);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
        return actionSheetView;
    }

//    @Nullable
//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
//        if(enter){
//            return AnimationUtils.loadAnimation(mActivity, R.anim.light_web_anim_show);
//        }
//        else {
//            return AnimationUtils.loadAnimation(mActivity, R.anim.light_web_anim_hide);
//        }
//    }
}
