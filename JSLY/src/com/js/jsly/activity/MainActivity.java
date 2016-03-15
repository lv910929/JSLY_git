package com.js.jsly.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.js.jsly.R;

public class MainActivity extends Activity {

	public static final String URL_STRING = "http://m.uu1.com/";

	private WebView mainWebView;
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
	}

	private void initUI() {
		mainWebView = (WebView) findViewById(R.id.webview_main);
		setWebView();
		// 加载需要显示的网页
		mainWebView.loadUrl(URL_STRING);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebView() {
		WebSettings webSettings = mainWebView.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(false);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// 启用数据库
		webSettings.setDatabaseEnabled(true);
		String dir = this.getApplicationContext()
				.getDir("database", MODE_PRIVATE).getPath();
		// 启用地理定位
		webSettings.setGeolocationEnabled(true);
		// 设置定位的数据库路径
		webSettings.setGeolocationDatabasePath(dir);
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		webSettings.setDomStorageEnabled(true);
		// 设置Web视图
		mainWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		mainWebView.setWebViewClient(new MyWebViewClient());
		mainWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});
		mainWebView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		if(Build.VERSION.SDK_INT >= 19) {
			mainWebView.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	    	mainWebView.getSettings().setLoadsImagesAutomatically(false);
	    }
	}

	/**
	 * 重写返回键点击事件
	 */
	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mainWebView.canGoBack()) {
				mainWebView.goBack();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
				}
				return true;
			}
		}
		return false;
	}

	// Web视图
	private class MyWebViewClient extends WebViewClient {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url != null && url.startsWith("mailto:")
					|| url.startsWith("geo:") || url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			mainWebView.loadUrl("file:///android_asset/404.html");
		}

		public void onLoadResource(WebView view, String url) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("加载中...");
				progressDialog.show();
			}
		}

		public void onPageFinished(WebView view, String url) {
			
			if(!mainWebView.getSettings().getLoadsImagesAutomatically()) {
				mainWebView.getSettings().setLoadsImagesAutomatically(true);
		    }
			try {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		if (mainWebView != null) {
			mainWebView.destroy();
		}
		super.onDestroy();
	}
}
