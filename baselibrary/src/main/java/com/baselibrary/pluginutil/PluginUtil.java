package com.baselibrary.pluginutil;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-4-24
 * Description
 *  插件工具类
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class PluginUtil {

    /**
     * 通过className获取插件名称
     * @param className
     * @return
     */
    public static String getClassName2PluginName(String className){
        List<PluginInfo> list = RePlugin.getPluginInfoList();
        for (PluginInfo info : list) {
            if(className.startsWith(info.getPackageName())){
                return info.getName();
            }
        }
        return "";
    }

    /**
     * 通过className获取插件的包名称
     * @param className
     * @return
     */
    public static String getClassName2PluginPackName(String className){
        List<PluginInfo> list = RePlugin.getPluginInfoList();
        for (PluginInfo info : list) {
            if(className.startsWith(info.getPackageName())){
                return info.getPackageName();
            }
        }
        return "";
    }

}
