package com.example.resource.base;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.resource.R;
import com.example.resource.network.BaseBean;
import com.example.resource.network.StatusInfo;

import cn.shyman.library.refresh.RecyclerAdapter;

public abstract class BaseRecyclerAdapter<AdapterInfo> extends RecyclerAdapter {
	/** 初始页码 */
	private final int initPage = 1;
	/** 当前页码 */
	private int currentPage = initPage;
	
	private AdapterInfo completeAdapterInfo;
	protected AdapterInfo adapterInfo;
	
	/**
	 * 重置数据信息并刷新
	 */
	public final void forceRefresh() {
		resetAdapterInfo(null);
		swipeRefresh();
	}
	
	/**
	 * 获取初始页码
	 *
	 * @return 初始页码
	 */
	public final int getInitPage() {
		return this.initPage;
	}
	
	/**
	 * 获取当前页码
	 *
	 * @return 当前页码
	 */
	public final int getCurrentPage() {
		return this.currentPage;
	}
	
	@Override
	protected void notifyRefreshReady() {
		this.currentPage = this.initPage;
		resetAdapterInfo(null);
		super.notifyRefreshReady();
	}
	
	@Override
	protected void notifyRefresh() {
		this.currentPage = this.initPage;
		super.notifyRefresh();
	}
	
	public void swipeResult(AdapterInfo adapterInfo) {
		this.completeAdapterInfo = adapterInfo;
		if (adapterInfo instanceof BaseBean) {
			StatusInfo statusInfo = ((BaseBean) adapterInfo).statusInfo;
			swipeStatus(statusInfo);
		}
	}
	
	public void swipeStatus(StatusInfo statusInfo) {
		boolean isRefreshing = this.currentPage == initPage;
		if (statusInfo != null && statusInfo.isSuccessful()) {
			if (isRefreshing) {
				resetAdapterInfo(this.completeAdapterInfo);
			} else if (this.completeAdapterInfo != null) {
				int oldItemCount = getItemCount();
				updateAdapterInfo(this.completeAdapterInfo);
				int newItemCount = getItemCount();
				notifyItemRangeInserted(oldItemCount, newItemCount - oldItemCount);
			}
			super.swipeComplete(statusInfo);
			if (hasMore()) {
				swipeLoadReady();
			}
		} else {
			if (this.currentPage == initPage) {
				resetAdapterInfo(null);
			}
			super.swipeComplete(statusInfo);
		}
		this.completeAdapterInfo = null;
	}
	
	@Override
	protected void notifyRefreshComplete(Object statusInfo) {
		if (statusInfo instanceof StatusInfo) {
			if (((StatusInfo) statusInfo).isSuccessful()) {
				this.currentPage++;
			}
		}
		super.notifyRefreshComplete(statusInfo);
	}
	
	@Override
	protected void notifyLoadComplete(Object statusInfo) {
		if (statusInfo instanceof StatusInfo) {
			if (((StatusInfo) statusInfo).isSuccessful()) {
				this.currentPage++;
			}
		}
		super.notifyLoadComplete(statusInfo);
	}
	
	/**
	 * 重置数据信息
	 *
	 * @param adapterInfo 数据信息
	 */
	protected void resetAdapterInfo(AdapterInfo adapterInfo) {
		this.adapterInfo = adapterInfo;
		notifyDataSetChanged();
	}
	
	/**
	 * 更新数据信息
	 *
	 * @param adapterInfo 数据信息
	 */
	protected abstract void updateAdapterInfo(@NonNull AdapterInfo adapterInfo);
	
	/**
	 * 获取指定位置数据
	 *
	 * @param position 指定位置
	 * @return 数据信息
	 */
	public Object getItem(int position) {
		return null;
	}
	
	/**
	 * 判断是否需加载更多
	 */
	public abstract boolean hasMore();
	
	@Override
	public RefreshHolder<StatusInfo> onCreateRefreshHolder() {
		return new BasicRefreshHolder(R.mipmap.img_data_empty, R.string.data_empty);
	}
	
	@Override
	public LoadHolder<StatusInfo> onCreateLoadHolder() {
		return new LoadHolder<StatusInfo>() {
			private TextView tvStatus;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				LayoutInflater inflater = LayoutInflater.from(parent.getContext());
				return inflater.inflate(R.layout.item_loading_basic_list, parent, false);
			}
			
			@Override
			protected void onViewCreated(View view) {
				this.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
			}
			
			@Override
			protected void onLoadReady() {
				this.tvStatus.setText(R.string.loading_dot);
			}
			
			@Override
			protected void onLoading() {
				this.tvStatus.setText(R.string.loading_dot);
			}
			
			@Override
			protected boolean onLoadComplete(StatusInfo statusInfo) {
				if (statusInfo == null) {
					tvStatus.setText(R.string.network_request_error);
					return true;
				} else if (!statusInfo.isSuccessful()) {
					tvStatus.setText(statusInfo.statusMessage);
					return true;
				} else {
					this.tvStatus.setText(R.string.load_complete);
					return false;
				}
			}
		};
	}
	
	public static class BasicRefreshHolder extends RefreshHolder<StatusInfo> {
		// private AnimationSet animation;
		private RotateAnimation animation;
		private ImageView ivStatus;
		private TextView tvStatus;
		private TextView tvCheck;
		protected TextView tvReload;
		
		private int emptyDrawableId;
		private int emptyTextId;
		
		public BasicRefreshHolder(int emptyDrawableId, int emptyTextId) {
			this.emptyDrawableId = emptyDrawableId;
			this.emptyTextId = emptyTextId;
			
			// Animation scaleAnimation = new ScaleAnimation(
			// 		1.0F, 0.55F, 1.0F, 0.55F,
			// 		Animation.RELATIVE_TO_SELF, 0.5F,
			// 		Animation.RELATIVE_TO_SELF, 0.5F);
			// scaleAnimation.setDuration(800);
			// scaleAnimation.setRepeatMode(Animation.REVERSE);
			// scaleAnimation.setRepeatCount(Animation.INFINITE);
			//
			// Animation rotateAnimation = new RotateAnimation(
			// 		0, 360,
			// 		Animation.RELATIVE_TO_SELF, 0.5F,
			// 		Animation.RELATIVE_TO_SELF, 0.5F);
			// rotateAnimation.setDuration(600);
			// scaleAnimation.setRepeatMode(Animation.REVERSE);
			// rotateAnimation.setRepeatCount(Animation.INFINITE);
			//
			// this.animation = new AnimationSet(true);
			// this.animation.setInterpolator(new LinearInterpolator());
			// this.animation.addAnimation(scaleAnimation);
			// this.animation.addAnimation(rotateAnimation);
			
			this.animation = new RotateAnimation(
					0, 3600,
					Animation.RELATIVE_TO_SELF, 0.5F,
					Animation.RELATIVE_TO_SELF, 0.5F);
			this.animation.setDuration(10000);
			this.animation.setInterpolator(new LinearInterpolator());
			this.animation.setRepeatCount(Animation.INFINITE);
		}
		
		@Override
		protected View onCreateView(ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			return inflater.inflate(R.layout.item_refresh_basic_list, parent, false);
		}
		
		@Override
		protected void onViewCreated(View view) {
			view.setOnClickListener(null);
			this.ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
			this.ivStatus.setOnClickListener(this);
			this.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
			this.tvStatus.setOnClickListener(this);
			this.tvCheck = (TextView) view.findViewById(R.id.tvCheck);
			this.tvReload = (TextView) view.findViewById(R.id.tvReload);
			this.tvReload.setOnClickListener(this);
		}
		
		@Override
		protected void onRefreshReady() {
			this.ivStatus.setImageDrawable(null);
			this.tvStatus.setText(R.string.loading_dot);
			this.tvCheck.setVisibility(View.GONE);
			this.tvReload.setVisibility(View.GONE);
		}
		
		@Override
		protected void onRefresh() {
			this.ivStatus.setImageResource(R.mipmap.ic_loading);
			this.ivStatus.startAnimation(this.animation);
			this.tvStatus.setText(R.string.loading_dot);
			this.tvCheck.setVisibility(View.GONE);
			this.tvReload.setVisibility(View.GONE);
		}
		
		@Override
		protected void onRefreshComplete(StatusInfo statusInfo) {
			this.ivStatus.clearAnimation();
			if (statusInfo == null) {
				this.ivStatus.setImageResource(R.mipmap.img_network_error);
				this.ivStatus.setEnabled(false);
				this.tvStatus.setText(R.string.network_request_error);
				this.tvCheck.setVisibility(View.VISIBLE);
				this.tvReload.setVisibility(View.VISIBLE);
			} else if (!statusInfo.isSuccessful()) {
				this.ivStatus.setImageResource(R.mipmap.img_data_error);
				this.ivStatus.setEnabled(true);
				this.tvStatus.setText(statusInfo.statusMessage);
				this.tvCheck.setVisibility(View.GONE);
				this.tvReload.setVisibility(View.GONE);
			} else {
				if (this.emptyDrawableId == 0) {
					this.ivStatus.setImageResource(R.mipmap.img_data_empty);
				} else {
					this.ivStatus.setImageResource(this.emptyDrawableId);
				}
				this.ivStatus.setEnabled(true);
				if (this.emptyTextId == 0) {
					this.tvStatus.setText(statusInfo.statusMessage);
				} else {
					this.tvStatus.setText(this.emptyTextId);
				}
				this.tvCheck.setVisibility(View.GONE);
				this.tvReload.setVisibility(View.GONE);
			}
		}
	}
}
