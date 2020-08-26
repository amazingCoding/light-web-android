package com.sanyuelanv.lightwebcore.Helper;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Create By songhang in 2020/8/18
 */
public class FileHelper {
    public static String rootPath = "light_web_app";
    public static String configPath = "appConfig.json";

    // 获取目录数据
    public static String readFile(File file){
        String result = "";
        try {
            InputStream inStream = new FileInputStream(file);
            if (inStream != null) {
                InputStreamReader inputReader = new InputStreamReader(inStream, "UTF-8");
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line = "";
                while ((line = buffReader.readLine()) != null) {  result += line;  }
                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
    public static boolean writeFile(String content,File file) throws IOException {
        if (file.exists() && file.isFile()) file.delete();
        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(file.length());
        raf.write(content.getBytes());
        raf.close();
        return  false;
    }
    // 删除目录/文件
    public static void removeFile(File file){
        //如果是文件直接删除
        if(file.isFile()){
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                removeFile(f);
            }
            file.delete();
        }
    }
    // 获取该目录下文件大小
    public static long getDirSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) size = size + getDirSize(fileList[i]);
                else size = size + fileList[i].length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    // Assets
    public  static String getAssetsData(Context context,String name) {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = context.getAssets().open(name);
            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
