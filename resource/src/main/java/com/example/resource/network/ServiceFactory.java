package com.example.resource.network;

import com.example.resource.base.BaseApp;
import com.example.resource.manager.EventBusManager;
import com.example.resource.utils.LogUtils;
import com.example.resource.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wp on 2018/6/25.
 */

public class ServiceFactory {
	private static final String TAG = "ServiceFactory";
	private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 * 10;
	private static final int DEFAULT_TIME_HOUR = 60 * 60;
	private static final int DEFAULT_TIME_DAY = DEFAULT_TIME_HOUR * 24;
	private static final int DEFAULT_TIME_WEEK = DEFAULT_TIME_DAY * 24 * 7;
	
	private volatile static Retrofit retrofit;
	private volatile static OkHttpClient okHttpClient;
	
	public static <T> T createService(String baseUrl, Class<T> tClass) {
		return getRetrofit(baseUrl).create(tClass);
	}
	
	public static Retrofit getRetrofit(String baseUrl) {
		if (retrofit == null) {
			synchronized (Retrofit.class) {
				if (retrofit == null) {
					retrofit = new Retrofit.Builder()
							.baseUrl(baseUrl)
							.client(getOkHttpClient())
							// .addConverterFactory(GsonConverterFactory.create())
							.addConverterFactory(CustomGsonConverterFactory.create())
							.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
							.build();
				}
			}
		}
		return retrofit;
	}
	
	public static OkHttpClient getOkHttpClient() {
		if (okHttpClient == null) {
			synchronized (OkHttpClient.class) {
				if (okHttpClient == null) {
					File cacheFile = new File(BaseApp.INSTANCE.getCacheDir(), "responses");
					Cache cache = new Cache(cacheFile, DEFAULT_CACHE_SIZE);
					okHttpClient = new OkHttpClient.Builder()
							// .addInterceptor(new CachesInterceptor())
							.addInterceptor(requestInterceptor)
							.addNetworkInterceptor(responseInterceptor)
							.addInterceptor(LogInterceptor)
							.connectTimeout(30, TimeUnit.SECONDS)
							.cache(cache)
							.build();
				}
			}
		}
		return okHttpClient;
	}
	
	private class TokenInterceptor implements Interceptor {
		private static final String USER_TOKEN = "Authorization";
		private final String token;
		
		public TokenInterceptor(String token) {
			this.token = token;
		}
		
		@Override
		public Response intercept(Chain chain) throws IOException {
			final Request originalRequest = chain.request();
			if (token == null || originalRequest.header("Authorization") != null) {
				return chain.proceed(originalRequest); // token 为空, 直接请求.
			}
			Request request = originalRequest.newBuilder()
					.header(USER_TOKEN, token) //add headers.
					.build();
			return chain.proceed(request);
		}
	}
	
	private static class CachesInterceptor implements Interceptor {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			boolean isNetworkAvailable = NetworkUtils.isConnected(BaseApp.INSTANCE);
			LogUtils.i(TAG, "CachesInterceptor -- isNetworkAvailable : " + isNetworkAvailable);
			if (!isNetworkAvailable) {
				request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
			}
			Response response = chain.proceed(request);
			if (isNetworkAvailable) {
				int maxAge = 0; // 有网络时 设置缓存超时时间0
				request.newBuilder()
						.removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
						.header("Cache-Control", "public, max-age=" + maxAge)
						.build();
			} else {
				int maxStale = DEFAULT_TIME_WEEK * 28; // 无网络时，设置超时为4周
				response.newBuilder()
						.removeHeader("Pragma")
						.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
						.build();
			}
			
			LogUtils.d(TAG, "response : " + response);
			return response;
		}
	}
	
	private static final Interceptor requestInterceptor = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			boolean isNetworkAvailable = NetworkUtils.isConnected(BaseApp.INSTANCE);
			Request request = chain.request();
			
			LogUtils.d(TAG, "requestInterceptor -- isNetworkAvailable : " + isNetworkAvailable);
			if (!isNetworkAvailable) {
				// BaseApp.toast("請檢查網絡!!");
				LogUtils.d(TAG, "請檢查網絡!!");
				EventBusManager.post(EventBusManager.EVENT_KEY_NETWORK_UNAVAILABLE);
				
				CacheControl tempCacheControl = new CacheControl.Builder()
						// .onlyIfCached()
						.maxStale(DEFAULT_TIME_DAY, TimeUnit.SECONDS)
						.build();
				request = request.newBuilder()
						.cacheControl(tempCacheControl)
						.build();
			} else {
				// 强制走网络
				request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
			}
			
			return chain.proceed(request);
		}
	};
	
	private static final Interceptor responseInterceptor = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			//针对那些服务器不支持缓存策略的情况下，使用强制修改响应头，达到缓存的效果
			//响应拦截只不过是出于规范，向服务器发出请求，至于服务器搭不搭理我们我们不管他，我们在响应里面做手脚，有网没有情况下的缓存策略
			Request request = chain.request();
			Response originalResponse = chain.proceed(request);
			int maxAge;
			// 缓存的数据
			LogUtils.d(TAG, "responseInterceptor--isNetworkAvailable : " + NetworkUtils.isConnected(BaseApp.INSTANCE));
			if (!NetworkUtils.isConnected(BaseApp.INSTANCE)) {
				maxAge = DEFAULT_TIME_WEEK * 7;
			} else {
				maxAge = 0;//有网络, 请求数据.
			}
			return originalResponse.newBuilder()
					.removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
					.removeHeader("Cache-Control")
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		}
	};
	
	private static final Interceptor LogInterceptor = new Interceptor() {
		@Override
		public Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			long t1 = System.nanoTime();
			LogUtils.i(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
			Response response = chain.proceed(request);
			long t2 = System.nanoTime();
			LogUtils.i(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
			return response;
		}
	};
}
