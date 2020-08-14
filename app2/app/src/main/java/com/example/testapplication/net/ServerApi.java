package com.example.testapplication.net;


import com.example.testapplication.dto.AnLogin;
import com.example.testapplication.dto.DangerousArea;
import com.example.testapplication.dto.LoginInfo;

import com.example.testapplication.dto.RidingRecord;

import com.example.testapplication.dto.ScrapStatus;
import com.example.testapplication.dto.Tag_Status;
import com.example.testapplication.dto.User;
import com.example.testapplication.ui.riding.EndActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServerApi {
    @GET("app/users")
    Call<List<User>> getUsers();

    @GET("app/users/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("app/login")
    Call<AnLogin> Login(@Body AnLogin login);

    @GET("app/getLoginInfo/{LoginId}")
    Call<LoginInfo> getLoginInfo(@Path("LoginId") String LoginId);

    @GET("app/getGPX/{directory}/{url}")
    Call<ResponseBody> getFile(@Path("directory") String directory, @Path("url") String url);

    @GET("app/ge/{rr_rider}")
    Call<List<RidingRecord>> getMyRecord(@Path("rr_rider") String rr_rider);

    //라이딩 기록 저장
    @POST("app/riding/upload")
    Call<RidingRecord> InsertRecord(@Body RidingRecord record);

    //라이딩 기록 저장 후 해당 기록 가져오기
    @GET("app/riding/getrecord")
    Call<List<RidingRecord>> getInsertSel();

    //태그삽입
    @POST("app/riding/taginsert")
    Call<Tag_Status> tagInsert(@Body Tag_Status tag_status);

    //위험지역가져오기
    @GET("app/riding/danger")
    Call<List<DangerousArea>> getDangerArea();

    //라이딩 기록
    @GET("app/get/{loginId}")
    Call<ArrayList<RidingRecord>> getRecord(@Path("loginId") String loginId);

    //라이딩 상세보기
    @GET("app/get/detail/{rr_num}")
    Call<List<RidingRecord>> getRecordDetail(@Path("rr_num") String rr_num);

    //라이딩 공개설정
    @FormUrlEncoded
    @POST("app/recordset/open")
    Call<RidingRecord> setOpen(@FieldMap HashMap<String, Object> param);

    //라이딩 기록
    @GET("app/riding/scrap/{rr_rider}")
    Call<ArrayList<ScrapStatus>> getScrap(@Path("rr_rider") String rr_rider);
}
