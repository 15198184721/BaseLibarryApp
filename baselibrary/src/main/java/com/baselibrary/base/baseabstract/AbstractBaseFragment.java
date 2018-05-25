package com.baselibrary.base.baseabstract;

import android.support.v4.app.Fragment;

import com.baselibrary.base.basecomponent.BaseApp;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public abstract class AbstractBaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        BaseApp.getInstan().getRefWatcher().watch(this);
    }
}
