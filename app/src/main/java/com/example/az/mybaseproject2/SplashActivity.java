package com.example.az.mybaseproject2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.resource.base.BaseActivity;
import com.example.resource.base.BaseContract;
import com.example.resource.utils.PermissionUtils;

public class SplashActivity extends BaseActivity implements Handler.Callback {
	private final int CODE_HOME = 1;
	
	private Handler handler = new Handler(this);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		requestPermissions();
	}
	
	@Override
	public BaseContract.Presenter initPresenter() {
		return null;
	}
	
	/**
	 * 请求权限
	 */
	private void requestPermissions() {
		PermissionUtils.build(this)
				.setOnPermissionListener(new PermissionUtils.OnPermissionListener() {
					@Override
					public void onGranted() {
						handler.sendEmptyMessageDelayed(0, 1000);
					}
					
					@Override
					public void onDenied() {
						promptMessage("onDenied()");
						// TODO: 2018/4/13
					}
					
					@Override
					public void onRationale(String... permissions) {
						this.requestPermission(permissions);
					}
				})
				.requestPermissions(
						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_WIFI_STATE,
						Manifest.permission.ACCESS_NETWORK_STATE,
						Manifest.permission.CHANGE_WIFI_STATE,
						Manifest.permission.READ_PHONE_STATE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.CAMERA
				);
	}
	
	/**
	 * 跳转首页
	 */
	private void turnHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivityForResult(intent, CODE_HOME);
		
		this.finish();
	}
	
	@Override
	public boolean handleMessage(Message message) {
		if (isFinishing()) {
			return false;
		}
		turnHome();
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.handler.removeMessages(0);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODE_HOME) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
