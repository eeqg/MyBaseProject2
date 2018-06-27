package com.example.resource.network;

public class StatusInfo {
	/** 成功 */
	public static final int STATUS_SUCCESS = 0;
	/** Token超时 */
	public static final int STATUS_TOKEN_TIMEOUT = 1004;
	/** Token未设置 */
	public static final int STATUS_TOKEN_NOT_FOUND = 1006;
	/** 自定义错误 */
	public static final int STATUS_CUSTOM_ERROR = 2001;
	
	public int statusCode;
	
	public String statusMessage;
	
	public StatusInfo() {
	}
	
	public StatusInfo(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public boolean isSuccessful() {
		return statusCode == STATUS_SUCCESS;
	}
	
	public boolean isOther() {
		return statusCode == STATUS_TOKEN_TIMEOUT
				|| statusCode == STATUS_TOKEN_NOT_FOUND;
	}
	
	public boolean isTokenTimeout() {
		return statusCode == STATUS_TOKEN_TIMEOUT;
	}
	
	public boolean isTokenNotFound() {
		return statusCode == STATUS_TOKEN_NOT_FOUND
				&& statusMessage.equals("参数： token 不能为空");
	}
}
