package com.baselibrary.base.baseabstract;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.baselibrary.R;
import com.baselibrary.base.basecomponent.BaseApp;
import com.baselibrary.base.interfaces.IBaseActivity;
import com.baselibrary.dialog.DialogUtilImpl;
import com.baselibrary.dialog.IDialogUtil;
import com.baselibrary.http.HttpManager;
import com.baselibrary.http.body.interceptor.ProgressInterceptor;
import com.baselibrary.http.body.listener.FileUploadProgressListener;
import com.baselibrary.http.intefaces.IHttpManager;
import com.jet.sweettips.toast.SweetToast;
import com.jet.sweettips.util.SnackbarUtils;
import com.qihoo360.replugin.model.PluginInfo;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-22
 * Description
 *  基础Activity的抽象类，将大部分实现方法抽离。防止{@link com.baselibrary.base.basecomponent.BaseActivity}
 *  文件的太长
 * </pre>
 */
public abstract class AbstractBaseActivity extends AppCompatActivity
        implements IBaseActivity {
    //TODO 属性申明
    /** Toast默认的颜色 */
    private static final int toastDefaultBgColor = 0xAA000000;
    /** Dialog提示框的工具类 */
    private IDialogUtil dialogUtil;

    /**
     * 静态输出一个Toast提示框
     * @param msg
     */
    public static void Toast(String msg){
        Toast(msg, toastDefaultBgColor);
    }

    /**
     * 输出一个静态的Toast提示框(默认时间长度)
     * @param msg 显示内容
     * @param bgColor 背景颜色
     */
    public static void Toast(String msg,int bgColor){
        SweetToast.makeText(
                BaseApp.getInstan(),msg,1200)
                .backgroundColor(bgColor).show();
    }

    //TODO 抽象方法，这里定义了抽象方法。需要子类进行实现的
    /**
     * 初始化布局等(如：设置视图等)
     */
    protected abstract void initLayout();

    //TODO 公共的方法，从接口继承而来的方法实现-订阅了事件的方法
    @Subscribe
    @Override
    public void httpRequestProgress(ProgressInterceptor.NteworkProgress progress) {}

    @Subscribe
    @Override
    public void installPluginSucceed(PluginInfo pluginInfo) {}

    //TODO 公共的方法，从接口继承而来的方法实现
    @Override
    public IDialogUtil getDialogInterface() {
        return dialogUtil;
    }

    @Override
    public Handler getHandler() {
        return BaseApp.getInstan().getHandler();
    }

    @Override
    public IHttpManager getHttpManager(boolean isShowHttpLoading){
        return HttpManager.getInstan(
                isShowHttpLoading ? this : BaseApp.getInstan());
    }

    @Override
    public <T> T getHttpApiInteface(Class<T> httpApiInteface) {
        return HttpManager.getRetrofit().create(httpApiInteface);
    }

    @Override
    public void toast(View view) {
        toast(view,toastDefaultBgColor);
    }

    @Override
    public void toast(View view,int color) {
        SweetToast.makeText(view,1500)
                .backgroundColor(color).show();
    }

    @Override
    public void toast(String msg){
        toast(msg,toastDefaultBgColor);
    }

    @Override
    public void toast(String msg,final int color){
        SweetToast.makeText(this,msg,1500)
                .backgroundColor(color).show();
    }

    @Override
    public void toastTopShort(String msg,int color){
        View v = getWindow().getDecorView().findViewById(android.R.id.content);
        toastGravityLong(v,msg,color,Gravity.TOP);
    }

    @Override
    public void toastTopLong(String msg,int color){
        View v = getWindow().getDecorView().findViewById(android.R.id.content);
        toastGravityShort(v,msg,color,Gravity.TOP);
    }

    @Override
    public void toastGravityShort(View view,String msg,int color,int gravity){
        toastGravity(view,msg,color,gravity,false,"",null);
    }

    @Override
    public void toastGravityLong(View view,String msg,int color,int gravity){
        toastGravity(view,msg,color,gravity,true,"",null);
    }

    @Override
    public void toastGravity(View view,String msg,int color,int gravity,boolean isLong,String actionString,View.OnClickListener click){
        SnackbarUtils barUtil = null;
        if(isLong){
            barUtil = SnackbarUtils.Long(view,msg)
                    .gravityFrameLayout(gravity)
                    .anim(R.anim.push_down_in,R.anim.push_up_out)
                    .backColor(color);
            if(click != null){
                barUtil.setAction(actionString,click);
            }
            barUtil.show();
            return;
        }
        barUtil = SnackbarUtils.Short(view,msg)
                .gravityFrameLayout(gravity)
                .anim(R.anim.push_down_in,R.anim.push_up_out)
                .backColor(color);
        if(click != null){
            barUtil.setAction(actionString,click);
        }
        barUtil.show();
    }

    //TODO 网页字段创建的实现(来之于:IHttpFiedCreate接口方法)
    @Override
    public MultipartBody.Part createFormPartFile(@NonNull String fieldName, @NonNull String filePath) {
        return getHttpManager(false).createFormPartFile(fieldName,filePath);
    }

    @Override
    public MultipartBody.Part createFormPartFile(@NonNull String fieldName, @NonNull String filePath, FileUploadProgressListener uploadListener) {
        return getHttpManager(false).createFormPartFile(fieldName,filePath,uploadListener);
    }

    @Override
    public MultipartBody.Part createFormPartFile(@NonNull String fieldName, @NonNull String filePath, FileUploadProgressListener uploadListener, String fileName) {
        return getHttpManager(false).createFormPartFile(fieldName,filePath,uploadListener,fileName);
    }

    @Override
    public List<MultipartBody.Part> createFormFileList(@NonNull String fieldName, @NonNull String[] filePath, FileUploadProgressListener uploadListener) {
        return getHttpManager(false).createFormFileList(fieldName,filePath,uploadListener);
    }

    @Override
    public List<MultipartBody.Part> createFormFileList(@NonNull String[] fieldName, @NonNull String[] filePath, FileUploadProgressListener uploadListener) {
        return getHttpManager(false).createFormFileList(fieldName,filePath,uploadListener);
    }

    @Override
    public RequestBody createBody(@NonNull String content) {
        return getHttpManager(false).createBody(content);
    }

    @Override
    public RequestBody createBody(String MediaType, @NonNull String content) {
        return getHttpManager(false).createBody(MediaType,content);
    }

    @Override
    public RequestBody createBody(@NonNull File file) {
        return getHttpManager(false).createBody(file);
    }

    @Override
    public RequestBody createBody(String MediaType, @NonNull File file) {
        return getHttpManager(false).createBody(MediaType,file);
    }

    @Override
    public RequestBody createBody(@NonNull byte[] bytes) {
        return getHttpManager(false).createBody(bytes);
    }

    @Override
    public RequestBody createBody(String MediaType, @NonNull byte[] bytes) {
        return getHttpManager(false).createBody(MediaType,bytes);
    }

    @Override
    public RequestBody createBody(@NonNull byte[] bytes, int offset, int byteCount) {
        return getHttpManager(false).createBody(bytes,offset,byteCount);
    }

    @Override
    public RequestBody createBody(String MediaType, @NonNull byte[] bytes, int offset, int byteCount) {
        return getHttpManager(false).createBody(MediaType,bytes,offset,byteCount);
    }

    @Override
    public RequestBody createBody(@NonNull ByteString bString) {
        return getHttpManager(false).createBody(bString);
    }

    @Override
    public RequestBody createBody(String MediaType, @NonNull ByteString bString) {
        return getHttpManager(false).createBody(MediaType,bString);
    }

    @Override
    public void toActivity(Intent intent, int closeId) {

    }

    @Override
    public void toActivity(Class<?> toActivityClass, int closeId) {

    }

    @Override
    public void toActivityResult(Class<?> toActivityClass, int requestCode) {

    }

    @Override
    public void toActivityResult(Intent intent, int requestCode) {

    }

    //TODO 公共的方法，从父类继承而来的方法实现
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogUtil = new DialogUtilImpl(this);
        initLayout();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BaseApp.getInstan().registerBus(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BaseApp.getInstan().unregisterBus(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApp.getInstan().getRefWatcher().watch(this);
    }
}
