package com.badlogic.androidgames.framework.game;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Screen;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.back_start = g.newPixmap("back_start.png", PixmapFormat.RGB565);
		Assets.back_play = g.newPixmap("back_play.png", PixmapFormat.RGB565);
		Assets.alices = g.newPixmap("alice.png", PixmapFormat.ARGB4444);
		Assets.block01 = g.newPixmap("block01.png", PixmapFormat.ARGB4444);
		Assets.block02 = g.newPixmap("block02.png", PixmapFormat.ARGB4444);
		Assets.block03 = g.newPixmap("block03.png", PixmapFormat.ARGB4444);
		Assets.tramp_Blue = g.newPixmap("tramp_Blue.png", PixmapFormat.ARGB4444);
		Assets.tramp_Red = g.newPixmap("tramp_Red.png", PixmapFormat.ARGB4444);
		Assets.tea = g.newPixmap("tea.png", PixmapFormat.ARGB4444);
		Assets.doors = g.newPixmap("doors.png", PixmapFormat.ARGB4444);
		Assets.clock = g.newPixmap("clock.png", PixmapFormat.ARGB4444);
		Assets.gameover = g.newPixmap("gameover.png", PixmapFormat.ARGB4444);
		Assets.alice_og = g.newPixmap("alice_og.png", PixmapFormat.ARGB4444);
		Assets.alice_gc = g.newPixmap("alice_gc.png", PixmapFormat.ARGB4444);
		Assets.alice_start = g.newPixmap("alice_start.png", PixmapFormat.ARGB4444);
		Assets.gameclear = g.newPixmap("gameclear.png", PixmapFormat.ARGB4444);
		Assets.play = g.newPixmap("play.png", PixmapFormat.ARGB4444);
		Assets.title = g.newPixmap("title.png", PixmapFormat.ARGB4444);
		Assets.close = g.newPixmap("close.png", PixmapFormat.ARGB4444);
		Assets.top = g.newPixmap("top.png", PixmapFormat.ARGB4444);
		Assets.continue_ = g.newPixmap("continue.png", PixmapFormat.ARGB4444);
		Assets.quit = g.newPixmap("quit.png", PixmapFormat.ARGB4444);
		Assets.boms = g.newPixmap("boms.png", PixmapFormat.ARGB4444);
		Assets.limit = g.newPixmap("limit.png", PixmapFormat.ARGB4444);
		Assets.numbers = g.newPixmap("numbers.png", PixmapFormat.ARGB4444);
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void present(float deltaTime) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void pause() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void resume() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void dispose() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
