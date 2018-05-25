package com.baselibrary.http.body.interceptor;

import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.http.body.ProgressResponseBody;
import com.baselibrary.http.body.listener.ResponseProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <pre>
 * Author: lcl
 * Date: 2017-6-5
 * Description
 *  进度拦截器，网络实体的进度拦截器
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class ProgressInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return newResponse(chain);
    }

    /**
     * 重新构造Request请求
     *
     * @param chain
     * @return
     */
    private Request newRequest(Chain chain) {
        Request rq = chain.request();
        Request.Builder builder = new Request.Builder()//重新构建一个Request请求对象
                .url(rq.url())
                .headers(rq.headers())
                .cacheControl(rq.cacheControl())
                .tag(rq.tag());
        if (rq.body() == null) {
            rq = builder.build();
        } else {
            rq = builder.method(rq.method(), rq.body()).build();
        }
        return rq;
    }

    /**
     * 重新构建Response对象
     *
     * @param chain 拦截对象
     * @return
     */
    private Response newResponse(Chain chain) throws IOException {
        final Request req = newRequest(chain);
        Response orginalResponse = chain.proceed(req);
        return orginalResponse.newBuilder()
                .body(new ProgressResponseBody(orginalResponse.body(), new ResponseProgressListener() {
                    long setpLen = 0;//每次读取的内容
                    long currLen = 0;//当前已经读取了的数据的总长度
                    NteworkProgress pross = new NteworkProgress(req);
                    long time = 0;
                    @Override
                    public void onProgress(long progress, long total, boolean done) {
                        //可以作为记录流量的依据
                        currLen = progress;
                        setpLen = (currLen - setpLen);
                        pross.progress = setpLen;
                        pross.total = currLen;
                        pross.isFinish = done;
                        //50毫秒刷新一次
                        if(pross.isFinish ||
                                System.currentTimeMillis() - time > 50) {
                            BaseApp.getInstan().postBus(pross);
                            time = System.currentTimeMillis();
                        }
                    }
                }))
                .build();
    }

    /**
     * 网络进度的订阅实体
     */
    public static class NteworkProgress {
        /**
         * 请求体对象
         */
        public Request req = null;
        /**
         * 请求体的长度
         */
        public long requestLen = 0L;
        /**
         * 已经加载的进度
         */
        public long progress = 0L;
        /**
         * 总的长度
         */
        public long total = 0L;
        /**
         * 是否完成了
         */
        public boolean isFinish = false;

        public NteworkProgress(Request req) {
            if (req == null) {
                throw new NullPointerException("请求参数不能为空:" + NteworkProgress.class.getName());
            }
            this.req = req;
            for (int i = 0; i < this.req.headers().size(); i++) {
                requestLen += this.req.headers().value(i).length();
            }
            if (req.body() != null) {
                try {
                    requestLen += this.req.body().contentLength();
                } catch (IOException e) {}
            }
        }

        @Override
        protected void finalize() {
            try {
                //此处可以做一些流量统计的操作
                super.finalize();
            } catch (Exception e) {
            } catch (Throwable throwable) {
            }
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer(this.getClass().getSimpleName()+"[");
            sb.append("当前读取数据长度="+progress+",");
            sb.append("返回数据总长度="+total+",");
            sb.append("请求体数据长度="+requestLen+",");
            sb.append("]");
            return sb.toString();
        }
    }

}
