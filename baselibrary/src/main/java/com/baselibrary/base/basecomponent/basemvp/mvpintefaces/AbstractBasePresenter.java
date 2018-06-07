package com.baselibrary.base.basecomponent.basemvp.mvpintefaces;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-4
 * Description
 *  {@link BasePresenter}的基础类抽象实现，实现了部分基础P层部分方法。
 *  实际使用可以继承这个抽象类直接使用
 *  也可以继承基础接口全部自定重新实现
 * </pre>
 */
public abstract class AbstractBasePresenter<V extends BaseView,M>
        implements BasePresenter<V,M> {

    /** V层对象，视图层对象*/
    protected V mView;
    /** Module层对象 */
    protected M mRepository;

    /**
     * 构造函数，需要传入View层对象和module层对象
     * @param mView 视图层对象
     * @param mRepository module层对象
     */
    public AbstractBasePresenter(V mView, M mRepository) {
        this.mView = mView;
        this.mRepository = mRepository;
        this.mView.setPresenter(this);//调用视图层对象设置当前对象
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {}
}
