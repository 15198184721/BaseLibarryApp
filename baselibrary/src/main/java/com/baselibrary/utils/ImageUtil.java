package com.baselibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;

import com.baselibrary.base.basecomponent.BaseApp;

/**
 * <pre>
 * Author: lcl
 * Date: 2018-6-4
 * Description
 *  图片操作工具类,图片工具类
 * </pre>
 */
public class ImageUtil {

    /**
     * 将指定图片资源id的图片转换为黑白
     * @param imgResId 图片的资源id
     * @return 转换后的图片Bitmap
     */
    public static Bitmap toBlackAndWhite(@DrawableRes int imgResId){
        if(imgResId == 0){
            return null;
        }
        return toBlackAndWhite(BitmapFactory.decodeResource(
                BaseApp.getInstan().getResources(),imgResId));
    }

    /**
     * 将彩色图片的bitmap转换为黑白颜色
     * @param bmpOriginal 需要转换的bitmap
     * @return 转换后的bitmap
     */
    public static Bitmap toBlackAndWhite(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        //此处就是讲图片转为黑白
        //也可设置点阵的方式来进行其他颜色操作
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

}
