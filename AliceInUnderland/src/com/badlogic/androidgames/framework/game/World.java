package com.badlogic.androidgames.framework.game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.util.TimingLogger;

public class World {

	private static final int BLOCK_SIZE = 80;
	private static final float TICK_INITIAL = 1.0f;
	private static final double GRAVITY = 1.0; // 重力
	private static float tick = TICK_INITIAL; // 更新速度
	private int timelimit;
	private float tickTime;
	private int player_x = 0;
	private int player_y = 0;
	private boolean world_end;

	private int ROW;
	private int COL;
	private int width;
	private int height;
	private LinkedList sprites;
	private boolean timerFlag;

	private int map[][];

	public World(Game game, int stage) {
		sprites = new LinkedList();
		Settings.mapload(game.getFileIO(), "map" + stage + ".txt");
		Settings.load(game.getFileIO());
		load(stage);
		width = COL * BLOCK_SIZE;
		height = ROW * BLOCK_SIZE;
		tickTime = 0;
		world_end = false;
		timerFlag = true;
	}

	public void update(float deltaTime) {
		tickTime += deltaTime;
		while (tickTime > tick) {
			tickTime -= tick;
			if (timerFlag) {
				timelimit -= tick;
			}
		}
		if (timelimit < 0)
			world_end = true;
	}

	public void load(int stage) {
		BufferedReader br = Settings.getBR();
		try {
			// 行数を読み込む
			String line = br.readLine();
			ROW = Integer.parseInt(line);
			// 列数を読み込む
			line = br.readLine();
			COL = Integer.parseInt(line);
			line = br.readLine();
			timelimit = Integer.parseInt(line);
			// マップを作成
			map = new int[ROW][COL];
			for (int i = 0; i < ROW; i++) {
				line = br.readLine();
				String list[] = line.split(",");
				for (int j = 0; j < COL; j++) {
					map[i][j] = Integer.parseInt(list[j]);
					switch (map[i][j]) {
					case 0:
						player_x = tilesToPixels(j);
						player_y = tilesToPixels(i);
						break;
					case 31:
						sprites.add(new GoolDoor(tilesToPixels(j),
								tilesToPixels(i), Assets.doors, this));
						break;
					case 34:
						sprites.add(new Clock(tilesToPixels(j),
								tilesToPixels(i), Assets.clock, this));
						break;
					case 35:
						sprites.add(new TeaCap(tilesToPixels(j),
								tilesToPixels(i), Assets.tea, this));
						break;
					case 36:
						sprites.add(new Tramp(tilesToPixels(j),
								tilesToPixels(i), Assets.tramp_Blue, this));
						break;
					case 37:
						sprites.add(new Tramp(tilesToPixels(j),
								tilesToPixels(i), Assets.tramp_Red, this));
						break;
					default:
						if (0 < map[i][j] && map[i][j] < 31) {
							sprites.add(new Door(tilesToPixels(j),
									tilesToPixels(i), Assets.doors, Integer
											.parseInt("" + map[i][j]), this));
						}
						break;
					}
					// switch (map[i][j]) {
					// case 'G':
					// sprites.add(new GoolDoor(tilesToPixels(j),
					// tilesToPixels(i), Assets.gooldoor, this));
					// break;
					// case 't':
					// Random rnd = new Random();
					// int ran = rnd.nextInt(2);
					// switch (ran) {
					// case 0:
					// sprites.add(new Tramp(tilesToPixels(j),
					// tilesToPixels(i), Assets.tramp_1, this));
					// break;
					// case 1:
					// sprites.add(new Tramp(tilesToPixels(j),
					// tilesToPixels(i), Assets.tramp2_1, this));
					// break;
					// default:
					// sprites.add(new Tramp(tilesToPixels(j),
					// tilesToPixels(i), Assets.tramp_1, this));
					// break;
					// }
					// break;
					// case 'a':
					// sprites.add(new TeaCap(tilesToPixels(j),
					// tilesToPixels(i), Assets.tea, this));
					// break;
					// case 'c':
					// sprites.add(new Clock(tilesToPixels(j),
					// tilesToPixels(i), Assets.clock, this));
					// break;
					// case 'p':
					// player_x = tilesToPixels(j);
					// player_y = tilesToPixels(i);
					// break;
					//
					// default:
					// if (map[i][j] >= '0' && map[i][j] <= '9') {
					// sprites.add(new Door(tilesToPixels(j),
					// tilesToPixels(i), Assets.door, Integer
					// .parseInt("" + map[i][j]), this));
					// }
					// break;
					// }
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g, int offsetX, int offsetY) {
		// オフセットを元に描画範囲を求める
		int firstTileX = pixelsToTiles(-offsetX);
		int lastTileX = firstTileX + pixelsToTiles(g.getWidth()) + 1;

		int firstTileY = pixelsToTiles(-offsetY);
		int lastTileY = firstTileY + pixelsToTiles(g.getHeight()) + 1;

		// 描画範囲がマップの大きさより大きくならないように調整
		lastTileX = Math.min(lastTileX, COL);
		lastTileY = Math.min(lastTileY, ROW);
		for (int i = firstTileY; i < lastTileY; i++) {
			for (int j = firstTileX; j < lastTileX; j++) {
				switch (map[i][j]) {
				case 32:
					g.drawPixmap(Assets.block01, tilesToPixels(j) + offsetX,
							tilesToPixels(i) + offsetY);
					break;
				case 33:
					g.drawPixmap(Assets.block02, tilesToPixels(j) + offsetX,
							tilesToPixels(i) + offsetY);
					break;
				case 38:
					g.drawPixmap(Assets.block03, tilesToPixels(j) + offsetX,
							tilesToPixels(i) + offsetY);
					break;
				}
			}
		}
		g.drawPixmap(Assets.limit, 410, 0);
		int number10 = timelimit / 10;
		int number1 = timelimit - (number10 * 10);
		Rect src = new Rect(50 * number10, 0, 50 * number10 + 50, 55);
		Rect dst = new Rect(700, 0, 700 + 50, 55);
		if (number10 > 0)
			g.drawPixmap(Assets.numbers, src, dst);
		src = new Rect(50 * number1, 0, 50 * number1 + 50, 55);
		dst = new Rect(740, 0, 740 + 50, 55);
		g.drawPixmap(Assets.numbers, src, dst);
	}

	public Point getTileCollision(Sprite sprite, double newX, double newY) {
		boolean tramp_swich = false; // トランプ兵がブロックから落ちないように判断するflag
		newX = Math.ceil(newX);
		newY = Math.ceil(newY);
		double fromX = Math.min(sprite.getX(), newX);
		double fromY = Math.min(sprite.getY(), newY);
		double toX = Math.max(sprite.getX(), newX);
		double toY = Math.max(sprite.getY(), newY);

		if (sprite instanceof Tramp && sprite.getX() < newX) {
			fromX = sprite.getX() + sprite.getWidth();
		}
		if (sprite instanceof Alice && sprite.getX() < newX) {
			fromX = sprite.getX() + sprite.getWidth();
		}

		// ブロックの位置を取得
		int fromTileX = pixelsToTiles(fromX);
		int fromTileY = pixelsToTiles(fromY);
		int toTitleX = pixelsToTiles(toX + sprite.getWidth() - 1);
		int toTitleY = pixelsToTiles(toY + sprite.getHeight() - 1);

		// 衝突しているか調べる
		for (int x = fromTileX; x <= toTitleX; x++) {
			for (int y = fromTileY; y <= toTitleY; y++) {

				// 画面外は衝突
				if (x < 0 || x >= COL)
					return new Point(x, y);
				if (y < 0)
					return new Point(x, y);
				if (y >= ROW)
					return null;

				// ブロックがあったら衝突
				if (map[y][x] == 32 || map[y][x] == 33|| map[y][x] == 38)
					return new Point(x, y);
				if (sprite instanceof Tramp) {
					tramp_swich = true;
				}
			}
			if (tramp_swich)
				return null;
		}
		return null;
	}

	// ピクセル単位をタイル単位に変更する pixels ピクセル単位 　タイル単位
	public static int pixelsToTiles(double pixels) {
		return (int) Math.floor(pixels / BLOCK_SIZE);
	}

	// タイル単位をピクセル単位に変更する tiles タイル単 位 retun ピクセル単位
	public static int tilesToPixels(int tiles) {
		return (int) (tiles * BLOCK_SIZE);
	}

	public LinkedList getSprites() {
		return sprites;
	}

	public int getPlayer_x() {
		return player_x;
	}

	public int getPlayer_y() {
		return player_y;
	}

	public void plus_timelimit(int plus) {
		timelimit += plus;
	}

	public boolean isWorld_end() {
		return world_end;
	}

	public static double getGravity() {
		return GRAVITY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
