package com.common.lib;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.personal.format.R;
import com.personal.format.domain.AbstractFormatTransformManager;
import com.personal.format.domain.IOnTransformListener;
import com.personal.format.domain.dao.AudioInfo;
import com.personal.format.domain.dao.Parameter;
import com.personal.format.domain.dao.VideoInfo;
import com.personal.format.domain.transformservices.AudioTransformService;
import com.personal.format.domain.transformservices.VideoTransformService;
import com.personal.format.domain.utils.FragmentStatus;
import com.personal.format.jni.AudioTransformJNI;
import com.personal.format.jni.ITransformJNI;
import com.personal.format.jni.VideoTransformJNI;
import com.personal.format.utils.FragmentUtils;
import com.personal.format.utils.LogUtil;
import com.personal.format.utils.ToastUtils;

/**
 * 跳转界面的逻辑，源程序为修改界面的颜色
 */
public class SampleFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_POSITION = "position";
    private int position;               //当前的UI所处位置

    private View mParentView;           //总体视图
    private ImageButton mTransBtn;      //转换按钮
    private Context mContext;           //上下文环境
    private EditText mSrcFilePathET;    //源文件输入框
    private EditText mDstFilePathET;    //目标文件输入框

    private String mSrcFile;            //源文件路径
    private String mDstFile;            //目标文件路径

    private boolean mIsVideoFlag;       //视屏标志位
    private boolean mIsAudioFlag;       //音频标志位

    private ProgressDialog mIndicatorDialog;

    private AbstractFormatTransformManager mTransformManager;//转换器

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FragmentStatus.VIDEO_TRANSFORM_EXCEPTION:
                    videoTransformExceptionAction();
                    break;
                case FragmentStatus.AUDIO_TRANSFORM_EXCEPTION:
                    audioTransformExceptionAction();
                    break;
                case FragmentStatus.VIDEO_TRANSFORM_SUCCESS:
                    videoTransformSuccessAction();
                    break;
            }
        }
    };

    private void videoTransformSuccessAction(){
        ToastUtils.showToast(mContext, getString(R.string.video_tranform_success));
        FragmentUtils.dismissDialog(mIndicatorDialog);
    }


    /**
     * 音频转换时的异常行为
     */
    private void audioTransformExceptionAction() {

    }

    /**
     * 视频转换时的异常行为
     */
    private void videoTransformExceptionAction() {
        ToastUtils.showToast(mContext, getString(R.string.video_tranform_exception));
        FragmentUtils.dismissDialog(mIndicatorDialog);
    }

    public static SampleFragment newInstance(int position) {
        SampleFragment fragment = new SampleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "onActivityCreated");
        position = getArguments().getInt(ARG_POSITION);
        if (position == 0) {
            mParentView = inflater.inflate(R.layout.fragment_audio, container, false);
            mContext = mParentView.getContext();
            findViewsAndSetListeners();
            return mParentView;
        } else if (position == 1) {
            mParentView = inflater.inflate(R.layout.fragment_video, container, false);
            mContext = mParentView.getContext();
            findViewsAndSetListeners();
            return mParentView;
        } else if (position == 2) {
            mParentView = inflater.inflate(R.layout.fragment_feedback, container, false);
            return mParentView;
        } else if (position == 3) {
            mParentView = inflater.inflate(R.layout.fragment_setting, container, false);
            return mParentView;
        } else if (position == 4) {
            mParentView = inflater.inflate(R.layout.fragment_about, container, false);
            return mParentView;
        } else {
            mParentView = inflater.inflate(R.layout.page, container, false);
            /*FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
            fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));*/
            return mParentView;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "onActivityCreated");
    }

    private void findViewsAndSetListeners() {
        if (position == 1 || position == 0) {
            mTransBtn = (ImageButton) mParentView.findViewById(R.id.transformIbvID);
            mSrcFilePathET = (EditText) mParentView.findViewById(R.id.srcOpenFolderETID);
            mDstFilePathET = (EditText) mParentView.findViewById(R.id.dstOpenFolderETID);
            mTransBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transformIbvID:
                startTransformAction();
                break;
        }
    }

    /**
     * 开始转换工作
     */
    private void startTransformAction() {
        LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "startTransformAction");
        mIndicatorDialog = FragmentUtils.showDialog(mContext, getString(R.string.show_indicator_dialog));
        if (position == 0) {
            startAudioTransformWork();
        } else if (position == 1) {
            startVideoTransformWork();
        }
    }

    /**
     * 开始音频转换工作
     */
    private void startAudioTransformWork() {
        try {
            mIsAudioFlag = true;
            Parameter parameter = initialParameter();
            mTransformManager = new AudioTransformService(mContext, parameter, new AudioTransformJNI());
            mTransformManager.setOnTransformListener(new TransformListener());
            mTransformManager.start();
            mIsAudioFlag = false;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "video transform start exception");
            FragmentUtils.sendTransformMessage(mHandler, FragmentStatus.VIDEO_TRANSFORM_EXCEPTION);
        }
    }

    /**
     * 初始化实验参数
     */
    private Parameter initialParameter() {
        mSrcFile = mSrcFilePathET.getText().toString();
        mDstFile = mDstFilePathET.getText().toString();
        Parameter parameter = new Parameter();
        if (mIsVideoFlag) {
            VideoInfo videoInfo = new VideoInfo();
            parameter.mVideoInfo = videoInfo;
            videoInfo.srcFile = mSrcFile;
            videoInfo.dstFile = mDstFile;
            parameter.isVideoFlag = true;
        } else if (mIsAudioFlag) {
            AudioInfo audioInfo = new AudioInfo();
            parameter.mAudioInfo = audioInfo;
            audioInfo.srcFile = mSrcFile;
            audioInfo.dstFile = mDstFile;
            parameter.isAudioFlag = true;
        }
        return parameter;
    }

    /**
     * 开始视频转换工作
     */
    private void startVideoTransformWork() {
        try {
            mIsVideoFlag = true;
            Parameter parameter = initialParameter();
            ITransformJNI jni = new VideoTransformJNI();
            LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "hashCode:" + jni.hashCode());
            mTransformManager = new VideoTransformService(mContext, parameter, jni);
            mTransformManager.setOnTransformListener(new TransformListener());
            mTransformManager.start();
            mIsVideoFlag = false;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logInfo(LogUtil.LOG_OPEN_FLAG, "video transform start exception");
            FragmentUtils.sendTransformMessage(mHandler, FragmentStatus.VIDEO_TRANSFORM_EXCEPTION);
        }
    }

    private final class TransformListener implements IOnTransformListener {

        @Override
        public void onUpdateTransformProgress(int msgType, int curProgress) {
            FragmentUtils.sendUpdateMessage(mHandler, msgType, curProgress);
        }

        @Override
        public void onNotifyUITransformException(int msgType) {
            FragmentUtils.sendTransformMessage(mHandler, msgType);
        }
    }
}