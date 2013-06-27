package com.badlogic.androidgames.framework2;

import com.badlogic.androidgames.framework2.Graphics.PixmapFormat;

public interface Pixmap {
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
