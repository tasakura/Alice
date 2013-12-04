package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Game;
import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Screen;
import com.badlogic.androidgames.framework2.Graphics.PixmapFormat;

public class LoadingScreen extends Screen {

	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.back_start = g.newPixmap("back_start.png", PixmapFormat.RGB565);
		Assets.back_play = g.newPixmap("back_play.png", PixmapFormat.RGB565);
		Assets.block = g.newPixmap("block.png", PixmapFormat.ARGB4444);
		/* 以下入力させる */
		Assets.alice_1 = g.newPixmap("alice_1.png", PixmapFormat.ARGB4444);
		Assets.alice_2 = g.newPixmap("alice_2.png", PixmapFormat.ARGB4444);
		Assets.alice_3 = g.newPixmap("alice_3.png", PixmapFormat.ARGB4444);
		Assets.alice_4 = g.newPixmap("alice_4.png", PixmapFormat.ARGB4444);
		/* ここまで */
		Assets.tramp_1 = g.newPixmap("tramp_1.png", PixmapFormat.ARGB4444);
		Assets.tramp_2 = g.newPixmap("tramp_2.png", PixmapFormat.ARGB4444);
		Assets.tramp_3 = g.newPixmap("tramp_3.png", PixmapFormat.ARGB4444);
		Assets.tramp_4 = g.newPixmap("tramp_4.png", PixmapFormat.ARGB4444);
		Assets.tramp2_1 = g.newPixmap("tramp2_1.png", PixmapFormat.ARGB4444);
		Assets.tramp2_2 = g.newPixmap("tramp2_2.png", PixmapFormat.ARGB4444);
		Assets.tramp2_3 = g.newPixmap("tramp2_3.png", PixmapFormat.ARGB4444);
		Assets.tramp2_4 = g.newPixmap("tramp2_4.png", PixmapFormat.ARGB4444);
		Assets.tea = g.newPixmap("tea.png", PixmapFormat.ARGB4444);
		Assets.gooldoor = g.newPixmap("gooldoor.png", PixmapFormat.ARGB4444);
		Assets.door = g.newPixmap("door.png", PixmapFormat.ARGB4444);
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
