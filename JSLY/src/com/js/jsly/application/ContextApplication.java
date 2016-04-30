package com.js.jsly.application;

import com.umeng.socialize.PlatformConfig;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class ContextApplication extends Application {

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

		PlatformConfig.setWeixin("wxe775f3f54d2f0c8e",
				"40a23cae3751ab13ea074c87a9b4b1fd");
		// Î¢ÐÅ appid appsecret
		PlatformConfig.setSinaWeibo("3921700954",
				"04b48b094faeb16683c32669824ebdad");
		// ÐÂÀËÎ¢²© appkey appsecret
		PlatformConfig.setQQZone("100424468",
				"c7394704798a158208a74ab60104f0ba");
	}
}
