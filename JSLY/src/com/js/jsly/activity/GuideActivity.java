package com.js.jsly.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.js.jsly.R;
import com.js.jsly.adapter.GuidePagerAdapter;
import com.js.jsly.application.ContextApplication;

public class GuideActivity extends Activity implements OnClickListener {

	private Button beginButton;

	private ViewPager guideViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ContextApplication.firstInstall) {// 首次安装
			setContentView(R.layout.activity_guide);
			initUI();
		} else {
			redirectToSplash();
		}
	}

	/**
	 * 跳转到启动页
	 */
	private void redirectToSplash() {
		Intent intent = new Intent(getApplicationContext(),
				SplashActivity.class);
		startActivity(intent);
		finish();
	}

	private void initUI() {

		guideViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
		guideViewPager.setAdapter(new GuidePagerAdapter(new int[] {
				R.drawable.bg_guide_1, R.drawable.bg_guide_2,
				R.drawable.bg_guide_3, R.drawable.bg_guide_4 }, this));
		guideViewPager
				.setOnPageChangeListener(new SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {
						if (arg0 == 3) {
							beginButton.setVisibility(View.VISIBLE);
						} else {
							beginButton.setVisibility(View.GONE);
						}
					}
				});
		beginButton = (Button) findViewById(R.id.btn_begin);
		beginButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_begin:
			changeFirstInstall();
			ContextApplication.firstInstall=false;
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
			finish();
			break;
		}
	}
	
	private void changeFirstInstall(){
		Editor editor=ContextApplication.sp.edit();
		editor.putBoolean("firstInstall", false);
		editor.commit();
	}

}
