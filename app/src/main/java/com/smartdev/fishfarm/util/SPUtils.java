package com.smartdev.fishfarm.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class SPUtils {
    public static final String DEVICE_NAME = "device_name";
    public static String EVENT_CLICK = "event_click";
    public static String MAP_ZOOM = "map_zoom";
    public static String IMG_NUM = "no_of_upload_img";
    public static String LOCATION_CLICK = "location_click";
    public static String MARKER_ID = "marker_id";
    public static String EMAIL = "email";
    public static String  ALARM_INFO = "alarm_info";
    public static String PREFERENCE_NAME = "SUSEJ IOT";

    private SPUtils() {
        throw new AssertionError();
    }

    public static boolean putString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getString(key, defaultValue);
    }

    public static boolean putInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getInt(key, defaultValue);
    }

    public static boolean putLong(Context context, String key, long value) {
        Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getLong(key, defaultValue);
    }

    public static boolean putFloat(Context context, String key, float value) {
        Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1.0f);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getFloat(key, defaultValue);
    }

    public static boolean putBoolean(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(PREFERENCE_NAME, 0).getBoolean(key, defaultValue);
    }
}
