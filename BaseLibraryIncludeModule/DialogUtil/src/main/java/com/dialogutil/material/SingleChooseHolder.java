package com.dialogutil.material;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.CheckedTextView;

import com.dialogutil.R;
import com.dialogutil.adapter.SuperLvAdapter;
import com.dialogutil.adapter.SuperLvHolder;
import com.dialogutil.config.ChooseBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/19.
 */

public class SingleChooseHolder extends SuperLvHolder<ChooseBean> {
    CheckedTextView checkedTextView;
    public SingleChooseHolder(Context context) {
        super(context);
    }

    @Override
    protected void findViews() {
        checkedTextView = (CheckedTextView) rootView.findViewById(R.id.checkTxt);
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.item_md_choose_single;
    }

    @Override
    public void assingDatasAndEvents(Context context, @Nullable ChooseBean bean) {


    }

    @Override
    public void assingDatasAndEvents(Context context, final ChooseBean bean, final int position, boolean isLast, boolean isListViewFling, final List datas, final SuperLvAdapter superAdapter) {
        super.assingDatasAndEvents(context, bean, position, isLast, isListViewFling, datas, superAdapter);
        //checkedTextView.setChecked(bean.choosen);
        checkedTextView.setText(bean.txt);

    }
}
