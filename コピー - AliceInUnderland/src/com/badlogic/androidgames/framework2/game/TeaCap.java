package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

public class TeaCap extends Sprite {

	public TeaCap(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.width = 50;
		this.height = 50;
	}

	@Override
	public void update() {}
	
	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		g.drawPixmap(image, (int) getX() + offsetX, (int) getY()
				+ offsetY+30);
	}

	/*
	 *アイテムを使う
	 */
	public void use(Alice player) {
		// プレイヤーのスピードがアップ！
		player.setSpeed(player.getSpeed()*1.2);
	}

}
