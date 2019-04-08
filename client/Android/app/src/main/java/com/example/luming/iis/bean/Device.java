package com.example.luming.iis.bean;

/**
 * Created by luming on 2018/11/23.
 */

public class Device {
    private String name;
    private String ip;
    private int port;
    private boolean isVisible;

    public Device(String name, String ip, int port, boolean isVisible) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.isVisible = isVisible;
    }

    public Device(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return "Device{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", isVisible=" + isVisible +
                '}';
    }
}
