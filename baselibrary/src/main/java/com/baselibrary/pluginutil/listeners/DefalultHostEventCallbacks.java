package com.baselibrary.pluginutil.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baselibrary.base.basecomponent.BaseApp;
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
        BaseApp.getInstan().postBus(pluginInfo);//通知订阅
        super.onInstallPluginSucceed(pluginInfo);
        Log.e("this","onInstallPluginSucceed");
    }

    @Override //当插件Activity准备分配坑位时执行
    public void onPrepareAllocPitActivity(Intent intent) {
        super.onPrepareAllocPitActivity(intent);
    }

    @Override //当插件Activity即将别打开时执行
    public void onPrepareStartPitActivity(Context context, Intent intent, Intent pittedIntent) {
        super.onPrepareStartPitActivity(context, intent, pittedIntent);
    }

    @Override //当插件Activity所在的坑位被执行“销毁”时被执行
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
    }

    @Override  //启动 Activity 完成
    public void onStartActivityCompleted(String s, String s1, boolean b) {
        super.onStartActivityCompleted(s, s1, b);
        Log.e("this","onStartActivityCompleted");
    }

    @Override  //当插件Service的Binder被释放时被执行
    public void onBinderReleased() {
        super.onBinderReleased();
        Log.e("this","onBinderReleased");
    }
}
