package com.badlogic.androidgames.framework.game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

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

	private Alice alice;
	private World world;

	public GameScreen(Game game) {
		super(game);
		this.stage = 1;
		world = new World(game, stage);
		alice = new Alice(world.getPlayer_x(), world.getPlayer_y(),
				Assets.alices, world);
		// alice.setWorldHeight(world.getHeight());
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
		float WAITTIME = 2.0f;
		tickTime += deltaTime;
		if (tickTime > WAITTIME) {
			state = GameState.Running;
			tickTime = 0;
		}
		alice.Cancel();
		touch_direction = 0;
		touch = -1;
		touch_jump = 0;
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int cx = 100;
		int cy = 380;
		int cr = 100;
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			switch (event.type) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
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
					if (touch_jump == 0) {
						alice.jump();
						touch_jump = 1;
					}
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
				break;
			}
		}
		LinkedList sprites = world.getSprites();
		Iterator iterator = sprites.iterator(); // Iterator=コレクション内の要素を順番に取り出す方法
		while (iterator.hasNext()) { // iteratorの中で次の要素がある限りtrue
			Sprite sprite = (Sprite) iterator.next();
			if (alice.getState() == 0)
				sprite.update();
			if (alice.isCollision(sprite) && alice.getState() == 0) {
				if (sprite instanceof GoolDoor) {
					state = GameState.Clear;
					break;
				} else if (sprite instanceof Door) {
					Door door = (Door) sprite;
					int Nextstage = door.getNextstage();
					world = new World(game, Nextstage);
					alice.setWorld(world, world.getPlayer_x(),
							world.getPlayer_y());
					alice.setAlicePotison();
					stage = Nextstage;
					state = GameState.Ready;
				} else if (sprite instanceof Tramp) {
					Tramp tramp = (Tramp) sprite;
					if (tramp.getState() == 2) {
						sprites.remove(tramp); // 削除
						break;
					}
					// 上から踏まれてたら
					if ((int) alice.getY() < (int) tramp.getY() - 15) {
						if (tramp.getState() == 0)
							tramp.setState(1);
						// 踏むとプレイヤーは再ジャンプ
						 alice.setForceJump(true);
						 alice.jump_half();
						 break;
					} else {
						if (tramp.getState() == 0) {
							if (alice.getState() == 0)
								alice.setState(1);
						}
						break;
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
				} else if (sprite instanceof Block) {
					Block block = (Block) sprite;
//					alice.CollisionJudge(block);
//					alice.setY(block.y-alice.getHeight());
					break;
				}
			}
		}
		if (world.isWorld_end())
			if (alice.getState() == 0)
				alice.setState(1);

		// ゲームオーバー判定
		if (alice.getY() > world.getHeight()) {
			if (alice.getStock() > 1) {
				world = new World(game, stage);
				alice.setWorld(world, world.getPlayer_x(), world.getPlayer_y());
				alice.setAlicePotison();
				alice.minusStock();
				state = GameState.Ready;
				tickTime = 0;
			} else {
				state = GameState.GameOver;
			}
		}
		alice.update();
		if (alice.getState() == 0)
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
					world = new World(game, 1);
					alice.setAlicePotison();
					state = GameState.Ready;
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
		}
		world.draw(g, offsetX, offsetY);
		alice.draw(g, offsetX, offsetY, deltaTime);
	}

	private void drawReadyUI() {
		// TODO 準備中処理(描画)
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, WIDTH + 1, HEIGHT + 1, Color.BLACK);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(255, 255, 255));
		paint.setTextSize(50);
		g.drawTextAlp("STAGE " + stage, 300, 220, paint);
		Rect src = new Rect(0, 0, 50, 50);
		Rect dst = new Rect(320, 270, 320 + alice.width, 270 + alice.height);
		g.drawPixmap(Assets.alices, src, dst);
		g.drawTextAlp(" × " + alice.getStock(), 370, 310, paint);
	}

	private void drawRunningUI() {
		int cx = 100;
		int cy = 380;
		int cr = 100;
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
