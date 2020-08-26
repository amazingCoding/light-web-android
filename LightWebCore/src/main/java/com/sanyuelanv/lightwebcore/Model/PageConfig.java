package com.sanyuelanv.lightwebcore.Model;

import com.sanyuelanv.lightwebcore.Helper.JsonHelper;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeConfig;
import com.sanyuelanv.lightwebcore.Model.Enum.ThemeTypes;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/14
 */
public class PageConfig {
    private  boolean isHideNav;
    private ThemeTypes statusStyle;
    private ThemeConfig theme;
    private String title;
    private String titleColor;
    private String navBackgroundColor;
    private String backgroundColor;
    private String global;
    private boolean isBounces;
    private boolean showCapsule;
    private boolean needBack;

    public PageConfig(JSONObject json) {
        isHideNav = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isHideNav",0));
        statusStyle = ThemeTypes.compare(JsonHelper.getIntInJson(json,"statusStyle",0));
        theme = ThemeConfig.compare(JsonHelper.getIntInJson(json,"theme",0));
        title = JsonHelper.getStringInJson(json,"title","");
        titleColor = JsonHelper.getStringInJson(json,"titleColor","#000000");
        navBackgroundColor = JsonHelper.getStringInJson(json,"navBackgroundColor","#FFFFFF");
        backgroundColor = JsonHelper.getStringInJson(json,"backgroundColor","#F1F1F1");
        global = JsonHelper.getStringInJson(json,"global","");
        isBounces = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isBounces",1));
        showCapsule = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"showCapsule",1));
    }
    public void update(JSONObject json){
        if(json.has("isHideNav")) isHideNav = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isHideNav",0));
        if(json.has("statusStyle"))  statusStyle = ThemeTypes.compare(JsonHelper.getIntInJson(json,"statusStyle",0));
        if(json.has("theme"))theme = ThemeConfig.compare(JsonHelper.getIntInJson(json,"theme",0));
        if(json.has("title"))title = JsonHelper.getStringInJson(json,"title","");
        if(json.has("titleColor"))titleColor = JsonHelper.getStringInJson(json,"titleColor","#000000");
        if(json.has("navBackgroundColor"))navBackgroundColor = JsonHelper.getStringInJson(json,"navBackgroundColor","#FFFFFF");
        if(json.has("backgroundColor"))backgroundColor = JsonHelper.getStringInJson(json,"backgroundColor","#F1F1F1");
        if(json.has("isBounces"))isBounces = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isBounces",1));
        if(json.has("showCapsule")) showCapsule = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"showCapsule",1));
    }

    @Override
    public String toString() {
        return "PageConfig{" +
                "isHideNav=" + isHideNav +
                ", statusStyle=" + statusStyle +
                ", theme=" + theme +
                ", title='" + title + '\'' +
                ", titleColor='" + titleColor + '\'' +
                ", navBackgroundColor='" + navBackgroundColor + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", global='" + global + '\'' +
                ", isBounces=" + isBounces +
                ", showCapsule=" + showCapsule +
                '}';
    }

    public boolean isHideNav() {
        return isHideNav;
    }

    public void setHideNav(boolean hideNav) {
        isHideNav = hideNav;
    }

    public ThemeTypes getStatusStyle() {
        return statusStyle;
    }

    public void setStatusStyle(ThemeTypes statusStyle) {
        this.statusStyle = statusStyle;
    }

    public ThemeConfig getTheme() {
        return theme;
    }

    public void setTheme(ThemeConfig theme) {
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getNavBackgroundColor() {
        return navBackgroundColor;
    }

    public void setNavBackgroundColor(String navBackgroundColor) {
        this.navBackgroundColor = navBackgroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBounces() {
        return isBounces;
    }

    public void setBounces(boolean bounces) {
        isBounces = bounces;
    }

    public boolean isShowCapsule() {
        return showCapsule;
    }

    public void setShowCapsule(boolean showCapsule) {
        this.showCapsule = showCapsule;
    }

    public String getGlobal() {
        return global;
    }

    public void setGlobal(String global) {
        this.global = global;
    }

    public boolean isNeedBack() {
        return needBack;
    }

    public void setNeedBack(boolean needBack) {
        this.needBack = needBack;
    }
}
