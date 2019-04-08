package com.example.luming.iis.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MySocket extends Socket {
    private static MySocket socket = null;
    private static OutputStream out = null;
    private static InputStream in = null;
    public static MySocket getInstance(){
        if(socket == null)
        {
            socket = new MySocket();
        }
        return socket;
    }
    public static OutputStream getOut()
    {
        if(out == null)
        {
            try {
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }
    public static InputStream getIn()
    {
        if(in == null)
        {
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return in;
    }
    public static void closeAll(){
        try {
            if (socket != null){
                socket.close();
            }
            socket = null;
            in = null;
            out = null;
        }catch (Exception e){
            System.out.print("cannot close");
        }
    }
}
