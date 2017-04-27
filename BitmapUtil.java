package momo.com.week12_project.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  realmo
 */
public class BitmapUtil {

    /**
     * 圆形图片
     * @param source
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int radiu=source.getWidth()<source.getHeight()?source.getWidth()/2:source.getHeight()/2;
        Bitmap target = Bitmap.createBitmap(radiu*2, radiu*2, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(radiu,radiu,radiu, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        Rect srcRect=new Rect();
        srcRect.left=source.getWidth()/2-radiu;
        srcRect.right=source.getWidth()/2+radiu;
        srcRect.top=source.getHeight()/2-radiu;
        srcRect.bottom=source.getHeight()/2+radiu;
        RectF descRect=new RectF();
        descRect.left=0;
        descRect.right=radiu*2;
        descRect.top=0;
        descRect.bottom=radiu*2;
        canvas.drawBitmap(source,srcRect,descRect, paint);
        return target;
    }

    /**
     * 圆角图片
     * @param source
     * @param corner
     * @return
     */
    public static Bitmap createRoundImage(Bitmap source,int corner)
    {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制椭圆形
         */
        RectF rect=new RectF();
        rect.left=0;
        rect.top=0;
        rect.right=source.getWidth();
        rect.bottom=source.getHeight();
        canvas.drawRoundRect(rect,corner,corner, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        Rect srcRect=new Rect();
        srcRect.left=0;
        srcRect.right=source.getWidth();
        srcRect.top=0;
        srcRect.bottom=source.getHeight();
        canvas.drawBitmap(source,srcRect,srcRect, paint);
        return target;
    }


    /**
     * 对res中的图片进行二次采样
     *
     * @param res   Resources对象
     * @param id    被加载的图片的id
     * @param viewW 控件的宽度
     * @param viewH 控件的高度
     * @return
     */
    public static Bitmap getBitmap(Resources res, int id, int viewW, int viewH) {
        //1.声明Options
        BitmapFactory.Options ops = new BitmapFactory.Options();
        //2.设置只解析边缘
        ops.inJustDecodeBounds = true;   //解析的bitmap 为null,在ops中，得到图片的宽高
        //3.解析图片，得到图片的宽高
        BitmapFactory.decodeResource(res, id, ops);

        int imgW = ops.outWidth;
        int imgH = ops.outHeight;

        //4.计算采样比：图片的宽高/控件的宽高
        int scalx = (int) Math.ceil(imgW / viewW);
        int scaly =(int) Math.ceil(imgH / viewH) ;

        //默认缩放比例为2
        int scal = 2;
        //采样比越高，图片加载的像素点越少
        //取采样比大的
        if(scalx>2 || scaly>2){
            if(scalx>scaly){
                scal =scalx;
            }else{
                scal =scaly;
            }
        }

        //5.设置真正解析图片
        ops.inJustDecodeBounds =false;
        //设置采样比
        ops.inSampleSize = scal;
        //设置图片的质量
        ops.inPreferredConfig = Bitmap.Config.ALPHA_8;
        //第二次采样,返回解析的图片
        return BitmapFactory.decodeResource(res,id,ops);
    }
	
		/**
	 * Save image to the SD card
	 * 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static String savePhoto(Bitmap photoBitmap, String path,
								   String photoName) {
		String localPath = null;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File photoFile = new File(path, photoName + ".png");
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) { // ת�����
						localPath = photoFile.getPath();
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				localPath = null;
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				localPath = null;
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
						fileOutputStream = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return localPath;
	}

}
