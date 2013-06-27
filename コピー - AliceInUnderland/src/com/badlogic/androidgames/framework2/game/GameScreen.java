package com.badlogic.androidgames.framework2.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.androidgames.framework2.Game;
import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Screen;
import com.badlogic.androidgames.framework2.Input.TouchEvent;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;


public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver, Clear
	}

	GameState state = GameState.Ready;

	private final int WIDTH = 800;
	private final int HEIGHT = 480;

	private float tickTime = 0;
	private int touch = 0;
	private int touch_direction = 0; // 1=right 2=left
	private int touch_jump = 0;
	private int stage;
	private char point;
	
	private Alice alice;
	private World world;

	public GameScreen(Game game, int stage, char point) {
		super(game);
		this.stage = stage;
		world = new World(stage, point);
		alice = new Alice(world.getPlayer_x(), world.getPlayer_y(),
				Assets.alice_1, world);
		this.point = point;
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		if (state == GameState.Ready)
			updateReady(touchEvents, deltaTime);
		if (state == GameState.Running)
			updateRunning(touchEvents, deltaTime);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);
		if (state == GameState.Clear)
			updateCrear(touchEvents);
	}

	private void updateReady(List<TouchEvent> touchEvents, float deltaTime) {
		tickTime += deltaTime;
		if (tickTime > 2) {
			state = GameState.Running;
		}
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int cx = 120;
		int cy = 360;
		int cr = 130;
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			switch (event.type) {
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN :
				if (isBounds(event, cx, cy - cr, cr, cy + cr)) {
					// （RIGHT処理）
					alice.accelerateRight();
					touch = event.pointer;
					touch_direction = 1;
				}
				if (isBounds(event, cx - cr, cy - cr, cr, cy + cr)) {
					// （LEFT処理）
					alice.accelerateLeft();
					touch = event.pointer;
					touch_direction = 2;
				}
				if (isBounds(event, 700 - 60, 350 - 60, 120, 120)) {
					// (Jumpボタン処理)
					alice.jump();
					touch_jump = 1;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (event.pointer == touch) {
					alice.Cancel();
					touch_direction = 0;
					touch = -1;
				} else {
					touch_jump = 0;
				}

			}
		}
		world.update(deltaTime);
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			switch (event.type) {
			case TouchEvent.TOUCH_UP:
				if (isBounds(event, 70, 160, 380, 100)) {
					Settings.mapload(game.getFileIO(), "map1.txt");
					game.setScreen(new GameScreen(game, 1, 'p'));
					break;
				}
				if (isBounds(event, 550, 160, 200, 100)) {
					game.setScreen(new MainMenuScreen(game));
					break;
				}
			}
		}
	}

	private void updateCrear(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			switch (event.type) {
			case TouchEvent.TOUCH_UP:
				if (isBounds(event, 660, 410, 140, 60)) {
					game.setScreen(new MainMenuScreen(game));
					break;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running) {
			drawWorld(world, deltaTime);
			drawRunningUI();
		}
		if (state == GameState.GameOver)
			drawGameOverUI();
		if (state == GameState.Clear)
			drawClearUI();
	}

	private void drawWorld(World world, float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.back_play, 0, 0);

		// ゲームステータス判定系
		if (alice.getY() > world.getHeight() || world.isWorld_end()) {
			state = GameState.GameOver;
		}

		alice.update();
		// マップの端ではスクロールしないようにする
		// x方向のオフセットを計算
		int offsetX = WIDTH / 2 - (int) alice.getX();
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, WIDTH - world.getWidth());

		// y方向のオフセットを計算
		int offsetY = HEIGHT / 2 - (int) alice.getY();
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, HEIGHT - world.getHeight());

		// Spriteとの当たり判定
		LinkedList sprites = world.getSprites();
		Iterator iterator = sprites.iterator(); // Iterator=コレクション内の要素を順番に取り出す方法
		while (iterator.hasNext()) { // iteratorの中で次の要素がある限りtrue
			Sprite sprite = (Sprite) iterator.next();
			sprite.draw(g, offsetX, offsetY, deltaTime);
			sprite.update();
			if (alice.isCollision(sprite)) {
				if (sprite instanceof GoolDoor) {
					state = GameState.Clear;
					break;
				} else if (sprite instanceof Door) {
					Door door = (Door) sprite;
					int Nextstage = door.getNextstage();
					char point = door.getNumber();
					setStage(Nextstage, point);
				} else if (sprite instanceof Tramp) {
					Tramp slime = (Tramp) sprite;
					// 上から踏まれてたら
					if ((int) alice.getY() < (int) slime.getY() - 15) {
						// スライムは消える
						sprites.remove(slime);
						// 踏むとプレイヤーは再ジャンプ
						alice.setForceJump(true);
						alice.jump();
						break;
					} else {
						state = GameState.GameOver;
					}
				} else if (sprite instanceof TeaCap) {
					// アイテムは消える
					TeaCap teacap = (TeaCap) sprite;
					sprites.remove(teacap);
					teacap.use(alice);
					break;
				} else if (sprite instanceof Clock) {
					Clock clock = (Clock) sprite;
					sprites.remove(clock);
					clock.use(world);
					break;
				}
			}
		}
		alice.draw(g, offsetX, offsetY, deltaTime);
		world.draw(g, offsetX, offsetY);
	}

	public GameState getState() {
		return state;
	}

	private void drawReadyUI() {
		// TODO 準備中処理(描画)
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, WIDTH + 1, HEIGHT + 1, Color.BLACK);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(50);
		g.drawTextAlp("STAGE " + stage, 300, 220, paint);
	}

	private void drawRunningUI() {
		int cx = 130;
		int cy = 350;
		int cr = 120;
		Graphics g = game.getGraphics();
		// TODO ゲーム中処理(描画)
		Paint paint = new Paint();
		paint.setColor(Color.argb(127, 200, 200, 200));
		paint.setStyle(Paint.Style.FILL);
		g.drawController(cx, cy, cr, paint, Color.WHITE, Color.YELLOW,
				touch_direction);
		if (touch_jump == 1) {
			paint.setColor(Color.argb(127, 255, 255, 0));
		} else {
			paint.setColor(Color.argb(127, 255, 255, 255));
		}
		g.drawCircle(700, 350, 60, paint);
	}

	private void drawPausedUI() {
	}

	private void drawGameOverUI() {
		// TODO ゲームオーバー画面中処理
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.back_start, 0, 0);
		g.drawPixmap(Assets.gameover, 50, 10);
		g.drawPixmap(Assets.continue_, 70, 160);
		g.drawPixmap(Assets.quit, 550, 160);
		g.drawPixmap(Assets.alice_og, 20, 270);
	}

	private void drawClearUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.back_start, 0, 0);
		g.drawPixmap(Assets.gameclear, 100, 30);
		g.drawPixmap(Assets.alice_gc, 190, 100);
		g.drawPixmap(Assets.top, 660, 410);
	}

	private boolean isBounds(TouchEvent event, int x, int y, int width,
			int height) { // 矩系領域
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private void setStage(int Nextstage, char point) {
		String fileName;
		fileName = "map" + Nextstage + ".txt";
		Settings.mapload(game.getFileIO(), fileName);
		game.setScreen(new GameScreen(game, Nextstage, point));
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
