package com.example.luming.iis.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * Created by TukulHX on 2019/4/7
 *
 */
public class FullScreenVideoView extends VideoView {
    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 保证VideoView全屏显示
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager window = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = window.getDefaultDisplay().getWidth();
        int height = window.getDefaultDisplay().getHeight();
        setMeasuredDimension(width, height);
    }
}
