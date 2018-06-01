package com.baselibrary.base.basecomponent;

import com.baselibrary.base.baseabstract.AbstractBaseFragment;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public abstract class BaseFragment extends AbstractBaseFragment {

    /*
     * 初始化相关操作,注意：需要注意的是需要getPermissions()注册的权限全部被允许才会执行
     */
    @Override
    public abstract void initData(boolean isInit);

    /*
     * 获取当前页面加载的视图
     */
    public abstract int getLayoutId();

    //这个方法只有当Fragment的可见状态发生改变时调用
    @Override
    public void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
    }

}
