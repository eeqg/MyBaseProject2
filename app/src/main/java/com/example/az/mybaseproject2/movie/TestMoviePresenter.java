package com.example.az.mybaseproject2.movie;

import com.example.az.mybaseproject2.movie.bean.MovieListBean;
import com.example.resource.base.BasePresenter;
import com.example.resource.network.ServiceFactory;
import com.example.resource.network.TaskObserver;
import com.example.resource.utils.LogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wp on 2018/4/10.
 */

class TestMoviePresenter extends BasePresenter<TestMovieContract.View>
		implements TestMovieContract.Presenter {
	
	TestMoviePresenter(TestMovieContract.View basicView) {
		super(basicView);
	}
	
	@Override
	public Disposable listMovie(int start, int count) {
		final Disposable d = ServiceFactory.createService("https://api.douban.com/v2/movie/", TestMovieService.class)
				.listMovie(start, count)
				.subscribeOn(Schedulers.io())
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable disposable) throws Exception {
						LogUtils.d("TestMoviePresenter", "doOnSubscribe()--disposable=" + disposable);
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				// .subscribeWith(new TaskObserver<MovieListBean>(basicView) {
				//
				// 	@Override
				// 	public void taskStart() {
				// 		super.taskStart();
				// 	}
				//
				// 	@Override
				// 	public void taskSuccess(MovieListBean basicBean) {
				// 		LogUtils.d("TestMoviePresenter", "taskSuccess()");
				// 		super.taskSuccess(basicBean);
				// 		basicView.updateMovieList(basicBean);
				// 	}
				//
				// 	@Override
				// 	public void taskFailure(MovieListBean basicBean) {
				// 		LogUtils.d("TestMoviePresenter", "taskFailure()");
				// 		super.taskFailure(basicBean);
				// 		basicView.updateMovieList(basicBean);
				// 	}
				//
				// 	@Override
				// 	public void taskError(Throwable throwable) {
				// 		LogUtils.d("TestMoviePresenter", "taskError()");
				// 		super.taskError(throwable);
				// 		basicView.updateMovieList(null);
				// 	}
				// });
				.subscribeWith(new DisposableObserver<MovieListBean>() {
					@Override
					public void onNext(MovieListBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskSuccess()");
						basicView.updateMovieList(basicBean);
					}
					
					@Override
					public void onError(Throwable e) {
						basicView.updateMovieList(null);
					}
					
					@Override
					public void onComplete() {
					
					}
				});
		LogUtils.d("TestMoviePresenter", "doOnSubscribe()-2222-disposable=" + d);
		
		addDisposable(d);
		
		return d;
	}
}
