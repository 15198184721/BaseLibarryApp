package com.baselibrary.base.interfaces;

import android.os.Handler;

import com.baselibrary.base.baseabstract.AbstractBaseApp;
import com.squareup.leakcanary.RefWatcher;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 *  对外提供的方法。基础上下文对外提供的方法
 * </pre>
 */
public interface IBaseApp {

    /**
     * 如果插件化框架的使用了后台则需要提供在后台的key
     * 如果没有使用直接返回 "" 即可
     * 【此方法是由于最终的Application实现】
     * @return
     */
    String getRePluginKey();

    /**
     * 获取Http配置信息
     * 包括基础地址
     * 基础的接口class
     * 该方法是有实际的Application实现的。
     * @return
     */
    AbstractBaseApp.HttpConfig getHttpConfig();

    /**
     * 获取内存检查对象
     * @return
     */
    RefWatcher getRefWatcher();

    /**
     * 获取全局的handler
     * @return
     */
    Handler getHandler();

    /**
     * 发送EvensBus消息，通知订阅了该消息的组件
     * @param obj 需要发行的消息
     */
    <T> void postBus(T obj);

    /**
     *  注册对象为事件或者消息监听者，
     *  注册之后使用带有{@link org.greenrobot.eventbus.Subscribe}注解的方法即可接受消息
     * @param obj 消息监听者对象
     * @param <T>
     */
    <T> void registerBus(T obj);

    /**
     * 取消事件订阅注册，需要在合适的时机取消注册，否则造成资源浪费
     * 例如:activity的onStart进行注册。而在onStop取消注册
     * @param obj
     * @param <T>
     */
    <T> void unregisterBus(T obj);



}
