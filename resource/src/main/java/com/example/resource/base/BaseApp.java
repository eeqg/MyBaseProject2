package com.example.resource.base;

import android.app.Application;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by wp on 2018/6/25.
 */

public class BaseApp extends Application {
	
	public static BaseApp INSTANCE;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		INSTANCE = this;
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
