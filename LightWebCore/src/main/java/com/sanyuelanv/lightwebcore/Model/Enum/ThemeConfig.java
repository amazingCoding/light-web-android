package com.sanyuelanv.lightwebcore.Model.Enum;

/**
 * Create By songhang in 2020/8/14
 */
public enum ThemeConfig {
    light,
    dark,
    auto;
    public static ThemeConfig compare(int i){
        switch (i){
            case 0: return ThemeConfig.light;
            case 1: return ThemeConfig.dark;
            default:return ThemeConfig.auto;
        }
    }
}
