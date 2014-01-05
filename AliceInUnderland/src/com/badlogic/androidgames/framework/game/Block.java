package com.badlogic.androidgames.framework.game;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.badlogic.androidgames.framework.Pixmap;

public class Block extends Sprite {
	private int type; // 01 = UP, 02 = DOWN
	private final int SPEED = 5;
	private int vy = SPEED;
	private int world_height;
	private World world;

	public Block(double x, double y, Pixmap pixmap, World world, int type) {
		super(x, y, pixmap, world);
		this.x = x;
		this.y = y;
		this.image = pixmap;
		this.type = type;
		this.width = 80;
		this.height = 80;
		this.world = world;
		this.world_height = world.getHeight();
	}

	@Override
	public void update() {
		double newY = 0;
		switch (type) {
		case 1:
			vy = -SPEED;
			newY = vy + y;
			if (world.getTileCollision(this, x, newY) == null) {
				y = newY;
				if (y < -height)
					y = world_height;
			} else {
				Return();
			}
			break;
		case 2:
			vy = +SPEED;
			newY = vy + y;
			if (world.getTileCollision(this, x, newY) == null) {
				y = newY;
				if (world_height < y) 
					y = -height;
			} else {
				Return();
			}
			break;
		}
	}

	public Point Collision(Sprite sprite, double newX, double newY) {
		if (!(sprite instanceof Alice))
			return null;

		Alice alice = (Alice) sprite;
		Rect rect_alice = new Rect((int) alice.getX(), (int) alice.getY(),
				alice.getWidth(), alice.getHeight());

		// ブロックの位置を取得
		Rect rect_block = new Rect((int) x, (int) y, width, height);

		if (rect_alice.intersect(rect_block)) {
			if (alice.getY() > y && (alice.x > x && alice.x < x + width)) {
				return new Point((int) newX, (int) x + alice.height);
			}
		}
		return null;
	}

	public void Return() {
		type = type == 2 ? 1 : 0;
	}

	public double getY() {
		return y;
	}

	public int getVy() {
		return vy;
	}
}
