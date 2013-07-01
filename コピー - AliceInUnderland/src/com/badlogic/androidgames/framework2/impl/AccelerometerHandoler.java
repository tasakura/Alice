package com.badlogic.androidgames.framework2.impl;
/*
 * ・ｽ・ｽ・ｽ・ｽ・ｽx・ｽZ・ｽ・ｽ・ｽT・ｽ[・ｽﾌ擾ｿｽ・ｽ・ｽ
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
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {		//・ｽZ・ｽ・ｽ・ｽT・ｽ[・ｽﾌ有・ｽ・ｽ・ｽﾌ確・ｽF
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);		//・ｽo・ｽ^・ｽv・ｽ・ｽ・ｽZ・ｽX・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾇゑｿｽ・ｽ・ｽ
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//・ｽ・ｽ・ｽ・ｽ・ｽﾍ会ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾈゑｿｽ
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
