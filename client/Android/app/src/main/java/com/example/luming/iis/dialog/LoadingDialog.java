package com.example.luming.iis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.luming.iis.R;

/**
 * 可多处使用的loadingDialog
 * Created by TukulHX on 2019/4/10
 */
public class LoadingDialog extends Dialog {

    protected Context mContext;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.DialogStyle);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context);
    }



    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog(context);
    }

    private void initDialog(Context context) {
        mContext = context;
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);//设置点击外部无效
    }

    public void isShow(){
        try {
            if (isShowing()){
                return;
            }
            this.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dissMiss(){
        try {
            if (isShowing()) {
                this.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
