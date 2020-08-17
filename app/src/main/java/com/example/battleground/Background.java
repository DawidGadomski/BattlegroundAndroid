package com.example.battleground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


public class Background {
    private Bitmap bitmap;
    private Paint paint;

    public Background(Context context) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.grass);
        paint = new Paint();
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(this.bitmap, 0, 0, paint);
    }

    public int getWidth(){
        return bitmap.getWidth();
    }
    public int getHeight(){
        return bitmap.getHeight();
    }
}
