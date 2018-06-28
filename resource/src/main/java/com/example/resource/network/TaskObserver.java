package com.example.resource.network;

import com.example.resource.R;
import com.example.resource.base.BaseApp;
import com.example.resource.base.BaseBean;
import com.example.resource.base.BaseContract;
import com.example.resource.utils.LogUtils;

import java.lang.ref.SoftReference;

/**
 * Created by wp on 2018/6/27.
 */

public abstract class TaskObserver<T extends BaseBean> extends AbstractTaskObserver<T> {
	private SoftReference<BaseContract.View> basicViewReference;
	
	protected TaskObserver(BaseContract.View basicView) {
		basicViewReference = new SoftReference<>(basicView);
	}
	
	@Override
	public void taskStart() {
		LogUtils.d("TaskObserver", "taskStart()--");
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			basicView.showLoading();
		}
	}
	
	@Override
	public void taskStop() {
		LogUtils.d("TaskObserver", "taskStop()--");
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			basicView.hideLoading();
		}
	}
	
	@Override
	public void taskSuccess(T basicBean) {
		LogUtils.d("TaskObserver", "taskSuccess()--");
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			basicView.hideLoading();
		}
	}
	
	@Override
	public void taskOther(T basicBean) {
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			if (basicBean.statusInfo.isTokenTimeout()) {
				basicView.tokenTimeOut();
			} else if (basicBean.statusInfo.isTokenNotFound()) {
				basicView.tokenNotFound();
			} else {
				BaseApp.toast(basicBean.statusInfo.statusMessage);
			}
		}
	}
	
	@Override
	public void taskFailure(T basicBean) {
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			BaseApp.toast(basicBean.statusInfo.statusMessage);
		}
	}
	
	@Override
	public void taskError(Throwable throwable) {
		BaseContract.View basicView = this.basicViewReference.get();
		if (basicView != null) {
			BaseApp.toast(R.string.network_request_error);
		}
	}
}
