package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luming.iis.R;
import com.example.luming.iis.adapter.MyDeviceAdapter;
import com.example.luming.iis.base.BaseActivity;
import com.example.luming.iis.bean.Device;
import com.example.luming.iis.database.DatabaseOperator;
import com.example.luming.iis.dialog.AddDeviceDialog;
import com.example.luming.iis.utils.MySocket;
import com.example.luming.iis.utils.TipDialogUtils;
import com.example.luming.iis.utils.WebService;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_FAIL;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_SUCCESS;

/**
 * TODO 1、 准备将此页面制作为Device专用页面，添加设备放置在导航栏右侧，内容采用列表形式，外带下拉刷新功能，准备重制
 * TODO 2、 添加退出登录功能
 *
 */
public class DeviceActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "DeviceActivity";
    public static final String SP_NULL = "SP_NULL";
    private DatabaseOperator dbOperator;
    private List<Device> deviceList = new ArrayList<Device>();
    private Button btnAdd;
    private TextView tv_title;
    private MyDeviceAdapter adapter;
    private SharedPreferences sp;
    private ListView lv_device;
    private String userId;

    public static final int CONNECTION_FAILED = 0;
    public static final int CONNECTION_SUCCESS = 1;
    private static final int LOGIN_SUCCESS = 2;

    public static final String CONFIG = "config";
    public static final String USER_ID = "user_id";
    public static final String JSON = "json";

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONNECTION_FAILED:
                    TipDialogUtils.getInstance(DeviceActivity.this, ICON_TYPE_FAIL, "连接失败", handler);
                    break;
                case CONNECTION_SUCCESS:
                    TipDialogUtils.getInstance(DeviceActivity.this, ICON_TYPE_SUCCESS, "连接成功", handler);
                    ManageActivity.ToManagActivity(DeviceActivity.this, (String) msg.obj);
                    break;
//                case LOGIN_SUCCESS:
//                    //TODO 此处还需要添加登录或者注册成功的跳转
//                    SharedPreferences.Editor editor = sp.edit();
//                    //把数据进行保存
//                    userId = msg.obj.toString();
//                    editor.putString(USER_ID, msg.obj.toString());
//                    editor.commit();
//                    btnLog.setText("用户 ID: " + msg.obj + "  点击注销");
//                    sync();
//                    break;
                case 3:
                    Toast.makeText(DeviceActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
//                case CONNECTION_SUCCESS:
//                    //TODO 连接成功的TipLog
//
//                    break;
//                case CONNECTION_FAILED:
//                    //TODO 连接失败的TipLog
//
//                    break;
            }
        }
    };
    private MySocket socket;

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
        btnAdd = findViewById(R.id.btnAdd);
        tv_title = findViewById(R.id.tv_title);
        dbOperator = DatabaseOperator.getInstance(this);
        adapter = new MyDeviceAdapter(this, deviceList);
        lv_device = findViewById(R.id.lv_device);
        lv_device.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
//        sp = getSharedPreferences(CONFIG, 0);
//        // 获取登陆信息并同步--start
//        userId = sp.getString(USER_ID, SP_NULL);
//        if (userId.equals(SP_NULL)) {
//            btnLog.setText("点击登陆");
//        } else {
//            btnLog.setText("用户ID: " + userId);
//            sync();
//        }
        // 获取登陆信息并同步--end
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // TODO 准备重写结构
            case R.id.btnAdd:
                AddDeviceDialog.newInstance(this).setOnAddDeviceListener(new AddDeviceDialog.OnAddDeviceListener() {
                    @Override
                    public void getDevice(String name, String ip, String port) {
                        if (dbOperator.isExistDevice(name)) {
                            Toast.makeText(DeviceActivity.this, "该设备已存在，请勿重复添加", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dbOperator.addDevice(new Device(name, ip, Integer.valueOf(port), false));
                        notifyChange();
                    }
                }).showDialog(this);
                break;

//            case R.id.btnLog:
//                if (sp.getString(USER_ID, SP_NULL).equals(SP_NULL)) {
//                    LoginDialog.newInstance("登录我的数据中心").setOnLoginListener(new LoginDialog.OnLoginListener() {
//                        @Override
//                        public void getLoginInfo(String name, String password) {
//                            //do login TODO后续添加跳转逻辑
//                            login(name, password);
//                        }
//                    }).showDialog(this);
//                } else {
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString(USER_ID, SP_NULL);
//                    editor.commit();
//                    btnLog.setText("点击登陆");
//                    Message msg = new Message();
//                    msg.what = 3;
//                    msg.obj = "注销成功";
//                    handler.sendMessage(msg);
//                }
//                break;
//            case R.id.btnReg:
//                LoginDialog.newInstance("注册新用户").setOnLoginListener(new LoginDialog.OnLoginListener() {
//                    @Override
//                    public void getLoginInfo(String name, String password) {
//                        //do Register TODO 后续添加跳转逻辑
//                        register(name, password);
//                    }
//                }).showDialog(this);
//                break;
        }
    }

    public void onResume() {
        super.onResume();
        notifyChange();
    }

    private void notifyChange() {
        if (adapter != null) {
            deviceList.clear();
            deviceList.addAll(dbOperator.queryAllDevice());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        dbOperator.closeDB();
        super.onDestroy();
    }

    public void connection(final String host, final Integer port) {
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
    private void sync() {
        Log.d(TAG, "同步数据开始");
        new Thread() {
            @Override
            public void run() {
                String localTime = dbOperator.queryRecentTime();
                String webTime = WebService.getWebRecTime(userId);
                Log.e(TAG, "local time: " + localTime + " webTime: " + webTime);
                if ((localTime.equals(SP_NULL) && webTime.equals(SP_NULL)) || localTime.contains(webTime))  //因为 sqlite 查询会多出小数点，因此使用 contains 判断时间相等
                    return;
                if ((localTime.compareTo(webTime) > 0 || webTime.equals(SP_NULL)) && localTime.equals(SP_NULL)) //上传
                {
                    String json = dbOperator.queryRecentData(webTime);
                    Log.e(TAG, "upload");
                    WebService.upLoad(userId, json);
                } else {         //下拉
                    dbOperator.addFromJsonArray(WebService.pull(userId, localTime));
                }
            }
        }.start();
    }

}