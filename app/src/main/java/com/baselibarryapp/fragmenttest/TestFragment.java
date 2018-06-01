package com.baselibarryapp.fragmenttest;

import android.widget.Button;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseFragment;

import butterknife.BindView;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-31
 * Description
 * </pre>
 */
public class TestFragment extends BaseFragment {

    @BindView(R.id.button)
    Button but;

    @Override
    public void initData(boolean isInit) {
        but.setOnClickListener(v->{
            getThisActivity().toast("单击操作。。。。。。");
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }
}
