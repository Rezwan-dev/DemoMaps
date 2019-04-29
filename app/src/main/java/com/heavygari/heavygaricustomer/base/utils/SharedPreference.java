package com.heavygari.heavygaricustomer.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreference {

    private static final String PREFS_NAME = "heavygari_driver";

    public static final String LOG_IN_STATUS = "login";

    private SharedPreference(){

    }

    public static boolean getBooleanValue(final Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }


    public static void setBooleanValue(final Context context, String key, Boolean status) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        editor.putBoolean(key, status);
        editor.apply();
    }


    public static String getStringValue(final Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    public static void setStringValue(final Context context, String key, String value) {
         SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
         Editor editor = prefs.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public static int getIntValue(final Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

    public static void setIntValue(final Context context, String key,
                                   int value) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public static long getLongValue(final Context context, String key) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getLong(key, 0);
    }

    public static void setLongValue(final Context context, String key, long value) {
        SharedPreferences prefs = context.getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean hasKey(Context context, String key){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).contains(key);
    }

}
