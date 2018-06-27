package com.example.az.mybaseproject2.movie.bean;

/**
 * Created by wp on 2018/4/10.
 */

public class MovieItemBean {
	public String id;
	public String title;
	public String year;
	public ImagesBean images;
	
	public class ImagesBean {
		public String small;
		public String medium;
		public String large;
	}
	
	public String obtainPictureUrl() {
		if (this.images != null) {
			return this.images.small;
		}
		return null;
	}
	
	public String formatId() {
		return "NO." + this.id;
	}
	
	public String formatYear() {
		return "AT: " + this.year;
	}
}
