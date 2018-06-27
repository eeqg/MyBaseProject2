package com.example.az.mybaseproject2.movie;

import com.example.az.mybaseproject2.movie.bean.MovieInfoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by wp on 2018/4/10.
 */

public interface TestMovieService {
	/**
	 *
	 * @param start
	 * @param count
	 * @return
	 */
	@GET("top250")
	Observable<MovieInfoBean> listMovie(@Query("start") int start, @Query("count") int count);
}
