package com.example.az.mybaseproject2.goods.bean;

import com.example.resource.network.ArrayBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wp on 2018/11/16.
 */
public class GoodsListBean extends ArrayBean {
	@SerializedName("result")
	public ArrayList<GoodsInfoItemBean> list;
	
	public int pageCount;
}
