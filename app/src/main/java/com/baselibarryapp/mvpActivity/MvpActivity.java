package com.baselibarryapp.mvpActivity;

import android.view.View;

import com.baselibarryapp.R;
import com.baselibarryapp.data.IpBeanRepository;
import com.baselibrary.base.basecomponent.basemvp.BaseMvpActivity;

import butterknife.OnClick;

public class MvpActivity extends BaseMvpActivity<MvpContract.MvpPresenter>
    implements MvpContract.MvpView{

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_mvp);
        //创建并且关联
        new MvpPresenter(this,new IpBeanRepository());
    }

    @Override
    public void loadToast() {
        toast("加载数据成功(mvp)");
    }

    @OnClick({R.id.mvpBut})
    void clk(View v){
        switch (v.getId()){
            case R.id.mvpBut:
                mPresenter.load();
                break;
        }
    }
}
