package com.example.az.mybaseproject2;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.az.mybaseproject2.databinding.ActivityHomeBinding;
import com.example.az.mybaseproject2.movie.MovieActivity;
import com.example.resource.base.BaseActivity;
import com.example.resource.manager.ActivityManager;

public class HomeActivity extends BaseActivity {
	
	private ActivityHomeBinding dataBinding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
		this.dataBinding.setLeftAction(createLeftBack());
		
		observeContent();
	}
	
	private void observeContent() {
		this.dataBinding.btnMovie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getAppInstance().launchActivity(mContext, MovieActivity.class);
			}
		});
	}
}
