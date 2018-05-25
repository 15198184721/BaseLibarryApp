package com.baselibrary.logutil;

import com.orhanobut.logger.Logger;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public class Lg{

    /**
     * 输出错误类型日志
     * @param msg
     */
    public static void e(String msg,Object...args){
        Logger.e(msg,args);
    }
    /**
     * 输出错误类型日志
     * @param throwable
     * @param msg
     * @param args
     */
    public static void e(Throwable throwable,String msg,Object...args){
        Logger.e(throwable,msg,args);
    }

    /**
     * 输出调试类型日志
     * @param msg
     */
    public static void d(String msg,Object...args){
        Logger.d(msg,args);
    }

    /**
     * 输出调试类型日志
     * @param object
     */
    public static void d(Object object){
        Logger.d(object);
    }

    /**
     * 输出提示类型日志
     * @param msg
     */
    public static void i(String msg,Object...args){
        Logger.i(msg,args);
    }

    /**
     * 输出版本类型日志
     * @param msg
     */
    public static void v(String msg,Object...args){
        Logger.v(msg,args);
    }

    /**
     * 输出xml类型日志
     * @param xmlString
     */
    public static void xml(String xmlString){
        Logger.xml(xmlString);
    }

    /**
     * 输出json日志
     * @param json
     */
    public static void json(String json){
        Logger.json(json);
    }

}
