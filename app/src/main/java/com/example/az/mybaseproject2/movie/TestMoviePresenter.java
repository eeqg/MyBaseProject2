package com.example.az.mybaseproject2.movie;

import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.resource.base.BasePresenter;
import com.example.resource.network.ServiceFactory;
import com.example.resource.network.TaskObserver;
import com.example.resource.utils.LogUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wp on 2018/4/10.
 */

class TestMoviePresenter extends BasePresenter<TestMovieContract.View>
		implements TestMovieContract.Presenter {
	
	MovieInfoBean currentInfoBean;
	
	TestMoviePresenter(TestMovieContract.View basicView) {
		super(basicView);
	}
	
	@Override
	public void listMovie(int start, int count) {
		ServiceFactory.createService("https://api.douban.com/v2/movie/", TestMovieService.class)
				.listMovie(start, count)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new TaskObserver<MovieInfoBean>(basicView) {
					@Override
					public void taskSuccess(MovieInfoBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskSuccess()");
						super.taskSuccess(basicBean);
						basicView.updateMovieList(basicBean);
						currentInfoBean = basicBean;
					}
					
					@Override
					public void taskFailure(MovieInfoBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskFailure()");
						super.taskFailure(basicBean);
						basicView.updateMovieList(basicBean);
						currentInfoBean = basicBean;
					}
					
					@Override
					public void taskError(Throwable throwable) {
						LogUtils.d("TestMoviePresenter", "taskError()");
						super.taskError(throwable);
						basicView.updateMovieList(null);
						// basicView.updateMovieList(currentInfoBean);
					}
				});
	}
}
