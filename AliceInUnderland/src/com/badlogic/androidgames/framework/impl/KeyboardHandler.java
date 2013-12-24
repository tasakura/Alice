package com.badlogic.androidgames.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnKeyListener;

import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

public class KeyboardHandler implements OnKeyListener{
	boolean[] pressedKeys = new boolean[128];					//各キーの現在の状態を格納 on:false off:true
	Pool<KeyEvent> keyEventPool;								//KeyEventクラスのインスタンスを管理するPool
	List<KeyEvent> keyEventBuffer = new ArrayList<KeyEvent>();	//KeyEventを格納
	List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();		//KeyBoradHandlerクラスのgetKeyEvents()メソッドを呼び出し時に返されるKeyEventを格納

	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);						//フォーカスを取得可能か設定
		view.requestFocus();									//フォーカスを当てる
	}

	public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
		if(event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
		return false;
		
		synchronized (this) {
			KeyEvent keyEvent = keyEventPool.newObject();
			keyEvent.keyCode = keyCode;
			keyEvent.keyChar = (char) event.getUnicodeChar();
			if(event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				keyEvent.type = KeyEvent.KEY_DOWN;
				if(keyCode > 0 && keyCode < 127)
					pressedKeys[keyCode] = false;
			}
			keyEventBuffer.add(keyEvent);
		}
		return false;
	}
	
	public boolean isKeyPressed(int keyCode) {
		if(keyCode < 0 || keyCode > 127)
			return false;
		return pressedKeys[keyCode];
	}
	
	public List<KeyEvent> getKeyEvents() {
		synchronized (this) {
			int len = keyEvents.size();
			for(int i=0; i<len; i++)
				keyEventPool.free(keyEvents.get(i));
			keyEvents.clear();
			keyEvents.addAll(keyEventBuffer);
			keyEventBuffer.clear();
			return keyEvents;
		}
	}
	
}
