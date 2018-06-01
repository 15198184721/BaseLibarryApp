package com.baselibrary.base.interfaces;

import com.baselibrary.acp.AcpListener;

import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2016-6-15
 * Description
 *  请求权限的接口，声明了权限请求有哪些方法
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public interface IRequestPermission {

    /**
     * 请求指定权限
     * @param permiss 需要申请的权限
     * @param lis 权限的监听
     */
    void requestPermiss(String[] permiss, AcpListener lis);

    /**
     * 申请权限
     * @param permiss 需要请求的权限集合
     * @param textMsg 这些权限申请需要给用户的提示信息
     * @param lis 监听
     */
    void requestPermiss(String[] permiss, String textMsg, AcpListener lis);

    /**
     * 获取当前页面需要的权限集合
     * @return 当前页面需要的权限
     */
    String[] getPermissions();

    /**
     * 检查当前页面申请权限是否通过(来源：getCheckPermission 方法)
     * @return 返回未被授权的集合(size<=0:表示所有权限已通过申请)
     */
    List<String> getCheckPermission();

    /**
     * 检查收否拥有指定权限
     * @param paramsList 检查指定权限是否被授权(可以多条)
     * @return 长度为0已授权，否则返回未授权的权限
     */
    List<String> getCheckPermission(String[] paramsList);

}
