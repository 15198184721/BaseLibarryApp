package com.baselibrary.base.interfaces;

import android.content.Intent;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;

import com.baselibrary.dialog.IDialogUtil;
import com.baselibrary.http.body.interceptor.ProgressInterceptor;
import com.baselibrary.http.intefaces.IHttpFromFiedCreate;
import com.baselibrary.http.intefaces.IHttpManager;
import com.qihoo360.replugin.model.PluginInfo;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 *  基础的Activity对外提供的接口
 *  除此之外接口还自带了一些订阅事件：所以如果需要你可以自己复写这些方法来接受订阅消息
 * </pre>
 */
public interface IBaseActivity extends IHttpFromFiedCreate {

    //TODO 以下是对外派发的接口
    /**
     * 获取当前页面提供的Dialog提示功能接口
     * @return
     */
    IDialogUtil getDialogInterface();

    /**
     * 获取全局的Handler
     * @return
     */
    Handler getHandler();

    /**
     * 获取Http管理对象，
     * @param isShowHttpLoading 是否需要显示loading对话框，T:需要显示,F:不显示
     * @return
     */
    IHttpManager getHttpManager(boolean isShowHttpLoading);

    /**
     * 获取Http接口类型代理对象
     * @param httpApiInteface
     * @param <T>
     * @return
     */
    <T> T getHttpApiInteface(Class<T> httpApiInteface);

    /**
     * 弹出一个消息框
     * @param msg
     */
    void toast(String msg);

    /**
     * 弹出一个消息框，自定义的View的Toast消息
     * @param view 自定义的视图
     */
    void toast(View view);

    /**
     * 弹出一个自定义View的Toast消息
     * @param view
     * @param color
     */
    void toast(View view,int color);

    /**
     * 弹出一个消息框
     * @param msg
     * @param color
     */
    void toast(String msg,int color);

    /**
     * 从顶部弹出一个Toast提示框,时间相对较短的
     * @param msg 消息
     * @param color 背景颜色
     */
    void toastTopShort(String msg,int color);

    /**
     * 设置一个长时间的自定义视图，自定义位置、文字、颜色的toast
     * @param view 自定义的视图
     * @param msg 消息
     * @param color 颜色
     * @param gravity 方位(Gravity中的属性)
     */
    void toastGravityLong(View view,String msg,int color,int gravity);

    /**
     * 设置短时间提示的自定义视图，自定义位置、文字、颜色的toast
     * @param view 自定义的视图
     * @param msg 消息
     * @param color 颜色
     * @param gravity 方位(Gravity中的属性)
     */
    void toastGravityShort(View view,String msg,int color,int gravity);

    /**
     * 最全的一个toast设置显示，可以产生给定的参数信息的toast
     * 同时以下的方法最终都是走的这个方法来实现:<br></br>
     * {@link IBaseActivity#toastGravityLong(View, String, int, int)}<br></br>
     * {@link IBaseActivity#toastGravityShort(View, String, int, int)}<br></br>
     * {@link IBaseActivity#toastTopLong(String, int)}<br></br>
     * {@link IBaseActivity#toastTopShort(String, int)}<br></br>
     * @param view 自定义的视图
     * @param msg 消息
     * @param color 背景颜色
     * @param gravity 显示的位置 参考:{@link Gravity}
     * @param isLong 是否使用长时间的显示
     * @param actionString 单击的项的文字
     * @param click 单击的项的操作
     */
    void toastGravity(
            View view,String msg,int color,int gravity,boolean isLong,String actionString,View.OnClickListener click);

    /**
     * 从顶部弹出一个Toast提示框,时间相对较长的
     * @param msg 消息
     * @param color 背景颜色
     */
    void toastTopLong(String msg,int color);

    /**
     * 跳转页面(方便设置动画)
     * @param intent :跳转页面的Intent对象
     * @param closeId :是否关闭当前的页面(0-不关闭，非0-关闭)
     */
    void toActivity(Intent intent, int closeId);

    /**
     * 跳转页面(方便设置动画)
     * @param toActivityClass :需要跳转到那个页面
     * @param closeId :是否关闭当前的页面(0-不关闭，非0-关闭)
     */
    void toActivity(Class<?> toActivityClass, int closeId);

    /**
     * 跳转页面。但返回结果的Fragment到页面的跳转
     * @param toActivityClass 目标的界面
     * @param requestCode 请求码
     */
    void toActivityResult(Class<?> toActivityClass, int requestCode);

    /**
     * 跳转到目标页面
     * @param intent 跳转的Intent对象
     * @param requestCode 请求码
     */
    void toActivityResult(Intent intent, int requestCode);



    //TODO 以下是系统已存在的一些默认的订阅方法(继承后需要加上 @Subscribe注解，否则无效)
    /**
     * 网络进度的订阅事件，这个是网路Response的监听，也就是下载实体<br></br>
     * 刷新频率为：50毫秒
     * @param progress
     */
    void httpRequestProgress(ProgressInterceptor.NteworkProgress progress);

    /**
     * 插件安装成功的订阅通知
     * @param pluginInfo 安装的插件信息
     */
    void installPluginSucceed(final PluginInfo pluginInfo);


}
