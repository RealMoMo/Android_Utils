package com.realmo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.realmo.utils.R;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.graphics.Color.BLACK;



public class ZxingUtil {
    private void Create2QR2(String urls, ImageView imageView, Activity activity) {
        String uri = urls;
        int mScreenWidth = 0;
        Bitmap bitmap;
        try {
            /**
             * 获取屏幕信息的区别
             * 只有activity可以使用WindowManager否则应该使用Context.getResources().getDisplayMetrics()来获取。
             * Context.getResources().getDisplayMetrics()依赖于手机系统，获取到的是系统的屏幕信息；
             * WindowManager.getDefaultDisplay().getMetrics(dm)是获取到Activity的实际屏幕信息。
             */
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
            bitmap = createQRImage(uri, mScreenWidth,
                    BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher));//自己写的方法

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成二维码图片（不带图片）
    public static Bitmap createQRCode(String content, int widthAndHeight) {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        //画黑点
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK; //0xff000000
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    //带图片的二维码
    public static Bitmap createQRImage(String content, int heightPix, Bitmap logoBm) {
        try {
            // if (content == null || "".equals(content)) {
            // return false;
            // }
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            //设置空白边距的宽度
            // hints.put(EncodeHintType.MARGIN, 2); //default is 4
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, heightPix, heightPix, hints);
            int[] pixels = new int[heightPix * heightPix];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < heightPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * heightPix + x] = 0xff000000;
                    } else {
                        pixels[y * heightPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(heightPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, heightPix, 0, 0, heightPix, heightPix);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    /**
     * 解析二维码（使用解析RGB编码数据的方式）
     *
     * @param path
     * @return
     */
    public static Result decodeBarcodeRGB(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap barcode = BitmapFactory.decodeFile(path, opts);
        Result result = decodeBarcodeRGB(barcode);
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * 解析二维码 （使用解析RGB编码数据的方式）
     *
     * @param barcode
     * @return
     */
    public static Result decodeBarcodeRGB(Bitmap barcode) {
        int width = barcode.getWidth();
        int height = barcode.getHeight();
        int[] data = new int[width * height];
        barcode.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * 解析二维码（使用解析YUV编码数据的方式）
     *
     * @param path
     * @return
     */
    public static Result decodeBarcodeYUV(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        Bitmap barcode = BitmapFactory.decodeFile(path, opts);
        Result result = decodeBarcodeYUV(barcode);
        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * 解析二维码（使用解析YUV编码数据的方式）
     *
     * @param barcode
     * @return
     */
    public static Result decodeBarcodeYUV(Bitmap barcode) {
        if (null == barcode) {
            return null;
        }
        int width = barcode.getWidth();
        int height = barcode.getHeight();
        //以argb方式存放图片的像素
        int[] argb = new int[width * height];
        barcode.getPixels(argb, 0, width, 0, 0, width, height);
        //将argb转换为yuv
        byte[] yuv = new byte[width * height * 3 / 2];
        encodeYUV420SP(yuv, argb, width, height);
        //解析YUV编码方式的二维码
        Result result = decodeBarcodeYUV(yuv, width, height);

        barcode.recycle();
        barcode = null;
        return result;
    }

    /**
     * 解析二维码（使用解析YUV编码数据的方式）
     *
     * @param yuv
     * @param width
     * @param height
     * @return
     */
    private static Result decodeBarcodeYUV(byte[] yuv, int width, int height) {
        long start = System.currentTimeMillis();
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(null);

        Result rawResult = null;
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(yuv, width, height, 0, 0,
                width, height, false);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                re.printStackTrace();
            } finally {
                multiFormatReader.reset();
                multiFormatReader = null;
            }
        }
        long end = System.currentTimeMillis();
        return rawResult;
    }


    /**
     * RGB转YUV的公式是:
     * Y=0.299R+0.587G+0.114B;
     * U=-0.147R-0.289G+0.436B;
     * V=0.615R-0.515G-0.1B;
     *
     * @param yuv
     * @param argb
     * @param width
     * @param height
     */
    private static void encodeYUV420SP(byte[] yuv, int[] argb, int width, int height) {
        // 帧图片的像素大小
        final int frameSize = width * height;
        // ---YUV数据---
        int Y, U, V;
        // Y的index从0开始
        int yIndex = 0;
        // UV的index从frameSize开始
        int uvIndex = frameSize;
        // ---颜色数据---
        int R, G, B;
        int rgbIndex = 0;
        // ---循环所有像素点，RGB转YUV---
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                R = (argb[rgbIndex] & 0xff0000) >> 16;
                G = (argb[rgbIndex] & 0xff00) >> 8;
                B = (argb[rgbIndex] & 0xff);
                //
                rgbIndex++;
                // well known RGB to YUV algorithm
                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;
                Y = Math.max(0, Math.min(Y, 255));
                U = Math.max(0, Math.min(U, 255));
                V = Math.max(0, Math.min(V, 255));
                // NV21 has a plane of Y and interleaved planes of VU each sampled by a factor of 2
                // meaning for every 4 Y pixels there are 1 V and 1 U. Note the sampling is every other
                // pixel AND every other scan line.
                // ---Y---
                yuv[yIndex++] = (byte) Y;
                // ---UV---
                if ((j % 2 == 0) && (i % 2 == 0)) {
                    //
                    yuv[uvIndex++] = (byte) V;
                    //
                    yuv[uvIndex++] = (byte) U;
                }
            }
        }
    }
}
