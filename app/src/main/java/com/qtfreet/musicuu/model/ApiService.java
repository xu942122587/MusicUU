package com.qtfreet.musicuu.model;

import com.qtfreet.musicuu.model.Bean.MusicUU.resultBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qtfreet on 2016/3/25.
 */
public interface ApiService {
    @GET("music/search/{type}/1?format=json&sign=a5cc0a8797539d3a1a4f7aeca5b695b9")
    Call<List<resultBean>> GetInfo(@Path("type") String type, @Query("keyword") String key);
}
