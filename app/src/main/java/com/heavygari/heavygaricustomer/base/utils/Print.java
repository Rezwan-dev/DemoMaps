package com.heavygari.heavygaricustomer.base.utils;

import android.util.Log;

public class Print {
    private static boolean isDebug = true;

    public static void e(String message){
        if(isDebug){
            Log.e("HeavyGari -->", message);
        }
    }

    public static void e(String tag, String message){
        if(isDebug){
            Log.e("HeavyGari --> "+tag, message);
        }
    }
}
