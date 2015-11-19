package com.personal.format.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by zhaokang on 2015/11/19.
 */
public class FragmentUtils {

    /**
     * 向UI发送信息
     */
    public static void sendTransformMessage(Handler handler, int type) {
        Message msg = new Message();
        msg.what = type;
        handler.sendMessage(msg);
    }

    /**
     * 向UI发送更新进度信息
     */
    public static void sendUpdateMessage(Handler handler, int msgType, int curProgress) {
        Message msg = new Message();
        msg.what = msgType;
        Bundle data = new Bundle();
        data.putInt("progress", curProgress);
        msg.setData(data);
        handler.sendMessage(msg);
    }

    /**
     * 在UI显示对话框
     */
    public static ProgressDialog showDialog(Context context, String msg) {
        return ProgressDialog.show(context, "", msg, true, true);
    }

    /**
     * 在UI消失对话框
     */
    public static void dismissDialog(ProgressDialog indicatorDialog) {
        if (indicatorDialog != null && indicatorDialog.isShowing()) {
            indicatorDialog.dismiss();
            indicatorDialog = null;
        }
    }

}
