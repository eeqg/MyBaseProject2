package com.example.az.mybaseproject2.movie;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ItemMovieListBinding;
import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.resource.base.BaseRecyclerAdapter;
import com.kycq.library.refresh.RecyclerAdapter;

/**
 * Created by wp on 2018/4/10.
 */

public class TestMovieListAdapter extends BaseRecyclerAdapter<MovieInfoBean> {
	
	private final LayoutInflater inflater;
	
	public TestMovieListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void updateAdapterInfo(@NonNull MovieInfoBean movieInfoBean) {
		this.mAdapterInfo.movieList.addAll(movieInfoBean.movieList);
	}
	
	@Override
	public boolean hasMore() {
		if (this.mAdapterInfo == null) {
			return false;
		}
		return this.mAdapterInfo.total > this.getItemCount();
	}
	
	@Override
	public int getItemCount() {
		int count = 0;
		if (this.mAdapterInfo != null && this.mAdapterInfo.movieList != null) {
			count = this.mAdapterInfo.movieList.size();
		}
		return count;
	}
	
	@Override
	public RecyclerAdapter.ItemHolder onCreateItemHolder(int viewType) {
		return new RecyclerAdapter.ItemHolder() {
			ItemMovieListBinding dataBinding;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_movie_list, parent, false);
				return this.dataBinding.getRoot();
			}
			
			@Override
			protected void onBindView(int position) {
				this.dataBinding.setIndex(String.valueOf(position + 1));
				this.dataBinding.setMovieItemBean(mAdapterInfo.movieList.get(position));
				this.dataBinding.executePendingBindings();
			}
		};
	}
}
