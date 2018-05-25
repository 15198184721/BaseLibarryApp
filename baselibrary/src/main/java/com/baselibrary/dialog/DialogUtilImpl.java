package com.baselibrary.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.dialogutil.DialogAssigner;
import com.dialogutil.StyledDialog;
import com.dialogutil.config.ConfigBean;
import com.dialogutil.interfaces.MyDialogListener;
import com.dialogutil.interfaces.MyItemDialogListener;

import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * </pre>
 */
public class DialogUtilImpl implements IDialogUtil{

    Activity act;

    public DialogUtilImpl(Activity activity) {
        act = activity;
    }

    @Override
    public DialogAssigner getStyleDialog() {
        return DialogAssigner.getInstance();
    }

    @Override
    public ConfigBean loading(String msg){
        return loading(msg,true,false);
    }

    @Override
    public ConfigBean loading(String msg,boolean cancelable,boolean outsideTouchable){
        ConfigBean cb = DialogAssigner
                .getInstance()
                .assignLoading(act, msg, cancelable, outsideTouchable).setActivity(act);
        return cb;
    }

    @Override
    public ConfigBean loadingMD(String msg){
        return loadingMD(msg,true,false);
    }

    @Override
    public ConfigBean loadingMD(String msg,boolean cancelable,boolean outsideTouchable){
        ConfigBean cb = DialogAssigner
                .getInstance()
                .assignMdLoading(act, msg, cancelable, outsideTouchable).setActivity(act);
        return cb;
    }

    @Override
    public ConfigBean progress(CharSequence msg, boolean isHorizontal) {
        ConfigBean cb = DialogAssigner.getInstance().assignProgress(act, msg, isHorizontal);
        return cb;
    }

    @Override
    public ConfigBean mdAlert(String title, String msg, MyDialogListener listener) {
        return DialogAssigner.getInstance().assignMdAlert(act,title,msg,listener);
    }

    @Override
    public ConfigBean bottomIosItemDialog(List<String> items,String bottonText,MyItemDialogListener listener){
        return DialogAssigner.getInstance().assignBottomItemDialog(
                act,items,bottonText,listener);
    }

    @Override
    public ConfigBean bottomCustomSheet(View customBottonSheetView) {
        return DialogAssigner.getInstance().assignCustomBottomSheet(act,customBottonSheetView);
    }

    @Override
    public ConfigBean bottomSheetLv(CharSequence title, List datas, CharSequence bottomTxt, MyItemDialogListener listener) {
        return DialogAssigner.getInstance().assignBottomSheetLv(act, title, datas, bottomTxt, listener);
    }

    @Override
    public ConfigBean bottomSheetGv(CharSequence title, List datas, CharSequence bottomTxt, int columnsNum,MyItemDialogListener listener) {
        return DialogAssigner.getInstance().assignBottomSheetGv(act,title,datas,bottomTxt,columnsNum,listener);
    }

    @Override
    public ConfigBean mdSingleChoose(CharSequence title, int defaultChosen, CharSequence[] words, MyItemDialogListener listener) {
        return DialogAssigner.getInstance().assignMdSingleChoose(
                act,title,defaultChosen,words,listener);
    }

    @Override
    public ConfigBean mdMultiChoose(CharSequence title, CharSequence[] words, List<Integer> selectedIndexs, MyDialogListener listener) {
        return DialogAssigner.getInstance().assignMdMultiChoose(
                act,title,words,selectedIndexs,listener);
    }

    @Override
    public ConfigBean updateLoadingMsg(ConfigBean cb,String msg){
        updateLoadingMsg(cb.dialog == null ? cb.dialog : cb.alertDialog,msg);
        return cb;
    }

    @Override
    public Dialog updateLoadingMsg(Dialog dialog, String msg) {
        try {
            StyledDialog.updateLoadingMsg(msg,dialog);
        }catch (Exception e){}
        return dialog;
    }

    @Override
    public ConfigBean updateProgress(ConfigBean cb,String msg,int progress,int max,boolean isHorizontal) {
        try {
            Dialog dio = cb.dialog == null ? cb.dialog : cb.alertDialog;
            updateProgress(dio,msg,progress,max,isHorizontal);
        }catch (Exception e){}
        return cb;
    }

    @Override
    public Dialog updateProgress(Dialog dialog,String msg,int progress, int max, boolean isHorizontal) {
        try {
            StyledDialog.updateProgress(dialog,progress,max,msg,isHorizontal);
        }catch (Exception e){}
        return dialog;
    }

    @Override
    public void dismissLoading(){
        StyledDialog.dismissLoading(act);
    }

    @Override
    public void dismissDialog(Dialog... dialogs){
        StyledDialog.dismiss(dialogs);
    }
}
