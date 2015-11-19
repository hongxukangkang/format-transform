package com.personal.format.jni;

import com.personal.format.domain.dao.AudioInfo;
import com.personal.format.domain.dao.Parameter;

/**
 * Created by zhaokang on 2015/11/13.
 */
public class AudioTransformJNI implements ITransformJNI{

    static {
        System.loadLibrary("audiotransform");
    }

    public native int audioTransform(String srcFileName,String dstFileName,int[] progress);

    @Override
    public Parameter transformService(Parameter parameter) {
        Parameter result = new Parameter();
        AudioInfo audioInfo = parameter.mAudioInfo;
        int status = audioTransform(audioInfo.srcFile,audioInfo.dstFile,audioInfo.currentProgress);
        result.returnStatus = status;
        return result;
    }
}
