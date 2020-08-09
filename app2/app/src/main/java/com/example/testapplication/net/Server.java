package com.example.testapplication.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server{
    private static Server instance;
    private ServerApi api;

    private Server(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.209.229.237:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(ServerApi.class);
    }

    public static Server getInstance(){
        if ( instance == null ) instance = new Server();
        return instance;
    }

    public ServerApi getApi() { return api; }
}
