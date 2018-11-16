package com.example.resource.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.resource.R;
import com.example.resource.dialog.LoadingDialog;
import com.example.resource.manager.ActivityManager;

/**
 * Created by wp on 2018/11/15.
 */
public class SimpleBaseActivity extends AppCompatActivity {
	public Context mContext;
	private LoadingDialog loadingDialog;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈
		
		this.loadingDialog = new LoadingDialog(this);
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
	
	public void promptMessage(int resId) {
		promptMessage(getString(resId));
	}
	
	public void showLoading() {
		if (!this.loadingDialog.isShowing()) {
			this.loadingDialog.show();
		}
	}
	
	public void hideLoading() {
		if (this.loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		ActivityManager.getAppInstance().removeActivity(this);//将当前activity移除管理栈
		super.onDestroy();
	}
}
