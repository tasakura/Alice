package com.badlogic.androidgames.framework2.impl;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.androidgames.framework2.Pool;
import com.badlogic.androidgames.framework2.Input.KeyEvent;
import com.badlogic.androidgames.framework2.Pool.PoolObjectFactory;

import android.view.View;
import android.view.View.OnKeyListener;


public class KeyboardHandler implements OnKeyListener{
	boolean[] pressedKeys = new boolean[128];					//�e�L�[�̌��݂̏�Ԃ��i�[ on:false off:true
	Pool<KeyEvent> keyEventPool;								//KeyEvent�N���X�̃C���X�^���X���Ǘ�����Pool
	List<KeyEvent> keyEventBuffer = new ArrayList<KeyEvent>();	//KeyEvent���i�[
	List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();		//KeyBoradHandler�N���X��getKeyEvents()���\�b�h���Ăяo�����ɕԂ����KeyEvent���i�[

	public KeyboardHandler(View view) {
		PoolObjectFactory<KeyEvent> factory = new PoolObjectFactory<KeyEvent>() {
			public KeyEvent createObject() {
				return new KeyEvent();
			}
		};
		keyEventPool = new Pool<KeyEvent>(factory, 100);
		view.setOnKeyListener(this);
		view.setFocusableInTouchMode(true);						//�t�H�[�J�X���擾�\���ݒ�
		view.requestFocus();									//�t�H�[�J�X�𓖂Ă�
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
