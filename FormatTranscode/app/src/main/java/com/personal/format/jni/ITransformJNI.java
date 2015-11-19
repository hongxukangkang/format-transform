package com.personal.format.jni;

import com.personal.format.domain.dao.Parameter;

/**
 * Created by zhaokang on 2015/11/13.
 */
public interface ITransformJNI {

    /**
     *返回转换的状态
     */

    Parameter transformService(Parameter parameter);

}
