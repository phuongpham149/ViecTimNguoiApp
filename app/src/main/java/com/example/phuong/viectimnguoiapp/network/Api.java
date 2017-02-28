package com.example.phuong.viectimnguoiapp.network;

import com.example.phuong.viectimnguoiapp.network.Objects.ResultListUser;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by phuong on 24/02/2017.
 */

public interface Api {
    @GET("users.json")
    Call<ResultListUser> UsersList();
}
