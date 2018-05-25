package com.baselibrary.http.intefaces;

import android.support.annotation.NonNull;

import com.baselibrary.http.body.listener.FileUploadProgressListener;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.ByteString;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-5-25
 * Description
 *  http网络请求字段创建<br></br>
 *  主要是文件、请求实体(RequestBody)的创建，单独成接口<br></br>
 *  方便单独继承，此接口在BaseActivity中也有实现
 * </pre>
 */
public interface IHttpFromFiedCreate {
    /**
     * 创建一个表单文件字段，即:input type=file 类型的字段<br></br>
     * 对应接口参数中是:(@Part 注解的参数)
     * @param fieldName 文件字段的名称(相当于 input type=file类型字段的name属性)
     * @param filePath 文件路径
     * @return 文件对应的实体对象，可作为参数
     */
    MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath);

    /**
     * 创建一个表单文件字段,即:input type=file 类型的字段<br></br>
     * 并且监听上传进度，<br></br>
     * 对应接口参数中是:(@Part 注解的参数)
     * @param fieldName 文件字段的名称(相当于 input type=file类型字段的name属性)
     * @param filePath 文件路径
     * @param uploadListener 文件上传进度监听(不关心可以为空)
     * @return 文件对应的实体对象，可作为参数
     */
    MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath,
            FileUploadProgressListener uploadListener);

    /**
     * 创建一个表单文件字段,即:input type=file 类型的字段<br></br>
     * 并且监听上传进度<br></br>
     * 指定后台服务器保存的名称<br></br>
     * 对应接口参数中是:(@Part 注解的参数)
     * @param fieldName 文件字段的名称(相当于 input type=file类型字段的name属性)
     * @param filePath 文件路径
     * @param uploadListener 文件上传进度监听(不关心可以为空)
     * @param fileName 文件名称，提供给后台的文件名称(可以为空或者"")
     * @return 文件对应的实体对象，可作为参数
     */
    MultipartBody.Part createFormPartFile(
            @NonNull String fieldName, @NonNull String filePath,
            FileUploadProgressListener uploadListener, String fileName);

    /**
     * 创建多个文件上传的请求参数,并且监听上传进度<br></br>
     * (@Part注解的参数，需要注意的是这个代表：一个表单file字段名称下对应有多个文件)
     * @param fieldName 表单字段名称(一个字段下放了多个文件,字段名称:type=file类型字段的name属性)
     * @param filePath 需要上传的文件路径集合(多个文件被放在了一个文件file字段下)
     * @param uploadListener 上传监听
     * @return 返回一个上传文件集合
     */
    List<MultipartBody.Part> createFormFileList(
            @NonNull String fieldName, @NonNull String[] filePath,
            FileUploadProgressListener uploadListener);

    /**
     * 创建多个文件上传的请求参数,也就是在表单中创建多个file文件字段,并且监听上传进度<br></br>
     * (@Part注解的参数，注意:这个表示一个file字段名称对应一个文件)
     * @param fieldName 文件字段的名称集合(这个文件名称和文件路径一一对应的,字段名称:type=file类型字段的name属性)
     * @param filePath 文件路径集合(文件路径集合)
     * @param uploadListener 文件上传进度监听(不关心可以为空)
     * @return 作为文件上传的参数
     */
    List<MultipartBody.Part> createFormFileList(
            @NonNull String fieldName[], @NonNull String[] filePath,
            FileUploadProgressListener uploadListener);

    /**
     * 创建文本内容的请求的实体,一般作用于 @Body 注解
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param content 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(@NonNull String content);

    /**
     * 创建文本内容的请求的实体,一般作用于 @Body 注解,并且指定类型
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param MediaType 指定媒体的类型
     * @param content 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(String MediaType,@NonNull String content);

    /**
     * 创建请求的文件实体,一般作用于 @Body 注解
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param file 写入实体的文件类容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(@NonNull File file);

    /**
     * 创建请求的文件实体,一般作用于 @Body 注解,并且指定类型
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param MediaType 指定媒体的类型
     * @param file 写入实体的文件类容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(String MediaType,@NonNull File file);

    /**
     * 创建byte字节的请求的实体,一般作用于 @Body 注解
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param bytes 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(@NonNull byte[] bytes);

    /**
     * 创建byte字节的请求的实体,一般作用于 @Body 注解,并且指定类型
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param MediaType 指定媒体的类型
     * @param bytes 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(String MediaType,@NonNull byte[] bytes);

    /**
     * 创建byte字节的请求的实体,一般作用于 @Body 注解
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param bytes 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(@NonNull byte[] bytes,int offset,int byteCount);

    /**
     * 创建byte字节的请求的实体,一般作用于 @Body 注解,并且指定类型
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param MediaType 指定媒体的类型
     * @param bytes 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(String MediaType,@NonNull byte[] bytes,int offset,int byteCount);

    /**
     * 创建@{@link ByteString}对象的请求的实体,Okio封装的序列化对象,一般作用于 @Body 注解
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param bString 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(@NonNull ByteString bString);

    /**
     * 创建@{@link ByteString}对象的请求的实体<br></br>
     * Okio封装的序列化对象,一般作用于 @Body 注解,并且指定类型
     * (注意：这个文本字段是无法被当做普通参数Request.getParameter()方法获取的)
     * @param MediaType 指定媒体的类型
     * @param bString 写入实体的文本内容
     * @return 返回一个创建好的网页实体
     */
    RequestBody createBody(String MediaType,@NonNull ByteString bString);
}
