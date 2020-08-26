package com.sanyuelanv.lightwebcore.Helper;

import android.content.Context;
import android.util.Log;

import com.sanyuelanv.lightwebcore.Model.AppFileInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Create By songhang in 2020/8/18
 */
public class DownLoadHelper {

    private boolean needDownLoad;
    private AppFileInfo appFileInfo;
    private DownLoadStatus status = DownLoadStatus.init;

    enum DownLoadStatus{
        init,
        loading,
        success,
        error,
        cancel,
    }
    public interface OnDownloadListener {
        void onDownloadSuccess(File file);
        void onDownloading(double progress);
        void onDownloadFailed(Exception e);
    }
    public static class  DownLoadException extends Exception{
        public DownLoadException(String message) {
            super(message);
        }
    }

    public static String rootPath = "light_web_app";
    public static String configPath = "appConfig.json";
    public static String appNamePath = "app.zip";

    private  String url;
    private  File appFileDir;
    private OnDownloadListener downloadListener;
    // 当前下载长度
    private long currentLength = 0;

    public DownLoadHelper(final  String url, Context context) {
        this.url = url;
        String[] arr = url.split("\\?");
        final String urlName =  arr[0];
        final String urlPara = arr.length > 1 ? arr[1] : "";
        // URL 分割 ？ 获取前半段 hash 化查找对应路径是否存在，存在则寻找路径下config.json 文件，读出version ,是否与 ？ 后半段对应
        String appName = (urlName.hashCode() & Integer.MAX_VALUE) + "";
        File rootFile = new File(context.getCacheDir(), rootPath);
        appFileDir = new File(rootFile,appName);
        File configFile = null;
        // 判断 root path 是否存在
        needDownLoad = true;
        // 根目录是否存在
        if (rootFile.exists() && rootFile.isDirectory()){
            // app 目录存在
            if (appFileDir.exists() && appFileDir.isDirectory()){
                configFile = new File(appFileDir, configPath);
            }
            else {  appFileDir.mkdir();  }
        }
        else {
            rootFile.mkdir();
            appFileDir.mkdir();
        }
        // configFile 存在的话，看看版本是否对
        if(configFile != null && configFile.exists()){
            String jsonStr = FileHelper.readFile(configFile);
            AppFileInfo info = new AppFileInfo(jsonStr);
            if (info.getVersion().equals(urlPara)){
                appFileInfo = info;
                needDownLoad = false;
            }
        }
        // 创建 appFileInfo
        appFileInfo = new AppFileInfo();
        appFileInfo.setDownloadURL(url);
        appFileInfo.setVersion(urlPara);
    }
    private void removeOldFile(){
        // 已经存在的旧版本删除
        if (appFileDir.exists() && appFileDir.isDirectory()){
            FileHelper.removeFile(appFileDir);
            appFileDir.mkdir();
        }
    }
    public  void download(boolean isForce){
        if(needDownLoad || isForce){
            removeOldFile();
            new Thread(new Runnable(){
                @Override
                public void run() {
                    loadFile();
                }
            }).start();
        }
        else {
            loadSuccess();
        }

    }
    public void setDownloadListener(OnDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
    public File getAppFileDir() {
        return appFileDir;
    }
    public void cancelDownLoad(){
        if(status == DownLoadStatus.loading)  status = DownLoadStatus.cancel;
    }

    private   void loadFile(){
        File file = new File(appFileDir, appNamePath);
        try {
            HttpURLConnection connection = getConnection(url);
            if (connection.getResponseCode() == 200) {
                // 文件总长度
                long contentLength = connection.getContentLength();
                // 拿到文件流
                FileOutputStream fos = new FileOutputStream(file);
                InputStream is = connection.getInputStream();
                int len;
                byte[] b = new byte[1024];
                status = DownLoadStatus.loading;
                while ((len = is.read(b)) != -1 && status != DownLoadStatus.cancel) {
                    currentLength += len;
                    fos.write(b,0,len);
                    if (downloadListener != null) downloadListener.onDownloading(currentLength / contentLength);
                    fos.flush();
                }
                fos.close();
                is.close();
                connection.disconnect();
                // 解压
                if(status != DownLoadStatus.cancel) unZipApp(file);
            }
            else {
                DownLoadException e = new DownLoadException(connection.getResponseCode() + " : " +connection.getResponseMessage());
                loadError(e);
            }
        }
        catch (IOException e){  loadError(e); }
    }
    private void unZipApp(File file){
        // 解压
        try{
            ZipInputStream inZip = new ZipInputStream(new FileInputStream(file.getAbsoluteFile()));
            ZipEntry zipEntry;
            String  szName = "";
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(appFileDir,szName);
                    folder.mkdirs();
                }
                else {
                    File newFile = new File(appFileDir, szName);
                    if (!newFile.exists()){
                        newFile.getParentFile().mkdirs();
                        newFile.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(newFile);
                    int length;
                    byte[] buffer = new byte[1024];
                    while ((length = inZip.read(buffer)) != -1) {
                        out.write(buffer, 0, length);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
            // 删除 app.zip
            FileHelper.removeFile(file);
            // 写入 json config
            FileHelper.writeFile(appFileInfo.toString(),new File(appFileDir,configPath));
            loadSuccess();
        }
        catch (IOException e){  loadError(e); }
    }
    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(1000 * 5);
        conn.setRequestMethod("GET");
        return conn;
    }
    private void loadSuccess(){
        status = DownLoadStatus.success;
        if (downloadListener != null) downloadListener.onDownloadSuccess(appFileDir);
    }
    private void loadError(Exception e){
        status = DownLoadStatus.error;
        if (downloadListener != null) downloadListener.onDownloadFailed(e);
    }
}
