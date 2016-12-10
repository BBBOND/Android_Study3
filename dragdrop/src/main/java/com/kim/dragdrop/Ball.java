package com.kim.dragdrop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by KIM on 15/12/25.
 */
public class Ball {
    private Context context;
    private int drawableID;
    private Point point;

    public Ball(Context context, int drawableID, Point point) {
        this.context = context;
        this.drawableID = drawableID;
        this.point = point;
    }

    public Bitmap getBitmap() {
        Resources res = context.getResources();
        Drawable drawable = res.getDrawable(drawableID);
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Log.d("Ball", "bitmap:" + bitmap);
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }
}
