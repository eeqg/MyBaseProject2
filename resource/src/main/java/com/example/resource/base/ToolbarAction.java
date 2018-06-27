package com.example.resource.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.resource.R;

public class ToolbarAction {
	private Drawable drawable;
	private String text;
	private int textColor;
	private View.OnClickListener listener;
	
	/**
	 * 构造方法
	 */
	private ToolbarAction() {
	}
	
	/**
	 * 创建返回工具栏操作
	 *
	 * @param context 设备上下文环境
	 * @return 工具栏操作
	 */
	public static ToolbarAction createBack(Context context) {
		return createBack(context, ContextCompat.getDrawable(context, R.mipmap.ic_back_white));
	}
	
	/**
	 * 创建返回工具栏操作
	 *
	 * @param context 设备上下文环境
	 * @param resId   图标资源ID
	 * @return 工具栏操作
	 */
	public static ToolbarAction createBack(Context context, int resId) {
		return createBack(context, ContextCompat.getDrawable(context, resId));
	}
	
	/**
	 * 创建返回工具栏操作
	 *
	 * @param context  设备上下文环境
	 * @param drawable 图标
	 * @return 工具栏操作
	 */
	public static ToolbarAction createBack(final Context context, Drawable drawable) {
		return ToolbarAction.createIcon(drawable)
				.setListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (context instanceof Activity) {
							((Activity) context).onBackPressed();
						}
					}
				});
	}
	
	/**
	 * 创建图标工具栏操作
	 *
	 * @param context 设备上下文环境
	 * @param resId   图标资源ID
	 * @return 工具栏操作
	 */
	public static ToolbarAction createIcon(Context context, int resId) {
		return createIcon(ContextCompat.getDrawable(context, resId));
	}
	
	/**
	 * 创建图标工具栏操作
	 *
	 * @param drawable 图标
	 * @return 工具栏操作
	 */
	public static ToolbarAction createIcon(Drawable drawable) {
		ToolbarAction toolbarAction = new ToolbarAction();
		toolbarAction.drawable = drawable;
		return toolbarAction;
	}
	
	/**
	 * 创建文字工具栏操作
	 *
	 * @param context   设备上下文环境
	 * @param textResId 文字资源ID
	 * @return 工具栏操作
	 */
	public static ToolbarAction createText(Context context, int textResId) {
		return createText(context.getString(textResId));
	}
	
	/**
	 * 创建文字工具栏操作
	 *
	 * @param text 文字
	 * @return 工具栏操作
	 */
	public static ToolbarAction createText(String text) {
		ToolbarAction toolbarAction = new ToolbarAction();
		toolbarAction.text = text;
		return toolbarAction;
	}
	
	/**
	 * 创建文字工具栏操作
	 *
	 * @param context   设备上下文环境
	 * @param textResId 文字资源ID
	 * @param textColor 文字颜色值
	 * @return 工具栏操作
	 */
	public static ToolbarAction createText(Context context, int textResId, @ColorInt int textColor) {
		return createText(context.getString(textResId), textColor);
	}
	
	/**
	 * 创建文字工具栏操作
	 *
	 * @param text      文字
	 * @param textColor 文字颜色值
	 * @return 工具栏操作
	 */
	public static ToolbarAction createText(String text, @ColorInt int textColor) {
		ToolbarAction toolbarAction = new ToolbarAction();
		toolbarAction.text = text;
		toolbarAction.textColor = textColor;
		return toolbarAction;
	}
	
	/**
	 * 获取图标
	 *
	 * @return 图标
	 */
	public Drawable getDrawable() {
		return drawable;
	}
	
	/**
	 * 设置图标
	 *
	 * @param drawable 图标
	 */
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	
	/**
	 * 获取文字
	 *
	 * @return 文字
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 设置文字
	 *
	 * @param text 文字
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 获取文字颜色值
	 *
	 * @return 文字颜色值
	 */
	public int getTextColor() {
		return textColor;
	}
	
	/**
	 * 设置文字颜色值
	 *
	 * @param textColor 文字颜色值
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	
	/**
	 * 获取工具栏操作监听器
	 *
	 * @return 监听器
	 */
	public View.OnClickListener getListener() {
		return listener;
	}
	
	/**
	 * 设置工具栏操作监听器
	 *
	 * @param listener 监听器
	 * @return 工具栏操作
	 */
	public ToolbarAction setListener(View.OnClickListener listener) {
		this.listener = listener;
		return this;
	}
}
