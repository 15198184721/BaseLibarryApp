package com.baselibrary.base.baseabstract;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.base.interfaces.IBaseApp;
import com.baselibrary.http.HttpManager;
import com.baselibrary.listeners.DefaultHostCallbacks;
import com.baselibrary.logutil.Lg;
import com.baselibrary.utils.RePluginConfigUtil;
import com.dialogutil.ActivityStackManager;
import com.dialogutil.DialogsMaintainer;
import com.dialogutil.StyledDialog;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginApplication;
import com.qihoo360.replugin.RePluginCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Retrofit;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 *  抽象的上下文方法。将公共实现放在这个类中，减少{@link BaseApp}单个文件的代码量
 * </pre>
 */
public abstract class AbstractBaseApp extends RePluginApplication
        implements IBaseApp {
    /** 单利的上下文对象 */
    protected static BaseApp baseApp = null;
    /** 弱应用对象，检查内存泄露对象返回 */
    protected RefWatcher refWatcher;
    /** 全局的一个handler */
    protected Handler pHandler;
    /** 全局的一个EventBus消息通知管理对象,来进行消息通知和管理 */
    protected EventBus bus;

    //TODO 来自父类的方法继承实现
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePlugin.enableDebugger(base, true);
//        RePlugin.enableDebugger(base, BuildConfig.DEBUG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RePluginConfigUtil.init(baseApp);
        initLocal();
        initLogger();
        initDialog();
    }

    @Override //创建插件配置(务必复写 createConfig() 和 createCallbacks() ⽅方法，否则会造成打点统计失效。)
    protected RePluginConfig createConfig() {
        return RePluginConfigUtil.getRepluginConfig();
    }

    @Override //3、设置回调(务必复写 createConfig() 和 createCallbacks() ⽅方法，否则会造成打点统计失效。)
    protected RePluginCallbacks createCallbacks() {
        return new DefaultHostCallbacks(this);
    }

    //TODO 来自接口的方法继承实现
    @Override
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public Handler getHandler() {
        return pHandler;
    }

    @Override
    public <T> void postBus(final T obj) {
        if(obj == null){
            return;//如果是null那么就丢弃本次消息
        }
        try{
            getHandler().post(()->{
                bus.post(obj);
            });
        }catch (Exception e){
            e.printStackTrace();
            Lg.e(e,"订阅事件执行错误:");
        }
    }

    @Override
    public <T> void registerBus(T obj) {
        if(obj == null){
            return;//如果是null那么就丢弃本次消息
        }
        try{
            if(!bus.isRegistered(obj)) {
                bus.register(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public <T> void unregisterBus(T obj) {
        try{
            bus.unregister(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO 私有方法区域
    //初始化本地相关方法和私有属性等
    private void initLocal() {
        pHandler = new Handler();
        bus = new EventBus();
        HttpManager.init(getHttpConfig());
    }
    //初始化日志打印库
    private void initLogger(){
        PrettyFormatStrategy st = PrettyFormatStrategy.newBuilder()
                .methodCount(3) //显示多少条方法线
                .showThreadInfo(false)//隐藏线程信息
                .tag(getPackageName()) //设置tag
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(st));
    }

    //初始化dialog
    private void initDialog(){
        refWatcher = LeakCanary.install(this);
        StyledDialog.init(getApplicationContext());
        registCallback();
    }

    //注册一个全局的Activity监听器
    private void registCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStackManager.getInstance().addActivity(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {

            }
            @Override
            public void onActivityResumed(Activity activity) {
            }
            @Override
            public void onActivityPaused(Activity activity) {
                DialogsMaintainer.onPause(activity);

            }
            @Override
            public void onActivityStopped(Activity activity) {

            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }
            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStackManager.getInstance().removeActivity(activity);
            }
        });
    }

    /**
     * http配置类，在Application初始化需要提供这个配置参数
     */
    public static class HttpConfig{
        private String baseUrl;
        private Retrofit retrofit;

        public HttpConfig(Retrofit retrofit) {
            this.retrofit = retrofit;
        }

        public HttpConfig(String baseUrl) {
            this.baseUrl = baseUrl;
        }


        /**
         * 获取基础地址
         * @return
         */
        public String getBaseUrl() {
            return baseUrl;
        }

        public HttpConfig setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 自定义的Retrofit框架对象
         * @return
         */
        public Retrofit getRetrofit() {
            return retrofit;
        }

        public HttpConfig setRetrofit(Retrofit retrofit) {
            this.retrofit = retrofit;
            return this;
        }
    }
}
