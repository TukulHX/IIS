package com.example.luming.iis.utils;

import android.util.Base64;

import com.example.luming.iis.database.DatabaseOperator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WebService {
    //private static String IP = "192.168.43.253:8080"; //修改为你的服务器 IP 地址
    private static String IP = "luuming.imwork.net:33136";
    /**
     * 通过Get方式获取HTTP服务器数据
     *
     * @return
     */
    public static String executeHttpGet(String url, String username, String password) {
        String path = "http://" + IP + "/IIS/";
        path = path + url + "?username=" + username + "&password=" + password;
        return connect(path);
    }

    public static String httpLogin( String username, String password) {
        return executeHttpGet("usrLogLet",username,password);
    }

    /**
     * 通过Request方式注册用户
     */
    public static String httpRegister(String username, String password){
        return  executeHttpGet("usrRegLet",username,password);
    }

    public static String getWebRecTime(String userId) {
        String path = "http://" + IP + "/IIS/RecLet?id=" + userId;
        return connect(path);
    }

    public static void httpDataPush(String id, String jsonArray) {
        String path = null;
        try {
            path = "http://" + IP + "/IIS/dataPushLet?id=" + id + "&data=" + URLEncoder.encode(jsonArray, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connect(path);
    }

    public static String httpDataPull(String id, String time) {
        String path = path = "http://" + IP + "/IIS/dataPullLet?id=" + id + "&time=" + time;
        return connect(path);
    }


    public static void httpDeviceSync(String user_id){
        String sql = DatabaseOperator.getDeviceOperation(user_id);
        String tmpPath = "http://" + IP + "/IIS/deviceSyncLet?sql=";
        while (sql != null){
            String encodedSql = Base64.encodeToString(sql.getBytes(), Base64.DEFAULT);
            String path = tmpPath + encodedSql;
            System.out.print("Sync path = " + path);
            String ret = connect(path);
            if(ret.equals("success")){
                DatabaseOperator.popDeviceOperation(user_id);
                sql =  DatabaseOperator.getDeviceOperation(user_id);
                System.out.print("get device opt +  "+ sql);
            }
            else
                break;
        }
    }
    public static String httpDevicePull(String user_id){
        String path = "http://" + IP + "/IIS/devicePullLet?id=" + user_id;
        return connect(path);
    }

    private static String connect(String path) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            // 用户名 密码
            // URL 地址
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(3000); // 设置超时时间
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置获取信息方式
            conn.setRequestProperty("Charset", "UTF-8"); // 设置接收数据编码格式

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                return parseInfo(is);
            }
            //网络正常，服务器异常返回值
            return "false";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 意外退出时进行连接关闭保护
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //网络不正常，返回
        return "false";
    }

    // 将输入流转化为 String 型
    private static String parseInfo(InputStream inStream) throws Exception {
        byte[] data = read(inStream);
        // 转化为字符串
        return new String(data, "UTF-8");
    }

    // 将输入流转化为byte型
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

}
