package testcomponent.heyongrui.com.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.RequiresApi;

/**
 * Created by lambert on 2018/11/27.
 */

public class ImageUtil {

    /**
     * @param blurRadius 模糊半径
     * @param scale      压缩因子
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap gaussianBlur(Context context, Bitmap source,
                                      @IntRange(from = 1, to = 25) int blurRadius,
                                      @FloatRange(from = 0.0, to = 1.0) float scale) {
        //模糊处理之前先将图片进行压缩处理，提高效率
        int width = Math.round(source.getWidth() * scale);
        int height = Math.round(source.getHeight() * scale);
        Bitmap inputBmp = Bitmap.createScaledBitmap(source, width, height, false);
        RenderScript renderScript = RenderScript.create(context);
        // Allocate memory for Renderscript to work with
        final Allocation input = Allocation.createFromBitmap(renderScript, inputBmp);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        scriptIntrinsicBlur.setInput(input);
        scriptIntrinsicBlur.setRadius(blurRadius);
        scriptIntrinsicBlur.forEach(output);
        output.copyTo(inputBmp);
        renderScript.destroy();
        return inputBmp;
    }

    /**
     * bitmap转圆形bitmap
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap circleBmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvasTmp = new Canvas(circleBmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvasTmp.drawCircle(size / 2, size / 2, size / 2, paint);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, size, size);
        Canvas canvas = new Canvas(bitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(circleBmp, 0, 0, paint);
        return bitmap;
    }
}
