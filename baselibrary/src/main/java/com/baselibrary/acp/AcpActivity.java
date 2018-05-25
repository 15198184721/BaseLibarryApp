package com.baselibrary.acp;

import android.content.Intent;
import android.view.WindowManager;

import com.baselibrary.base.basecomponent.BaseActivity;

public class AcpActivity extends BaseActivity {

    @Override
    protected void initLayout() {
        //不接受触摸屏事件
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Acp.getInstance(this).requestPermissions(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Acp.getInstance(this).requestPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Acp.getInstance(this).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Acp.getInstance(this).onActivityResult(requestCode, resultCode, data);
    }
}
