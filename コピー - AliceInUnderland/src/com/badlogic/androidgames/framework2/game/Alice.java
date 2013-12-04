package com.badlogic.androidgames.framework2.game;


import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

import android.graphics.Point;
import android.util.Log;

public class Alice extends Sprite {

	private static final int JUMP_SPEED = 15; // ジャンプ力
	private static final float TICK_INITIAL = 0.15f;
	private static float tick = TICK_INITIAL; // 更新速度

	private double vx;
	private double vy;
	private double speed; // 移動速度

	private boolean onGround = true;
	private boolean forceJump = false;
	private float tickTime = 0;

	private int direction = 1; // 右=1 左=2
	private int image_swich = 1;
	private World world;

	public Alice(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.x = x;
		this.y = y;
		this.width = 50;
		this.height = 50;
		this.speed = 7;
		this.world = world;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setMuki(int muki) {
		this.direction = muki;
	}

	public int getMuki() {
		return direction;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public boolean isForceJump() {
		return forceJump;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	// 左移動
	public void accelerateLeft() {
		direction = 2;
		vx = -speed;
	}

	// 右移動
	public void accelerateRight() {
		direction = 1;
		vx = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	// 停止時
	public void Cancel() {
		vx = 0;
	}

	// ジャンプ
	public void jump() {
		if (onGround || forceJump) {
			// 上向きに速度を加える
			vy = -JUMP_SPEED;
			onGround = false;
			forceJump = false;
		}
	}

	public void update() {
		// 重力で下向きに速度がかかる
		vy += world.getGravity();

		// x方向の当たり判定
		// 移動先座標を求める
		double newX = x + vx;
		// 移動先座標で衝突するタイルの位置を取得
		// x方向だけ考えるのでｘ座標は変化しないと家庭
		Point tile = world.getTileCollision(this, newX, y);
		if (tile == null) {
			// 衝突するブロックがなければ移動
			x = newX;
		} else {
			// 衝突するタイルがある場合
			if (vx > 0) {
				// 右へ移動中なので右ブロックと衝突
				// ブロックにのめりこむ or　隙間がないように位置調整
				x = world.tilesToPixels(tile.x) - width;
			} else if (vx < 0) {
				// 左へ移動中なので左のブロックと衝突
				// 位置調整
				x = world.tilesToPixels(tile.x + 1);
			}
		}

		// y方向の当たり判定
		double newY = y + vy;
		// 移動先座標で衝突するブロックの位置を取得
		// y方向だけ考えるのでx座標は変化しないと家庭
		tile = world.getTileCollision(this, x, newY);
		if (tile == null) {
			// 衝突するブロ ックがなければ移動
			y = newY;
			// 衝突していないということは空中
			onGround = false;
		} else {
			// 衝突するブロックがある場合
			if (vy > 0) {
				// 下へ移動中なので下のブロックと衝突（着地）
				// 位置調整
				y = world.tilesToPixels(tile.y) - height;
				// 着地したのでy方向速度を0に
				vy = 0;
				// 着地
				onGround = true;
			} else if (vy < 0) {
				// 上へ移動中なので上のブロックと衝突
				// 位置調整
				y = world.tilesToPixels(tile.y + 1);
				// 天井にぶつかったのでy方向速度を0に
				vy = 0;
			}
		}
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;
			if (image_swich == 1)
				image_swich = 2;
			else
				image_swich = 1;
		}
		switch (getMuki()) {
		case 1:
			if (image_swich == 1) {
				g.drawPixmap(Assets.alice_1, (int) getX() + offsetX,
						(int) getY() + offsetY + 10);
			} else {
				g.drawPixmap(Assets.alice_2, (int) getX() + offsetX,
						(int) getY() + offsetY + 10);
			}
			break;
		case 2:
			if (image_swich == 2) {
				g.drawPixmap(Assets.alice_3, (int) getX() + offsetX,
						(int) getY() + offsetY + 10);
			} else {
				g.drawPixmap(Assets.alice_4, (int) getX() + offsetX,
						(int) getY() + offsetY + 10);
			}
			break;
		}

	}

	public void setForceJump(boolean forceJump) {
		this.forceJump = forceJump;
	}
}
