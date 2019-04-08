package com.example.luming.iis;

import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

/**
 * Created by TukulHX on 2019/4/5
 *
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //这里是全局初始化的一个地方，此处是QMUI的arch初始化
        QMUISwipeBackActivityManager.init(this);
    }
}
