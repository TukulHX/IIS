package com.example.luming.iis.base;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.luming.iis.broadcast.NetWorkStateReceiver;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

/**
 * Created by TukulHX on 2019/4/5
 */
public abstract class BaseActivity extends FragmentActivity {

    private NetWorkStateReceiver netWorkStateReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        QMUIStatusBarHelper.translucent(getActivity());//实现全局的沉浸式状态栏
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initView();
        setTitle();
        initEvent();
        IntentFilter filter = new IntentFilter();
        netWorkStateReceiver = new NetWorkStateReceiver();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(netWorkStateReceiver, filter);
    }

    protected abstract Activity getActivity();

    protected abstract int getLayoutID();

    protected abstract void setTitle();

    protected abstract void initView();

    protected abstract void initEvent();

    @Override
    protected void onDestroy() {
        unregisterReceiver(netWorkStateReceiver);
        super.onDestroy();
    }
}
