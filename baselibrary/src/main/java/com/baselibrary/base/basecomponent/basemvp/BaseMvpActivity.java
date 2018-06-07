package com.baselibrary.base.basecomponent.basemvp;

import android.support.annotation.Nullable;

import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.base.basecomponent.basemvp.mvpintefaces.BasePresenter;
import com.baselibrary.base.basecomponent.basemvp.mvpintefaces.BaseView;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-1
 * Description
 *  Mvp模式的基础页面
 * </pre>
 */
public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity
        implements BaseView<P> {

    @Nullable
    protected P mPresenter = null;

    //TODO 来自接口BaseView的方法实现
    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public P getPresenter() {
        return mPresenter;
    }

    //TODO 来之父类的方法Activity实现
    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.unsubscribe();
    }
}
