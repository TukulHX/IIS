package com.example.luming.iis.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luming.iis.R;
import com.example.luming.iis.base.BaseActivity;



/**
 * TODO 在TextInputLayout中解决输入注册信息的各种情况即可。 不符合情况，设置注册按钮不可点击
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    private TextView tv_title;
    private EditText et_id, et_password1, et_password2;
    private ImageView bt_back;
    private Button bt_register;
    public static final String USER_ID = "user_id";
    public static final String USER_PASSWORD = "user_password";


    public static void ToRegisterActivity(Context context){
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
                String id = et_id.getText().toString().trim();
                String password = et_password1.getText().toString().trim();
                Intent intent = new Intent(this, SplashActivity.class);
                intent.putExtra(USER_ID, id);
                intent.putExtra(USER_PASSWORD, password);
                this.startActivity(intent);
                break;
        }
    }
}
