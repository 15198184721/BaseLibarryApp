package com.baselibrary.http.intefaces;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-1
 * Description
 * </pre>
 */
public interface Action1<T> {
    /**
     * 通过只有一个动作的参数传递。主要用于替换rxjava到Rxjava2没有了这个接口
     * 造成数据传递和原来框架不匹配报错的问题
     * @param t
     */
    void call(T t);
}
