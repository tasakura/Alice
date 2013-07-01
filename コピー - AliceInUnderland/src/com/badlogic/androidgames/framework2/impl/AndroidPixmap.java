package com.badlogic.androidgames.framework2.impl;

import com.badlogic.androidgames.framework2.Pixmap;
import com.badlogic.androidgames.framework2.Graphics.PixmapFormat;

import android.graphics.Bitmap;


public class AndroidPixmap implements Pixmap{
	Bitmap bitmap;
	PixmapFormat format;

	public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}
	
	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public PixmapFormat getFormat() {
		return format;
	}

	public void dispose() {
		bitmap.recycle();		//bitmap・ｽ・ｽ・ｽJ・ｽ・ｽ
	}

}
