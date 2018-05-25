package com.baselibrary.utils;

import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.listeners.DefalultHostEventCallbacks;
import com.qihoo360.replugin.RePluginConfig;
import com.qihoo360.replugin.sdk.PluginConfig;
import com.qihoo360.replugin.sdk.PluginManager;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-18
 * Description
 *  插件配置工具
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class RePluginConfigUtil {
    /**
     * 在xml中配置的appkey的mata的key
     */
    private static String rePluginAppkey_MataKey = "com.baselibrary_PLUGIN_KEY";

    /**
     * 初始化方法
     */
    public synchronized static void init(BaseApp app) {
        if(!(app.getRePluginKey() == null || "".equals(app.getRePluginKey()))) {
            PluginConfig.setAppKey(app.getRePluginKey());
        }
        PluginManager.init(app);
    }

    /**
     * 获取插件管理的默认配置文件
     *
     * @return
     */
    public synchronized static RePluginConfig getRepluginConfig() {
        RePluginConfig rePluginConfig = new RePluginConfig();
        //开启插件类不存在时候读取宿主中的类
        rePluginConfig.setUseHostClassIfNotFound(true);
        //暂时关闭安全校验
        rePluginConfig.setVerifySign(false);
		//设置打印详细的日志
		rePluginConfig.setPrintDetailLog(true);
		// 在Art上，优化第一次loadDex的速度
		rePluginConfig.setOptimizeArtLoadDex(true);
//            rePluginConfig.setVerifySign(!BuildConfig.DEBUG);
        rePluginConfig.setEventCallbacks(new DefalultHostEventCallbacks(BaseApp.getInstan()));
        return rePluginConfig;
    }
}
