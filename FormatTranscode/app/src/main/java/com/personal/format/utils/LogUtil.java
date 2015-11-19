package com.personal.format.utils;

import android.util.Log;

/**
 * Created by zhaokang on 2015/11/19.
 */
public class LogUtil {

    private static final String TAG = "LogUtil";
    public static final boolean LOG_OPEN_FLAG = true;

    public static void logInfo(boolean flag,String msg){
        if (flag){
            Log.i(TAG,msg);
        }
    }

}
