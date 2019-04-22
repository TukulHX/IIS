package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luming.iis.R;
import com.example.luming.iis.adapter.MyDeviceAdapter;
import com.example.luming.iis.base.BaseActivity;
import com.example.luming.iis.bean.Device;
import com.example.luming.iis.database.DatabaseOperator;
import com.example.luming.iis.dialog.AddDeviceDialog;
import com.example.luming.iis.dialog.LoadingDialog;
import com.example.luming.iis.utils.MySocket;
import com.example.luming.iis.utils.SharedPreferenceUtils;
import com.example.luming.iis.utils.TipDialogUtils;
import com.example.luming.iis.utils.WebService;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import static com.example.luming.iis.activity.SplashActivity.IS_LOGIN;
import static com.example.luming.iis.activity.SplashActivity.LOGIN_INFO;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_FAIL;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_SUCCESS;

/**
 * TODO 下拉刷新
 */
public class DeviceActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "DeviceActivity";
    public static final String SP_NULL = "SP_NULL";
    private DatabaseOperator dbOperator;
    private List<Device> deviceList = new ArrayList<Device>();
    private ImageView bt_add;
    private TextView tv_title, tv_logout;
    private MyDeviceAdapter adapter;
    private SharedPreferences sp;
    private ListView lv_device;
    private String userId;
    private LoadingDialog loadingDialog;

    /**
     * 登录信息
     */
    private String loginInfo;

    public static final int CONNECTION_FAILED = 0;
    public static final int CONNECTION_SUCCESS = 1;
    private static final int SYNC_FINISHED = 2;

    public static final String CONFIG = "config";
    public static final String USER_ID = "user_id";
    public static final String JSON = "json";

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECTION_FAILED:
                    loadingDialog.dissMiss();
                    TipDialogUtils.getInstance(DeviceActivity.this, ICON_TYPE_FAIL, "连接失败", handler);
                    break;
                case CONNECTION_SUCCESS:
                    loadingDialog.dissMiss();
                    TipDialogUtils.getInstance(DeviceActivity.this, ICON_TYPE_SUCCESS, "连接成功", handler);
                    ManageActivity.ToManagActivity(DeviceActivity.this, (String) msg.obj);
                    break;
            }
        }
    };
    private MySocket socket;
    private SwipeRefreshLayout swipeRefresh;
    private Boolean isLogin;

    public static void ToDeviceActivity(Context context) {
        Intent intent = new Intent(context, DeviceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Activity getActivity() {
        return DeviceActivity.this;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device;
    }

    @Override
    protected void setTitle() {
        tv_title.setText("Device");
    }

    @Override
    protected void initView() {
        bt_add = findViewById(R.id.bt_add);
        tv_title = findViewById(R.id.tv_title);
        tv_logout = findViewById(R.id.tv_logout);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        dbOperator = DatabaseOperator.getInstance(this);
        swipeRefresh.setOnRefreshListener(this);

        // TODO 以后可以将userId 与 loginInfo 分开
        userId =  SharedPreferenceUtils.getString(getApplicationContext(), LOGIN_INFO, "-1");

        adapter = new MyDeviceAdapter(this, deviceList, userId);
        lv_device = findViewById(R.id.lv_device);
        lv_device.setAdapter(adapter);
        //先设置Logout为Login
        tv_logout.setText("Login");
        //设置logout是否可见
        isLogin = SharedPreferenceUtils.getBoolean(getApplicationContext(), IS_LOGIN, false);
        if (isLogin) {
            //设置可见
            tv_logout.setText("Logout");
            //获取登录信息
            loginInfo = SharedPreferenceUtils.getString(getApplicationContext(), LOGIN_INFO, "");
            System.out.println("登录信息Device页面:" + loginInfo);
            //同步
            dataSync();
            deviceSync();
        }
        tv_logout.setOnClickListener(this);
        bt_add.setOnClickListener(this);

    }

    @Override
    protected void initEvent() {

    }

    /**
     * 下拉刷新监听方法
     */
    @Override
    public void onRefresh() {
        if (isLogin) {
            deviceSync();
            dataSync();
            swipeRefresh.setRefreshing(false);
            TipDialogUtils.getInstance(DeviceActivity.this, ICON_TYPE_SUCCESS, "同步设备和数据成功", handler);
        } else {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(this,"请在登录后再次进行同步操作！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                AddDeviceDialog.newInstance(this).setOnAddDeviceListener(new AddDeviceDialog.OnAddDeviceListener() {
                    @Override
                    public void getDevice(String name, String ip, String port) {
                        if (dbOperator.isExistDevice(name)) {
                            Toast.makeText(DeviceActivity.this, "该设备已存在，请勿重复添加", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbOperator.addDevice(new Device(name, ip,port), userId);
                        notifyChange();
                    }
                }).showDialog(this);
                break;

            case R.id.tv_logout:
                if (tv_logout.getText().equals("Login")) {
                    //do login
                    SplashActivity.ToSplashActivity(DeviceActivity.this);
                    finish();
                } else {
                    //do logout
                    tv_logout.setText("Login");
                    //退出前清空数据
                    SharedPreferenceUtils.clear(getApplicationContext());
                    dbOperator.clearLocalDevice(userId);
                    SplashActivity.ToSplashActivity(DeviceActivity.this);
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("执行了onRestart");
        loginInfo = SharedPreferenceUtils.getString(getApplicationContext(), LOGIN_INFO, "");
        System.out.println("onRestart:" + loginInfo);
    }

    public void onResume() {
        super.onResume();
        notifyChange();
    }

    private void notifyChange() {
        if (adapter != null) {
            deviceList.clear();
            deviceList.addAll(dbOperator.queryAllDevice(userId));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        dbOperator.closeDB();
        System.out.println("执行了onDestory");
        super.onDestroy();
    }

    public void connection(final String host, final Integer port) {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.isShow();
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    socket = MySocket.getInstance();
                    SocketAddress socketAddress = new InetSocketAddress(host, port);
                    socket.connect(socketAddress, 300);
                    InputStream in = MySocket.getIn();
                    byte[] buffer = new byte[1024];
                    in.read(buffer, 0, buffer.length);
                    message.what = CONNECTION_SUCCESS;
                    message.obj = new String(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("异常： " + e.getMessage());
                    message.what = CONNECTION_FAILED;
                    message.obj = "连接失败";
                    if (socket != null) {
                        MySocket.closeAll();
                        System.out.println("连接失败，关闭socket");
                    }
                }
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 同步数据
     */
    private void dataSync() {
        Log.d(TAG, "同步数据开始");
        new Thread() {
            @Override
            public void run() {
                String localTime = dbOperator.queryRecentTime(userId);
                String webTime = WebService.getWebRecTime(userId);
                Log.e(TAG, "local time: " + localTime + " webTime: " + webTime);
                if ((localTime.equals(SP_NULL) && webTime.equals("false")) || localTime.contains(webTime))  //因为 sqlite 查询会多出小数点，因此使用 contains 判断时间相等
                    return;
                if (localTime.compareTo(webTime) > 0 || webTime.equals("NULL")) { //本地有新数据或网络无数据
                    String json = dbOperator.queryRecentData(webTime,userId);
                    Log.e(TAG, "upload");
                    WebService.httpDataPush(userId, json);
                } else {         //下拉
                    dbOperator.addFromJsonArray(WebService.httpDataPull(userId, localTime),"data");
                }
            }
        }.start();
    }
    private void deviceSync(){
        Log.d(TAG,"同步设备开始");
        new Thread(){
            @Override
            public void run() {
                WebService.httpDeviceSync(userId);
            }
        }.start();
    }


    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            if (tv_logout.getText().equals("Login")) {
                Toast.makeText(DeviceActivity.this, "再按一次返回登录界面", Toast.LENGTH_SHORT).show();

            } else{
                Toast.makeText(DeviceActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
            mExitTime = System.currentTimeMillis();
        } else {
            if (tv_logout.getText().equals("Login")) {
                finish();
            } else {
                System.exit(0);
            }
        }
    }
}
