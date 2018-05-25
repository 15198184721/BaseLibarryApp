package com.baselibarryapp;

import com.baselibarryapp.api.HttpApi;
import com.baselibrary.base.basecomponent.BaseApp;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public class App extends BaseApp {
    @Override
    public String getRePluginKey() {
        return "";
    }

    @Override
    public HttpConfig getHttpConfig() {
        return new HttpConfig(HttpApi.BaseUrl);
    }
}
