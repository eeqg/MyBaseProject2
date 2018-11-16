package com.example.resource.dialog;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.example.resource.R;
import com.example.resource.databinding.DialogLoadingBinding;

public class LoadingDialog extends Dialog {
	/** 最少显示时间 */
	private static final long MIN_SHOW_TIME = 5000;
	
	private long showTime;
	private ObjectAnimator animator;
	
	public LoadingDialog(Context context) {
		super(context, R.style.DialogLoadingTheme);
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DialogLoadingBinding dataBinding = DataBindingUtil.inflate(
				getLayoutInflater(),
				R.layout.dialog_loading,
				null, false
		);
		setContentView(dataBinding.getRoot());
		
		animator = ObjectAnimator.ofFloat(dataBinding.ivLoading, "rotation", 0F, 3600F);
		animator.setInterpolator(new LinearInterpolator());
		animator.setRepeatCount(Animation.INFINITE);
		animator.setDuration(10000);
	}
	
	@Override
	public void show() {
		super.show();
		animator.start();
		this.showTime = System.currentTimeMillis();
	}
	
	@Override
	public void dismiss() {
		animator.end();
		super.dismiss();
	}
	
	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - this.showTime >= MIN_SHOW_TIME) {
			super.onBackPressed();
		}
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		if (getWindow() == null) {
			return;
		}
		
		float density = getContext().getResources().getDisplayMetrics().density;
		int size = (int) (100.0 * density);
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.width = size;
		getWindow().setAttributes(layoutParams);
	}
}
