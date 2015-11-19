package com.personal.format.domain.transformservices;

import android.content.Context;

import com.personal.format.domain.AbstractFormatTransformManager;
import com.personal.format.domain.IOnTransformListener;
import com.personal.format.domain.dao.Parameter;
import com.personal.format.domain.utils.FragmentStatus;
import com.personal.format.jni.ITransformJNI;

/**
 * Created by zhaokang on 2015/11/13.
 */
public class VideoTransformService extends AbstractFormatTransformManager {

    private Parameter mParameter;
    private IOnTransformListener mListener;

    public VideoTransformService(Context context,Parameter parameter, ITransformJNI jni) {
        super(context,parameter, jni);
        this.mParameter = parameter;
    }

    @Override
    public void start(){
        try{
            super.start();
        }catch (Exception e){
            super.sendTransformExceptionMessage(FragmentStatus.VIDEO_TRANSFORM_EXCEPTION);
        }
    }

}
