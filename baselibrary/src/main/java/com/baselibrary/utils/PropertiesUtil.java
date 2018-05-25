package com.baselibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-16
 * Description
 *  .properties属性文件的操作工具类
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class PropertiesUtil {

    /**
     * 获取asstes中获取配置文件(.properties)中的值
     * @param con 上下文
     * @param propertiesFilePath 属性文件的路径
     * @param key 属性名称
     * @return
     */
    public static  String getProperties(Context con, String propertiesFilePath ,String key){
        Properties p = new Properties();
        try {
            p.load(con.getAssets().open(propertiesFilePath));
            final String s = p.getProperty(key,"");
            if(TextUtils.isEmpty(s)){
                Log.e("this","请确保配置文件中存在属性'"+key+"'");
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("this","配置文件不存在，请增加配置文件");
            return "";
        }
    }
}
