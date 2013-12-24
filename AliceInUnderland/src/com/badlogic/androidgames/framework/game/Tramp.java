package com.badlogic.androidgames.framework.game;

import android.graphics.Point;
import android.graphics.Rect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class Tramp extends Sprite {

	private int state = 0; // 0=生存, 1=爆発中, 2=死亡後

	private static final double SPEED = 5;
	static final float TICK_INITIAL = 0.5f;
	static float tick = TICK_INITIAL; // 更新速度

	private double vx;
	private double vy;
	private int direction = 0; // 右=1 左=2
	private float tickTime = 0;
	private int image_swich = 1;
	private int version;
	private boolean onGround = false;
	private Rect src[][] = new Rect[2][2];
	private World world;

	public Tramp(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.x = x;
		this.y = y;
		this.width = 50;
		this.height = 50;
		this.vx = SPEED;
		this.world = world;
		src[0][0] = new Rect(0, 0, width, height);
		src[0][1] = new Rect(width, 0, width * 2, height);
		src[1][0] = new Rect(0, height, width, height * 2);
		src[1][1] = new Rect(width, height, width * 2, height * 2);
	}

	@Override
	public void update() {
		if (state == 0) {
			// 重力で下向きに加速度がかかる
			vy += world.getGravity();
			Point tile;
			if (world.getTileCollision(this, x + 1, y + 1) != null)
				onGround = true;
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
			if (onGround
					&& world.getTileCollision(this, x + vx, y + vy) == null) {
				vx = -vx;
			} else {
				// y方向の当たり判定
				double newY = y + vy; // 移動先座標を求める
				// 移動先座標で衝突するタイルの位置を処理
				// y方向だけ考えるのでx座標は変化しないと仮定
				tile = world.getTileCollision(this, x, newY);
				if (tile == null) {
					y = newY; // 衝突するタイルがなければ移動
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
			direction = vx > 0 ? 0 : 1;
		}
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;
			switch (state) {
			case 0:
				image_swich = image_swich == 0 ? 1 : 0;
				break;
			case 1:
				version++;
				if (version > 4)
					state = 2;
				break;
			}
		}

		// 描画部分
		switch (state) {
		case 0:
			Rect dst = new Rect((int) getX() + offsetX, (int) getY() + offsetY
					+ 10, (int) (getX() + offsetX + width), (int) (getY()
					+ offsetY + 10 + height));
			g.drawPixmap(image, src[direction][image_swich], dst);
			break;
		case 1:
			int row = version / 2;
			int col = version - (row * 2);
			Rect src = new Rect(64 * col, 64 * row, (64 * col) + 64,
					(64 * row) + 64);
			dst = new Rect((int) (x + offsetX + 10), (int) (y + offsetY),
					(int) (x + offsetX + width), (int) (y + offsetY + height));
			g.drawPixmap(Assets.boms, src, dst);
			break;
		}
	}

	public int getMuki() {
		return direction;
	}

	public void setMuki(int muki) {
		this.direction = muki;
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		if (state == 1) {
			version = 0;
			tick = 0.1f;
		}
	}
}
