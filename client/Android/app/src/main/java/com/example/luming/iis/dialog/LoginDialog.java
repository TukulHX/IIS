package com.example.luming.iis.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.luming.iis.R;

/**
 * 可扩展复用的登录对话框
 *
 * Created by TukulHX on 2019/4/4
 *
 */
public class LoginDialog extends DialogFragment implements View.OnClickListener {


    private Button bt_left, bt_right;
    private EditText et_name, et_password;
    private OnLoginListener listener;
    public static final String TAG = "LoginDialog";
    private static final String HEAD = "header";
    private TextView tv_title;


    public static LoginDialog newInstance(String title) {
        LoginDialog dialog = new LoginDialog();
        Bundle args = new Bundle();
        args.putString(HEAD, title);
//        args.putString(HEAD_DESC, headerDesc);
//        args.putString(LEFT_BTN, leftBtn);
//        args.putString(RIGHT_BTN, rightBtn);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_Dialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_login_register, null);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;


        tv_title = view.findViewById(R.id.tv_title);
        et_name = view.findViewById(R.id.et_name);
        et_password = view.findViewById(R.id.et_password);
        bt_left = view.findViewById(R.id.bt_left);
        bt_right = view.findViewById(R.id.bt_right);
        tv_title.setText(getArguments().getString(HEAD));
        bt_left.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        //设置宽度只占屏幕宽度的80%
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width * 4 / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, layoutParams);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    @Override
    public void dismiss() {
        if (null != getActivity() && !getActivity().isFinishing()) {
            super.dismissAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_left) {
            dismiss();
        }
        if (v.getId() == R.id.bt_right) {
            if (listener != null) {
                //获取输入内容
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (name.equals("") || password.equals(""))
                    return;
                listener.getLoginInfo(name, password);
            }
            dismiss();
        }
    }

    public LoginDialog setOnLoginListener(OnLoginListener listener) {
        this.listener = listener;
        return this;
    }

    public void showDialog(FragmentActivity activity) {
        try {
            show(activity.getSupportFragmentManager(), TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface OnLoginListener {
        void getLoginInfo(String name, String password);
    }


}
