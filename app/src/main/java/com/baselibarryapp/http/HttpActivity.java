package com.baselibarryapp.http;

import android.view.View;
import android.widget.Toast;

import com.baselibarryapp.R;
import com.baselibarryapp.api.HttpApi;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.http.body.bean.FileUploadTag;
import com.baselibrary.http.body.interceptor.ProgressInterceptor;
import com.baselibrary.http.intefaces.IHttpManager;
import com.baselibrary.logutil.Lg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;

public class HttpActivity extends BaseActivity {

    //上传的文件路径
    String filePath = "/storage/emulated/0/360llq.apk";
    IHttpManager httpM = null;
//    String filePath = "/storage/emulated/0/hword.tmp";

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_http);
    }

    @OnClick({R.id.httpGet,R.id.httpPost,R.id.updownfile1,R.id.updownfile2,R.id.updownfile3
    ,R.id.downFile,R.id.downFile2})
    void click(View v){
        switch (v.getId()){
            case R.id.httpGet:
                httpGet();
                break;
            case R.id.httpPost:
                httpPost();
                break;
            case R.id.updownfile1:
                httpUpdownFile1();
                break;
            case R.id.updownfile2:
                httpUpdownFile2();
                break;
            case R.id.updownfile3:
                httpUpdownFile3();
                break;
            case R.id.downFile:
                downFile();
                break;
            case R.id.downFile2:
                downFile2();
                break;
        }
    }

    //http get请求
    private void httpGet(){
        getHttpManager(true).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).getIp("json","218.4.255.255"),(s -> {
                    toast("获取数据成功");
                }));
    }

    //post请求
    private void httpPost(){
        getHttpManager(true).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).getSo("中国"),(s -> {
                    Lg.d(s);
                    toast("获取数据成功");
                }));
    }

    //上传单个文件(服务器是本地的 所以只是本地测试通过的)
    private void httpUpdownFile1(){
        httpM = getHttpManager(true);
        MultipartBody.Part par = createFormPartFile(
                "aaa", filePath, (FileUploadTag tag, long currentBytesCount, long totalBytesCount) -> {
                    httpM.updateLoadingMsg("上传文件:"+tag.getProgress()+"%");
        });
        RequestBody reqbody = createBody("《这个是RequestBody参数的值》");
        Subscription sub = httpM.request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).updownFile(
                        par,reqbody,"《这个是表单的文字字段内容》"),(s -> {
                    Toast.makeText(getApplicationContext(),"上传文件成功",Toast.LENGTH_SHORT).show();
                }));
    }

    //上传多个文件(服务器是本地的 所以只是本地测试通过的)
    private void httpUpdownFile2(){
        List<MultipartBody.Part> parList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            parList.add(createFormPartFile("aaa"+i,filePath,
                    (FileUploadTag tag, long currentBytesCount, long totalBytesCount) -> {
                    httpM.updateLoadingMsg(tag.pTime + "\n上传文件:"+tag.getProgress()+"%");
            }));
        }
        (httpM = getHttpManager(true)).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).updownFile2(
                        parList,"《这个是表单的文字字段内容》"),(s -> {
                    Toast.makeText(getApplicationContext(),"上传文件成功",Toast.LENGTH_SHORT).show();
                }));
    }

    //上传多个文件,使用@PartMap参数(服务器是本地的 所以只是本地测试通过的)
    //无法使用上传进度监听(但是:)
    private void httpUpdownFile3(){
        Map<String,RequestBody> files = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            files.put("aaa"+i,createBody(new File(filePath)));
        }
        (httpM = getHttpManager(true)).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).updownFile3(
                        files,"《这个是表单的文字字段内容》"),(s -> {
                    Toast.makeText(getApplicationContext(),"上传文件成功",Toast.LENGTH_SHORT).show();
                }));
    }

    //下载文件
    private void downFile(){
        getHttpManager(true).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).downLoadFile(),(s -> {
                    toast("下载成功");
                }));
    }

    //下载文件，地址可变
    private void downFile2(){
        String url = "http://p5.so.qhimgs1.com/t011ba366d8afe44a9b.jpg";
        getHttpManager(true).request(
                "获取数据",
                getHttpApiInteface(HttpApi.class).downLoadFile2(url),(s -> {
                    toast("下载成功");
                }));
    }


    @Override
    public void httpRequestProgress(ProgressInterceptor.NteworkProgress progress) {
        super.httpRequestProgress(progress);
        Lg.d(progress.toString());
    }
}
