package com.baselibrary.acp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hupei on 2016/4/26.
 */
public class Acp {

    private static Acp mInstance;
    private AcpManager mAcpManager;

    public static Acp getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Acp(context);
        }
        return mInstance;
    }

    private Acp(Context context) {
        mAcpManager = new AcpManager(context.getApplicationContext());
    }

    /**
     * 检查权限，检查程序是否拥有指定权限
     * @param permissions 需要检查的权限集合
     * @return list集合(没有获得权限的集合)
     */
    public List<String> checkPermission(String... permissions){
        List<String> errPermission = new ArrayList<>();
        if(permissions == null || permissions.length <= 0){
            return errPermission;//没有申请任何权限直接返回成功
        }else{
            return mAcpManager.checkSelfPermission(permissions);
        }
    }

    public void request(AcpOptions options, AcpListener acpListener) {
        if (options == null) new RuntimeException("AcpOptions is null...");
        if (acpListener == null) new RuntimeException("AcpListener is null...");
        mAcpManager.request(options, acpListener);
    }

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mAcpManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAcpManager.onActivityResult(requestCode, resultCode, data);
    }

    void requestPermissions(Activity activity) {
        mAcpManager.requestPermissions(activity);
    }
}
