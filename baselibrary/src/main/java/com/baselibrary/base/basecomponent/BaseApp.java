package com.baselibrary.base.basecomponent;

import com.baselibrary.base.baseabstract.AbstractBaseApp;
import com.baselibrary.pluginutil.listeners.DefaultHostCallbacks;
import com.baselibrary.pluginutil.RePluginConfigUtil;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-21
 * Description
 *  基础的Application类。继承了插件化框架的Application类。所以初始化工作已经帮忙做了
 *  1、如果需要自定义配置：请在项目Application中复写:
 *      createConfig():插件配置信息创建方法(默认使用:{@link RePluginConfigUtil#getRepluginConfig()})
 *      createCallbacks():插件相关回调方法(默认使用:{@link DefaultHostCallbacks})
 *  2、如果您有后台管理，请在360插件管理平台申请自己的key,并且AndroidManifest文件创建如下内容:
 *      &lt;meta-data
 *          android:name="com.baselibrary_PLUGIN_KEY"
 *          android:value=""/&gt;
 * Version: v0.0.1
 * </pre>
 */
public abstract class BaseApp extends AbstractBaseApp {

    /**
     * 获取单利的上下文对象
     * @return
     */
    public static final BaseApp getInstan(){
        return baseApp;
    }

    @Override
    public void onCreate() {
        baseApp = this;
        super.onCreate();
    }

}
