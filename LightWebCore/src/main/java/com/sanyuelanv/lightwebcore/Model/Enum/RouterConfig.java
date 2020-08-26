package com.sanyuelanv.lightwebcore.Model.Enum;

/**
 * Create By songhang in 2020/8/24
 */
public enum RouterConfig {
    push,
    pop,
    replace,
    setPopExtra,
    restart;

    public static RouterConfig compare(int i){
        switch (i){
            case 0: return RouterConfig.push;
            case 1: return RouterConfig.pop;
            case 2: return RouterConfig.replace;
            case 3: return RouterConfig.setPopExtra;
            default: return RouterConfig.restart;
        }
    }
}
