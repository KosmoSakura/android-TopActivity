package com.willme.topactivity.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.willme.topactivity.R;

/**
 * @Author: Kosmos
 * @Time: 2018/1/11 001116:29
 * @Email:ZeroProject@foxmail.com
 * @Description:
 */
public class FloatView {
    public final static int REQUEST_CODE = 100;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static TextView tSHow;
    private static View mView;
    private static boolean isShow = false;//悬浮框是否已经显示

    private static OnClickListener mListener;//view的点击回调listener

    public static void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public static boolean isFloatting() {
        return isShow;
    }

    public static void setText(String str) {
        if (str != null && tSHow != null) {
            tSHow.setText(str);
        }
    }

    /**
     * 显示悬浮框
     */
    public static void showFloatView(Activity context, int layoutId) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            context.startActivityForResult(intent, REQUEST_CODE);
            return;
        } else if (isShow) {
            return;
        }
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.CENTER;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.x = context.getResources().getDisplayMetrics().widthPixels;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mView = LayoutInflater.from(context).inflate(layoutId, null);
        tSHow = mView.findViewById(R.id.float_text);
        mWindowManager.addView(mView, wmParams);


        mView.setOnTouchListener(new View.OnTouchListener() {
            float downX = 0;
            float downY = 0;
            int oddOffsetX = 0;
            int oddOffsetY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        oddOffsetX = wmParams.x;
                        oddOffsetY = wmParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float moveX = event.getX();
                        float moveY = event.getY();
                        //不除以3，拖动的view抖动的有点厉害
                        wmParams.x += (moveX - downX) / 3;
                        wmParams.y += (moveY - downY) / 3;
                        if (mView != null) {
                            mWindowManager.updateViewLayout(mView, wmParams);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        int newOffsetX = wmParams.x;
                        int newOffsetY = wmParams.y;
                        if (Math.abs(newOffsetX - oddOffsetX) <= 20 && Math.abs(newOffsetY - oddOffsetY) <= 20) {
                            if (mListener != null) {
                                mListener.onClick(mView);
                            }
                        }
                        break;
                }
                return true;
            }

        });

        isShow = true;
    }

    /**
     * 隐藏悬浮窗
     */
    public static void hideFloatView() {
        if (mWindowManager != null && isShow) {
            mWindowManager.removeView(mView);
            isShow = false;
        }
    }
}
