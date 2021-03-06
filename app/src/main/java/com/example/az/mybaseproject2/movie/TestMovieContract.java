package com.example.az.mybaseproject2.movie;

import com.example.az.mybaseproject2.movie.bean.MovieListBean;
import com.example.resource.base.BaseContract;

import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2018/4/10.
 */

interface TestMovieContract {
	interface View extends BaseContract.View {
		void updateMovieList(MovieListBean movieInfoBean);
	}
	
	interface Presenter extends BaseContract.Presenter {
		Disposable listMovie(int start, int count);
	}
}
