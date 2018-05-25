package com.baselibarryapp.api;

import com.baselibarryapp.http.beans.IPBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-24
 * Description
 * </pre>
 */
public interface HttpApi {

    static String BaseUrl = "http://www.baidu.com/";

    /**
     * get请求测试
     * @param format
     * @param ip
     * @return
     */
    @GET("http://int.dpool.sina.com.cn/iplookup/iplookup.php")
    Observable<IPBean> getIp(@Query("format") String format,@Query("ip") String ip);

    /**
     * post请求测试，必须加上；@FormUrlEncoded
     * @param queryStr
     * @return
     */
    @FormUrlEncoded
    @POST("https://www.so.com/s")
    Observable<String> getSo(@Field("q") String queryStr);

    /**
     * 上传文件测试:@Multipart 注解表示文件上传
     * 注意:@Part不能包含名称，也就是不能使用 @Part("...")的形式
     * @param part
     * @return
     */
    @Multipart //标记一个请求是multipart/form-data类型
    @POST("http://192.168.0.223/updownFile/upload")
    Observable<String> updownFile(
            @Part MultipartBody.Part part,
            //请求实体(名称为:fromBody，这个字段无法用request.getParameter获取。需要特殊处理)
            //这个是相当于post一样，将其他类型的文本压入到实体中的，相当于表单的其他字段
            @Part("fromBody") RequestBody fromTextBody,
            //普通的文本参数，可使用 request.getParameter("textText")获取的参数
            @Query("testText") String textField);

    /**
     * 上传多个文件，使用List上传
     * @param part 多文件集合
     * @param textField 文本参数
     * @return
     */
    @Multipart //标记一个请求是multipart/form-data类型
    @POST("http://192.168.0.223/updownFile/upload")
    Observable<String> updownFile2(
            @Part List<MultipartBody.Part> part,
            //普通的文本参数，可使用 request.getParameter("textText")获取的参数
            @Query("testText") String textField);

    /**
     * 上传多个文件，使用@PartMap
     * @param files 多文件集合
     * @param textField 文本参数
     * @return
     */
    @Multipart //标记一个请求是multipart/form-data类型
    @POST("http://192.168.0.223/updownFile/upload")
    Observable<String> updownFile3(
            @PartMap Map<String,RequestBody> files,
            //普通的文本参数，可使用 request.getParameter("textText")获取的参数
            @Query("testText") String textField);

    //下载文件的方法
    @GET("http://p5.so.qhimgs1.com/t011ba366d8afe44a9b.jpg")
    Observable<ResponseBody> downLoadFile();

    //下载文件的方法2(由于地址是可变的，所以直接用参数)
    @Streaming//这个注解表示是大文件
    @GET
    Observable<ResponseBody> downLoadFile2(@Url String url);

}
