package com.example.resource.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.resource.R;
import com.example.resource.manager.ActivityManager;

/**
 * Created by wp on 2018/6/25.
 */

public abstract class BaseActivity<P extends BaseContract.Presenter> extends AppCompatActivity
		implements BaseContract.View {
	
	protected P presenter;
	public Context mContext;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈
		presenter = initPresenter();
	}
	
	protected P initPresenter() {
		return null;
	}
	
	@Override
	public void showLoading() {
	
	}
	
	@Override
	public void hideLoading() {
	
	}
	
	@Override
	public void tokenTimeOut() {
	
	}
	
	@Override
	public void tokenNotFound() {
	
	}
	
	/**
	 * 创建返回操作
	 *
	 * @return 返回操作
	 */
	protected ToolbarAction createLeftBack() {
		return ToolbarAction.createIcon(ActivityCompat.getDrawable(this, R.mipmap.ic_back_white))
				.setListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onBackPressed();
					}
				});
	}
	
	public void promptMessage(String message) {
		BaseApp.toast(message);
	}
	
	@Override
	protected void onDestroy() {
		ActivityManager.getAppInstance().removeActivity(this);//将当前activity移除管理栈
		if (presenter != null) {
			presenter.detach();//在presenter中解绑释放view
			presenter = null;
		}
		super.onDestroy();
	}
}
