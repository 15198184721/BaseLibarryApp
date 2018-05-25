package com.baselibarryapp.plugins;

import android.content.Intent;
import android.view.View;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.logutil.Lg;
import com.qihoo360.replugin.RePlugin;

public class PluginsActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_plugins);
        findViewById(R.id.startPlugin).setOnClickListener(this);
        Lg.e("日志测试");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startPlugin:
                Intent it = new Intent("com.plugin1.main");
                boolean res = RePlugin.startActivity(this,it,"webview","");
                break;
        }
    }
}
