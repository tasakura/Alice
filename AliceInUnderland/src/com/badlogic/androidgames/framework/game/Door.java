package com.badlogic.androidgames.framework.game;

import android.graphics.Rect;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class Door extends Sprite {
	private int Nextstage;

	public Door(double x, double y, Pixmap pixmap, int Nextstage, World world) {
		super(x, y, pixmap, world);
		this.width = 60;
		this.height = 60;
		this.Nextstage = Nextstage;
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		int row = Nextstage / 6;
		int col = Nextstage - (row * 6);
		Rect src = new Rect(60 * col, 60 * row, (60 * col) + 60,
				(60 * row) + 60);
		Rect dst = new Rect((int) (x + offsetX + 10), (int) (y + offsetY + 30),
				(int) (x + offsetX + 10 + width),
				(int) (y + offsetY + 30 + height));
		g.drawPixmap(image, src, dst);
		// g.drawPixmap(image, (int) getX() + offsetX + 10, (int) getY() +
		// offsetY
		// + 30);
	}

	public void update() {
	}

	public int getNextstage() {
		return Nextstage;
	}

}
