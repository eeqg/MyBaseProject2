package com.example.resource.network;

import com.example.resource.base.BaseBean;
import com.example.resource.utils.LogUtils;

import org.reactivestreams.Subscription;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2018/6/27.
 */

public abstract class AbstractObserver<T extends BaseBean> implements Observer<T> {
	@Override
	public void onSubscribe(Disposable d) {
		LogUtils.d("AbstractTaskSubscriber", "onSubscribe()");
		taskStart();
	}
	
	@Override
	public void onNext(T baseBean) {
		LogUtils.d("AbstractTaskSubscriber", "onNext()");
		if (baseBean.statusInfo.isSuccessful()) {
			taskSuccess(baseBean);
		} else if (baseBean.statusInfo.isOther()) {
			taskOther(baseBean);
		} else {
			taskFailure(baseBean);
		}
	}
	
	@Override
	public void onError(Throwable throwable) {
		taskStop();
		throwable.printStackTrace();
		taskError(throwable);
	}
	
	@Override
	public void onComplete() {
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
