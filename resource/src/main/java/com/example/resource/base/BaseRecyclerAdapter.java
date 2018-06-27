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
import com.example.resource.network.StatusInfo;
import com.kycq.library.refresh.RecyclerAdapter;

public abstract class BaseRecyclerAdapter<AdapterInfo> extends RecyclerAdapter<StatusInfo> {
	protected AdapterInfo mAdapterInfo;
	private int initPage = 1;
	protected int currentPage = initPage;
	
	public int getInitPage() {
		return this.initPage;
	}
	
	public int getCurrentPage() {
		return this.currentPage;
	}
	
	@Override
	public void swipeRefreshReady() {
		this.currentPage = this.initPage;
		resetAdapterInfo(null);
		super.swipeRefreshReady();
	}
	
	public void forceRefresh() {
		this.currentPage = this.initPage;
		resetAdapterInfo(null);
		super.swipeRefresh();
	}
	
	@Override
	public void swipeRefresh() {
		this.currentPage = this.initPage;
		super.swipeRefresh();
	}
	
	@Override
	public void swipeComplete(StatusInfo statusInfo) {
		if (statusInfo != null && statusInfo.isSuccessful()) {
			this.currentPage++;
		}
		super.swipeComplete(statusInfo);
	}
	
	public void swipeResult(AdapterInfo adapterInfo) {
		StatusInfo statusInfo = adapterInfo instanceof BaseBean ? ((BaseBean) adapterInfo).statusInfo : null;
		boolean isRefreshing = this.currentPage == initPage;
		if (statusInfo == null) {
			if (isRefreshing) {
				resetAdapterInfo(adapterInfo);
			}
			swipeComplete(null);
		} else if (statusInfo.isSuccessful()) {
			if (isRefreshing) {
				resetAdapterInfo(adapterInfo);
			} else {
				int oldItemCount = getItemCount();
				updateAdapterInfo(adapterInfo);
				int newItemCount = getItemCount();
				notifyItemRangeInserted(oldItemCount, newItemCount - oldItemCount);
			}
			swipeComplete(statusInfo);
			if (hasMore()) {
				swipeLoadReady();
			}
		} else {
			swipeComplete(statusInfo);
		}
	}
	
	private void resetAdapterInfo(AdapterInfo adapterInfo) {
		mAdapterInfo = adapterInfo;
	}
	
	public abstract void updateAdapterInfo(@NonNull AdapterInfo adapterInfo);
	
	public abstract boolean hasMore();
	
	public Object getItem(int position) {
		return null;
	}
	
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
		private Animation animation;
		private ImageView ivStatus;
		private TextView tvStatus;
		private TextView tvCheck;
		private TextView tvReload;
		
		private int emptyDrawableId;
		private int emptyTextId;
		
		public BasicRefreshHolder(int emptyDrawableId, int emptyTextId) {
			this.emptyDrawableId = emptyDrawableId;
			this.emptyTextId = emptyTextId;
			
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
		protected void onRefreshing() {
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
