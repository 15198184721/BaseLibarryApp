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

package com.baselibrary.base.basecomponent.basemvp.data.sourcess;

import com.baselibrary.base.bases.BaseBean;
import com.baselibrary.http.intefaces.Action1;

import java.util.List;


/**
 * MVP模式中。module的接口，所有module都需要继承这个方法并且实现相关方法，一次来达到统一性
 * 这个主要是规范所有module对数据的操作规范
 * 注:
 * 此MVP中M层是面向指定实体(Modul)类的。一个实体类会对应一个本地和网络操作。
 * 和传统的M层面向每个界面(View)和主持(Presenter)有所区别(参考：官方mvp+rxjava)
 * git:https://github.com/googlesamples/android-architecture
 *
 * 建议：
 *  在项目中创建一个local包，存放本地操作的module层
 *  创建一个remote包存放网络操作的Module层的操作
 *  详细参考dome，也可以参考官方dome的Module层设计
 * @param <Module> 面向Module的类型
 */
public interface IDataSource<Module extends BaseBean> {

    /**
     * 获取数据集合,可以是
     *  网络数据
     *  本地数据库数据
     *  @param soucessCall 成功后的回调
     * @return
     */
    void getModuleList(Action1<List<Module>> soucessCall);

}
