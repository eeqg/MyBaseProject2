package com.example.az.mybaseproject2.goods;

import com.example.az.mybaseproject2.goods.bean.GoodsListBean;
import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.az.mybaseproject2.movie.bean.MovieListBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wp on 2018/4/10.
 */

public interface GoodsService {
	@GET("index/index/queryGoodsRecommendByPosition")
	Observable<GoodsListBean> listGoods(@Query("token") String token,
	                                    @Query("positionCode") String positionCode,
	                                    @Query("page") String page);
}
