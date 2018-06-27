package com.example.resource.network;

import com.example.resource.base.BaseApp;
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
							.addConverterFactory(GsonConverterFactory.create())
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
							.addInterceptor(new CachesInterceptor())
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
			LogUtils.i(TAG, "CachesInterceptor -- isNetworkAvailable : "+ isNetworkAvailable);
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
				int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
				response.newBuilder()
						.removeHeader("Pragma")
						.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
						.build();
			}
			
			LogUtils.d(TAG, "response : "+response);
			return response;
		}
	}
	
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
