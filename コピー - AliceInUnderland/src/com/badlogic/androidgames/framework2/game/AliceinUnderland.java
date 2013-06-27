package com.badlogic.androidgames.framework2.game;


import com.badlogic.androidgames.framework2.Screen;
import com.badlogic.androidgames.framework2.impl.AndroidGame;



public class AliceinUnderland extends AndroidGame{

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

}
