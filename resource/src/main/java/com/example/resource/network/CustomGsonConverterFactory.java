package com.example.resource.network;

import android.support.annotation.NonNull;

import com.example.resource.base.BaseApp;
import com.example.resource.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by wp on 2018/4/11.
 */

public class CustomGsonConverterFactory extends Converter.Factory {
	
	private final Gson gson;
	private String TAG = CustomGsonConverterFactory.class.getSimpleName();
	
	private CustomGsonConverterFactory(Gson gson) {
		if (gson == null) throw new NullPointerException("gson == null");
		this.gson = gson;
	}
	
	public static CustomGsonConverterFactory create() {
		return create(new Gson());
	}
	
	public static CustomGsonConverterFactory create(Gson gson) {
		return new CustomGsonConverterFactory(gson);
	}
	
	@Override
	public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
		return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
	}
	
	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		TypeToken typeToken = TypeToken.get(type);
		TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
		if (typeToken.getRawType() == BaseBean.class) {
			return new BaseResponseBodyConverter<>();
		}
		if (BaseBean.class.isAssignableFrom(typeToken.getRawType())) {
			if (ArrayBean.class.isAssignableFrom(typeToken.getRawType())) {
				return new ArrayResponseBodyConverter<>(type);
			} else {
				return new ObjectResponseBodyConverter<>(type);
			}
		}
		return new OtherResponseBodyConverter<>(gson, adapter);
	}
	
	private class BaseResponseBodyConverter<T> implements Converter<ResponseBody, BaseBean> {
		@Override
		public BaseBean convert(@NonNull ResponseBody value) throws IOException {
			BasicBean basicBean = gson.fromJson(value.charStream(), BasicBean.class);
			if (BaseApp.isDebug()) {
				LogUtils.i(TAG, "code = " + basicBean.statusCode
						+ ", message = " + basicBean.statusMessage
						+ ", data = " + basicBean.resultData);
			}
			
			BaseBean resultBean = new BaseBean();
			resultBean.statusInfo.statusCode = basicBean.statusCode;
			resultBean.statusInfo.statusMessage = basicBean.statusMessage;
			resultBean.resultData = basicBean.resultData;
			
			return resultBean;
		}
	}
	
	private class ObjectResponseBodyConverter<T extends BaseBean> implements Converter<ResponseBody, T> {
		private Type type;
		
		ObjectResponseBodyConverter(Type type) {
			this.type = type;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			BasicBean basicBean = gson.fromJson(value.charStream(), BasicBean.class);
			if (BaseApp.isDebug()) {
				LogUtils.i(TAG, "code = " + basicBean.statusCode
						+ ", message = " + basicBean.statusMessage
						+ ", data = " + basicBean.resultData);
			}
			
			T resultBean = gson.fromJson(
					basicBean.resultData == null ? "{}" : basicBean.resultData, this.type);
			resultBean.statusInfo.statusCode = basicBean.statusCode;
			resultBean.statusInfo.statusMessage = basicBean.statusMessage;
			
			return resultBean;
		}
	}
	
	private class ArrayResponseBodyConverter<T extends ArrayBean> implements Converter<ResponseBody, T> {
		private Type type;
		
		ArrayResponseBodyConverter(Type type) {
			this.type = type;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			BasicListBean basicBean = gson.fromJson(value.charStream(), BasicListBean.class);
			if (BaseApp.isDebug()) {
				LogUtils.i(TAG, "code = " + basicBean.statusCode
						+ ", message = " + basicBean.statusMessage
						+ ", data = " + basicBean.resultData);
			}
			
			T resultBean = gson.fromJson(
					String.format("{\"result\":%s}", basicBean.resultData == null ? "[]" : gson.toJson(basicBean.resultData)),
					this.type);
			resultBean.statusInfo.statusCode = basicBean.statusCode;
			resultBean.statusInfo.statusMessage = basicBean.statusMessage;
			
			return resultBean;
		}
	}
	
	private class OtherResponseBodyConverter<T> implements Converter<ResponseBody, T> {
		private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
		private final Charset UTF_8 = Charset.forName("UTF-8");
		private final Gson gson;
		private final TypeAdapter<T> adapter;
		
		OtherResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
			this.gson = gson;
			this.adapter = adapter;
		}
		
		@Override
		public T convert(@NonNull ResponseBody value) throws IOException {
			String response = value.string();
			// BaseBean baseBean = gson.fromJson(response, BaseBean.class);
			// // if (httpStatus.isCodeInvalid()) {
			// // 	value.close();
			// // 	throw new ApiException(httpStatus.getCode(), httpStatus.getMessage());
			// // }
			LogUtils.d("isDebug : " + BaseApp.isDebug());
			if (BaseApp.isDebug()) {
				LogUtils.json(response);
			}
			
			MediaType contentType = value.contentType();
			Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
			InputStream inputStream = new ByteArrayInputStream(response.getBytes());
			Reader reader = null;
			JsonReader jsonReader = null;
			if (charset != null) {
				reader = new InputStreamReader(inputStream, charset);
				jsonReader = gson.newJsonReader(reader);
			}
			
			try {
				return adapter.read(jsonReader);
			} finally {
				value.close();
			}
		}
	}
	
	private static class BasicBean {
		@SerializedName("status")
		int statusCode;
		@SerializedName("msg")
		String statusMessage;
		@SerializedName("data")
		String resultData;
	}
	
	private static class BasicListBean {
		@SerializedName("status")
		int statusCode;
		@SerializedName("msg")
		String statusMessage;
		@SerializedName("data")
		List resultData;
	}
}
