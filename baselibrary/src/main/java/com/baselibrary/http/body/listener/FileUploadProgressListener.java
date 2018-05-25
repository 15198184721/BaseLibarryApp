package com.baselibrary.http.body.listener;
import com.baselibrary.http.body.bean.FileUploadTag;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-5-25
 * Description
 *  文件上传进度的监听器
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public interface FileUploadProgressListener {
    /**
     * 进度监听方法
     * @param tag 标志,这个是方便在接收到订阅后的区分标志(一般在多文件上传等时候被当做区分标志)
     * @param currentBytesCount 当前的进度
     * @param totalBytesCount 总的长度
     */
    void onProgress(FileUploadTag tag, long currentBytesCount, long totalBytesCount);
}
