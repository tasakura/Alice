package com.badlogic.androidgames.framework2.impl;

import java.io.IOException;

import com.badlogic.androidgames.framework2.Music;


import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AndroidMusic implements Music, OnCompletionListener{
	MediaPlayer mediaPlayer;
	boolean isPrepared = false;
	
	public AndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
									  assetDescriptor.getStartOffset(),
									  assetDescriptor.getLength());
			mediaPlayer.prepare();	//����
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		}catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	public void onCompletion(MediaPlayer mp) {	//�Đ����I��������̃C�x���g
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void play() {
		if(mediaPlayer.isPlaying())
			return ;
		
		try {
			synchronized (this) {			//�X���b�h�̔r������
				if(!isPrepared)
					mediaPlayer.prepare();
				mediaPlayer.start();
			}
		}catch (IllegalStateException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		mediaPlayer.stop();
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void pause() {					//�Đ������ǂ����`�F�b�N���A�Đ����Ȃ�ꎞ��~
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(isLooping());
	}

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	public boolean isPlaying() {			//�Đ�
		return mediaPlayer.isPlaying();
	}

	public boolean isStopped() {			//��~���Ă��邩�Ԃ�
		return !isPrepared;
	}

	public boolean isLooping() {			//���[�v
		return mediaPlayer.isLooping();
	}

	public void dispose() {					//�p��
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();		//�J��
	}

}
