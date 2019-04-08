package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.luming.iis.R;
import com.example.luming.iis.base.BaseActivity;
import com.example.luming.iis.utils.TipDialogUtils;
import com.example.luming.iis.utils.WebService;
import com.example.luming.iis.widgets.FullScreenVideoView;

import static com.example.luming.iis.activity.RegisterActivity.USER_PASSWORD;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_FAIL;
import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_SUCCESS;

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "SplashActivity";
    private FullScreenVideoView videoView;
    private EditText et_password;
    private EditText et_name;
    private LinearLayout root;
    private Button bt_login;
    private TextView tv_register, tv_tourist;

    private static final int LOGIN_FAILED = 0;
    private static final int LOGIN_SUCCESS = 1;

    private static final int REGISTER_FAILED = 2;
    private static final int REGISTER_SUCCESS = 3;


    public static final String CONFIG = "config";
    public static final String USER_ID = "user_id";
    public static final String JSON = "json";
    public static final String SP_NULL = "SP_NULL";

    //用于标记用户是否登录 TODO 登录的用户不退出无法重新进入登录界面
    private boolean isLogin = false;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    // TODO 1、登录成功后检测网络状态，有网络则从服务器获取设备信息并加载设备界面
                    // TODO 2、无网络则直接加载本地数据即可
                    isLogin = true;
                    TipDialogUtils.getInstance(SplashActivity.this, ICON_TYPE_SUCCESS, "登录成功", handler);
//                    DeviceActivity.ToDeviceActivity(SplashActivity.this);
                    Intent intent = new Intent(SplashActivity.this, DeviceActivity.class);
                    intent.putExtra("isLogin", isLogin);
                    SplashActivity.this.startActivity(intent);
                    break;

                case LOGIN_FAILED:
                    isLogin = false;
                    TipDialogUtils.getInstance(SplashActivity.this, ICON_TYPE_FAIL, "登录失败", handler);
                    break;

                case REGISTER_SUCCESS:
                    isLogin = true;
                    TipDialogUtils.getInstance(SplashActivity.this, ICON_TYPE_SUCCESS, "注册成功", handler);
                    DeviceActivity.ToDeviceActivity(SplashActivity.this);
                    break;
                case REGISTER_FAILED:
                    isLogin = false;
                    TipDialogUtils.getInstance(SplashActivity.this, ICON_TYPE_FAIL, "注册失败", handler);
                    break;
            }
        }
    };

    @Override
    protected Activity getActivity() {
        return SplashActivity.this;
    }

    public static void ToSplashActivity(Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void initView() {
        root = findViewById(R.id.splash_root);
        videoView = findViewById(R.id.videoview);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);
        tv_register = findViewById(R.id.tv_register);
        tv_tourist = findViewById(R.id.tv_tourist);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra(USER_ID);
        String user_password = intent.getStringExtra(USER_PASSWORD);
        et_name.setText(user_id);
        et_password.setText(user_password);

        bt_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_tourist.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        playBackgroundVideo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                //do login TODO 此处登录为了测试需要，暂时做成跳转功能。
//                String name = et_name.getText().toString().trim();
//                String password = et_password.getText().toString().trim();
//                login(name, password);
                DeviceActivity.ToDeviceActivity(SplashActivity.this);
                break;

            case R.id.tv_register:
                //do register TODO 这里无法使用TextInputLayout，将在后续进行注册界面重制
                RegisterActivity.ToRegisterActivity(this);
//                LoginDialog.newInstance("注册新用户").setOnLoginListener(new LoginDialog.OnLoginListener() {
//                    @Override
//                    public void getLoginInfo(String name, String password) {
//                        //do Register TODO 后续添加跳转逻辑
//                        register(name, password);
//                    }
//                }).showDialog(this);
                break;
            case R.id.tv_tourist:
                //do tourist
                DeviceActivity.ToDeviceActivity(SplashActivity.this);
                break;
        }
    }

    /**
     * 登录
     */
    private void login(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                String info = WebService.executeHttpGet("LogLet", name, password);
                if (info.equals(SP_NULL)) {
                    message.what = LOGIN_FAILED;
                    message.obj = "登陆失败";
                } else {
                    message.what = LOGIN_SUCCESS;
                    message.obj = info;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 注册
     */
    private void register(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                String info = WebService.executeHttpGet("RegLet", name, password);
                if (info.equals(SP_NULL)) {
                    message.what = REGISTER_FAILED;
                    message.obj = "注册失败";
                } else {
                    message.what = REGISTER_SUCCESS;
                    message.obj = "注册成功";
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 播放背景视频
     */
    private void playBackgroundVideo() {
        //1、设置播放路径
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login));
        //2、播放前准备
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //静音后开始播放
                mp.setVolume(0f, 0f);
                videoView.start();
                root.setVisibility(View.VISIBLE);
                startAnimation();
            }
        });
        //3、循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    /**
     * 动画效果,由下向上
     */
    private void startAnimation() {
        int startX = 0;
        float startY = 0f;
        System.out.println("startX = " + startX + "   startY = " + startY);
        TranslateAnimation animation = new TranslateAnimation(startX, startX, startY + 300f, startY);
        animation.setDuration(1000);
        root.setAnimation(animation);
        animation.startNow();
//        ObjectAnimator animatorY = new ObjectAnimator().ofFloat(root, "translationY", 300f, startY);
//        animatorY.setDuration(1500);
//        animatorY.start();

    }

    @Override
    protected void onStop() {
        videoView.stopPlayback();
        root.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        playBackgroundVideo();
        startAnimation();
        super.onRestart();
    }
}
