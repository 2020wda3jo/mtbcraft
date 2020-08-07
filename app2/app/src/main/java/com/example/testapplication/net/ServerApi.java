package com.example.testapplication.net;


import com.example.testapplication.dto.AnLogin;
import com.example.testapplication.dto.LoginInfo;

import com.example.testapplication.dto.RidingRecord;

import com.example.testapplication.dto.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerApi {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("android/login")
    Call<AnLogin> Login(@Body AnLogin login);

    @GET("android/getLoginInfo/{LoginId}")
    Call<LoginInfo> getLoginInfo(@Path("LoginId") String LoginId);

    @GET("app/getGPX/{directory}/{url}")
    Call<ResponseBody> getFile(@Path("directory") String directory, @Path("url") String url);

    @GET("api/get/{rr_rider}")
    Call<List<RidingRecord>> getMyRecord(@Path("rr_rider") String rr_rider);

}
