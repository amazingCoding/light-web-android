package com.sanyuelanv.lightwebcore.Model.Enum;

/**
 * Create By songhang in 2020/8/14
 */
public enum ThemeTypes {
    light,dark;
    public static ThemeTypes compare(int i){
        switch (i){
            case 0: return ThemeTypes.light;
            default:return ThemeTypes.dark;
        }
    }
    public static ThemeTypes compare(ThemeConfig config){
        switch (config){
            case dark:return ThemeTypes.dark;
            default:return ThemeTypes.light;
        }
    }
}
