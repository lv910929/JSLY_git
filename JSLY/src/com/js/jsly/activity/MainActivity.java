package com.js.jsly.activity;

import java.io.File;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.js.jsly.R;
import com.js.jsly.data.Constant;
import com.js.jsly.utils.FileUtils;
import com.js.jsly.utils.IHandler;
import com.js.jsly.webview.JavaScriptObject;
import com.js.jsly.webview.MyWebChromeClient;
import com.js.jsly.webview.MyWebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class MainActivity extends Activity implements IHandler,
		MyWebChromeClient.OpenFileChooserCallBack,
		MyWebChromeClient.LollipopFileCallBack {

	private static final int REQUEST_CAMERA = 1;
	private static final int REQUEST_CHOOSE = 2;

	ValueCallback<Uri> mUploadMessage;
	ValueCallback<Uri[]> mUploadMessagesAboveL;
	private Uri cameraUri;

	private WebView webView;

	private JavaScriptObject javaScriptObject;

	private MyWebViewClient myWebViewClient;

	private MyWebChromeClient myWebChromeClient;

	public static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		initHandler();
	}

	private void initUI() {
		webView = (WebView) findViewById(R.id.webview_main);
		setWebView();
		// 加载需要显示的网页
		webView.loadUrl(Constant.URL_STRING);
	}

	private void setWebView() {
		myWebChromeClient = new MyWebChromeClient(MainActivity.this,
				MainActivity.this);
		myWebViewClient = new MyWebViewClient(MainActivity.this);
		WebSettings webSettings = webView.getSettings();
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
		webSettings.setDomStorageEnabled(true);
		webView.setWebViewClient(myWebViewClient);
		webView.setWebChromeClient(myWebChromeClient);
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		if (Build.VERSION.SDK_INT >= 19) {
			webView.getSettings().setLoadsImagesAutomatically(true);
		} else {
			webView.getSettings().setLoadsImagesAutomatically(false);
		}
		addJavascriptInterface();
	}

	private void addJavascriptInterface() {
		webView.addJavascriptInterface(new JavaScriptObject(this), "bluet");
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Map<String, String> shareMap = (Map<String, String>) msg.obj;
					showShareDialog(shareMap);
					break;
				default:
					break;
				}
			}
		};
	}

	private void showShareDialog(Map<String, String> shareMap) {

		String title = shareMap.get("title");
		String content = shareMap.get("content");
		String url = shareMap.get("url");
		new ShareAction(this)
				.setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
						SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
						SHARE_MEDIA.WEIXIN_CIRCLE).withTitle(title)
				.withText(content).withTargetUrl(url)
				.setCallback(umShareListener).open();

	}

	@Override
	public void lollipopFileCallBack(ValueCallback<Uri[]> filePathCallback) {
		if (mUploadMessagesAboveL != null) {
			mUploadMessagesAboveL.onReceiveValue(null);
		} else {
			mUploadMessagesAboveL = filePathCallback;
			selectImage();
		}
	}

	@Override
	public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg,
			String acceptType) {
		if (mUploadMessage != null)
			return;
		mUploadMessage = uploadMsg;
		selectImage();
	}

	private void selectImage() {
		if (!FileUtils.checkSDcard(this)) {
			return;
		}
		String[] selectPicTypeStr = { "拍照", "图库" };
		new AlertDialog.Builder(this)
				.setOnCancelListener(new ReOnCancelListener())
				.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0: // 相机拍摄
									openCarcme();
									break;
								case 1:// 手机相册
									chosePicture();
									break;
								default:
									break;
								}
							}
						}).show();
	}

	/**
	 * 本地相册选择图片
	 */
	private void chosePicture() {
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		innerIntent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQUEST_CHOOSE);
	}

	/**
	 * 选择照片后结束
	 */
	private Uri afterChosePic(Intent data) {
		if (data != null) {
			final String path = data.getData().getPath();
			if (path != null
					&& (path.endsWith(".png") || path.endsWith(".PNG")
							|| path.endsWith(".jpg") || path.endsWith(".JPG"))) {
				return data.getData();
			} else {
				Toast.makeText(this, "上传的图片仅支持png或jpg格式", Toast.LENGTH_SHORT)
						.show();
			}
		}
		return null;
	}

	/**
	 * 打开照相机
	 */
	private void openCarcme() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/JSLY/Images/" + (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {
			return;
		}
		if (mUploadMessagesAboveL != null) {
			onActivityResultAboveL(requestCode, resultCode, data);
		}
		if (mUploadMessage == null)
			return;
		Uri uri = null;
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			uri = cameraUri;
		}
		if (requestCode == REQUEST_CHOOSE && resultCode == RESULT_OK) {
			uri = afterChosePic(data);
		}
		mUploadMessage.onReceiveValue(uri);
		mUploadMessage = null;
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 5.0以后机型 返回文件选择
	 */
	private void onActivityResultAboveL(int requestCode, int resultCode,
			Intent data) {

		Uri[] results = null;
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
			results = new Uri[] { cameraUri };
		}
		if (requestCode == REQUEST_CHOOSE && resultCode == RESULT_OK) {
			if (data != null) {
				String dataString = data.getDataString();
				if (dataString != null)
					results = new Uri[] { Uri.parse(dataString) };
			}
		}
		mUploadMessagesAboveL.onReceiveValue(results);
		mUploadMessagesAboveL = null;
		return;
	}

	/**
	 * dialog监听类
	 */
	private class ReOnCancelListener implements
			DialogInterface.OnCancelListener {
		@Override
		public void onCancel(DialogInterface dialogInterface) {
			if (mUploadMessage != null) {
				mUploadMessage.onReceiveValue(null);
				mUploadMessage = null;
			}

			if (mUploadMessagesAboveL != null) {
				mUploadMessagesAboveL.onReceiveValue(null);
				mUploadMessagesAboveL = null;
			}
		}
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(MainActivity.this, platform + " 分享成功啦",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(MainActivity.this, platform + " 分享失败啦",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(MainActivity.this, platform + " 分享取消了",
					Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 重写返回键点击事件
	 */
	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webView.canGoBack()) {
				webView.goBack();
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

	@Override
	protected void onDestroy() {
		if (webView != null) {
			webView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public Handler getHandler() {
		return handler;
	}
}
