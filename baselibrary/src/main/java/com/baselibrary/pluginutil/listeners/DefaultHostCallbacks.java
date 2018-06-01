package com.baselibrary.pluginutil.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baselibrary.pluginutil.classloader.MHostDexClassLoader;
import com.baselibrary.pluginutil.classloader.MPluginDexClassLoader;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.RePluginClassLoader;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.sdk.HostCallbacks;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-18
 * Description
 *  插件打开相关回调，此回调中会统计插件打开情况，并进⾏行行⽇日志打点。
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class DefaultHostCallbacks extends HostCallbacks {

    public DefaultHostCallbacks(Context context) {
        super(context);
    }

    @Override //插件不存在的回调(可以做下载的逻辑)
    public boolean onPluginNotExistsForActivity(Context context, String s, Intent intent, int i) {
        Log.e("this","插件Activity不存在:"+getClass().getName());
        return super.onPluginNotExistsForActivity(context, s, intent, i);
    }

    @Override //插件的Dex文件加载的Classloader
    public PluginDexClassLoader createPluginClassLoader(PluginInfo pi, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        return new MPluginDexClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent);
//        return super.createPluginClassLoader(pi, dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    @Override //RePlugin的class加载的classLoader
    public RePluginClassLoader createClassLoader(ClassLoader parent, ClassLoader original) {
        return new MHostDexClassLoader(parent, original);
//        return super.createClassLoader(parent, original);
    }
}
