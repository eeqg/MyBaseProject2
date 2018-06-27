package com.example.resource.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by wp on 2018/6/25.
 */

public class BaseApp extends Application {
	/** 屏幕宽度 */
	public static int SCREEN_WIDTH;
	/** 屏幕高度 */
	public static int SCREEN_HEIGHT;
	
	public static BaseApp INSTANCE;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		INSTANCE = this;
		
		initScreenSize();
		initFresco();
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
	
	/**
	 * toast提示
	 *
	 * @param text 提示内容
	 */
	public static void toast(String text) {
		toast(text, Gravity.CENTER);
	}
	
	/**
	 * toast提示
	 *
	 * @param text    提示内容
	 * @param gravity 显示位置
	 */
	public static void toast(String text, int gravity) {
		if (android.text.TextUtils.isEmpty(text)) {
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
}
