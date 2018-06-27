package com.example.resource.base;

import com.example.resource.utils.LogUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2018/6/25.
 */

public abstract class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter {
	protected V basicView;
	
	//将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
	private CompositeDisposable mCompositeDisposable;
	
	public BasePresenter(V basicView) {
		this.basicView = basicView;
		this.start();
	}
	
	@Override
	public void start() {
	}
	
	@Override
	public void detach() {
		this.basicView = null;
		unDisposable();
	}
	
	@Override
	public void addDisposable(Disposable disposable) {
		LogUtils.d("BasePresenter", "addDisposable()--disposable="+disposable);
		if (this.mCompositeDisposable == null || this.mCompositeDisposable.isDisposed()) {
			this.mCompositeDisposable = new CompositeDisposable();
		}
		this.mCompositeDisposable.add(disposable);
	}
	
	/**
	 * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
	 */
	@Override
	public void unDisposable() {
		LogUtils.d("BasePresenter", "unDisposable()--mCompositeDisposable="+mCompositeDisposable);
		if (this.mCompositeDisposable != null) {
			this.mCompositeDisposable.dispose();
		}
	}
}
