package com.example.az.mybaseproject2.movie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ActivityMovieBinding;
import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.resource.base.BaseSwipeBackActivity;
import com.example.resource.component.NormalItemDecoration;
import com.example.resource.utils.LogUtils;
import com.kycq.library.refresh.RecyclerAdapter;

import io.reactivex.disposables.Disposable;

public class MovieActivity extends BaseSwipeBackActivity<TestMovieContract.Presenter> implements TestMovieContract.View {
	
	private ActivityMovieBinding dataBinding;
	private TestMovieListAdapter testMovieListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
		this.dataBinding.setLeftAction(createLeftBack());
		
		observerContent();
		
		this.dataBinding.refreshLayout.swipeRefresh();
	}
	
	@Override
	protected TestMovieContract.Presenter initPresenter() {
		return new TestMoviePresenter(this);
	}
	
	private void observerContent() {
		this.dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		this.dataBinding.recyclerView.addItemDecoration(new NormalItemDecoration(this));
		
		this.testMovieListAdapter = new TestMovieListAdapter(this);
		this.testMovieListAdapter.setRefreshLayout(this.dataBinding.refreshLayout);
		this.testMovieListAdapter.setRecyclerView(this.dataBinding.recyclerView);
		this.testMovieListAdapter.setOnTaskListener(new RecyclerAdapter.OnTaskListener<Disposable>() {
			@Override
			public Disposable onTask() {
				int currentPage = testMovieListAdapter.getCurrentPage();
				// LogUtils.d("currentPage=" + currentPage);
				int count = 20;
				int start = (currentPage - 1) * count;
				return presenter.listMovie(start, count);
			}
			
			@Override
			public void onCancel(Disposable disposable) {
				disposable.dispose();
			}
		});
	}
	
	@Override
	public void updateMovieList(MovieInfoBean movieInfoBean) {
		LogUtils.d("movieInfoBean" + movieInfoBean);
		this.testMovieListAdapter.swipeResult(movieInfoBean);
	}
}
