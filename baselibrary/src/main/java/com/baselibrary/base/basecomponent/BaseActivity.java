package com.baselibrary.base.basecomponent;

import com.baselibrary.base.baseabstract.AbstractBaseActivity;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 *  基础页面
 * </pre>
 */
public abstract class BaseActivity extends AbstractBaseActivity {

    //TODO 抽象方法，这里定义了抽象方法。需要子类进行实现的
    /**
     * 初始化布局等(如：设置视图等)
     */
    protected abstract void initLayout();

}
