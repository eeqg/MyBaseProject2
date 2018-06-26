package com.example.resource.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 日志工具类
 */
public class LogUtils {
	private static boolean mIsShowToast = true;
	private static boolean mIsFormat = true;
	private final static String TAG = "BaseProject";
	
	static {
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
				.methodCount(1)         // (Optional) How many method line to show. Default 2
				.methodOffset(1)        // (Optional) Hides internal method calls up to offset. Default 5
				// .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
				.tag(TAG)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
				.build();
		
		Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
	}
	
	public static void d(String tag, String msg) {
		if (mIsFormat) {
			Logger.t(tag).d(msg);
		} else {
			Log.d(tag, msg);
		}
		
	}
	
	public static void d(Object msg) {
		if (mIsFormat) {
			Logger.d(msg);
		} else {
			Log.d(TAG, msg + "");
		}
		
	}
	
	public static void i(String tag, String msg) {
		if (mIsFormat) {
			Logger.t(tag).i(msg);
		} else {
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		if (mIsFormat) {
			Logger.t(tag).w(msg);
		} else {
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (TextUtils.isEmpty(msg))
			return;
		
		if (mIsFormat) {
			Logger.t(tag).e(msg);
		} else {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String msg, Throwable throwable) {
		if (mIsFormat) {
			Logger.e(msg);
		} else {
			Log.e(TAG, msg);
		}
	}
	
	public static void json(String json) {
		if (mIsFormat) {
			Logger.json(json);
		} else {
			Log.d(TAG, json);
		}
	}
	
	/**
	 * 带toast的error日志输出
	 *
	 * @param act act
	 * @param msg 日志
	 */
	public static void errorWithToast(Activity act, String msg) {
		if (mIsFormat) {
			Logger.e(msg);
		} else {
			Log.e(TAG, msg);
		}
		showToast(act, msg);
	}
	
	/**
	 * 带toast的error日志输出
	 *
	 * @param act act
	 * @param msg 日志
	 */
	public static void errorWithToast(Activity act, String msg, Throwable throwable) {
		if (mIsFormat) {
			Logger.e(throwable, msg);
		} else {
			Log.e(TAG, msg);
		}
		showToast(act, msg);
	}
	
	/**
	 * 带toast的debug日志输出
	 *
	 * @param act act
	 * @param msg 日志
	 */
	public static void debugWithToast(Activity act, String msg) {
		if (mIsFormat) {
			Logger.d(msg);
		} else {
			Log.d(TAG, msg);
		}
		showToast(act, msg);
	}
	
	/**
	 * 带toast的debug日志输出
	 */
	public static void onClick() {
		if (mIsFormat) {
			Logger.d("*** onClick ***");
		} else {
			Log.d(TAG, "*** onClick ***");
		}
	}
	
	/**
	 * 带toast的debug日志输出
	 *
	 * @param msg 日志
	 */
	public static void onClick(String msg) {
		if (mIsFormat) {
			Logger.d("*** onClick ***" + msg);
		} else {
			Log.d(TAG, "*** onClick ***" + msg);
		}
	}
	
	/**
	 * 带toast的debug日志输出
	 *
	 * @param act act
	 * @param msg 日志
	 */
	public static void onClickWithToast(Activity act, String msg) {
		if (mIsFormat) {
			Logger.d("*** onClick ***" + msg);
		} else {
			Log.d(TAG, "*** onClick ***" + msg);
		}
		showToast(act, msg);
	}
	
	/**
	 * toast，带判断isShowToast和isDebugMode
	 *
	 * @param msg 内容
	 */
	private static void showToast(Context context, String msg) {
		if (mIsShowToast) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}
	
	public static void test(String msg) {
		if (mIsFormat) {
			Logger.d("test ==> " + msg);
		} else {
			Log.d(TAG, "test ==> " + msg);
		}
	}
	
	public static void testWithOutFormat(String msg) {
		Log.i("test", msg);
	}
	
	public static boolean isShowToast() {
		return mIsShowToast;
	}
	
	public static void setShowToast(boolean mIsShowToast) {
		LogUtils.mIsShowToast = mIsShowToast;
	}
}
