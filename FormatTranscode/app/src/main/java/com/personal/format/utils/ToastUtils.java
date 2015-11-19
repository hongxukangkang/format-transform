package com.personal.format.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhaokang on 2015/11/19.
 */
public class ToastUtils {

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

}
