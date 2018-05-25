package com.baselibrary.listeners;

import android.content.Context;
import android.util.Log;

import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.sdk.HostEventCallbacks;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-18
 * Description
 *  插件安装相关回调，此回调中会统计插件安装情况，并进行日志打点
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class DefalultHostEventCallbacks extends HostEventCallbacks {
    public DefalultHostEventCallbacks(Context context) {
        super(context);
    }

    @Override //当插件安装失败触发此逻辑
    public void onInstallPluginFailed(String s, InstallResult installResult) {
        super.onInstallPluginFailed(s, installResult);
        Log.e("this","onInstallPluginFailed");
    }

    @Override //当插件安装成功时触发此逻辑
    public void onInstallPluginSucceed(final PluginInfo pluginInfo) {
        super.onInstallPluginSucceed(pluginInfo);
        Log.e("this","onInstallPluginSucceed");
    }

    @Override  //当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
    public void onStartActivityCompleted(String s, String s1, boolean b) {
        super.onStartActivityCompleted(s, s1, b);
        Log.e("this","onStartActivityCompleted");
    }

    @Override
    public void onBinderReleased() {
        super.onBinderReleased();
        Log.e("this","onBinderReleased");
    }
}
