package com.badlogic.androidgames.framework2;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.FrameLayout;

public interface Graphics {
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    public Pixmap newPixmap(String fileName, PixmapFormat format);
    
    public void clear(int color);

    public void drawPixel(int x, int y, int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);

    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawPixmap(Pixmap pixmap, int x, int y);
    
    public void drawController(int cx, int cy, int cr, Paint circle_paint, int color, int color2, int direction);

    public void drawCircle(int cx, int cy, int cr, Paint circle_paint);

    public int getWidth();

    public int getHeight();
    
    public void drawTextAlp(String line, float x, float y, Paint paint);
    
}
