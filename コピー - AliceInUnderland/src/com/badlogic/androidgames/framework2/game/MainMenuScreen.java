package com.badlogic.androidgames.framework2.game;

import java.util.List;

import com.badlogic.androidgames.framework2.Game;
import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Screen;
import com.badlogic.androidgames.framework2.Input.TouchEvent;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;


public class MainMenuScreen extends Screen {

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
					Settings.mapload(game.getFileIO(), "map1.txt");
					game.setScreen(new GameScreen(game, 1, 'p'));
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
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}

}
