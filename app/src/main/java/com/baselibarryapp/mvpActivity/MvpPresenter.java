package com.baselibarryapp.mvpActivity;

import com.baselibarryapp.data.IpBeanRepository;
import com.baselibrary.logutil.Lg;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-4
 * Description
 * </pre>
 */
public class MvpPresenter extends MvpContract.MvpPresenter {

    public MvpPresenter(MvpContract.MvpView mView, IpBeanRepository mRepositor) {
        super(mView, mRepositor);
    }

    @Override
    void load() {
        mRepository.getModuleList((soucessCall) ->{
            Lg.e("mvp获取数据成功:"+soucessCall.size());
            mView.loadToast();
        });
    }
}
