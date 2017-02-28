package com.example.phuong.viectimnguoiapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phuong on 24/02/2017.
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    private static String mBaseUrl = "https://viectimnguoi-469e6.firebaseio.com/";


    public static Retrofit retrofit () {

        retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }
}
