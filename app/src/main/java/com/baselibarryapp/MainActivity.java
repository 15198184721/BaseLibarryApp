package com.baselibarryapp;

import android.content.Intent;
import android.view.View;

import com.baselibarryapp.dialog.DialogTestActivity;
import com.baselibarryapp.fragmenttest.FragmentTestActivity;
import com.baselibarryapp.http.HttpActivity;
import com.baselibarryapp.plugins.PluginsActivity;
import com.baselibarryapp.toast.ToastActivity;
import com.baselibrary.base.basecomponent.BaseActivity;

import butterknife.OnClick;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-21
 * Description
 *
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class MainActivity extends BaseActivity{

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @OnClick({R.id.pluginsTest,R.id.dialogTest,R.id.httpTest,R.id.toastTest
            ,R.id.fragmentTest})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.pluginsTest:
                startActivity(new Intent(this, PluginsActivity.class));
                break;
            case R.id.dialogTest:
                startActivity(new Intent(this, DialogTestActivity.class));
                break;
            case R.id.httpTest:
                startActivity(new Intent(this, HttpActivity.class));
                break;
            case R.id.toastTest:
//                Toast("提示消息");
                startActivity(new Intent(this, ToastActivity.class));
                break;
            case R.id.fragmentTest:
                startActivity(new Intent(this, FragmentTestActivity.class));
                break;
        }
    }
}
