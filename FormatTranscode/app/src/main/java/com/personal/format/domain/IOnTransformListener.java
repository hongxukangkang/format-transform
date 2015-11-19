package com.personal.format.domain;

/**
 * Created by zhaokang on 2015/11/13.
 */
public interface IOnTransformListener {
    /**
     * 用于通知UI界面进行刷新
     */
    void onUpdateTransformProgress(int msgType, int curProgress);

    /**
     * 用于通知UI界面转换异常
     */
    void onNotifyUITransformException(int msgType);
}
