package com.badlogic.androidgames.framework2.impl;

import java.util.List;

import com.badlogic.androidgames.framework2.Input.TouchEvent;


import android.view.MotionEvent;
import android.view.View.OnTouchListener;

public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<TouchEvent> getTouchEvents();

}
