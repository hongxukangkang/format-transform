package com.personal.format.domain.transformservices;

import android.content.Context;

import com.personal.format.domain.AbstractFormatTransformManager;
import com.personal.format.domain.dao.Parameter;
import com.personal.format.jni.ITransformJNI;

/**
 * Created by zhaokang on 2015/11/13.
 * 代理模式的使用，可以方便进行额外操作
 */
public class AudioTransformService extends AbstractFormatTransformManager {

    private ITransformJNI mTransformJNI;

    public AudioTransformService(Context context,Parameter parameter,ITransformJNI jni) {
        super(context,parameter,jni);
    }


}
