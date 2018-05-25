package com.baselibrary.http.body.bean;

import android.content.Context;

import com.baselibrary.utils.sdutils.SDUtils;

import java.text.DecimalFormat;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-6-6
 * Description
 *  网络图片上传的实体标志，主要用于监听文件上传时候的回调的第一个参数的tag，
 *  即:{@link com.baselibrary.http.body.listener.FileUploadProgressListener#onProgress(FileUploadTag, long, long)}的第一个参数
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class FileUploadTag {

    /** 创建的时间 */
    public long pTime = System.currentTimeMillis();
    /** 上下文对象(如果在界面那么就是界面的Context,如果非界面那么是AppContext对象) */
    public Context pContext;
    /** 当前上传的文件完整名称 */
    public String pFileName;
    /** 当前上传的文件名称 */
    public String pName;
    /** 当前已经上传的长度 */
    public long pCurrentLen = 0L;
    /** 总长度 */
    public long pTotal = 0L;

    //当前的进度(根据pCurrentLen 和 pTotal计算得出)
    private float progress = 0;
    //格式化小数位数对象
    DecimalFormat format = new DecimalFormat("#.0");


    public FileUploadTag(Context pContext, String fileName) {
        this.pContext = pContext;
        this.pFileName = fileName;
        pName = SDUtils.getFileName(pFileName);
    }

    /**
     * 获取百分比的进度
     * @return 0 - 100之间的整数值
     */
    public float getProgress() {
        progress = Float.valueOf(format.format((pCurrentLen*1.0F / pTotal*1.0F) * 100));
        return progress;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(this.getClass().getSimpleName()+"[");
        sb.append("当前长度="+pCurrentLen+",");
        sb.append("总长度="+pTotal+",");
        sb.append("pTime="+pTime+",");
        sb.append("pContext="+pContext+",");
        sb.append("pFileName="+pFileName+",");
        sb.append("progress="+getProgress()+",");
        sb.append("]");
        return sb.toString();
    }
}
