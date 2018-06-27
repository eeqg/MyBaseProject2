package com.example.az.mybaseproject2.movie.bean;

import com.example.resource.base.BaseBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by wp on 2018/4/10.
 */

public class MovieInfoBean extends BaseBean {
	public String title;
	public int total;
	@SerializedName("subjects")
	public ArrayList<MovieItemBean> movieList;
}
