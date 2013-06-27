package com.badlogic.androidgames.framework2.game;

import com.badlogic.androidgames.framework2.Graphics;
import com.badlogic.androidgames.framework2.Pixmap;

public class GoolDoor extends Sprite {

	public GoolDoor(double x, double y, Pixmap pixmap, World world) {
		super(x, y, pixmap, world);
		this.width=60;
		this.height=60;
	}
	
	public void draw(Graphics g, int offsetX, int offsetY, float deltaTime) {
		g.drawPixmap(image, (int) getX() + offsetX+10, (int) getY()
				+ offsetY+30);
	}

	public void update(){
		
	}
}
