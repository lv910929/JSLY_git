package com.js.jsly.webview;

import android.content.Context;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.js.jsly.activity.MainActivity;
import com.js.jsly.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lv on 2016/4/3.
 */
public class JavaScriptObject {

	Context mContext;

	public JavaScriptObject(Context mContext) {
		this.mContext = mContext;
	}

	/*
	 * JS调用android的实现微信分享
	 */
	@JavascriptInterface
	public void shareaction(String shareMsg) {
		Map<String, String> shareMap = parseShareMsg(shareMsg);
		Message message = MainActivity.handler.obtainMessage(0);
		message.obj = shareMap;
		message.sendToTarget();
	}

	private Map<String, String> parseShareMsg(String shareMsg) {

		Map<String, String> shareMap = new HashMap<>();
		if (!shareMsg.equals("")) {
			String[] shareStrings = shareMsg.split("&");
			String[] titleStrings = shareStrings[0].split("=");
			shareMap.put("title", StringUtil.decodeString(titleStrings[1]));
			String[] contentStrings = shareStrings[1].split("=");
			shareMap.put("content", StringUtil.decodeString(contentStrings[1]));
			String[] urlStrings = shareStrings[2].split("=");
			shareMap.put("url", StringUtil.decodeString(urlStrings[1]));
		}
		return shareMap;
	}

}
