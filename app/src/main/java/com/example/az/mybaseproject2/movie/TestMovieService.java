package com.example.az.mybaseproject2.movie;

import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;
import com.example.az.mybaseproject2.movie.bean.MovieListBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wp on 2018/4/10.
 */

public interface TestMovieService {
	/**
	 * @param start
	 * @param count
	 * @return
	 */
	@GET("top250")
	Observable<MovieListBean> listMovie(@Query("start") int start, @Query("count") int count);
	
	@GET("subject/{movieId}")
	Observable<MovieInfoBean> getMovieInfo(@Path("movieId") String movieId,
	                                              @Query("apikey") String apikey);
}
