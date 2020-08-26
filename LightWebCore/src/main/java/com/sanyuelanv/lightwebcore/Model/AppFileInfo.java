package com.sanyuelanv.lightwebcore.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Create By songhang in 2020/8/18
 */
public class AppFileInfo {
    private String downloadURL;
    private String version = "";

    public AppFileInfo(String jsonString) {
        if (!jsonString.equals("")){
            try {
                JSONObject json = new JSONObject(jsonString);
                downloadURL =  json.getString("downloadURL");
                version =  json.getString("version");
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "{\"downloadURL\":\""+ downloadURL +"\",\"version\":\""+ version +"\"}";
    }

    public AppFileInfo() {
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
