package com.example.resource.network;

import com.example.resource.utils.LogUtils;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by wp on 2018/6/27.
 */

public abstract class AbstractTaskObserver<T extends BaseBean> extends DisposableObserver<T> {
	@Override
	public final void onStart() {
		super.onStart();
		LogUtils.d("AbstractTaskSubscriber", "onStart()--");
		taskStart();
	}
	
	@Override
	public final void onNext(T baseBean) {
		LogUtils.d("AbstractTaskSubscriber", "onNext()--statusInfo.statusCode : "+baseBean.statusInfo.statusCode);
		if (baseBean.statusInfo.isSuccessful()) {
			taskSuccess(baseBean);
		} else if (baseBean.statusInfo.isOther()) {
			taskOther(baseBean);
		} else {
			taskFailure(baseBean);
		}
	}
	
	@Override
	public final void onError(Throwable throwable) {
		taskStop();
		throwable.printStackTrace();
		taskError(throwable);
	}
	
	@Override
	public final void onComplete() {
		LogUtils.d("AbstractTaskSubscriber", "onComplete()");
		taskStop();
	}
	
	/**
	 * 任务开始
	 */
	public abstract void taskStart();
	
	/**
	 * 任务结束
	 */
	public abstract void taskStop();
	
	/**
	 * 任务成功
	 *
	 * @param basicBean 结果信息
	 */
	public abstract void taskSuccess(T basicBean);
	
	/**
	 * 任务失败
	 *
	 * @param basicBean 结果信息
	 */
	public abstract void taskFailure(T basicBean);
	
	/**
	 * 任务特殊处理
	 *
	 * @param basicBean 结果信息
	 */
	public abstract void taskOther(T basicBean);
	
	/**
	 * 任务错误
	 *
	 * @param throwable 错误信息
	 */
	public abstract void taskError(Throwable throwable);
}
