package com.example.az.mybaseproject2.movie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ActivityMovieInfoBinding;
import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.resource.base.BaseConst;
import com.example.resource.base.SimpleBaseSwipeBackActivity;
import com.example.resource.network.ServiceFactory;
import com.example.resource.network.SimpleTaskObserver;
import com.example.resource.utils.LogUtils;

import cn.shyman.library.refresh.OnTaskListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieInfoActivity extends SimpleBaseSwipeBackActivity {
	private ActivityMovieInfoBinding dataBinding;
	
	private String movieId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_info);
		this.dataBinding.setLeftAction(createLeftBack());
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			movieId = bundle.getString(BaseConst.ID);
		}
		
		observeMovieInfo();
		
		this.dataBinding.refreshLayout.swipeRefresh();
	}
	
	private void observeMovieInfo() {
		this.dataBinding.refreshLayout.setOnTaskListener(new OnTaskListener<Disposable>() {
			@Override
			public Disposable onTask() {
				return getMovieInfo(movieId);
			}
			
			@Override
			public void onCancel(Disposable disposable) {
				disposable.dispose();
			}
		});
	}
	
	private Disposable getMovieInfo(String movieId) {
		return ServiceFactory.createService("https://api.douban.com/v2/movie/", TestMovieService.class)
				.getMovieInfo(movieId, "0b2bdeda43b5688921839c8ecb20399b")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new SimpleTaskObserver<MovieInfoBean>(MovieInfoActivity.this) {
					@Override
					public void taskStart() {
						// super.taskStart();
					}
					
					@Override
					public void taskSuccess(MovieInfoBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskSuccess()");
						super.taskSuccess(basicBean);
						dataBinding.refreshLayout.swipeComplete(basicBean.statusInfo);
						dataBinding.setTitle(basicBean.title);
						dataBinding.setMovieInfoBean(basicBean);
					}
					
					@Override
					public void taskFailure(MovieInfoBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskFailure()");
						super.taskFailure(basicBean);
						dataBinding.refreshLayout.swipeComplete(basicBean.statusInfo);
					}
					
					@Override
					public void taskError(Throwable throwable) {
						LogUtils.d("TestMoviePresenter", "taskError()");
						super.taskError(throwable);
						dataBinding.refreshLayout.swipeComplete(null);
						dataBinding.setMovieInfoBean(null);
					}
				});
	}
}
