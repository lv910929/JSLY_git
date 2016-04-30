package com.js.jsly.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Lv on 2016/4/14.
 */
public class DialogUtils {

    private static ProgressDialog progressDialog;

    public static void showInfoDialog(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton("È·¶¨", null)
                .show();
    }

    public static void showWaitDialog(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void hideWaitDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static void releaseWaitDialog() {
        progressDialog = null;
    }
    
    

}

