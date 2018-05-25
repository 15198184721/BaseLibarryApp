package com.baselibrary.http.body;

import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.http.body.bean.FileUploadTag;
import com.baselibrary.http.body.listener.FileUploadProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-5-25
 * Description
 *  带进度的请求实体，可以用于任何网络请求(主要用于文件上传监听)
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class ProgressRequestBody extends RequestBody {
    //
    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final FileUploadProgressListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    //标志(null:普通实体，非空：表示文件实体)
    private FileUploadTag tag = null;

    /**
     * 这个参数
     * @param tag 区分的标志
     * @param requestBody
     * @param progressListener
     */
    public ProgressRequestBody(FileUploadTag tag,RequestBody requestBody, FileUploadProgressListener progressListener) {
        this.tag = tag;
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }
    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }
    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == bufferedSink) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;
            //每100毫秒调用一次回调
            final int timeBetween = 200;
            //当前时间
            long currentTime = System.currentTimeMillis();

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //增加当前写入的字节数
                writtenBytesCount += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                Observable.just(writtenBytesCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((Long aLong)->{
                        tag.pCurrentLen = writtenBytesCount;
                        tag.pTotal = totalBytesCount;
                        if(writtenBytesCount >= totalBytesCount){
                            BaseApp.getInstan().postBus(tag);
                            if(progressListener == null){
                                return ;
                            }
                            progressListener.onProgress(tag,writtenBytesCount, totalBytesCount);
                            return;
                        }
                        if(System.currentTimeMillis() - currentTime >= timeBetween) {
                            currentTime = System.currentTimeMillis();
                            BaseApp.getInstan().postBus(tag);
                            if(progressListener == null){
                                return ;
                            }
                            progressListener.onProgress(tag,writtenBytesCount, totalBytesCount);
                        }
                });
            }
        };
    }
}
