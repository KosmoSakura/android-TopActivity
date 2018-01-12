package com.willme.topactivity.receiver;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.willme.topactivity.R;
import com.willme.topactivity.constant.Code;
import com.willme.topactivity.tool.SPHelper;
import com.willme.topactivity.tool.TasksWindow;
import com.willme.topactivity.view.MainActivity;

import java.util.List;

/**
 * Created by Wen on 4/18/15.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int command = intent.getIntExtra(Code.ConstantStr.EXTRA_NOTIFICATION_ACTION, -1);
        switch (command) {
            case Code.ConstantInt.ACTION_RESUME:
                showNotification(context, false);
                SPHelper.setIsShowWindow(context, true);
                boolean lollipop = Build.VERSION.SDK_INT >= 21;
                if (!lollipop) {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> rtis = am.getRunningTasks(1);
                    String act = rtis.get(0).topActivity.getPackageName() + "\n"
                        + rtis.get(0).topActivity.getClassName();
                    TasksWindow.getInstance(context).show(act);
                } else {
                    TasksWindow.getInstance(context).show("");
                }
                break;
            case Code.ConstantInt.ACTION_PAUSE:
                showNotification(context, true);
                TasksWindow.getInstance(context).dismiss();
                SPHelper.setIsShowWindow(context, false);
                break;
        }
        context.sendBroadcast(new Intent(Code.ConstantStr.ACTION_UPDATE_TITLE));
    }

    public static void showNotification(Context context, boolean isPaused) {
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setContentTitle(context.getString(R.string.is_running,
                context.getString(R.string.app_name)))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(context.getString(R.string.touch_to_open))
            .setColor(0xFFe215e0)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setOngoing(!isPaused);
        if (isPaused) {
            builder.addAction(R.drawable.ic_noti_action_resume, context.getString(R.string.noti_action_resume), getPendingIntent(context, Code.ConstantInt.ACTION_RESUME));
        } else {
            builder.addAction(R.drawable.ic_noti_action_pause, context.getString(R.string.noti_action_pause), getPendingIntent(context, Code.ConstantInt.ACTION_PAUSE));
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(Code.ConstantInt.NOTIFICATION_ID, builder.build());
        SPHelper.setNotification(context, true);
    }

    public static PendingIntent getPendingIntent(Context context, int command) {
        Intent intent = new Intent(Code.ConstantStr.ACTION_NOTIFICATION_RECEIVER);
        intent.putExtra(Code.ConstantStr.EXTRA_NOTIFICATION_ACTION, command);
        return PendingIntent.getBroadcast(context, command, intent, 0);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Code.ConstantInt.NOTIFICATION_ID);
        SPHelper.setNotification(context, false);
    }

}
