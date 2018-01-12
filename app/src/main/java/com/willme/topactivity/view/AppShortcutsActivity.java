package com.willme.topactivity.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.willme.topactivity.constant.Code;
import com.willme.topactivity.receiver.NotificationReceiver;
import com.willme.topactivity.service.WatchingAccessibilityService;
import com.willme.topactivity.tool.SPHelper;
import com.willme.topactivity.tool.TasksWindow;

/**
 * Created by Wen on 16/02/2017.
 */

@TargetApi(Build.VERSION_CODES.N)
public class AppShortcutsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (WatchingAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            SPHelper.setIsShowWindow(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        boolean isShow = !SPHelper.isShowWindow(this);
        SPHelper.setIsShowWindow(this, isShow);
        if (!isShow) {
            TasksWindow.getInstance(this).dismiss();
            NotificationReceiver.showNotification(this, true);
        } else {
            TasksWindow.getInstance(this).show(getPackageName() + "\n" + getClass().getName());
            NotificationReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(Code.ConstantStr.ACTION_STATE_CHANGED));
        finish();
    }
}
