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
	 * ����������״̬�����ж�
	 * 
	 * @return true, ����; false�� ������
	 */
	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	/**
	 * ������þ͵�����һ����Ҫ���еķ����� ���粻������������
	 */
	private void initIntener() {

		// �ж������Ƿ����
		if (isOpenNetwork() == true) {
			// ������ã���ʼ����
			redirectTo();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SplashActivity.this);
			builder.setTitle("û�п��õ�����").setMessage("�Ƿ�������������?");

			builder.setPositiveButton("��",
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
					.setNegativeButton("��",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									Toast.makeText(SplashActivity.this,
											"�����쳣������ʧ�ܣ�", Toast.LENGTH_SHORT)
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
