package com.example.luming.iis.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WebService {
    private static String IP = "192.168.43.253:8080"; //修改为你的服务器 IP 地址

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

    public static String getWebRecTime(String userId) {
        String path = "http://" + IP + "/IIS/RecLet?id=" + userId;
        return connect(path);
    }

    public static void upLoad(String id, String jsonArray) {

        String path = null;
        try {
            path = "http://" + IP + "/IIS/SyncLet?id=" + id + "&data=" + URLEncoder.encode(jsonArray, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        connect(path);
    }

    public static String pull(String id, String time) {
        String path = null;
        path = "http://" + IP + "/IIS/PullLet?id=" + id + "&time=" + time;
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
            return "SP_NULL";

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
        return "SP_NULL";
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
