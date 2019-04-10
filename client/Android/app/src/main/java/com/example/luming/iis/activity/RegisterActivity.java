package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luming.iis.R;
import com.example.luming.iis.base.BaseActivity;
import com.example.luming.iis.dialog.LoadingDialog;
import com.example.luming.iis.utils.TipDialogUtils;
import com.example.luming.iis.utils.WebService;

import static com.qmuiteam.qmui.widget.dialog.QMUITipDialog.Builder.ICON_TYPE_INFO;



public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private TextView tv_title;
    private EditText et_id, et_password1, et_password2;
    private ImageView bt_back;
    private Button bt_register;
    private LoadingDialog loadingDialog;


    private static final int REGISTER_FAILED = 2;
    private static final int REGISTER_SUCCESS = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case REGISTER_SUCCESS:
                    loadingDialog.dissMiss();
                    Toast.makeText(RegisterActivity.this, "注册成功,两秒后返回登录界面!", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashActivity.ToSplashActivity(RegisterActivity.this);
                            finish();
                        }
                    }, 2000);
                    break;

                case REGISTER_FAILED:
                    loadingDialog.dissMiss();
                    Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


    public static void ToRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Activity getActivity() {
        return RegisterActivity.this;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register;
    }

    @Override
    protected void setTitle() {
        tv_title.setText("注册新用户");
    }

    @Override
    protected void initView() {
        tv_title = findViewById(R.id.tv_title);
        et_id = findViewById(R.id.et_id);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        bt_back = findViewById(R.id.bt_back);
        bt_register = findViewById(R.id.bt_register);
        bt_back.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                //直接返回
                finish();
                break;

            case R.id.bt_register:
                //将注册信息传回SplashActivity并且自动填入
                String name = et_id.getText().toString().trim();
                String password1 = et_password1.getText().toString().trim();
                String password2 = et_password2.getText().toString().trim();
                if (checkInfo(name, password1, password2)){
                    register(name, password1);
                }
                break;
        }
    }

    /**
     * 简单判断输入信息
     * @param name
     * @param password1
     * @param password2
     */
    private boolean checkInfo(String name, String password1, String password2){
        if (name.equals("") || password1.equals("") || password2.equals("")) {
            TipDialogUtils.getInstance(this, ICON_TYPE_INFO, "信息不能为空");
            dissmissDialogWithDelayed();
            return false;
        }
        if (name.length()<2){
            TipDialogUtils.getInstance(this, ICON_TYPE_INFO, "用户名太短");
            dissmissDialogWithDelayed();
            return false;
        }
        if (password1.length()<3){
            TipDialogUtils.getInstance(this, ICON_TYPE_INFO, "密码太短");
            dissmissDialogWithDelayed();
            return false;
        }
        if (!password2.equals(password1)){
            TipDialogUtils.getInstance(this, ICON_TYPE_INFO, "两次密码不一致");
            dissmissDialogWithDelayed();
            return false;
        }
        return true;
    }

    private void dissmissDialogWithDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TipDialogUtils.dissMiss();
            }
        },1500);
    }

    /**
     * 注册
     */
    private void register(final String name, final String password) {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.isShow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                String info = WebService.httpRegister(name, password);
                if (info.equals("false")) {
                    message.what = REGISTER_FAILED;
                    message.obj = "注册失败,用户名已存在或网络连接有问题";
                    handler.sendMessage(message);
                } else if (info.equals("true")) {
                    message.what = REGISTER_SUCCESS;
                    message.obj = "注册成功";
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}
