package com.example.az.mybaseproject2.movie.bean;

import com.example.resource.network.BaseBean;

import java.util.List;

/**
 * Created by wp on 2018/11/15.
 */
public class MovieInfoBean extends BaseBean {
	public String title;
	public Images images;
	public String year;
	public List<String> tags;
	public List<String> genres;
	public RatingInfoBean rating;
	
	public String summary;
	
	public class Images {
		public String small;
		public String medium;
		public String large;
	}
	
	public class RatingInfoBean{
		public String average;
	}
	
	
}
