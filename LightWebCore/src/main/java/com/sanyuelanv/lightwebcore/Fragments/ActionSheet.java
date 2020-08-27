package com.sanyuelanv.lightwebcore.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sanyuelanv.lightwebcore.Helper.UIHelper;
import com.sanyuelanv.lightwebcore.LightWebCoreActivity;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;
import com.sanyuelanv.lightwebcore.View.ActionSheetView;

/**
 * Create By songhang in 8/26/20
 */
public class ActionSheet extends Fragment implements Animation.AnimationListener {
    private LightWebCoreActivity mActivity;
    private ActionSheetView actionSheetView;
    private  static String ID  = "lightWeb_actionSheet";

    private boolean isDev;
    private ThemeTypes nowTheme;
    private ThemeConfig theme;
    protected ActionSheetView.OnControlBtnListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LightWebCoreActivity activity = (LightWebCoreActivity)getActivity();
        assert activity != null;
        mActivity = activity;
        UIHelper.initStatusBar(mActivity,mActivity.getWindow());
        actionSheetView = new ActionSheetView(mActivity,isDev, nowTheme,theme);
        if(listener != null)actionSheetView.setListener(listener);
        return actionSheetView;
    }

    public ActionSheet(boolean isDev, ThemeTypes nowTheme, ThemeConfig theme) {
        this.isDev = isDev;
        this.nowTheme = nowTheme;
        this.theme = theme;
    }
    public void changeStyle(ThemeTypes nowTheme){
        this.nowTheme = nowTheme;
        actionSheetView.changStyle(nowTheme);
    }
    public void hide(){
        this.actionSheetView.hideView(this);
    }

    public static String getID() {
        return ID;
    }

    public void setListener(ActionSheetView.OnControlBtnListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        FragmentManager manager =   mActivity.getSupportFragmentManager();
        manager.popBackStackImmediate(ID,1);
        mActivity.setActionSheet(null);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
