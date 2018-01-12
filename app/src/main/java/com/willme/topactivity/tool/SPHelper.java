package com.willme.topactivity.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.willme.topactivity.constant.Code;

public class SPHelper {

    public static boolean isShowWindow(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(Code.SP.WINDOWS_SHOW_FLAG, false);
    }

    public static void setIsShowWindow(Context context, boolean isShow) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(Code.SP.WINDOWS_SHOW_FLAG, isShow).apply();
    }

    public static boolean hasQSTileAdded(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(Code.SP.HAS_AS_TITLE_ADD, false);
    }

    public static void setQSTileAdded(Context context, boolean added) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(Code.SP.HAS_AS_TITLE_ADD, added).apply();
    }

    public static boolean isNotificationOn(Context context) {
//        if (!hasQSTileAdded(context)) {
//            return true;
//        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(Code.SP.TOGGLE_ENABLED, false);
    }

    public static void setNotification(Context context, boolean isEnabled) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(Code.SP.TOGGLE_ENABLED, isEnabled).apply();
    }
}
