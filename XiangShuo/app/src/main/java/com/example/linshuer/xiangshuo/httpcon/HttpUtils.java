package com.example.linshuer.xiangshuo.httpcon;

/**
 * Created by Linshuer on 2018/6/10.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class HttpUtils {
    private static String head_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/headimage/";
    private static String pic_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/picimage/";

    //private static String url ="http://192.168.43.30:8080/APPService/headimage/linshuer_headimage.png";
    //============================
    public static String sendPost(String url, String params) {
        String result = "";

        try {
            URL realurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realurl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("POST");
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.write(params.getBytes());//这样可以解决乱码问题
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                result = line;
            }
        }catch (java.net.SocketTimeoutException e) {
            return "outtime";
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //=======================下载图片
    public static Bitmap dowloadImage(String url) {
        Bitmap bitmap = null;
        try {
            URL realurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //===================================获得图片
   public static Bitmap setPic(String fileName, Bitmap bitmap,String dir) {
        //调用getPath来初始化这个文件
        Bitmap mBitmap = null;
       try {
           // 创建文件流，指向该路径，文件名叫做fileName
           File file = new File(dir, fileName);
           // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
           File fileParent = file.getParentFile();
           if (!fileParent.exists()) {
               // 文件夹不存在
               fileParent.mkdirs();// 创建文件夹
               // 将图片保存到本地
               bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                       new FileOutputStream(file));
                    mBitmap=bitmap;
           }else{
                   Bitmap realbitmap = BitmapFactory.decodeStream(new FileInputStream(
                           file));
                   mBitmap=realbitmap;
           }

       } catch (Exception e) {
           e.printStackTrace();
       }
       return mBitmap;
    }


    //==============//从本地获取图片
    public static Bitmap getBitmapFromLocal(String fileName,String dir) {
        Bitmap bitmap =null;
        try {
            File file = new File(dir, fileName);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    //=====================//文件夹是否存在
    public static boolean isExistFile(String fileName,String dir){
        boolean flag =false;
        try {
            File file = new File(dir, fileName);
            if (file.exists()) {
                // 文件夹不存在
                flag =true;
            }else {
                flag =false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    //===============保存文件
    public static void resetpic(String fileName, Bitmap bitmap,String dir) {
        //调用getPath来初始化这个文件
        Bitmap mBitmap = null;
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(dir, fileName);
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();
            }
                // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,new FileOutputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //===================================设置pic临时dir
    public static void makeDir(String dir) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(dir);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //=========================
    /**
     * 文件上传
     *
     * @param urlStr 接口路径
     * @param filePath 本地图片路径
     * @return
     */
    public static String formUpload(String urlStr, String filePath) {
        String rsp = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "|"; // request头和上传文件内容分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            File file = new File(filePath);
            String filename = file.getName();
            String contentType = "";
            if (filename.endsWith(".png")) {
                contentType = "image/png";
            }
            if (filename.endsWith(".jpg")) {
                contentType = "image/jpg";
            }
            if (filename.endsWith(".gif")) {
                contentType = "image/gif";
            }
            if (filename.endsWith(".bmp")) {
                contentType = "image/bmp";
            }
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"" + filePath
                    + "\"; filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);//==================================================
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            rsp = buffer.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return rsp;
    }

}

