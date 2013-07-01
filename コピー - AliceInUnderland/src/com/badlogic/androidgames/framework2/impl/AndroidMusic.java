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
			mediaPlayer.prepare();	//・ｽ・ｽ・ｽ・ｽ
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		}catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}
	}
	
	public void onCompletion(MediaPlayer mp) {	//・ｽﾄ撰ｿｽ・ｽ・ｽ・ｽI・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌイ・ｽx・ｽ・ｽ・ｽg
		synchronized (this) {
			isPrepared = false;
		}
	}

	public void play() {
		if(mediaPlayer.isPlaying())
			return ;
		
		try {
			synchronized (this) {			//・ｽX・ｽ・ｽ・ｽb・ｽh・ｽﾌ排・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
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

	public void pause() {					//・ｽﾄ撰ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾇゑｿｽ・ｽ・ｽ・ｽ`・ｽF・ｽb・ｽN・ｽ・ｽ・ｽA・ｽﾄ撰ｿｽ・ｽ・ｽ・ｽﾈゑｿｽ齊橸ｿｽ・ｽ~
		if(mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(isLooping());
	}

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
	}

	public boolean isPlaying() {			//・ｽﾄ撰ｿｽ
		return mediaPlayer.isPlaying();
	}

	public boolean isStopped() {			//・ｽ・ｽ~・ｽ・ｽ・ｽﾄゑｿｽ・ｽ驍ｩ・ｽﾔゑｿｽ
		return !isPrepared;
	}

	public boolean isLooping() {			//・ｽ・ｽ・ｽ[・ｽv
		return mediaPlayer.isLooping();
	}

	public void dispose() {					//・ｽp・ｽ・ｽ
		if(mediaPlayer.isPlaying())
			mediaPlayer.stop();
		mediaPlayer.release();		//・ｽJ・ｽ・ｽ
	}

}
