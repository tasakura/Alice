package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

public class Door extends Sprite {
	private int Nextstage;
	private int Nowstage;
	private char number;
	private int[][] list = { { 0, 8 }, { 8, 5 }, { 1, 5 }, { 1, 9 }, { 2, 8 },
			{ 9, 6 }, { 6, 3 }, { 2, 5 }, { 4, 7 }, { 2, 3 } };

	public Door(double x, double y, Pixmap pixmap, int Nowstage, int number,
			World world) {
		super(x, y, pixmap, world);
		this.width = 60;
		this.height = 60;
		this.Nowstage = Nowstage;
		this.number = (char) number;
		this.Nextstage = Search(this.number);
	}

	public int getNowstage() {
		return Nowstage;
	}

	public char getNumber() {
		return number;
	}

	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		g.drawPixmap(image, (int) getX() + offsetX + 10, (int) getY() + offsetY
				+ 30);
	}

	public void update() {
	}

	public int getNextstage() {
		return Nextstage;
	}

	public int Search(char number) {
		int[] poss = { -1, -1 };
		for (int i = 0; i < 2; i++) {
			poss[i] = list[Integer.parseInt("" + number)][i];
			if (poss[i] != Nowstage)
				return poss[i];
		}
		return -1;
	}

}
