package com.example.az.mybaseproject2.movie;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ActivityMovieBinding;
import com.example.az.mybaseproject2.movie.bean.MovieItemBean;
import com.example.az.mybaseproject2.movie.bean.MovieListBean;
import com.example.resource.base.BaseConst;
import com.example.resource.base.BaseSwipeBackActivity;
import com.example.resource.component.NormalItemDecoration;
import com.example.resource.utils.LaunchUtil;
import com.example.resource.utils.LogUtils;
import com.example.resource.utils.StatusBarUtil;

import java.util.HashMap;

import cn.shyman.library.refresh.OnTaskListener;
import cn.shyman.library.refresh.RecyclerAdapter;
import io.reactivex.disposables.Disposable;

public class MovieActivity extends BaseSwipeBackActivity<TestMovieContract.Presenter> implements TestMovieContract.View {
	
	private ActivityMovieBinding dataBinding;
	private TestMovieListAdapter testMovieListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
		this.dataBinding.setLeftAction(createLeftBack());
		
		StatusBarUtil.setColorForSwipeBack(this, getResources().getColor(R.color.colorPrimaryDark), 0);
		
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
		this.testMovieListAdapter.setOnTaskListener(new OnTaskListener<Disposable>() {
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
		
		testMovieListAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(RecyclerAdapter.ItemHolder itemHolder) {
				MovieItemBean itemBean = testMovieListAdapter.getItem(itemHolder.getAdapterPosition());
				HashMap<String, Object> params = new HashMap<>();
				params.put(BaseConst.ID, itemBean.id);
				params.put(BaseConst.TITLE, itemBean.title);
				LaunchUtil.launchActivity(mContext, MovieInfoActivity.class, params);
			}
		});
	}
	
	@Override
	public void updateMovieList(MovieListBean movieInfoBean) {
		LogUtils.d("movieInfoBean=" + movieInfoBean);
		this.testMovieListAdapter.swipeResult(movieInfoBean);
	}
}
