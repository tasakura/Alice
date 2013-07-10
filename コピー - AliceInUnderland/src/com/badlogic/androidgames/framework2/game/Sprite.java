package com.badlogic.androidgames.framework2.game;


import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

import android.graphics.Rect;

public abstract class Sprite {
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected Pixmap image;

	public Sprite(double x, double y, Pixmap pixmap, World world) {
		this.x = x;
		this.y = y;
		image = pixmap;
	}

	/*
	 * スプライトの状態を更新する
	 */
	public abstract void update();

	public void draw(Graphics g, int offsetX, int offsetY) {
		g.drawPixmap(image, (int) getX() + offsetX, (int) getY()
				+ offsetY);
	}

	/*
	 * 他のスプライトと接触しているか
	 */
	public boolean isCollision(Sprite sprite) {
		Rect playerRect = new Rect((int) x, (int) y, width+(int)x, height+(int)y);
		Rect spriteRect = new Rect((int) sprite.getX(), (int) sprite.getY(),
				(int) sprite.getWidth()+(int)sprite.getX(), (int) sprite.getHeight()+(int)sprite.getY());
		 if(playerRect.intersect(spriteRect)) {return true;}	// //Rect同士ぶつかり合っていたらtrue
			return false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		g.drawPixmap(image, (int) getX() + offsetX, (int) getY()
				+ offsetY);
	}
}
