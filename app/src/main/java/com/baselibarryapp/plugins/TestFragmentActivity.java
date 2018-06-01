package com.baselibarryapp.plugins;

import android.support.v4.app.Fragment;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.pluginutil.factory.HostFactory;

public class TestFragmentActivity extends BaseActivity {

    /**
     * 注意：
     *
     * 如果一个插件是内置插件，那么这个插件的名字就是文件的前缀，比如：demo1.jar插件的名字就是demo1(host-gradle插件自动生成)，可以执行诸如RePlugin.fetchClassLoader("demo1")的操作；
     * 如果一个插件是外置插件，通过RePlugin.install("/sdcard/demo1.apk")安装的，则必须动态获取这个插件的名字来使用：
     * PluginInfo pluginInfo = RePlugin.install("/sdcard/demo1.apk");
     * RePlugin.preload(pluginInfo);//耗时
     * String name = pluginInfo != null ? pluginInfo.getName() : null;
     * ClassLoader classLoader = RePlugin.fetchClassLoader(name);
     */
    final String pluginName = "webview";
    final String fragementPackage = "plugin1.com.plugin1.fragments.PluginFragemnt";

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_fragment);
        try {
            Fragment fram = HostFactory.getPluginFragment(pluginName,
                    fragementPackage);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmetnLayout,fram)
                    .commit();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
