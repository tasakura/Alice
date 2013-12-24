package com.badlogic.androidgames.framework.game;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

import android.R.dimen;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Alice extends Sprite {

	private int state = 0; // 0=生存,1=アニメーション中,2=死亡
	private static final int JUMP_SPEED = 17; // ジャンプ力
	private static final float TICK_INITIAL = 0.3f;
	private static float tick = TICK_INITIAL; // 更新速度

	private double vx;
	private double vy;
	private double speed; // 移動速度

	private boolean onGround = true;
	private boolean forceJump = false;
	private float tickTime = 0;

	private int direction = 0; // 右=0 左=1
	private int image_swich = 1;
	private Rect[][] src = new Rect[2][2];
	private World world;

	private double point_start_x;
	private double point_start_y;

	private boolean anim_switch = false;
	private double anim_max_y;
	private int WorldHeight = 0;
	private int stock = 3;

	public Alice(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.x = point_start_x = x;
		this.y = point_start_y = y;
		this.width = 50;
		this.height = 50;
		this.speed = 7;
		this.world = world;
		src[0][0] = new Rect(0, 0, width, height);
		src[0][1] = new Rect(width, 0, width * 2, height);
		src[1][0] = new Rect(0, height, width, height * 2);
		src[1][1] = new Rect(width, height, width * 2, height * 2);
		WorldHeight = world.getHeight();
	}

	public void setWorld(World world, double x, double y) {
		this.world = world;
		this.x = point_start_x = x;
		this.y = point_start_y = y;
		WorldHeight = world.getHeight();
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

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getDirection() {
		return direction;
	}

	public void setAlicePotison() {
		this.x = point_start_x;
		this.y = point_start_y;
		state = 0;
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
		direction = 1;
		vx = -speed;
	}

	// 右移動
	public void accelerateRight() {
		direction = 0;
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
		if (state == 0) {
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
		} else if (state == 1) {
			forceJump = false;
			if (!anim_switch) {
				y -= 8;
				if (anim_max_y > y)
					anim_switch = true;
			} else {
				y += 8;
				if (y > WorldHeight) {
					state = 2;
					stock--;
					anim_switch = false;
				}

			}

		}
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		tickTime += deltaTime;
		if (state == 0)
			while (tickTime > tick) {
				tickTime -= tick;
				image_swich = image_swich == 0 ? 1 : 0;
			}
		Rect dst = new Rect((int) getX() + offsetX,
				(int) getY() + offsetY + 10, (int) (getX() + offsetX + width),
				(int) (getY() + offsetY + 10 + height));
		g.drawPixmap(Assets.alices, src[direction][image_swich], dst);
	}

	public void setForceJump(boolean forceJump) {
		this.forceJump = forceJump;
	}

	public int getStock() {
		return stock;
	}

	public void minusStock() {
		this.stock--;
	}

	public void plusStock() {
		this.stock++;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		double up_y = 100;
		if (state == 1)
			anim_max_y = y - up_y;
	}

}
