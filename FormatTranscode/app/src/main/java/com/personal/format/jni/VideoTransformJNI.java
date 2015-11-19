package com.personal.format.jni;

import com.personal.format.domain.dao.Parameter;
import com.personal.format.domain.dao.VideoInfo;

/**
 * Created by zhaokang on 2015/11/13.
 */
public class VideoTransformJNI implements ITransformJNI {

    static {
        System.loadLibrary("videotransform");
    }

    public native int videoTransform(String srcFileName,String dstFileName);//,int[] progress

    @Override
    public Parameter transformService(Parameter parameter) {
        Parameter result = new Parameter();
        VideoInfo videoInfo = parameter.mVideoInfo;
        int status = videoTransform(videoInfo.srcFile,videoInfo.dstFile);
        result.returnStatus = status;
        return result;
    }
}
