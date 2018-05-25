package com.baselibrary.http.body.listener;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-5-25
 * Description
 *  网络数据加载监听，返回实体的监听
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public interface ResponseProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
