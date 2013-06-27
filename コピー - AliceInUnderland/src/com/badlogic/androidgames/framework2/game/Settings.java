package com.badlogic.androidgames.framework2.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.androidgames.framework2.FileIO;

import android.content.res.Resources;
import android.test.suitebuilder.TestSuiteBuilder.FailedToCreateTests;
import android.util.Log;


public class Settings {
	public static BufferedReader maptxt;
	public static  void mapload(FileIO files, String fileName) {
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(files.readAsset(fileName)));
			maptxt = in;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public static BufferedReader getBR() {
		return maptxt;
	}
}
