package com.badlogic.androidgames.framework.game;

import java.util.List;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class MainMenuScreen extends Screen {

	private int WIDTH = 800;
	private int HEIGHT = 480;
	
	public MainMenuScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (isBounds(event, 70, 280, 230, 80)) {
					game.setScreen(new GameScreen(game));
				}
			}
			if (event.type == TouchEvent.TOUCH_UP) {
				if (isBounds(event, 590, 425, 200, 50)) {
					System.exit(0);
				}
			}
		}
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
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.back_start, 0, 0);
		g.drawPixmap(Assets.title, 10, 10);
		g.drawPixmap(Assets.alice_start, 320, 175);
		g.drawPixmap(Assets.play, 70, 280);
		g.drawPixmap(Assets.close, 590, 425);
	}

	@Override
	public void pause() {
		Settings.save(game.getFileIO());
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
