package com.baselibrary.classloader;

import android.util.Log;

import com.baselibrary.utils.PluginUtil;
import com.qihoo360.replugin.PluginDexClassLoader;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-23
 * Description
 *  自定义插件的Classloader方便实现更多的功能
 *  这个classLoader是专门用于插件加载class的
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class MPluginDexClassLoader extends PluginDexClassLoader {

    //完成加载的插件集合
    final Set<String> finishLoadPlugins = new HashSet<>();

    public MPluginDexClassLoader(PluginInfo pi, String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(pi, dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(className, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //检查插件是否创建了上下文(可能会影响性能)
        String plName = PluginUtil.getClassName2PluginName(name);
        if(!finishLoadPlugins.contains(plName) && !"".equals(plName)){
            Log.e("this","使用插件中的类,加载插件：<"+plName+">"+",class="+name);
            finishLoadPlugins.add(plName);
            RePlugin.fetchContext(plName);
        }
        return super.findClass(name);
    }
}
