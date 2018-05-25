package com.baselibarryapp.toast;

import android.view.Gravity;
import android.view.View;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.jet.sweettips.constant.Constant;

import butterknife.OnClick;

public class ToastActivity extends BaseActivity {

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_toast);
    }

    @OnClick({R.id.toast1,R.id.toast2,R.id.toast3,R.id.toast4,R.id.toast5,R.id.toast6})
    void click(View v){
        switch (v.getId()){
            case R.id.toast1:
                toast("一个普通的toast");
                break;
            case R.id.toast2:
                toast("一个普通的toast，自定义颜色", Constant.color_info);
                break;
            case R.id.toast3:
                toastTopShort("一个突破那个的toast",Constant.color_info);
                break;
            case R.id.toast4:
                toastTopLong("一个突破那个的toast",Constant.color_info);
                break;
            case R.id.toast5:
                View vw = View.inflate(this,R.layout.item_btn_bottomalert,null);
                toast(vw,Constant.color_confirm);
                break;
            case R.id.toast6:
                //弹出的消息按钮的字体颜色为: ?arrt:colorAccent
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                toastGravity(rootView, "提示", Constant.color_info
                        , Gravity.TOP, true, "单击",clk->{
                            toast("单击了");
                        });
                break;
        }
    }
}
