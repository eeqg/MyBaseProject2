package com.example.resource.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2018/6/25.
 */

public interface BaseContract {
	interface View {
		void showLoading();
		
		void hideLoading();
		
		void tokenTimeOut();
		
		void tokenNotFound();
	}
	
	interface Presenter {
		void start();
		
		void detach();
		
		void addDisposable(Disposable subscription);
		
		void unDisposable();
	}
}
