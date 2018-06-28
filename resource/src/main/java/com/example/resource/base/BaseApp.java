package com.example.resource.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.resource.manager.EventBusManager;
import com.example.resource.utils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by wp on 2018/6/25.
 */

public class BaseApp extends Application {
	private static final String TAG = "BaseApp";
	
	/** 屏幕宽度 */
	public static int SCREEN_WIDTH;
	/** 屏幕高度 */
	public static int SCREEN_HEIGHT;
	
	public static BaseApp INSTANCE;
	private static Boolean isDebug = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		INSTANCE = this;
		
		syncIsDebug();
		initScreenSize();
		initFresco();
		
		EventBusManager.register(this);
	}
	
	/**
	 * 获取屏幕大小
	 */
	private void initScreenSize() {
		final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		final Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		boolean isPortrait = displayMetrics.widthPixels < displayMetrics.heightPixels;
		SCREEN_WIDTH = isPortrait ? displayMetrics.widthPixels : displayMetrics.heightPixels;
		SCREEN_HEIGHT = isPortrait ? displayMetrics.heightPixels : displayMetrics.widthPixels;
	}
	
	/**
	 * 初始化Fresco
	 */
	private void initFresco() {
		Fresco.initialize(this, ImagePipelineConfig.newBuilder(this)
				.setDownsampleEnabled(true)
				// .setMainDiskCacheConfig(
				// 		DiskCacheConfig.newBuilder(this)
				// 				.setIndexPopulateAtStartupEnabled(true)
				// 				.build()
				// )
				.build());
	}
	
	private void syncIsDebug() {
		if (getApplicationInfo() != null) {
			isDebug = (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		}
	}
	
	public static boolean isDebug() {
		return isDebug == null ? false : isDebug;
	}
	
	/**
	 * toast提示
	 *
	 * @param text 提示内容
	 */
	public static void toast(String text) {
		LogUtils.d(TAG, "toast---text="+text);
		toast(text, Gravity.CENTER);
	}
	
	/**
	 * toast提示
	 *
	 * @param text    提示内容
	 * @param gravity 显示位置
	 */
	public static void toast(String text, int gravity) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		Toast toast = Toast.makeText(INSTANCE, text, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}
	
	/**
	 * toast提示
	 *
	 * @param resId 提示内容资源ID
	 */
	public static void toast(int resId) {
		toast(resId, Gravity.CENTER);
	}
	
	/**
	 * toast提示
	 *
	 * @param resId   提示内容资源ID
	 * @param gravity 显示位置
	 */
	public static void toast(int resId, int gravity) {
		Toast toast = Toast.makeText(INSTANCE, resId, Toast.LENGTH_SHORT);
		toast.setGravity(gravity, 0, 0);
		toast.show();
	}
	
	@Subscribe
	public void handleEvent(EventBusManager.Event event) {
		LogUtils.d(TAG, "------------"+event.key);
		if (event.key == EventBusManager.EVENT_KEY_NETWORK_UNAVAILABLE) {
			LogUtils.d(TAG, "receive event--EVENT_KEY_NETWORK_UNAVAILABLE");
			toast("請檢查網絡!!");
			Toast.makeText(BaseApp.INSTANCE, "----------------------", Toast.LENGTH_LONG).show();
		}
	}
}
