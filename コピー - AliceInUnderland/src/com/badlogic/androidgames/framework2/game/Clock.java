package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

public class Clock extends Sprite {

	public Clock(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.width = 50;
		this.height = 50;
	}

	@Override
	public void update() {

	}
	
	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		g.drawPixmap(image, (int) getX() + offsetX, (int) getY()
				+ offsetY+40);
	}

	/*
	 *アイテムを使う
	 */
	public void use(World world) {
		int plus = 5;
		world.plus_timelimit(plus);
	}

}
