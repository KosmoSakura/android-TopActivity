package com.willme.topactivity.tool;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.willme.topactivity.R;
import com.willme.topactivity.widget.FloatView;

/**
 * @Author: Kosmos
 * @Time: 2018/1/11 001116:31
 * @Email:ZeroProject@foxmail.com
 * @Description:
 */
public class FloatHelper {
    private Activity activity;


    /**
     * 显示悬浮窗
     */
    private void showFloatView() {
        FloatView.showFloatView(activity, R.layout.window_tasks);
        FloatView.setOnClickListener(new FloatView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("399", "点击了");
            }
        });
    }

    /**
     * 隐藏悬浮窗
     */
    public void hideFloatView() {
        FloatView.hideFloatView();
    }
}
