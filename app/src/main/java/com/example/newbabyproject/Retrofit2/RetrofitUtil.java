package com.example.newbabyproject.Retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    private Retrofit mRetrofit;

    private userApi mUserApi;

    private String baseUrl = "http://tkdanr2427.cafe24.com/NewBaby";

    public RetrofitUtil(){

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mUserApi = mRetrofit.create(userApi.class);
    }

    public userApi getUserApi(){
        return mUserApi;

    }
}
