package com.baselibrary.acp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hupei on 2016/4/26.
 */
class AcpManager {
    private static final int REQUEST_CODE_PERMISSION = 0x38;
    private static final int REQUEST_CODE_SETTING = 0x39;
    private Context mContext;
    private Activity mActivity;
    private AcpService mService;
    private AcpOptions mOptions;
    private AcpListener mCallback;
    private final List<String> mDeniedPermissions = new LinkedList<>();
    //错误的权限(检查权限后没有被授权的权限)
    private final List<String> errPermissions = new LinkedList<>();

    AcpManager(Context context) {
        mContext = context;
        mService = new AcpService();
    }


    synchronized void request(AcpOptions options, AcpListener acpListener) {
        mCallback = acpListener;
        mOptions = options;
        checkSelfPermission();
    }

    /**
     * 检查权限，检查程序是否拥有指定权限
     * @param permiss 需要检查的权限
     * @return
     */
    public synchronized List<String> checkSelfPermission(String[] permiss) {
        errPermissions.clear();
        for (String permission : permiss) {
            int permissionCheck = mService.checkSelfPermission(mContext, permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                errPermissions.add(permission);
            }
        }
        return errPermissions;
    }

    private synchronized void checkSelfPermission() {
        mDeniedPermissions.clear();
        String[] permissions = mOptions.getPermissions();
        for (String permission : permissions) {
            int permissionCheck = mService.checkSelfPermission(mContext, permission);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                mDeniedPermissions.add(permission);
            }
        }
        if (mDeniedPermissions.isEmpty()) {
            mCallback.onGranted();
            onDestroy();
            return;
        }
        startAcpActivity();
    }

    private synchronized void startAcpActivity() {
        Intent intent = new Intent(mContext, AcpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    synchronized void requestPermissions(Activity activity) {
        mActivity = activity;
        boolean shouldShowRational = false;
        for (String permission : mDeniedPermissions) {
            shouldShowRational = shouldShowRational || mService.shouldShowRequestPermissionRationale(mActivity, permission);
        }
        String[] permissions = mDeniedPermissions.toArray(new String[mDeniedPermissions.size()]);
        if (shouldShowRational) showRationalDialog(permissions);
        else requestPermissions(permissions);
    }

    private synchronized void showRationalDialog(final String[] permissions) {
        //如果为空字符串。那么第一个提示窗口将不会提示
        if(mOptions.getRationalMessage() != null &&
                !"".equals(mOptions.getRationalMessage())) {
            new AlertDialog.Builder(mActivity)
                    .setMessage(mOptions.getRationalMessage())
                    .setPositiveButton(mOptions.getRationalBtnText(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(permissions);
                        }
                    }).show();
        }else{
            requestPermissions(permissions);
        }
    }

    private synchronized void requestPermissions(String[] permissions) {
        mService.requestPermissions(mActivity, permissions, REQUEST_CODE_PERMISSION);
    }

    synchronized void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                LinkedList<String> grantedPermissions = new LinkedList<>();
                LinkedList<String> deniedPermissions = new LinkedList<>();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        grantedPermissions.add(permission);
                    else deniedPermissions.add(permission);
                }
                if (!grantedPermissions.isEmpty() && deniedPermissions.isEmpty()) {
                    mCallback.onGranted();
                    onDestroy();
                } else if (!deniedPermissions.isEmpty()) showDeniedDialog(deniedPermissions);
                break;
        }
    }

    private synchronized void showDeniedDialog(final List<String> permissions) {
        new AlertDialog.Builder(mActivity)
                .setMessage(mOptions.getDeniedMessage())
                .setCancelable(false)
                .setNegativeButton(mOptions.getDeniedCloseBtn(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mCallback.onDenied(permissions);
                        onDestroy();
                    }
                })
                .setPositiveButton(mOptions.getDeniedSettingBtn(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSetting();
                    }
                }).show();
    }

    private void onDestroy() {
        if (mActivity != null) mActivity.finish();
    }

    private void startSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.parse("package:" + mActivity.getPackageName()));
            mActivity.startActivityForResult(intent, REQUEST_CODE_SETTING);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            mActivity.startActivityForResult(intent, REQUEST_CODE_SETTING);
        }
    }

    synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mCallback == null || mOptions == null
                || requestCode != REQUEST_CODE_SETTING) {
            onDestroy();
            return;
        }
        checkSelfPermission();
    }
}
