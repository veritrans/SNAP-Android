package com.midtrans.sdk.corekit.core;

import android.util.Log;

import com.midtrans.sdk.uikit.BuildConfig;

public class Logger {
    public static boolean enabled = false;

    public static final String TAG = "MidtransSDK";
    public static void d(String tag, String message) {
        if (enabled) {
            Log.d("" + tag, "" + message);
        }
    }

    public static void d(String message) {
        if (enabled) {
            Log.d(TAG, "" + message);
        }
    }


    public static void i(String tag, String message) {
        if (enabled) {
            Log.i("" + tag, "" + message);
        }
    }

    public static void i(String message) {
        if (enabled) {
            Log.i(TAG, "" + message);
        }
    }


    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e("" + tag, "" + message);
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("" + tag, "exeption:", throwable);
        }
    }

    public static void e(String message) {
        if (enabled) {
            Log.e(TAG, "" + message);
        }
    }

}
