package com.badlogic.androidgames.framework.impl;

import java.io.IOException;

import com.badlogic.androidgames.framework.Music;

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
			mediaPlayer.prepare();	//準備
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		}catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	public void onCompletion(MediaPlayer mp) {	//再生が終わった時のイベント
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void play() {
		if(mediaPlayer.isPlaying())
			return ;
		
		try {
			synchronized (this) {			//スレッドの排他制御
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

	public void pause() {					//再生中かどうかチェックし、再生中なら一時停止
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(isLooping());
	}

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	public boolean isPlaying() {			//再生
		return mediaPlayer.isPlaying();
	}

	public boolean isStopped() {			//停止しているか返す
		return !isPrepared;
	}

	public boolean isLooping() {			//ループ
		return mediaPlayer.isLooping();
	}

	public void dispose() {					//廃棄時
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();		//開放
	}

}
