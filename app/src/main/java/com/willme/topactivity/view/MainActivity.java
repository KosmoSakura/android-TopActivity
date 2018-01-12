package com.willme.topactivity.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.willme.topactivity.R;
import com.willme.topactivity.constant.Code;
import com.willme.topactivity.receiver.NotificationReceiver;
import com.willme.topactivity.service.WatchingService;
import com.willme.topactivity.tool.SPHelper;
import com.willme.topactivity.tool.TasksWindow;

public class MainActivity extends Activity implements OnCheckedChangeListener {
    private CompoundButton mWindowSwitch, mNotificationSwitch;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWindowSwitch = findViewById(R.id.sw_window);
        mNotificationSwitch = findViewById(R.id.sw_notification);
        mNotificationSwitch.setOnCheckedChangeListener(this);
        mWindowSwitch.setOnCheckedChangeListener(this);

//        TasksWindow.getInstance(this).show("");
        startService(new Intent(this, WatchingService.class));

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshWindowSwitch();
                refreshNotificationSwitch();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Code.ConstantStr.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        refreshWindowSwitch();
        refreshNotificationSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWindowSwitch();
        refreshNotificationSwitch();
    }


    private void refreshWindowSwitch() {
        mWindowSwitch.setChecked(SPHelper.isShowWindow(this));
    }

    private void refreshNotificationSwitch() {
        mNotificationSwitch.setChecked(SPHelper.isNotificationOn(this));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_window://显示可拖拽悬浮窗
                if (isChecked) {
                    TasksWindow.getInstance(this).show(true, getPackageName() + "\n" + getClass().getName());
                } else {
                    TasksWindow.getInstance(this).dismiss();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshWindowSwitch();
                    }
                }, 1000);
                break;
            case R.id.sw_notification://快速设置开关启用时关闭通知开关
                if (isChecked) {
                    NotificationReceiver.showNotification(this, false);
                } else {
                    NotificationReceiver.cancelNotification(this);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshNotificationSwitch();
                    }
                }, 1000);

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Code.ConstantInt.REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                TasksWindow.getInstance(this).show(getPackageName() + "\n" + getClass().getName());
            } else {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
