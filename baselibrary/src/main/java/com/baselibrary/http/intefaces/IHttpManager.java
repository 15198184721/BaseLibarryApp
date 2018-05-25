package com.baselibrary.http.intefaces;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-24
 * Description
 * </pre>
 */
public interface IHttpManager extends IHttpFromFiedCreate{

    /**
     * 发起一个网络请求
     * @param obs 订阅参数，调用接口返回的对象
     * @param succ 成功的处理
     * @param <T>
     * @return 控制对象
     */
    <T> Subscription request(Observable<T> obs, Action1<T> succ);

    /**
     * 发起一个网络请求
     * @param obs 订阅参数，调用接口返回的对象
     * @param succ 成功的处理
     * @param err 错误的处理
     * @param <T>
     * @return
     */
    <T> Subscription request(Observable<T> obs, Action1<T> succ,Action1<T> err);

    /**
     * 发起一个网络请求
     * @param loadingStr 显示等待对话框的内容
     * @param obs 订阅参数，调用接口返回的对象
     * @param succ 成功的处理
     * @param <T>
     * @return
     */
    <T> Subscription request(String loadingStr, Observable<T> obs, Action1<T> succ);

    /**
     * 发起一个网络请求
     * @param loadingStr 显示等待对话框的内容
     * @param obs 订阅参数，调用接口返回的对象
     * @param succ 成功的处理
     * @param err 错误的处理
     * @param <T>
     * @return
     */
    <T> Subscription request(String loadingStr, Observable<T> obs, Action1<T> succ, Action1<Throwable> err);

    /**
     * 发起一个自定义的文本的加载等待框的网络请求
     * @param loadMsg 需要显示的加载等待文字
     * @param obser 被观察者
     * @param subscript 订阅者
     * @return 控制对象
     */
    <T> Subscription request(final String loadMsg, final Observable<T> obser, final Subscriber<T> subscript);

    /**
     * 更新当前的loading内容
     * @param msg 更新的内容
     */
    void updateLoadingMsg(String msg);

}
