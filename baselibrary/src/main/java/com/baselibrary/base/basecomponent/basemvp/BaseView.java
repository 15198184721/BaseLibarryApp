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

package com.baselibrary.base.basecomponent.basemvp;

/**
 * MVP模式的View视图层的基础类
 * @param <P>
 */
public interface BaseView<P extends BasePresenter> {

    /**
     * 设置当前View层持有的P层的Presenter对象
     * @param presenter
     */
    void setPresenter(P presenter);

    /**
     * 获取当前MVP中P层管理对象
     * 获取当前Mvp模式中的Presenter对象
     * @return
     */
    P getPresenter();


}
