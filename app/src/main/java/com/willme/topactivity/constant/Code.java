package com.willme.topactivity.constant;

/**
 * Description:消息类型定义
 * <p>
 * Author: Kosmos
 * Time: 2017/4/19 001911:35
 * Email:ZeroProject@foxmail.com
 * Events:新增接口分类
 */
public interface Code {
    interface ConstantStr {
        String EXTRA_FROM_QS_TILE = "from_qs_tile";
        String ACTION_STATE_CHANGED = "com.willme.topactivity.ACTION_STATE_CHANGED";
        String ACTION_NOTIFICATION_RECEIVER = "com.willme.topactivity.ACTION_NOTIFICATION_RECEIVER";
        String EXTRA_NOTIFICATION_ACTION = "command";
        String ACTION_UPDATE_TITLE = "com.willme.topactivity.ACTION.UPDATE_TITLE";
    }

    interface SP {
        String WINDOWS_SHOW_FLAG = "is_show_window";//悬浮框是否开启
        String HAS_AS_TITLE_ADD = "has_qs_tile_added";
        String TOGGLE_ENABLED = "is_noti_toggle_enabled";
    }
    interface ConstantInt {
        int REQUEST_CODE = 100;
        int NOTIFICATION_ID = 1;
        int ACTION_PAUSE = 0;
        int ACTION_RESUME = 1;
        int ACTION_STOP = 2;
    }
}
