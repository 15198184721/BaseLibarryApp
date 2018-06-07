/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baselibrary.base.basecomponent.basemvp.mvpintefaces;

/**
 * 基础主持类，MVP模式Presenter层基础接口
 * 项目实际使用也可以直接使用:@{@link AbstractBasePresenter}
 * 也可以使用当前接口自定义实现
 */
public interface BasePresenter<V extends BaseView,M> {

    /**
     * 订阅，开启订阅(也就是加载数据的方法)
     * 初始化的方法(每次界面显示到前台都会加载一次)
     */
    void subscribe();

    /**
     * 取消注册订阅(取消关联的方法)
     * 当界面从前台切换到后台即会调用此方法
     */
    void unsubscribe();

}
