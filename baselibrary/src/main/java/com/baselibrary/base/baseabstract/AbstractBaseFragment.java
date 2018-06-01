package com.baselibrary.base.baseabstract;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baselibrary.R;
import com.baselibrary.acp.Acp;
import com.baselibrary.acp.AcpListener;
import com.baselibrary.acp.AcpOptions;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.base.interfaces.IBaseActivity;
import com.baselibrary.base.interfaces.IBaseFragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public abstract class AbstractBaseFragment extends Fragment
    implements IBaseFragment,AcpListener{

    // 缓存布局对象
    protected View rootView;

    /* 当前Fragment状态变化的总次数 */
    private int visibleChangeCount = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //缓存fragment，先判断是否加载过
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            BaseApp.getInstan().registerBus(this);
            rootView = inflater.inflate(getLayoutId(),container,false);
            ButterKnife.bind(this,rootView);
            initBaseData();
        }
        return rootView;
    }

    @Override
    public void onFragmentVisibleChange(boolean isVisible) {}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) {
            return;
        }else{
            initBaseData();
        }
        if(visibleChangeCount >= 0)
            visibleChangeCount++;
        onFragmentVisibleChange(isVisibleToUser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApp.getInstan().unregisterBus(this);
        BaseApp.getInstan().getRefWatcher().watch(this);
    }

    /*----------------------EventBus事件订阅开始-----------------*/


    /*----------------------EventBus事件订阅结束-----------------*/

    @Override
    public BaseActivity getThisActivity() {
        if(getActivity() instanceof BaseActivity){
            return (BaseActivity) getActivity();
        }
        throw new NullPointerException("关联Activity非BaseActivity无法使用");
    }

    @Override
    public String[] getPermissions(){
        return new String[0];
    }

    @Override
    public void onGranted() {
        //延时切换动画相同时间长度 /2 的毫秒再去加载数据。保证页面切换的顺滑度
        BaseApp.getInstan().getHandler().postDelayed(()->{
            initData(isInitData());
        }, 1000);
    }

    @Override
    public void onDenied(List<String> permissions) {
        BaseActivity.Toast((String) getText(R.string.baseactivity_permission));
    }

    @Override
    public void requestPermiss(String[] permiss, AcpListener lis) {
        requestPermiss(permiss, "", lis);
    }

    @Override
    public void requestPermiss(String[] permiss, String textMsg, AcpListener lis) {
        if (lis == null)
            lis = new AcpListener() {
                @Override
                public void onGranted() {
                }

                @Override
                public void onDenied(List<String> permissions) {
                    BaseActivity.Toast("未获得授权");
                }
            };
        if (getPermissions() != null && getPermissions().length > 0) {
            Acp.getInstance(getActivity()).request(
                    new AcpOptions.Builder()
                            .setRationalMessage(textMsg)//设置第一个对话框的内容(没有表示不显示)
                            .setPermissions(permiss)//需要的申请的权限
                            .build(), lis);
        } else {
            lis.onGranted();//直接成功
        }
    }

    @Override
    public List<String> getCheckPermission() {
        return Acp.getInstance(getContext()).checkPermission(getPermissions());
    }

    @Override
    public List<String> getCheckPermission(String[] paramsList) {
        return Acp.getInstance(getContext()).checkPermission(paramsList);
    }

    @Override
    public void toActivity(Intent intent, int closeId) {
        if (getActivity() instanceof IBaseActivity) {
            ((IBaseActivity) getActivity()).toActivity(intent, 0);
        }
    }

    @Override
    public void toActivity(Class<?> toActivityClass, int closeId) {
        if (getActivity() instanceof IBaseActivity) {
            ((IBaseActivity) getActivity()).toActivity(toActivityClass, 0);
        }
    }

    @Override
    public void toActivityResult(Class<?> toActivityClass, int requestCode) {
        if (getActivity() instanceof IBaseActivity) {
            ((IBaseActivity) getActivity()).toActivityResult(toActivityClass, requestCode);
        }
    }

    @Override
    public void toActivityResult(Intent intent, int requestCode) {
        if (getActivity() instanceof IBaseActivity) {
            ((IBaseActivity) getActivity()).toActivityResult(intent, requestCode);
        }
    }

    /**************************************************************
     *  私有方法
     *************************************************************/
    //初始化数据方法
    private void initBaseData() {
        if (visibleChangeCount == -1 && getUserVisibleHint()) {
            visibleChangeCount = 0;
            requestPermiss(getPermissions(), this);//申请权限，如果成功则初始化加载数据
            onFragmentVisibleChange(true);
        }
    }

    //检查是否为第一次加载数据
    private boolean isInitData() {
        return visibleChangeCount == 0;
    }
}
