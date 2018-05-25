package com.baselibrary.http;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.baselibrary.R;
import com.baselibrary.base.baseabstract.AbstractBaseApp;
import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.base.interfaces.IBaseActivity;
import com.baselibrary.http.body.ProgressRequestBody;
import com.baselibrary.http.body.bean.FileUploadTag;
import com.baselibrary.http.body.interceptor.ProgressInterceptor;
import com.baselibrary.http.body.listener.FileUploadProgressListener;
import com.baselibrary.http.gsonconverter.GsonConverterFactory;
import com.baselibrary.http.intefaces.IHttpManager;
import com.baselibrary.logutil.Lg;
import com.baselibrary.utils.GsonUtils;
import com.baselibrary.utils.sdutils.SDUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.ByteString;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-24
 * Description
 *  网络请求管理类
 * </pre>
 */
public class HttpManager implements IHttpManager {
    /** 日期格式化器 */
    public static final SimpleDateFormat DATAFORMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表单类型字符串
     */
    private static final String FROM_TYPE = "multipart/form-data";
    /**
     * Retrofit 框架对象
     */
    private static Retrofit retrofit;
    private static String baseUrl;//基础地址
    /**
     * 当前程序使用的网络请求对象
     */
    private static final OkHttpClient okHttpClient = getOkHttpClick();
    /**
     * 默认连接超时时长
     */
    private static final int DEFAULT_TIMEOUT = 5;
    /**
     * 应用的上下文
     */
    private BaseApp appContext;
    /**
     * 当前发起的界面的上下文，统一加载对话框处理
     */
    private IBaseActivity ActivityContext;
    /**
     * Loading对话框的Dialog
     */
    private Dialog loadingDialog;

    //单利创建
    private HttpManager() {
    }

    /**
     * 初始化http管理
     * @param config 自定义retrofit对象
     */
    public static void init(AbstractBaseApp.HttpConfig config) {
        synchronized (HttpManager.class) {
            if(config == null){
                return;
            }
            HttpManager.retrofit = config.getRetrofit();
            HttpManager.baseUrl = config.getBaseUrl();
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        //初始化基础地址(基础地址必须以：‘/’结束，否则报 IllegalArgumentException)
                        .client(okHttpClient)
                        //这个用于字符串转换(自定义转换器)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        //添加RxJava支持
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        //这个用于Json转换(自定义转换器)
                        .addConverterFactory(GsonConverterFactory.create(GsonUtils.getGson()))
                        .build();
            }
        }
    }

    /**
     * 获取Retrofit对象，这个初始化之后才会存在
     * @return
     */
    public static Retrofit getRetrofit() {
        if(retrofit == null){
            throw new NullPointerException("请先初始化Http管理器,还未初始化");
        }
        return retrofit;
    }

    /**
     * 获取一个网络请求管理对象
     *
     * @return
     */
    public static IHttpManager getInstan(Context con) {
        if (retrofit == null) {
            throw new NullPointerException("网络管理未初始化,请先初始化后再使用");
        }
        HttpManager hm = new HttpManager();
        if (con != null && con instanceof IBaseActivity) {
            hm.ActivityContext = (IBaseActivity) con;
        } else {
            hm.appContext = con == null ? BaseApp.getInstan() : (BaseApp) con;
        }
        return hm;
    }

    /**
     * 获取一个网络请求管理对象
     *
     * @return
     */
    public static IHttpManager getInstan() {
        return getInstan(null);
    }

    /**
     * 创建一个okhttp客户端
     *
     * @return
     */
    private static OkHttpClient getOkHttpClick() {
        if (okHttpClient != null) {
            return okHttpClient;
        }
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(new ProgressInterceptor());
        return httpClientBuilder.build();
    }

    //TODO 继承接口的对外派方法开始处(来至接口:IHttpFromFiedCreate)
    @Override
    public synchronized MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath){
        return createFormPartFile(fieldName,filePath,null,null);
    }

    @Override
    public synchronized MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath,
            FileUploadProgressListener uploadListener){
        return createFormPartFile(fieldName,filePath,uploadListener,null);
    }

    @Override
    public synchronized MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath,
            FileUploadProgressListener uploadListener, String fileName){
        // 创建 RequestBody，用于封装 请求RequestBody
        File file = new File(filePath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FROM_TYPE), file);
        fileName = fileName == null || "".equals(fileName) ? file.getName() : fileName;
        //创建上传文件的标志
        Context ct = ActivityContext == null ? appContext : (Context) ActivityContext;
        FileUploadTag fTag = new FileUploadTag(ct,file.getAbsolutePath());
        MultipartBody.Part body = MultipartBody.Part.createFormData(
                fieldName,fileName, new ProgressRequestBody(fTag,requestFile,uploadListener));
        return body;
    }

    @Override
    public synchronized List<MultipartBody.Part> createFormFileList(
            @NonNull String fieldName, @NonNull String[] filePath,
            FileUploadProgressListener uploadListener){
        List<MultipartBody.Part> fileList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        int repeatCount = 0;
        for (String path : filePath) {
            String fileName = SDUtils.getFileName(path);
            for (String name : fileNames) {
                if(name.equals(fileName)){
                    repeatCount++;
                }
            }
            MultipartBody.Part fBody = null;
            String sFilename = fileName;
            if(repeatCount > 0){
                final String exName = SDUtils.getFileNameEx(fileName);
                final String notExName = SDUtils.getFileNameNoEx(fileName);
                sFilename = notExName+"_"+repeatCount+"."+exName;
            }
            fBody = createFormPartFile(fieldName,path,uploadListener,sFilename);
            fileNames.add(fileName);
            fileList.add(fBody);
            repeatCount = 0;
        }
        return fileList;
    }

    @Override
    public synchronized List<MultipartBody.Part> createFormFileList(
            @NonNull String fieldName[], @NonNull String[] filePath,
            FileUploadProgressListener uploadListener){
        // 创建 RequestBody，用于封装 请求RequestBody
        if(fieldName.length != filePath.length)
            throw new NullPointerException("构建文件参数异常，字段名称和文件数量不一致："+HttpManager.class.getName());
        List<MultipartBody.Part> fileList = new ArrayList<>();
        for (int i = 0; i < fieldName.length; i++) {
            MultipartBody.Part fBody = createFormPartFile(fieldName[i],filePath[i],uploadListener);
            fileList.add(fBody);
        }
        return fileList;
    }

    @Override
    public synchronized RequestBody createBody(@NonNull String content){
        // 创建 RequestBody，用于封装 请求RequestBody
        return createBody(FROM_TYPE,content);
    }

    @Override
    public synchronized RequestBody createBody(String FROM_TYPE,@NonNull String content){
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody body = RequestBody.create(MediaType.parse(FROM_TYPE), content);
        return body;
    }

    @Override
    public synchronized RequestBody createBody(@NonNull File file){
        // 创建 RequestBody，用于封装 请求RequestBody
        return createBody(FROM_TYPE,file);
    }

    @Override
    public synchronized RequestBody createBody(String FROM_TYPE,@NonNull File file){
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody body = RequestBody.create(MediaType.parse(FROM_TYPE), file);
        return body;
    }

    @Override
    public synchronized RequestBody createBody(@NonNull byte[] bytes){
        // 创建 RequestBody，用于封装 请求RequestBody
        return createBody(FROM_TYPE, bytes);
    }

    @Override
    public synchronized RequestBody createBody(String FROM_TYPE,@NonNull byte[] bytes){
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody body = RequestBody.create(MediaType.parse(FROM_TYPE), bytes);
        return body;
    }

    @Override
    public synchronized RequestBody createBody(@NonNull byte[] bytes,int offset,int byteCount){
        // 创建 RequestBody，用于封装 请求RequestBody
        return createBody(FROM_TYPE, bytes,offset,byteCount);
    }

    @Override
    public synchronized RequestBody createBody(String FROM_TYPE,@NonNull byte[] bytes,int offset,int byteCount){
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody body = RequestBody.create(MediaType.parse(FROM_TYPE), bytes,offset,byteCount);
        return body;
    }

    @Override
    public synchronized RequestBody createBody(@NonNull ByteString bString){
        // 创建 RequestBody，用于封装 请求RequestBody
        return createBody(FROM_TYPE,bString);
    }

    @Override
    public synchronized RequestBody createBody(String FROM_TYPE,@NonNull ByteString bString){
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody body = RequestBody.create(MediaType.parse(FROM_TYPE),bString);
        return body;
    }

    //TODO 继承接口的对外派方法开始处(来至接口:IHttpManager)
    @Override
    public <T> Subscription request(Observable<T> obs, Action1<T> succ){
        return request(obs,succ,null);
    }

    @Override
    public <T> Subscription request(Observable<T> obs, Action1<T> succ,Action1<T> err){
        return request(
                BaseApp.getInstan().getResources().getString(R.string.httpLoading),
                obs,succ);
    }

    @Override
    public <T> Subscription request(String loadingStr, Observable<T> obs, Action1<T> succ){
        return request(loadingStr,obs,succ,null);
    }

    @Override
    public <T> Subscription request(String loadingStr, Observable<T> obs, Action1<T> succ,Action1<Throwable> err){
        Subscriber<T> subscriber = new Subscriber<T>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                if(err != null)
                    err.call(e);
            }
            @Override
            public void onNext(T o) {
                if(succ != null)
                    succ.call(o);
            }
        };
        return request(loadingStr,obs,subscriber);
    }

    @Override
    public <T> Subscription request(String loadMsg, Observable<T> obser, Subscriber<T> subscript) {
        if(ActivityContext != null && loadMsg != null && loadMsg.length() > 0){
            loadingDialog = ActivityContext.getDialogInterface().loadingMD(loadMsg).show();
        }else if(ActivityContext != null){
            loadingDialog = ActivityContext.getDialogInterface().loadingMD("加载中").show();
        }
        //可以在这里对数据做统一性处理等，目前制作了基本的操作
        Subscription subscribe = obser
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((T t)-> {
                    if(ActivityContext != null){
                        loadingDialog = null;
                        ActivityContext.getDialogInterface().dismissLoading();
                    }
                    try {
                        subscript.onNext(t);
                    }catch (Throwable e){
                        e.printStackTrace();
                        Lg.e("网络请求成功，执行成功后的监听异常，请检查！"+e.toString());
                    }
                }, (Throwable throwable)->{
                    if(ActivityContext != null){
                        ActivityContext.getDialogInterface().dismissLoading();
                    }
                    try {
                        Lg.e("网络请求异常 "+DATAFORMT.format(new Date())+"："+throwable.toString());
                        subscript.onError(throwable);
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }, ()->{
                    if(ActivityContext != null){
                        ActivityContext.getDialogInterface().dismissLoading();
                    }
                    try {
                        subscript.onCompleted();
                    }catch (Throwable e){
                        e.printStackTrace();
                        Lg.e("执行您设置的监听异常，请检查！"+e.toString());
                    }
                });
        return subscribe;
    }

    @Override
    public synchronized void updateLoadingMsg(String msg) {
        if(ActivityContext != null && loadingDialog != null){
            ActivityContext.getDialogInterface().updateLoadingMsg(loadingDialog,msg);
        }
    }
}
