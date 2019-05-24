package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.example.luming.iis.R;
import com.example.luming.iis.adapter.ManageVPAdapter;
import com.example.luming.iis.base.BaseActivity;
import com.example.luming.iis.fragment.ActionFragment;
import com.example.luming.iis.fragment.StatusFragment;
import com.example.luming.iis.fragment.TriggerFragment;
import com.example.luming.iis.utils.MySocket;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends BaseActivity {

    private QMUITabSegment tabSegment;
    private ViewPager vp_content;
    private TextView tv_title;
    private List<Fragment> fragments;
    private StatusFragment statusFragment;
    private ActionFragment actionFragment;
    private TriggerFragment triggerFragment;
    private ManageVPAdapter vpAdapter;
    private static String device_name;
    public static final String JSON = "json";
    public static final String DEVICE_NAME = "device_name";

    public static void ToManagActivity(Context context, String config, String device_name) {
        Intent intent = new Intent(context, ManageActivity.class);
        intent.putExtra(JSON, config);
        intent.putExtra(DEVICE_NAME,device_name);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_manage;
    }
    @Override
    protected void setTitle() {
        tv_title.setText("Manage");
    }
    @Override
    protected void initView() {
        device_name = this.getIntent().getExtras().getString(DEVICE_NAME,"-1");
        fragments = new ArrayList<>();
        tabSegment = findViewById(R.id.tabSegment);
        vp_content = findViewById(R.id.contentViewPager);
        tv_title = findViewById(R.id.tv_title);
        actionFragment = new ActionFragment();
        statusFragment = new StatusFragment();
        triggerFragment = new TriggerFragment();
        fragments.add(actionFragment);
        fragments.add(statusFragment);
        fragments.add(triggerFragment);
        vpAdapter = new ManageVPAdapter(getSupportFragmentManager(), fragments);
        vp_content.setAdapter(vpAdapter);
        vp_content.setOffscreenPageLimit(3);
        vp_content.addOnPageChangeListener(new MyOnPageChangeListener());
        tabSegment.addTab(new QMUITabSegment.Tab("控制"));
        tabSegment.addTab(new QMUITabSegment.Tab("检测"));
        tabSegment.addTab(new QMUITabSegment.Tab("触发"));
        tabSegment.setHasIndicator(true);//设置包含指示器
        tabSegment.setMode(QMUITabSegment.MODE_FIXED);//设置均分宽度
        tabSegment.setIndicatorWidthAdjustContent(false);//指示器宽度不随内容变化
        tabSegment.setupWithViewPager(vp_content, false);
    }

    @Override
    protected Activity getActivity() {
        return ManageActivity.this;
    }

    @Override
    protected void initEvent() {

    }
    public static String getDeviceName(){
        return device_name;
    }
    /**
     * ViewPager监听方法
     */
    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onDestroy() {
        try {
            MySocket.closeAll();
            System.out.println("mSocket断开");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
