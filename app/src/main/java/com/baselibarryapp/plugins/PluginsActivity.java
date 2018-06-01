package com.baselibarryapp.plugins;

import android.content.Intent;
import android.view.View;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.component.service.PluginServiceClient;
import com.qihoo360.replugin.model.PluginInfo;

import butterknife.OnClick;

public class PluginsActivity extends BaseActivity {

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_plugins);
        PluginInfo inof = RePlugin.getPluginInfo("webview");
        if (inof != null) {
            RePlugin.preload(inof);
        }
    }

    @OnClick({R.id.startPlugin, R.id.startPlugin1, R.id.startPlugin2, R.id.startPlugin3
            , R.id.startPlugin4,R.id.startPlugin5})
    void onClick(View v) {
        //注意，下发sdk目前(1.2版本,后续版本可能支持的)还不支持别名时不存在下载
        // 所以需要全名才可以，如果用别名可能导致下载插件失败
        Intent it = null;
        switch (v.getId()) {
            case R.id.startPlugin:
                it = RePlugin.createIntent(
                        "plugin1.com.plugin1", "plugin1.com.plugin1.MainActivity");
                RePlugin.startActivity(this, it);
                break;
            case R.id.startPlugin1:
                it = RePlugin.createIntent(
                        "webview", "plugin1.com.plugin1.WebViewService");
                PluginServiceClient.startService(this, it);
                break;
            case R.id.startPlugin2:
                it = RePlugin.createIntent(
                        "webview", "plugin1.com.plugin1.WebViewService");
                PluginServiceClient.stopService(this, it);
                break;
            case R.id.startPlugin3:
                it = new Intent("com.plugin1.receiver");
                RePlugin.fetchContext("plugin1.com.plugin1")
                        .sendBroadcast(it);
                break;
            case R.id.startPlugin4:
                startActivity(new Intent(this, TestFragmentActivity.class));
                break;
            case R.id.startPlugin5:
                startActivity(new Intent(this, TestXmlFragmentActivity.class));
                break;
        }
    }
}
