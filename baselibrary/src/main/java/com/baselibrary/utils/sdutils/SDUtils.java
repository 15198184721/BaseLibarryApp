package com.baselibrary.utils.sdutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.FileProvider;

import com.baselibrary.R;
import com.baselibrary.acp.Acp;
import com.baselibrary.acp.AcpListener;
import com.baselibrary.acp.AcpOptions;
import com.baselibrary.base.basecomponent.BaseActivity;
import com.baselibrary.base.basecomponent.BaseApp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * CLASS Name: com.jylibary.utils.ScreenUtils}
 * Author: lcl
 * Date: 2015/11/24
 * Description
 *  SD卡相关的操作方法
 *  可以更具需要进行添加
 * Version: v0.0.1
 * Others:
 * </pre>
 */
public class SDUtils {

    //扩展sd卡是否能被正常访问(<0：未检查过，=0：不可以访问，>0:可以访问)
    private static int EXTSD_STATUS = -1;

    /**
     * 读取SD卡的时候的操作权限监听
     */
    public interface SDPermissionListener{
        /**
         *  权限申请成功后调用
         * @param path
         */
        void setPathString(String path);
        /**
         * 权限申请失败调用
         * @param errPermiss
         */
        void onError(List<String> errPermiss);
    }

    /**
     * 检查SD目录是否可用
     *
     * @return
     */
    public static boolean isSdCardAvailable() {
        String stat = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(stat) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(stat)) {//正常挂载，可以正常使用
            if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(stat)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取本应用程序的缓存目录
     *
     * @param dirName 目录名称(如果不存在会创建)
     * @param context 上下文对象
     * @return 返回这个目录的File对象
     */
    public static File getCacheDir(String dirName, Context context) {
        File result;
        if (isSdCardAvailable()) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) {
                result = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + context.getPackageName() + "/cache/" + dirName);
            } else {
                result = new File(cacheDir, dirName);
            }
        } else {
            result = new File(context.getCacheDir(), dirName);
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * 检查磁盘总空间是否大于指定值
     *
     * @param size 指定的大小
     * @return true 大于
     */
    public static boolean isDiskAvailable(long size) {
        long temSize = getDiskAvailableSize();
        return temSize > size; // > size
    }

    /**
     * 获取磁盘可用空间
     * @return byte 单位 kb
     */
    public static long getDiskAvailableSize() {
        if (!isSdCardAvailable()) return 0;
        File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        StatFs stat = new StatFs(path.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 将文件的路径转换提供者，规避Android7.0以上的私有目录隐私权限问题
     * @param filePath
     * @return
     */
    public static Uri getFile2Provider(String filePath){
        try {
            String newPath = filePath;
            if(filePath.toLowerCase().startsWith("file")){
                newPath = filePath.split("[:]")[1];
            }
            String provider = BaseApp.getInstan().getApplicationContext().getPackageName() + ".fileprovider";
            File file = new File(newPath);
            Uri photoURI = FileProvider.getUriForFile(
                    BaseApp.getInstan(),
                    provider,
                    file);
            return photoURI;
        }catch (Exception e){
            String es = e.toString();
            return null;
        }

    }

    /**
     * 获取内置SD卡
     * @return
     */
    public static void getInnerSDCardPath(final SDPermissionListener listener) {
        //申请相关权限
        final BaseApp bapp = BaseApp.getInstan();
        Acp acp = Acp.getInstance(bapp);
        final String textMsg = bapp.getResources().getString(R.string.baseactivity_permission_msg);
        acp.request(new AcpOptions.Builder()
                .setRationalMessage(String.format(textMsg, "SD卡读写"))//设置第一个对话框的内容(没有表示不显示)
                .setPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                })//需要的申请的权限
                .build(),new AcpListener() {
            @Override
            public void onGranted() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
                    if(listener != null)
                        listener.setPathString("");
                String sdpath = Environment.getExternalStorageDirectory().getPath();
                if (!checkDirPermissions(sdpath)) {
                    if(listener != null)
                        listener.setPathString("");//SD卡不可以访问
                    return;
                }
                if(listener != null)
                    listener.setPathString(sdpath);
            }
            @Override
            public void onDenied(List<String> permissions) {
                final String textMsg = bapp.getResources().getString(R.string.baseactivity_permission_err);
                BaseActivity.Toast(String.format(textMsg, "SD卡读写"));
                if(listener != null)
                    listener.onError(permissions);
            }
        });
    }

    /**
     * 获取SD卡路径，默认先外置在内置(末尾不包含分隔符)
     * @param parissListener 权限监听
     * @return
     * @p
     */
    public static void getSDCardPath(SDPermissionListener parissListener) throws NullPointerException {
        getExtSDCardPath(parissListener);
    }

    /**
     * <pre>
     * 获取外置SD卡路径（如果没有外置sd卡。那么就会返回内置的sd卡路径）
     * 推荐使用
     *
     * @return 列表的第一项就是具体得到的路径。没有集合长度为0
     * </pre>
     */
    public static void getExtSDCardPath(final SDPermissionListener listener) {
        //申请相关权限
        final BaseApp bapp = BaseApp.getInstan();
        Acp acp = Acp.getInstance(bapp);
        final String textMsg = bapp.getResources().getString(R.string.baseactivity_permission_msg);
        acp.request(new AcpOptions.Builder()
                .setRationalMessage(String.format(textMsg, "SD卡读写"))//设置第一个对话框的内容(没有表示不显示)
                .setPermissions(new String[]{
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                })//需要的申请的权限
                .build(),new AcpListener() {
            @Override
            public void onGranted() {
                List<String> SdList = new ArrayList<String>();
                try {
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec("mount");
                    InputStream is = proc.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("secure"))
                            continue;
                        if (line.contains("asec"))
                            continue;
                        if (line.contains("media"))
                            continue;
                        if (line.contains("system") || line.contains("cache")
                                || line.contains("sys") || line.contains("data")
                                || line.contains("tmpfs") || line.contains("shell")
                                || line.contains("root") || line.contains("acct")
                                || line.contains("proc") || line.contains("misc")
                                || line.contains("obb")) {
                            continue;
                        }

                        if (line.contains("fat") || line.contains("fuse")
                                || (line.contains("ntfs"))) {

                            String columns[] = line.split(" ");
                            if (columns != null && columns.length > 1) {
                                String path = columns[1];
                                if (path != null && !SdList.contains(path)
                                        && path.contains("sd"))
                                    SdList.add(columns[1]);
                            }
                        }
                    }
                    isr.close();
                } catch (Exception e) {
                }
                try {
                    //检查外部存储是否可以读写
                    if (isSdCardAvailable()) {
                        String extPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                        SdList.add(extPath);
                        if (SdList.size() > 0) {
                            if (EXTSD_STATUS < 0) {
                                String path = SdList.get(0);
                                if (checkDirPermissions(path)) {
                                    EXTSD_STATUS = 1;//设置标志可以访问
                                } else {
                                    EXTSD_STATUS = 0;//设置标志为不可以访问
                                    SdList.clear();
                                }
                            }
                        } else if (EXTSD_STATUS == 0) {
                            SdList.clear();    //如果外置sd卡不可以操作。那么清楚列表路径让其获取内置sd卡返回
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //如果没有找到。那么获取内置存储卡
                if (SdList.size() <= 0) {
                    getInnerSDCardPath(listener);
                    return;
                }
                if(listener != null)
                    listener.setPathString(SdList.get(0));
                return;
            }
            @Override
            public void onDenied(List<String> permissions) {
                final String textMsg = bapp.getResources().getString(R.string.baseactivity_permission_err);
                BaseActivity.Toast(String.format(textMsg, "SD卡读写"));
                if(listener != null)
                    listener.onError(permissions);
            }
        });
    }

    /**
     * 检查目录权限(检查是否可以正常读写操作)
     *
     * @param pathDir 目录路径，例如：/xx/xx/(最后有斜杠)
     * @return false:不可以访问
     * true:可以访问
     */
    public static boolean checkDirPermissions(String pathDir) {
        String dir = File.separator + "" + System.currentTimeMillis() + File.separator;
        File f = new File(pathDir + dir);
        if (!f.exists()) {
            boolean creDir = f.mkdirs();
            if (creDir) {
                deleteDir(f);
                return true;
            }
        }
        return false;
    }

    /**
     * 保存Bitmap对象到到SD卡(保存路径为：文件全名称+后缀名[.tem])
     *
     * @param mBitmap  需要保存到SD卡的Bitmap对象
     * @param filePath 图片的路径
     */
    public static synchronized void saveBitmap(Bitmap mBitmap, String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            return;// 文件已经存在。直接返回
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
        } finally {
            try {
                if (fOut != null) {
                    fOut.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 将图片转换为Bitmap对象
     *
     * @param path 图片路径(具体的图片路径)
     * @param w    宽度
     * @param h    高度
     * @return 转换后的Bitmap对象
     */
    public synchronized static Bitmap fileToBitmap(String path, int w, int h) {
        if (!new File(path).exists()) {
            return null;// 文件不存在。直接返回
        }
        // TODO 将sd卡的图片转换为Bitmap对象
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
                BitmapFactory.decodeFile(path, opts));
        Bitmap b = null;
        try {
            b = Bitmap.createScaledBitmap(weak.get(), w, h, true);
        } catch (Exception e) {
            b = null;
        }
        return b;
    }

    /**
     * 通过文件完整的路径名称获取文件名称
     *
     * @param filePath 完整的文件路径
     * @return 返回这个路径的拆分出来的文件名。最后的部分
     */
    public synchronized static String getFileName(String filePath) {
        String[] s = filePath.split("[" + File.separator + "]");
        if (s != null && s.length > 0) {
            return s[s.length - 1];
        }
        return "-";
    }

    /**
     * <pre>
     * 检查图片是否完整
     *  思路：获取图片的宽高。如果能正常获取。那么表示图片正常。如果损坏那么宽高一定不正常
     * @param file 图片路径
     * </pre>
     */
    public synchronized static boolean checkImage(String file) {
        boolean b = true;
        try {
            BitmapFactory.Options options = null;
            if (options == null)
                options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(file, options); // filePath代表图片路径
            if (options.mCancel || options.outWidth == -1
                    || options.outHeight == -1) {
                b = false;
            }
        } catch (Exception e) {
            b = false;
        }
        return b;
    }

    /**
     * <pre>
     * 将Bitmap对象转换为byte字节型数组
     * @param bm 需要转换为byte类型数组的Bitmap对象
     * @return
     * 	成功：bm所对应的byte字节数组
     * 	失败：长度为0的byte数组
     * </pre>
     */
    public synchronized static byte[] BitmapToBytes(Bitmap bm) {
        if (bm == null) {
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * <pre>
     * 将字节(byte)数组转换为Bitmap对象
     * @param b 需要转换为Bitmap对象的byte数组
     * @return
     * 	成功：b所对应的Bitmap对象
     * 	失败：null
     * </pre>
     */
    public synchronized static Bitmap BytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 将文件转换为字节数组
     *
     * @param f 文件对象
     * @return 这个文件对象的字节数组
     */
    public synchronized static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        if (!f.canRead()) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            for (int n; (n = stream.read(b)) != -1; ) {
                out.write(b, 0, n);
            }
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * <pre>
     * 将字节数组输出到文件
     * @param b 需要输出的字节数组
     * @param outputFile 目标文件路径
     * @return
     * 	成功：返回文件对象
     * 	失败：失败，返回null
     * </pre>
     */
    public synchronized static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 将assets中的指定文件保存到SD卡的指定位置
     * <pre>
     * @param context 上下文对象。用于获取资源文件的管理器
     * @param resFileName 源文件名称。assets中的某个文件名称
     * @param sdFileName 目标文件的保存路径，保存到SD卡的文件名称。完整路径
     * @return
     * 	T：成功
     * 	F：失败
     * </pre>
     */
    public synchronized static boolean saveAssetsToSd(Context context,
                                                      String resFileName,
                                                      String sdFileName) {
        boolean b = true;
        InputStream in = null;
        FileOutputStream fileOutputStream = null;
        File file = new File(sdFileName);
        byte[] buffer = new byte[1024 * 1024];
        int count = 0;
        try {
            if (file.isDirectory()) {
                return false;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            in = (context.getAssets().open(resFileName));
            File outputFile = new File(sdFileName);
            fileOutputStream = new FileOutputStream(outputFile);
            while ((count = in.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
            b = false;
        } finally {
            try {
                fileOutputStream.close();
                in.close();
            } catch (Exception e2) {
            }
        }
        return b;
    }

    /**
     * 读取assets中的文件
     * <pre>
     * @param context 上下文对象。用于获取资源文件的管理器
     * @param resFileName 源文件名称。assets中的某个文件名称
     * @return
     * 	T：成功
     * 	F：失败
     * </pre>
     */
    public synchronized static String getAssetsString(Context context,
                                                      String resFileName) {
        String b = "";
        InputStream in = getAssetsInputStream(context, resFileName);

        if (in != null) {
            b = getInputStreamToString(in);
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return b;
    }

    /**
     * 获取资源Assets中文件的输入流
     *
     * @param context     上下文
     * @param resFileName 文件名称
     * @return
     */
    public synchronized static InputStream getAssetsInputStream(Context context,
                                                                String resFileName) {
        InputStream in = null;
        try {
            in = (context.getAssets().open(resFileName));
        } catch (IOException e) {
            e.printStackTrace();
            in = null;
        }
        return in;
    }

    /**
     * 将InputStream转换为String
     *
     * @param in
     * @return
     */
    public synchronized static String getInputStreamToString(InputStream in) {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[2048];
        try {
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    /**
     * 将String转换为InputStream(默认编码)
     *
     * @param str 字符串
     * @return
     */
    public synchronized static InputStream getStringToInputStream(String str) {
        return getStringToInputStream(str, "UTF-8");
    }

    /*=======================私有目录操作方法开始==========================*/

    /**
     * 获取当前app的私有数据目录: /data/data/<application package>/cache
     *
     * @param con 上下文
     * @return 私有目录的路径
     */
    public static synchronized String getAppPrivateFilesDir(Context con) {
        return con.getFilesDir().getAbsolutePath();
    }

    /**
     * 获取当前app的私有数据缓存目录(如果有外置SD卡返回SD卡目录，否则返回手机存储上的缓存数据目录)
     *
     * @param con 上下文
     * @return 私有缓存目录路径
     */
    public static synchronized String getAppPrivateCacheDir(Context con) {
        String cacheRootPath = "";
        if (isSdCardAvailable() || !Environment.isExternalStorageRemovable()) {
            //获取SD卡上的缓存目录(对应手机清除缓存按钮,可以直接查看到内容)
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = con.getExternalCacheDir().getPath();
        } else {
            //手机存储上的缓存目录(对应手机清除数据按钮，需要root权限后用资源管理器查看)
            // /data/data/<application package>/cache
            cacheRootPath = con.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    /**
     * 获取当前app的私有数据缓存目录(但是可以直接查看到)
     *
     * @param con 上下文
     * @return 私有缓存目录路径
     */
    public static synchronized String getAppPrivateShowCacheDir(Context con) {
        String cacheRootPath = "";
        //获取SD卡上的缓存目录(对应手机清除缓存按钮,可以直接查看到内容)
        // /sdcard/Android/data/<application package>/cache
        cacheRootPath = con.getExternalCacheDir().getPath();
        return cacheRootPath;

    }

    /**
     * 获取当前app的私有数据缓存目录的指定文件或者目录(/data/data/youPackageName/)
     *
     * @param dirOrFileName 需要获取的文件目录名称()
     * @param con           上下文
     * @return 私有缓存目录路径
     */
    public static synchronized File getAppPrivateCacheDir(String dirOrFileName, Context con) {
        return con.getDir(dirOrFileName, Context.MODE_PRIVATE);
    }

    /**
     * 获取当前app的私有数据目录下所有的文件名称
     *
     * @param con 上下文
     * @return 私有缓存目录下的所有文件名称
     */
    public static synchronized String[] getAppPrivateDirFileList(Context con) {
        return con.fileList();
    }

    /*=======================私有目录操作方法结束==========================*/

    /**
     * 将String转换为InputStream
     *
     * @param str     字符串
     * @param EnCcode 转换为InputStream的编码格式
     * @return
     */
    public synchronized static InputStream getStringToInputStream(String str, String EnCcode) {
        InputStream in_withcode = null;
        try {
            in_withcode = new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return in_withcode;
    }


    /**
     * 获取目录的大小
     *
     * @param file
     * @return
     */
    public static long getDirSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getDirSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
        }
        return size;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录的file对象
     * @return T:成功
     * F:失败
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，不会删除目录
     *
     * @param dir 将要删除的文件目录的file对象
     * @return T:成功
     * F:失败
     */
    public static boolean deleteDirFile(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if(dir.isFile())
            return dir.delete();
        return true;
    }

    /**
     * 复制文件或者文件夹到指定的目标目录
     *
     * @param srcFile    源文件或者原目录
     * @param targetFile 目标目录
     * @return 成功返回完成
     */
    public static int copyFileOrDir(File srcFile, File targetFile) {
        if (targetFile == null) {
            return -1;//目标文件为空
        }
        if (!srcFile.exists()) {
            return -3;//源文件或者目录不存在
        }
        if (srcFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
            return -4;//目标和源是同一个目录
        }
        if(srcFile.isFile()){
            return copyFile(srcFile,targetFile);
        }
        File[] children = srcFile.listFiles();
        //递归删除目录中的子目录下
        for (int i = 0; i < children.length; i++) {
            if (children[i].isDirectory()) {
                String tra = targetFile.getPath() + File.separator + children[i].getName() + File.separator;
                String sr = children[i].getPath() + File.separator;
                copyFileOrDir(new File(sr), new File(tra));
            } else {
                copyFile(children[i],targetFile);
            }
        }
        return 0;
    }

    /**
     * 复制文件
     *
     * @param src
     * @param tarage
     * @return
     */
    public static int copyFile(File src, File tarage) {
        if (tarage == null) {
            return -5;//目标文件为空
        }
        if (!src.exists()) {
            return -7;//源文件录不存在
        }
        InputStream fosfrom = null;
        OutputStream fosto = null;
        try {
            File f = null;
            if(!tarage.exists()){
                if(tarage.isDirectory()){
                    tarage.mkdirs();
                    String ot = tarage.getPath()+File.separator+src.getName();
                    f = new File(ot);
                    f.createNewFile();
                }else{
                    tarage.getParentFile().mkdirs();
                    tarage.createNewFile();
                    f = tarage;
                }
            }else{
                if(tarage.isDirectory()){
                    String ot = tarage.getPath()+File.separator+src.getName();
                    f = new File(ot);
                    f.createNewFile();
                }else{
                    tarage.getParentFile().mkdirs();
                    tarage.createNewFile();
                    f = tarage;
                }
            }

            fosfrom = new FileInputStream(src.getPath());
            fosto = new FileOutputStream(f.getPath());
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) {
            return -8;
        } finally {
            try {
                fosfrom.close();
            } catch (Exception e) {
            }
            try {
                fosto.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 获取文件名称(去除扩展名称)
     * @param fileName
     * @return
     */
    public static String getFileNameNoEx(String fileName){
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    /**
     * 获取文件名称的扩展名称
     * @param fileName
     * @return
     */
    public static String getFileNameEx(String fileName){
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
}
