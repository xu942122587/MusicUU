package com.qtfreet.musicuu.utils;

import com.qtfreet.musicuu.model.APi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qtfreet00 on 2016/8/17.
 */
public class NetUtil {
    public static final Retrofit getInstance() {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APi.MUSIC_HOST).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
