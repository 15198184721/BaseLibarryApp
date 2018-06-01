package com.baselibrary.pluginutil.factory;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-9
 * Description
 *  此类是专门针对插件操作工厂类<br></br>
 *  主要功能：<br><br/>
 *  1、通过反射将插件中的Fargment加载出来并且更具需要返回
 *  2、加载指定的插件中的类或在View
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class HostFactory {

    /**
     * 此方法作用是在指定的插件名称中加载指定的fragment类。并创建后返回Fragment对象<br><br/>
     * 如果失败则返回null
     * @param pluginName
     * @param fragmentClassName
     * @return 失败=null,成功=Fragment对象
     */
    public static Fragment getPluginFragment(String pluginName,String fragmentClassName){
        if(TextUtils.isEmpty(pluginName) || TextUtils.isEmpty(fragmentClassName)){
            return null;
        }
        Class<?> fragmentClass = getPluginClass(pluginName,fragmentClassName);
        if(fragmentClass == null){
            return null;
        }
        try {
            //使用插件的Classloader获取指定Fragment实例
            //这种方式的是直接使用如果需要在xml配置使用需要 RePlugin.registerHookingClass();重定向
            Fragment frm = getClass2Object(fragmentClass,Fragment.class);
            return frm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  加载指定插件中的class对象
     * @param pluginName 插件名称
     * @param className 插件类的名称
     * @return
     */
    public static Class<?> getPluginClass(String pluginName,String className){
        ClassLoader pluginClassLoader = RePlugin.fetchClassLoader(pluginName);
        if(pluginClassLoader == null){
            return null;
        }
        Class<?> loadClass = null;
        try {
            loadClass = pluginClassLoader
                    .loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loadClass;
    }

    /**
     * 在所有插件中查找指定的class<br><br/>
     * 注意：<br><br/>
     * 如果多个插件中存在同一个类，那么可能无法找到准确的类，只会找到第一个查询到类
     * 查找顺序：宿主自身 -> 插件(插件顺序更具{@link RePlugin#getPluginInfoList()}返回的顺序查找)
     * @param className
     * @return
     */
    public static Class<?> getPluginClass(String className){
        List<PluginInfo> list = RePlugin.getPluginInfoList();
        Class<?> czz = null;
        try {
            czz = ClassLoader.getSystemClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(czz != null){
            return czz;
        }
        for (PluginInfo inof : list) {
            czz = getPluginClass(inof.getName(),className);
            if(czz != null)
                return czz;
        }
        return czz;
    }

    /**
     * 将指定原class和目标class进行判断。是否是其子类或相等，如果是则进行转换后返回
     * @param srcClass
     * @param targetTypeClass
     * @return 如果可以转换那么返回转换后的class，如果不可以那么返回null
     */
    public static <T> Class<? extends T> getClass2TargetTypeClass(Class<?> srcClass,Class<T> targetTypeClass){
        try {
            Class<? extends T> cz = srcClass.asSubclass(targetTypeClass);
            return cz;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 类型转换并创建对象<br><br/>
     * 如果源class是目标class或者其子类则进行转换后创建对象并且返回
     * @param srcClass 源class
     * @param targetTypeClass 目标类型class
     * @return 可以转换，转换成功则是目标类型对象，不可转或者不符合转换返回null
     */
    public static <T> T getClass2Object(Class<?> srcClass,Class<T> targetTypeClass){
        return getClass2Object(srcClass,targetTypeClass,new Object[]{});
    }

    /**
     * 类型转换并创建对象,带有构造参数的方式<br><br/>
     * 如果源class是目标class或者其子类则进行转换后创建对象并且返回
     * @param srcClass 源class
     * @param targetTypeClass 目标类型class
     * @return 可以转换，转换成功则是目标类型对象，不可转或者不符合转换返回null
     */
    public static <U> U getClass2Object(
            Class<?> srcClass,Class<U> targetTypeClass,Object[] params){
        if(srcClass == null || targetTypeClass == null){
            return null;
        }
        U obj = null;
        try {
            Class<? extends U> c = getClass2TargetTypeClass(srcClass,targetTypeClass);
            if(c != null) {
                obj = CreateObjectFactory.getObject(c, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取指定class的View对象
     * @param pluginName
     * @param viewClassName
     * @return 成功View对象，失败返回null
     */
    public static View getView(String pluginName,String viewClassName){
        return getView(pluginName,viewClassName,View.class);
    }

    /**
     * 获取指定class的View对象。可以指定返回的View类型，创建时候会进行转换
     * @param pluginName 插件名称
     * @param viewClassName 获取的View的class路径
     * @param targetViewClass 需要返回时候转换为的类型
     * @return 成功View对象，失败返回null
     */
    public static <T extends View> T getView(
            String pluginName, String viewClassName,Class<T> targetViewClass){
        Class<?> vClass = getPluginClass(pluginName,viewClassName);
        if(vClass == null){
            return null;
        }
        return getClass2Object(vClass,targetViewClass,new Object[]{RePlugin.fetchContext(pluginName)});
    }
}
