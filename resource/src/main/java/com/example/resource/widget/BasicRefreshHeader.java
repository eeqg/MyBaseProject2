package com.example.resource.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.resource.R;
import com.example.resource.network.StatusInfo;
import com.kycq.library.refresh.RefreshHeader;

public class BasicRefreshHeader extends LinearLayout
		implements RefreshHeader<StatusInfo> {
	private ImageView ivStatus;
	private TextView tvStatus;
	
	private ObjectAnimator animator;
	
	public BasicRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.ivStatus = (ImageView) findViewById(R.id.ivStatus);
		this.tvStatus = (TextView) findViewById(R.id.tvStatus);
		
		animator = ObjectAnimator.ofFloat(this.ivStatus, "rotation", 0F, 3600F);
		animator.setInterpolator(new LinearInterpolator());
		animator.setRepeatCount(Animation.INFINITE);
		animator.setDuration(10000);
	}
	
	@Override
	public int getRefreshOffsetPosition() {
		return (int) (getMeasuredHeight() * 0.8);
	}
	
	@Override
	public void onRefreshScale(float scale) {
	}
	
	@Override
	public void onPullToRefresh() {
		this.animator.end();
		this.ivStatus.setRotationY(0);
		this.tvStatus.setText(R.string.pull_to_refresh);
	}
	
	@Override
	public void onReleaseToRefresh() {
		this.animator.end();
		this.ivStatus.setRotationY(0);
		this.tvStatus.setText(R.string.release_to_refresh);
	}
	
	@Override
	public void onRefresh() {
		this.animator.start();
		this.tvStatus.setText(R.string.loading_dot);
	}
	
	@Override
	public void onRefreshComplete(StatusInfo statusInfo) {
		this.animator.end();
		this.ivStatus.setRotationY(0);
		this.tvStatus.setText(R.string.refresh_complete);
	}
}
