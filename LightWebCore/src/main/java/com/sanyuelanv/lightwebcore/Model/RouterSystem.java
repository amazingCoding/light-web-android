package com.sanyuelanv.lightwebcore.Model;

import com.sanyuelanv.lightwebcore.Helper.JsonHelper;
import com.sanyuelanv.lightwebcore.Model.Enum.RouterConfig;

import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/24
 */
public class RouterSystem {
    private RouterConfig action;
    private String name;
    private String extra;

    private int pos;

    public RouterSystem(JSONObject json) {
        // push
        action  = RouterConfig.compare(JsonHelper.getIntInJson(json,"action",0));
        name = JsonHelper.getStringInJson(json,"name","");
        extra = JsonHelper.getStringInJson(json,"extra","");

        // pos
        pos = JsonHelper.getIntInJson(json,"pos",-1);
        boolean isToRoot = JsonHelper.IntToBoolean(JsonHelper.getIntInJson(json,"isToRoot",0));
        if(isToRoot) pos = 0;
    }


    public RouterConfig getAction() {
        return action;
    }

    public void setAction(RouterConfig action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

}
