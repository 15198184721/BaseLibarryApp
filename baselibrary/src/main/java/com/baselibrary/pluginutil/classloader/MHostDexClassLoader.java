package com.baselibrary.pluginutil.classloader;

import com.qihoo360.replugin.RePluginClassLoader;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-23
 * Description
 *  框架的ClassLoader。即宿主加载的class的Classloader
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class MHostDexClassLoader extends RePluginClassLoader {
    public MHostDexClassLoader(ClassLoader parent, ClassLoader orig) {
        super(parent, orig);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(className, resolve);
    }
}
