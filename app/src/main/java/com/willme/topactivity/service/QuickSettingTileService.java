package com.willme.topactivity.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.willme.topactivity.constant.Code;
import com.willme.topactivity.receiver.NotificationReceiver;
import com.willme.topactivity.tool.SPHelper;
import com.willme.topactivity.tool.TasksWindow;
import com.willme.topactivity.view.MainActivity;

/**
 * Created by Wen on 5/3/16.
 */
@TargetApi(Build.VERSION_CODES.N)
public class QuickSettingTileService extends TileService {
    private UpdateTileReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new UpdateTileReceiver();
    }

    @Override
    public void onTileAdded() {
        SPHelper.setQSTileAdded(this, true);
        sendBroadcast(new Intent(Code.ConstantStr.ACTION_STATE_CHANGED));
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        SPHelper.setQSTileAdded(this, false);
        sendBroadcast(new Intent(Code.ConstantStr.ACTION_STATE_CHANGED));
    }

    @Override
    public void onStartListening() {
        registerReceiver(mReceiver, new IntentFilter(Code.ConstantStr.ACTION_UPDATE_TITLE));
        super.onStartListening();
        updateTile();
    }


    @Override
    public void onStopListening() {
        unregisterReceiver(mReceiver);
        super.onStopListening();
    }

    @Override
    public void onClick() {
        if (WatchingAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Code.ConstantStr.EXTRA_FROM_QS_TILE, true);
            startActivityAndCollapse(intent);
        } else {
            SPHelper.setIsShowWindow(this, !SPHelper.isShowWindow(this));
            if (SPHelper.isShowWindow(this)) {
                TasksWindow.getInstance(this).show("");
                NotificationReceiver.showNotification(this, false);
            } else {
                TasksWindow.getInstance(this).dismiss();
                NotificationReceiver.showNotification(this, true);
            }
            sendBroadcast(new Intent(Code.ConstantStr.ACTION_STATE_CHANGED));
        }
    }

    private void updateTile() {
        if (WatchingAccessibilityService.getInstance() == null) {
            getQsTile().setState(Tile.STATE_INACTIVE);
        } else {
            getQsTile().setState(SPHelper.isShowWindow(this) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        }
        getQsTile().updateTile();
    }


    class UpdateTileReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTile();
        }
    }

    public static void updateTile(Context context) {
        TileService.requestListeningState(context.getApplicationContext(), new ComponentName(context, QuickSettingTileService.class));
        context.sendBroadcast(new Intent(Code.ConstantStr.ACTION_UPDATE_TITLE));
    }
}
