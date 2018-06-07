package com.baselibarryapp.calculater;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.baselibarryapp.api.HttpApi;
import com.baselibarryapp.bases.IPBean;
import com.baselibrary.http.HttpManager;
import com.baselibrary.http.intefaces.Action1;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.component.service.PluginServiceClient;
import com.qihoo360.replugin.model.PluginInfo;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-5
 * Description
 *  单元测试示例
 *  单元测试代码在:androidTest/com.baselibarryapp.DyTestTest
 * </pre>
 */
public class DyTest {

    /**
     * 单元测试代码 将使用单元测试来测试这个方法
     * (测试返回值和指定值是否相等)
     * @param a
     * @param b
     */
    public int add(int a,int b){
        return a+b;
    }

    /**
     * 测试网络请求方法
     * @param suc
     */
    public void requestHttp(Action1<IPBean> suc){
        HttpManager.getInstan().request(
                "获取数据",
                HttpManager.getRetrofit().create(HttpApi.class).getIp("json","218.4.255.255"),suc);
    }

    /**
     * 启动插件
     */
    public ComponentName startPlugins(Context con) {
        PluginInfo inof = RePlugin.getPluginInfo("webview");
        if (inof != null) {
            RePlugin.preload(inof);
        }
        Intent it = RePlugin.createIntent(
                "webview", "plugin1.com.plugin1.WebViewService");
        return PluginServiceClient.startService(con, it);
    }

}
