package com.js.jsly.activity;

import com.js.jsly.R;
import com.js.jsly.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private AlphaAnimation start_anima;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		initData();
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.3f, 1.0f);
		start_anima.setDuration(2000);
		view.startAnimation(start_anima);
		start_anima.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				initIntener();
			}
		});
	}

	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用; false， 不可用
	 */
	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * 网络可用就调用下一步需要进行的方法， 网络不可用则需设置
	 */
	private void initIntener() {

		// 判断网络是否可用
		if (isOpenNetwork() == true) {
			// 网络可用，则开始加载
			redirectTo();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SplashActivity.this);
			builder.setTitle("没有可用的网络").setMessage("是否对网络进行设置?");

			builder.setPositiveButton("是",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = null;

							try {
								String sdkVersion = android.os.Build.VERSION.SDK;
								if (Integer.valueOf(sdkVersion) > 10) {
									intent = new Intent(
											android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								} else {
									intent = new Intent();
									ComponentName comp = new ComponentName(
											"com.android.settings",
											"com.android.settings.WirelessSettings");
									intent.setComponent(comp);
									intent.setAction("android.intent.action.VIEW");
								}
								SplashActivity.this.startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					})
					.setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Toast.makeText(SplashActivity.this,
											"网络异常，加载失败！", Toast.LENGTH_SHORT)
											.show();
								}
							}).show();

		}
	}

	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}

}
