package com.badlogic.androidgames.framework.impl;
/*
 * 加速度センサーの処理
 * */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerHandoler implements SensorEventListener{
	float accelX;
	float accelY;
	float accelZ;
	
	public AccelerometerHandoler(Context context) {
		SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {		//センサーの有無の確認
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);		//登録プロセスが成功したかどうか
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//ここは何もしない
	}

	public void onSensorChanged(SensorEvent event) {
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	public float getAccelX() {
		return accelX;
	}

	public float getAccelY() {
		return accelY;
	}

	public float getAccelZ() {
		return accelZ;
	}
	
	
}
