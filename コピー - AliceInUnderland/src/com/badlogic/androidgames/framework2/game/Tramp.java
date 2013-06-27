package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

import android.graphics.Point;
import android.util.Log;


public class Tramp extends Sprite {

	private static final double SPEED = 5;
	static final float TICK_INITIAL = 0.5f;
	static float tick = TICK_INITIAL; // 更新速度

	private double vx;
	private double vy;
	private int muki = 1; // 右=1 左=2
	private float tickTime = 0;
	private int image_swich = 1;
	private int version;
	private boolean onGround = false;

	private World world;

	public Tramp(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.x = x;
		this.y = y;
		this.width = 50;
		this.height = 50;
		this.vx = SPEED;
		this.world = world;
		if (pixmap == Assets.tramp_1) {
			version = 1;
		} else if (pixmap == Assets.tramp2_1) {
			version = 2;
		}
	}

	@Override
	public void update() {
		// 重力で下向きに加速度がかかる
		vy += world.getGravity();
		Point tile;
		if (world.getTileCollision(this, x + 1, y + 1) != null) 
			onGround = true;
//		if(onGround) Log.d("ONGROUND", "OK");
		if (onGround) {
			// x方向の当たり判定
			double newX = x + vx; // 移動先座標を求める
			// 移動先座標で衝突するタイルの位置を取得
			// x方向だけ考えるのでy座標は変化しないと家庭
			tile = world.getTileCollision(this, newX, y);
			if (tile == null) {
				// 衝突するタイルがなければ移動
				x = newX;
			} else {
				// 衝突するタイルがある場合
				if (vx > 0) {
					// 右へ移動中なので右のブロックと衝突
					// ブロックにめりこむ or 隙間がないように位置調整
					x = world.tilesToPixels(tile.x) - width;
				} else if (vx < 0) {
					// 左へ移動中なので左のブロックと衝突
					// 位置調整
					x = world.tilesToPixels(tile.x + 1);
				}
				// 移動方向を反転
				vx = -vx;
			}
		}
		if (onGround && world.getTileCollision(this, x + vx, y + vy) == null) {
			vx = -vx;
		} else {
			// y方向の当たり判定
			double newY = y + vy; // 移動先座標を求める
			// 移動先座標で衝突するタイルの位置を処理
			// y方向だけ考えるのでx座標は変化しないと仮定
			tile = world.getTileCollision(this, x, newY);
			if (tile == null) {
					y = newY;	// 衝突するタイルがなければ移動
			} else {
				// 衝突するタイルがある場合
				if (vy > 0) {
					// 下へ移動中なので下のブロックと衝突（着地）
					// 位置調整
					y = world.tilesToPixels(tile.y) - height;
					// 着地したのでy方向速度を0に
					vy = 0;
				} else if (vy < 0) {
					// 上へ移動中なので上のブロックと衝突
					// 位置調整
					y = world.tilesToPixels(tile.y + 1);
					// 天井にぶつかったのでy方向速度を０に
					vy = 0;
				}
			}
		}
		if (vx > 0) {
			muki = 1;
		} else {
			muki = 2;
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
		if (version == 1) {
			switch (getMuki()) {
			case 1:
				if (image_swich == 1) {
					g.drawPixmap(Assets.tramp_1, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				} else {
					g.drawPixmap(Assets.tramp_2, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				}
				break;
			case 2:
				if (image_swich == 2) {
					g.drawPixmap(Assets.tramp_3, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				} else {
					g.drawPixmap(Assets.tramp_4, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				}
				break;
			}
		} else if (version == 2) {
			switch (getMuki()) {
			case 1:
				if (image_swich == 1) {
					g.drawPixmap(Assets.tramp2_1, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				} else {
					g.drawPixmap(Assets.tramp2_2, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				}
				break;
			case 2:
				if (image_swich == 2) {
					g.drawPixmap(Assets.tramp2_3, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				} else {
					g.drawPixmap(Assets.tramp2_4, (int) getX() + offsetX,
							(int) getY() + offsetY + 10);
				}
				break;
			}
		}
	}

	public int getMuki() {
		return muki;
	}

	public void setMuki(int muki) {
		this.muki = muki;
	}

	public Point getPos() {
		return new Point((int) x, (int) y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
