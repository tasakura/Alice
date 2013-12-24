package com.badlogic.androidgames.framework.game;

public class MyUtil {
	private float PlayTime = 0;

	public String[] split(String strTarget, String token) {
		return strTarget.split("\\Q" + token + "\\E", -1);
	}


}
