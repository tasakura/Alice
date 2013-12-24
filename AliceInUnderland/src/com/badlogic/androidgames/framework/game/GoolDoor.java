package com.badlogic.androidgames.framework.game;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class GoolDoor extends Sprite {

	public GoolDoor(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.width = 60;
		this.height = 60;
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		Rect src = new Rect(0, 0, 60, 60);
		Rect dst = new Rect((int) (x + offsetX + 10), (int) (y + offsetY + 30),
				(int) (x + offsetX + 10 + width),
				(int) (y + offsetY + 30 + height));
		g.drawPixmap(image, src, dst);
		// g.drawPixmap(image, (int) getX() + offsetX+10, (int) getY()
		// + offsetY+30);
	}

	public void update() {

	}
}
