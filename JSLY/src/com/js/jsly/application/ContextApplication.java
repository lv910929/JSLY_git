package com.js.jsly.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ContextApplication extends Application{
	
	private static ContextApplication instance;
	
	public static ContextApplication getInstance() {
		return instance;
	}
	
	public static boolean firstInstall;

	public static SharedPreferences sp;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		sp = instance.getSharedPreferences("config", Context.MODE_PRIVATE);
		firstInstall = sp.getBoolean("firstInstall", true);
	}
}
