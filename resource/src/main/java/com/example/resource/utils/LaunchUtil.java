package com.example.resource.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by wp on 2018/7/4.
 */

public class LaunchUtil {
	
	public static void launchActivity(Context context, Class<? extends Activity> clazz) {
		launchActivity(context, clazz, null);
	}
	
	public static void launchActivity(Context context, Class<? extends Activity> clazz, HashMap<String, Object> params) {
		context.startActivity(createIntent(context, clazz, params));
	}
	
	public static void launchActivityForResult(Activity activity, Class<? extends Activity> clazz, int requestCode) {
		launchActivityForResult(activity, clazz, requestCode, null);
	}
	
	public static void launchActivityForResult(Activity activity, Class<? extends Activity> clazz, int requestCode, HashMap<String, Object> params) {
		activity.startActivityForResult(createIntent(activity, clazz, params), requestCode);
	}
	
	public static void launchActivityForResult(Fragment fragment, Class<? extends Activity> clazz, int requestCode) {
		launchActivityForResult(fragment, clazz, requestCode, null);
	}
	
	public static void launchActivityForResult(Fragment fragment, Class<? extends Activity> clazz, int requestCode, HashMap<String, Object> params) {
		fragment.startActivityForResult(createIntent(fragment.getContext(), clazz, params), requestCode);
	}
	
	private static Intent createIntent(Context context, Class<? extends Activity> clazz, HashMap<String, Object> params) {
		Intent intent = new Intent(context, clazz);
		if (params != null && params.size() > 0) {
			Set<HashMap.Entry<String, Object>> entrySet = params.entrySet();
			for (HashMap.Entry<String, Object> entry : entrySet) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value instanceof String) {
					intent.putExtra(key, (String) value);
				}
				if (value instanceof Boolean) {
					intent.putExtra(key, (boolean) value);
				}
				if (value instanceof Integer) {
					intent.putExtra(key, (int) value);
				}
				if (value instanceof Float) {
					intent.putExtra(key, (float) value);
				}
				if (value instanceof Double) {
					intent.putExtra(key, (double) value);
				}
				if (value instanceof Parcelable) {
					intent.putExtra(key, (Parcelable) value);
				}
				if (value instanceof Serializable) {
					intent.putExtra(key, (Serializable) value);
				}
				if (value instanceof ArrayList) {
					intent.putExtra(key, (ArrayList<Parcelable>) value);
				}
			}
		}
		
		return intent;
	}
}
