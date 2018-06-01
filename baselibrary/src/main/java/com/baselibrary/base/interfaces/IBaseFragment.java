package com.baselibrary.base.interfaces;

import android.content.Intent;

import com.baselibrary.base.basecomponent.BaseActivity;

/**
 * <pre>
 * Author: lcl
 * Date: 2016-6-15
 * Description
 *  基础的Fragment需要的操作接口类型
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public interface IBaseFragment extends IRequestPermission {

    /**
     * <pre>
     * 初始化相关操作,注意：需要注意的是需要getPermissions()注册的权限全部被允许才会执行
     * 需要注意的是参数,这个参数决定是否为第一次加载数据
     * @param isInit T:第一次加载数据，F:非第一次加载
     * </pre>
     */
    void initData(boolean isInit);

    /**
     * 获取要加载视图的资源id
     * @return 当前资源id
     */
    int getLayoutId();

    /**
     * 获取当前关联的Activity
     * 返回的是基础的Activity
     * @return
     */
    BaseActivity getThisActivity();

    /**
     * 跳转页面(方便设置动画)     * @param intent :跳转页面的Intent对象
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

    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 Fragment.setUserVisibleHint(boolean)一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    void onFragmentVisibleChange(boolean isVisible);

}
