package com.example.resource.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by wp on 2018/6/25.
 */

public abstract class BaseFragment<P extends BaseContract.Presenter> extends Fragment
		implements BaseContract.View {
	
	protected P presenter;
	private boolean isViewCreate = false;
	private boolean isViewVisible = false;
	public Context mContext;
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mContext = context;
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.presenter = initPresenter();
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.isViewCreate = true;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		this.isViewVisible = isVisibleToUser;
		if (isVisibleToUser && this.isViewCreate) {
			visibleToUser();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (this.isViewVisible) {
			visibleToUser();
		}
	}
	
	protected void visibleToUser() {
	}
	
	public abstract P initPresenter();
	
	@Override
	public void showLoading() {
	}
	
	@Override
	public void hideLoading() {
	}
	
	@Override
	public void onDestroyView() {
		if (this.presenter != null) {
			this.presenter.detach();
		}
		this.isViewCreate = false;
		super.onDestroyView();
	}
}
