package com.personal.format.domain;

import android.content.Context;

import com.personal.format.R;
import com.personal.format.domain.dao.Parameter;
import com.personal.format.domain.utils.FragmentStatus;
import com.personal.format.jni.ITransformJNI;
import com.personal.format.utils.FragmentUtils;
import com.personal.format.utils.LogUtil;
import com.personal.format.utils.ToastUtils;

import java.io.File;

/**
 * Created by zhaokang on 2015/11/13.
 * 抽象的格式转换器管理者
 */
public abstract class AbstractFormatTransformManager {

    private Context mContext;
    private Parameter mParameter;
    private ITransformJNI mTransformJNI;
    private IOnTransformListener mListener;

    public AbstractFormatTransformManager(Context context, Parameter parameter, ITransformJNI jni) {
        this.mParameter = parameter;
        this.mTransformJNI = jni;
        this.mContext = context;
    }

    /**
     * 格式转换服务
     */
    private int transformService() {
        LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "mTransformJNI== null" + (mTransformJNI == null));
        LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "mTransformJNI.hashCode:" + mTransformJNI.hashCode());
        Parameter parameter = mTransformJNI.transformService(mParameter);
        return parameter.returnStatus;
    }

    public void setOnTransformListener(IOnTransformListener listener) {
        this.mListener = listener;
    }

    /**
     * 启动格式转换服务
     */
    public void start() throws Exception {
        int status = checkInfo();
        if (status == 1) {
            int transformStatus = transformService();
            if (transformStatus == 1){
                sendUISuccessMessage();
            }
            /*
            启动界面显示转换进度
            while (mParameter.mCurrentProgress >= 0) {
                Thread.sleep(100);
                mListener.onUpdateTransformProgress(0, mParameter.mCurrentProgress);
            }*/
        } else {
            sendUIExceptionMessage();
        }
    }

    private void sendUISuccessMessage(){
        if (mListener != null) {
            if (mParameter.isVideoFlag) {
                LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "isVideoFlag:" + true);
                mListener.onNotifyUITransformException(FragmentStatus.VIDEO_TRANSFORM_SUCCESS);
            } else if (mParameter.isAudioFlag) {
                LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "isAudioFlag:" + true);
                mListener.onNotifyUITransformException(FragmentStatus.AUDIO_TRANSFORM_SUCCESS);
            }
        }
    }

    /**给UI发送转换异常信息*/
    private void sendUIExceptionMessage(){
        if (mListener != null) {
            if (mParameter.isVideoFlag) {
                LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "isVideoFlag:" + true);
                mListener.onNotifyUITransformException(FragmentStatus.VIDEO_TRANSFORM_EXCEPTION);
            } else if (mParameter.isAudioFlag) {
                LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "isAudioFlag:" + true);
                mListener.onNotifyUITransformException(FragmentStatus.AUDIO_TRANSFORM_EXCEPTION);
            }
        }
    }

    /**
     * 通知UI更新
     */
    public void sendTransformExceptionMessage(int type) {
        if (mListener != null) {
            mListener.onNotifyUITransformException(type);
        }
    }

    /**
     * 检测程序的启动信息准备是否正常
     */
    private int checkInfo() {
        int status = -1;
        if (mParameter == null) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.parameter_setting_exception));
            return status;
        }

        String mSrcFile = "";
        String mDstFile = "";
        if (mParameter.isVideoFlag) {
            mSrcFile = mParameter.mVideoInfo.srcFile;
            mDstFile = mParameter.mVideoInfo.dstFile;
        } else if (mParameter.isAudioFlag) {
            mSrcFile = mParameter.mAudioInfo.srcFile;
            mDstFile = mParameter.mAudioInfo.dstFile;
        }

        if (mSrcFile == null || "".equals(mSrcFile)) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.src_file_path_not_null));
            return status;
        }

        File srcFile = new File(mSrcFile);
        if (!srcFile.exists()) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.src_file_path_not_exist));
            return status;
        }


        if (mDstFile == null || "".equals(mDstFile)) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.dst_file_path_not_null));
            return status;
        }

        if (mDstFile.equals(mSrcFile)) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.src_dst_file_not_same));
            return status;
        }

        String dstPath = "";
        String[] paths = mDstFile.split("/");

        for (int i = 0; i < paths.length - 1; i++) {
            dstPath += ("/" + paths[i]);
        }

        File dstFile = new File(dstPath);
        if (!dstFile.exists()) {
            ToastUtils.showToast(mContext, mContext.getString(R.string.dst_file_path_not_exist));
            return status;
        }
        try {
            File resultFile = new File(mDstFile);
            if (resultFile.exists()) {
                resultFile.delete();
                resultFile.createNewFile();
            } else {
                resultFile.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(mContext.getString(R.string.dst_file_creat_exception));
        }
        status = 1;
        return status;
    }
}
