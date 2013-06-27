package com.badlogic.androidgames.framework2.impl;
/*
 * �����x�Z���T�[�̏���
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
		if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {		//�Z���T�[�̗L���̊m�F
			Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);		//�o�^�v���Z�X�������������ǂ���
		}
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//�����͉������Ȃ�
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
