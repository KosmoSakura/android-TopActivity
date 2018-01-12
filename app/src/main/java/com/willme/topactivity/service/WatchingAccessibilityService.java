package com.willme.topactivity.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import com.willme.topactivity.constant.Code;
import com.willme.topactivity.receiver.NotificationReceiver;
import com.willme.topactivity.tool.SPHelper;
import com.willme.topactivity.tool.TasksWindow;

/**
 * Created by Wen on 1/14/15.
 */
public class WatchingAccessibilityService extends AccessibilityService {
    private static WatchingAccessibilityService sInstance;

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (SPHelper.isShowWindow(this)) {
                TasksWindow.getInstance(this).show(event.getPackageName() + "\n" + event.getClassName());
            }
        }

    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sInstance = this;
        if (SPHelper.isShowWindow(this)) {
            NotificationReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(Code.ConstantStr.ACTION_UPDATE_TITLE));
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sInstance = null;
        TasksWindow.getInstance(this).dismiss();
        NotificationReceiver.cancelNotification(this);
        sendBroadcast(new Intent(Code.ConstantStr.ACTION_UPDATE_TITLE));
        return super.onUnbind(intent);
    }

    public static WatchingAccessibilityService getInstance() {
        return sInstance;
    }

}
