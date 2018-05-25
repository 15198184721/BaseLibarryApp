package com.baselibrary.dialog;

import android.app.Dialog;
import android.view.View;

import com.dialogutil.DialogAssigner;
import com.dialogutil.config.ConfigBean;
import com.dialogutil.interfaces.MyDialogListener;
import com.dialogutil.interfaces.MyItemDialogListener;

import java.util.List;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 * 对话框管理接口
 * 对话框来来自开源项目:https://github.com/hss01248/DialogUtil
 * 内部进行了少量改动
 * </pre>
 */
public interface IDialogUtil {

    /**
     * 获取Dialog对话框管理对象
     * 获取样式对话框管理对象自己创建
     * @return
     */
    DialogAssigner getStyleDialog();

    //TODO 创建Dialog
    /**
     * 创建一个loading的加载框
     * @param msg
     */
    ConfigBean loading(String msg);

    /**
     * 创建一个Loading类型的加载框
     * @param msg 消息
     * @param cancelable 是否允许取消
     * @param outsideTouchable 是否允许手指触摸其他地方取消
     * @return
     */
    ConfigBean loading(String msg,boolean cancelable,boolean outsideTouchable);

    /**
     * 创建一个MD类型的loading的加载框
     * @param msg
     */
    ConfigBean loadingMD(String msg);

    /**
     * 创建一个Loading MD类型加载框
     * @param msg 消息
     * @param cancelable 是否允许取消
     * @param outsideTouchable 是否允许手指触摸其他地方取消
     * @return
     */
    ConfigBean loadingMD(String msg,boolean cancelable,boolean outsideTouchable);

    /**
     * 创建进度框，创建一个默认的带有进度的Dialog框
     * @param msg 显示消息
     * @param isHorizontal 是否为横向显示(T:横向，F:纵向)
     * @return
     */
    ConfigBean progress(CharSequence msg, boolean isHorizontal);

    /**
     * 创建一个MD类型的提示对话框，可以是一个按钮、2个按钮、3个按钮的
     * 根据返回对象的{@link ConfigBean#setBtnText(CharSequence)}个数决定按钮数量
     * 根据返回对象的{@link ConfigBean#setBtnColor(int, int, int)}顺序设置按钮颜色
     * 也可单独设置:详情参见demo或者ConfigBean类
     * @param title
     * @param msg
     * @param listener
     * @return
     */
    ConfigBean mdAlert(String title, String msg, MyDialogListener listener);

    /**
     * 底部item项选择的对话框，类似于头像选择
     * @param items 选项的列表数据集合
     * @param bottonText 最底下的项文本
     * @param listener 操作的监听
     * @return
     */
    ConfigBean bottomIosItemDialog(List<String> items, String bottonText, MyItemDialogListener listener);

    /**
     * 底部的listview列表类型的提示框
     * @param title 标题
     * @param datas 列表数据集合
     * @param bottomTxt 底部的文字
     * @param listener 监听
     * @return
     */
    ConfigBean bottomSheetLv(CharSequence title, List<?> datas, CharSequence bottomTxt, MyItemDialogListener listener);

    /**
     * 底部的GridView网格样式列表类型的提示框
     * @param title 标题
     * @param datas 列表数据集合
     * @param bottomTxt 底部的文字
     * @param columnsNum 列数
     * @param listener 监听
     * @return
     */
    ConfigBean bottomSheetGv(CharSequence title, List<?> datas, CharSequence bottomTxt,int columnsNum, MyItemDialogListener listener);

    /**
     * 创建一个单选提示框
     * @param title 标题
     * @param defaultChosen 默认选中的项
     * @param words 选项集合
     * @param listener 操作监听
     * @return
     */
    ConfigBean mdSingleChoose(CharSequence title, int defaultChosen, CharSequence[] words, MyItemDialogListener listener);

    /**
     * 创建一个多项选择的对话框
     * @param title 标题
     * @param selectedIndexs 默认选中项集合
     * @param words 选项集合
     * @param listener 操作监听
     * @return
     */
    ConfigBean mdMultiChoose(CharSequence title, CharSequence[] words, List<Integer> selectedIndexs, MyDialogListener listener);

    /**
     * 创建一个自定义的MD风格的底部提示框，可以是列表或者其他的自定义视图
     * @param customBottonSheetView 之定义的视图
     * @return
     */
    ConfigBean bottomCustomSheet(View customBottonSheetView);

    //TODO 更新Loading和进度框数据
    /**
     * 更新Loading对话框的文本信息
     * @param cb 创建对话框返回的ConfigBean对象
     * @param msg 更新显示的消息
     * @return
     */
    ConfigBean updateLoadingMsg(ConfigBean cb,String msg);

    /**
     * 更新Loading对话框的文本信息
     * @param dialog show方法返回的Dialog对象
     * @param msg 更新显示的消息
     * @return
     */
    Dialog updateLoadingMsg(Dialog dialog, String msg);

    /**
     * 这个方法是更新进度提示框的文本信息
     * @param cb 创建对话框返回的ConfigBean
     * @param msg 更新的消息，如果是转圈圈,会将msg变成'msg:78%'的形式.如果是水平,msg不起作用
     * @param progress 当前的进度
     * @param max 最大进度
     * @param isHorizontal 是水平线状,还是转圈圈(T:水平，F:圆圈)
     * @return
     */
    ConfigBean updateProgress(ConfigBean cb,String msg,int progress,int max,boolean isHorizontal);

    /**
     * 这个方法是更新进度提示框的文本信息
     * @param dialog show方法返回的Dialog对象
     * @param msg 更新的消息，如果是转圈圈,会将msg变成'msg:78%'的形式.如果是水平,msg不起作用
     * @param progress 当前的进度
     * @param max 最大进度
     * @param isHorizontal 是水平线状,还是转圈圈(T:水平，F:圆圈)
     * @return
     */
    Dialog updateProgress(Dialog dialog,String msg,int progress,int max,boolean isHorizontal);

    //TODO 关闭 Dialog
    /**
     * 关闭Loading类型提示框的方法
     */
    void dismissLoading();

    /**
     * 关闭其他类型的Dialog
     * @param dialogs
     */
    void dismissDialog(Dialog... dialogs);

}
