package com.personal.format.domain.dao;

/**
 * Created by zhaokang on 2015/11/13.
 * 參數類
 */
public class Parameter {
    /**
     * 返回状态
     */
    public int returnStatus;
    /**
     * 当前的进度
     */
    public int mCurrentProgress;
    /**
     * 音频信息
     */
    public AudioInfo mAudioInfo;
    /**
     * 视频信息
     */
    public VideoInfo mVideoInfo;
    /**
     * 视频标志位
     */
    public boolean isVideoFlag;
    /**
     * 音频标志位
     */
    public boolean isAudioFlag;
}
