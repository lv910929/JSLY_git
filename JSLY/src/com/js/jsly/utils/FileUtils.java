package com.js.jsly.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class FileUtils {

	/**
	 * ���SD���Ƿ����
	 */
	public static boolean checkSDcard(Context context) {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(context, "������ֻ��洢����ʹ�ñ�����", Toast.LENGTH_SHORT)
					.show();
		}
		return flag;
	}
}
