package com.willme.topactivity.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.willme.topactivity.R;
import com.willme.topactivity.constant.Code;
import com.willme.topactivity.service.QuickSettingTileService;

public class TasksWindow {
    private static TasksWindow window;
    private Context context;

    private TasksWindow(Context ctx) {
        context = ctx;
        mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.CENTER;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.x = context.getResources().getDisplayMetrics().widthPixels;
        wmParams.y = 0;

        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        mView = LayoutInflater.from(context).inflate(R.layout.window_float, null);
        tSHow = mView.findViewById(R.id.float_text);
    }

    public static TasksWindow getInstance(Context context) {
        if (window == null || mWindowManager == null) {
            window = new TasksWindow(context);
        }
        return window;
    }

    private static WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private TextView tSHow;
    private View mView;
    private boolean isShow = false;//悬浮框是否已经显示

    private OnClickListener mListener;//view的点击回调listener

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    public boolean isShow() {
        return isShow;
    }


    public void show(final String str) {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
            new AlertDialog.Builder(context)
                .setMessage(R.string.dialog_enable_overlay_window_msg)
                .setPositiveButton(R.string.dialog_enable_overlay_window_positive_btn
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            context.startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPHelper.setIsShowWindow(context, false);
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SPHelper.setIsShowWindow(context, false);
                    }
                })
                .create()
                .show();

            if (context instanceof Activity) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                ((Activity) context).startActivityForResult(intent, Code.ConstantInt.REQUEST_CODE);
            }
            return;
        }


        mWindowManager.addView(mView, wmParams);
        SPHelper.setIsShowWindow(context, true);

        tSHow.setText(str);
        isShow = true;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            QuickSettingTileService.updateTile(context);
    }

    public void dismiss() {
        if (isShow && mWindowManager != null) {
            try {
                mWindowManager.removeView(mView);

                SPHelper.setIsShowWindow(context, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isShow = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            QuickSettingTileService.updateTile(context);
    }
}
