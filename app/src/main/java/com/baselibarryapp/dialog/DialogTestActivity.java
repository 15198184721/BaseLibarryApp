package com.baselibarryapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baselibarryapp.R;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.logutil.Lg;
import com.dialogutil.adapter.SuperRcvAdapter;
import com.dialogutil.adapter.SuperRcvHolder;
import com.dialogutil.bottomsheet.BottomSheetBean;
import com.dialogutil.interfaces.MyDialogListener;
import com.dialogutil.interfaces.MyItemDialogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;

public class DialogTestActivity extends BaseActivity {

    List<BottomSheetBean> datas2 = new ArrayList<>();

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_dialog_test);
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"1"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"222"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"333333"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"444"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"55"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"666"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"7777"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"fddsf"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"67gfhfg"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"oooooppp"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"7777"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"8"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"9"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"10"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"11"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"12"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"13"));
        datas2.add(new BottomSheetBean(R.mipmap.ic_launcher,"14"));
    }

    @OnClick({R.id.loading, R.id.loadingMD, R.id.buildProgress, R.id.buildProgressC,
            R.id.buildMdAlter,R.id.ios_bottom_sheet,R.id.btn_md_bs,R.id.bottomSheetLv,R.id.bottomSheetGv,
            R.id.btn_singlechoose,R.id.btn_muluchoose})
    void click(View v) {
        switch (v.getId()) {
            case R.id.loading:
                final Dialog dl = getDialogInterface().loading("加载中").show();
                getHandler().postDelayed(() -> {
                    getDialogInterface().updateLoadingMsg(dl, "update:" + new Random().nextInt(100));
                }, 3000);
                getHandler().postDelayed(() -> {
                    getDialogInterface().dismissLoading();
                }, 5000);
                break;
            case R.id.loadingMD:
                final Dialog dialog1 = getDialogInterface().loadingMD("加载中").show();
                getHandler().postDelayed(() -> {
                    getDialogInterface().updateLoadingMsg(dialog1, "update:" + new Random().nextInt(100));
                }, 3000);
                getHandler().postDelayed(() -> {
                    getDialogInterface().dismissLoading();
                }, 5000);
                break;
            case R.id.buildProgress:
                final ProgressDialog dialog2 = (ProgressDialog) getDialogInterface().progress(
                        "初始化...", true).show();
                getHandler().postDelayed(() -> {
                    getDialogInterface().updateProgress(
                            dialog2, "更新的数据", 25, 100, true);
                }, 3000);
                break;
            case R.id.buildProgressC:
                final ProgressDialog dialog3 = (ProgressDialog) getDialogInterface().progress(
                        "下载中...", false).show();
                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    int a = 0;

                    @Override
                    public void run() {
                        a += 10;
                        getDialogInterface().updateProgress(dialog3, "progress", a, 100, false);
                        if (a > 100) {
                            timer2.cancel();
                            getDialogInterface().dismissDialog(dialog3);
                        }
                    }
                }, 500, 500);
                break;
            case R.id.buildMdAlter:
                final Dialog dialog4 = getDialogInterface().mdAlert(
                        "标题", "内容内容", new MyDialogListener() {
                            @Override
                            public void onFirst() {
                                Toast.makeText(
                                        DialogTestActivity.this, "完成，右边的", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSecond() {
                                Toast.makeText(
                                        DialogTestActivity.this, "取消，中间的", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onThird() {
                                Toast.makeText(
                                        DialogTestActivity.this, "忽略，最左边的", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setBtnSize(29)//按钮字体
//                        .setForceHeightPercent(0.75f)//窗口占用屏幕的高度
                        //.setForceWidthPercent(0.90f)//窗口站屏幕的宽度
                        .setBtnText("i", "b", "3")//设置按钮
                        //.setBtnText("i")//设置一个按钮
                        //设置顺序按钮的颜色
                        .setBtnColor(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.dialogutil_text_black)
                        .show();
                break;
            case R.id.ios_bottom_sheet:
                final List<String> strings = new ArrayList<>();
                strings.add("1111");
                strings.add("2222");
                strings.add("aaaaaaaaaa");
                strings.add("4444");
                strings.add("5555");
                Dialog dialog5 = getDialogInterface().bottomIosItemDialog(strings, "取消", new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        Toast.makeText(DialogTestActivity.this,text,Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
                case R.id.btn_md_bs:
                String[] words3 = new String[]{"12","78","45","89","88","00"};
                List<String> datas = Arrays.asList(words3);
                // final BottomSheetDialog dialog = new BottomSheetDialog(this);
                RecyclerView recyclerView = new RecyclerView(this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                SuperRcvAdapter adapter = new SuperRcvAdapter(this) {
                    @Override
                    protected SuperRcvHolder generateCoustomViewHolder(int viewType) {
                        return new SuperRcvHolder<String>(inflate(R.layout.item_bottomsheet_lv)) {
                            TextView mButton;
                            @Override
                            public void assignDatasAndEvents(Activity context, final String data) {
                                if (mButton==null){
                                    mButton = itemView.findViewById(R.id.tv_msg);
                                }
                                mButton.setText(data);
                                mButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(DialogTestActivity.this,""+data,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };
                    }
                };
                recyclerView.setAdapter(adapter);
                adapter.addAll(datas);
                adapter.addAll(datas);
                adapter.addAll(datas);
                getDialogInterface().bottomCustomSheet(recyclerView).show();
                break;
            case R.id.bottomSheetLv:
                getDialogInterface().bottomSheetLv("拉出来溜溜", datas2, "底部的文字", new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        Toast.makeText(DialogTestActivity.this,text+"---"+position,Toast.LENGTH_SHORT).show();
                    }
                }).setBottomSheetDialogMaxHeightPercent(0.3f)
                        .show();
                break;
            case R.id.bottomSheetGv:
                getDialogInterface().bottomSheetGv("标题标题", datas2, "底部的文字", 4,new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        Toast.makeText(DialogTestActivity.this,text+"---"+position,Toast.LENGTH_SHORT).show();
                    }
                }).setHasBehaviour(false)//控制是否采用底部拉出的方式(默认true，采用)
                        .show();
                break;
            case R.id.btn_singlechoose:
                String[] words2 = new String[]{"12","78","45","89","88","00"};
                getDialogInterface().mdSingleChoose(
                        "标题", 2, words2, new MyItemDialogListener() {
                            @Override
                            public void onItemClick(CharSequence text, int position) {
                                Toast.makeText(DialogTestActivity.this,text+"---"+position,Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.btn_muluchoose:
                String[] words4 = new String[]{"12","78","45","89","88","00"};
                List<Integer> sele = new ArrayList(){
                    {add(1);add(3);}
                };
                getDialogInterface().mdMultiChoose("标题标题", words4, sele, new MyDialogListener() {
                    @Override
                    public void onFirst() {}
                    @Override
                    public void onSecond() {}
                    @Override
                    public void onChoosen(List<Integer> selectedIndex, List<CharSequence> selectedStrs, boolean[] states) {
                        super.onChoosen(selectedIndex, selectedStrs, states);
                        Lg.d(states);
                        Lg.d(selectedIndex);
                        Lg.d(selectedStrs);
                    }
                }).show();
                break;
        }
    }
}
