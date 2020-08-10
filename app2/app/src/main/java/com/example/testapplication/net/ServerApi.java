package com.example.testapplication.net;


import com.example.testapplication.dto.AnLogin;
import com.example.testapplication.dto.DangerousArea;
import com.example.testapplication.dto.LoginInfo;

import com.example.testapplication.dto.RidingRecord;

import com.example.testapplication.dto.Tag_Status;
import com.example.testapplication.dto.User;
import com.example.testapplication.ui.riding.EndActivity;

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

    @POST("login")
    Call<AnLogin> Login(@Body AnLogin login);

    @GET("getLoginInfo/{LoginId}")
    Call<LoginInfo> getLoginInfo(@Path("LoginId") String LoginId);

    @GET("app/getGPX/{directory}/{url}")
    Call<ResponseBody> getFile(@Path("directory") String directory, @Path("url") String url);

    @GET("api/ge/{rr_rider}")
    Call<List<RidingRecord>> getMyRecord(@Path("rr_rider") String rr_rider);

    //라이딩 기록 저장
    @POST("android/riding/upload")
    Call<RidingRecord> InsertRecord(@Body RidingRecord record);

    //라이딩 기록 저장 후 해당 기록 가져오기
    @GET("android/riding/getrecord")
    Call<List<RidingRecord>> getInsertSel();

    //태그삽입
    @POST("android/riding/taginsert")
    Call<Tag_Status> tagInsert(@Body Tag_Status tag_status);

    //위험지역가져오기
    @GET("android/riding/danger")
    Call<List<DangerousArea>> getDangerArea();

    @GET("android/get/{loginId}")
    Call<List<RidingRecord>> getRecord(@Path("loginId") String loginId);
}
