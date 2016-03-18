package com.boding.microscreen.util.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 *
 * Bitmap压缩工具
 * Created by JerryloveEmily on 15/3/15.
 */
public class BitmapCompressUtil {

    /**
     * 创建一个指定宽高的bitmap对象
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(
            Resources res, int resId, int reqWidth, int reqHeight,
            Bitmap.Config config){

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true; // 在解码的时候，获取原始图片的宽高，并且避免申请内存
        BitmapFactory.decodeResource(res, resId, opts);
        // 计算图片压缩采样比例
        opts.inSampleSize = caculateInSampleSize(opts, reqWidth, reqHeight);
        // 压缩完后，则可以申请内存来生产bitmap图片
        opts.inJustDecodeBounds = false;
        // 重新获取资源图片
        opts.inPreferredConfig = config;
        return BitmapFactory.decodeResource(res, resId, opts);
    }

    /**
     * 计算图片压缩采样比例
     * @param opts
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int caculateInSampleSize(BitmapFactory.Options opts, int reqWidth, int reqHeight){

        // 原始图片的宽高
        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;

        // 初始化图片采样比例为原始图大小
        int inSampleSize = 1;

        // 判断原始图片宽高和需要的图片宽高大小，
        if (outWidth > reqWidth || outHeight > reqWidth){

            int widthScale = Math.round(outWidth / reqWidth);
            int heightScale = Math.round(outHeight / reqHeight);
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }

        return inSampleSize;
    }

}
