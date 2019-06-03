package com.example.luming.iis.utils;

import android.content.Context;
import android.os.Handler;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * 封装QMUITipDialog的常用方法工具类
 * Created by TukulHX on 2019/4/7
 *
 */
public class TipDialogUtils {
    public static QMUITipDialog dialog;

    public static void getInstance(Context context, int iconType, String desc, Handler handler) {
        dialog = new QMUITipDialog.Builder(context).setIconType(iconType).setTipWord(desc).create();
        dialog.show();
        if (handler != null){
            handlerDismiss(handler, 1500);
        }
    }

    public static void getInstance(Context context, int iconType, String desc) {
        dialog = new QMUITipDialog.Builder(context).setIconType(iconType).setTipWord(desc).create();
        dialog.show();
    }

    public static void handlerDismiss(Handler handler, long delayMillis){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dissMiss();
            }
        }, delayMillis);
    }

    public static void dissMiss(){
        try {
            if (dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
