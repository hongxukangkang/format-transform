package com.personal.format.domain.dao;

/**
 * Created by zhaokang on 2015/11/13.
 */
public class AudioInfo {
    public String srcFile;//源文件路径
    public String dstFile;//目标文件路径
    public String srcFormat;//源文件格式
    public String dstFormat;//目标文件格式
    public int[] currentProgress;//转换的当前进度，传给底层，用于实时的检测，以便于进行节目的刷新
}
