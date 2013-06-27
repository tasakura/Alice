package com.badlogic.androidgames.framework2.impl;

import java.io.IOException;
import java.io.InputStream;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.widget.FrameLayout;


public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	public Pixmap newPixmap(String fileName, PixmapFormat format) {
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;

		Options options = new Options();
		options.inPreferredConfig = config;

		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap frim asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap frim asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

		if (bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;

		return new AndroidPixmap(bitmap, format);
	}

	public void clear(int color) {
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
				(color & 0xff));
	}

	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}

	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;

		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;

		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
				null);
	}

	public void drawPixmap(Pixmap pixmap, int x, int y) {
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, x, y, null);
	}

	public int getWidth() {
		return frameBuffer.getWidth();
	}

	public int getHeight() {
		return frameBuffer.getHeight();
	}

	@Override
	public void drawController(int cx, int cy, int cr, Paint circle_paint,	int color, int color2, int direction) {
		int cw = 70;
		canvas.drawCircle(cx, cy, cr, circle_paint);
		switch (direction) {
		case 1:
			Arrow(cx + cr, cy, cx + cr - cw, cy - cw / 2, cx + cr - cw, cy + cw
					/ 2, color2); // right
			Arrow(cx - cr, cy, cx - cr + cw, cy - cw / 2, cx - cr + cw,
					cy + cw / 2, color); // left
			break;
			
		case 2:
			Arrow(cx - cr, cy, cx - cr + cw, cy - cw / 2, cx - cr + cw,
					cy + cw / 2, color2); // left
			Arrow(cx + cr, cy, cx + cr - cw, cy - cw / 2, cx + cr - cw, cy + cw
					/ 2, color); // right
			break;
		default:
			Arrow(cx + cr, cy, cx + cr - cw, cy - cw / 2, cx + cr - cw, cy + cw
					/ 2, color); // right
			Arrow(cx - cr, cy, cx - cr + cw, cy - cw / 2, cx - cr + cw,
					cy + cw / 2, color); // left
			break;
		}
		paint.setColor(color);
		Path path = new Path();
		canvas.drawPath(path, paint);
	}

	public void drawCircle(int cx, int cy, int cr, Paint circle_paint) {
		canvas.drawCircle(cx, cy, cr, circle_paint);
	}

	public void drawTextAlp(String line, float x, float y, Paint paint) {
		canvas.drawText(line, x, y, paint);
	}

	void Arrow(float x1, float y1, float x2, float y2, float x3, float y3,
			int color) {
		Paint paint = new Paint();
		paint.setColor(color);
		Path path = new Path();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		path.lineTo(x3, y3);
		canvas.drawPath(path, paint);
	}

}
