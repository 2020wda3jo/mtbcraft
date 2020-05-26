package com.example.gpstest;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class MyReport extends AppCompatActivity  {
    private RecyclerAdapter adapter;
    ArrayList<RidingRecord> arrlist = new ArrayList<>();
    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreport);
        recyclerView= findViewById(R.id.recyclerView);

        try{
            GetTask getTask = new GetTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("rr_rider", "345");
            getTask.execute(params);
        }catch(Exception e){

        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String>{

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/api/get/345");
            // Parameter 를 전송한다.
            http.addAllParameters(maps[0]);
            //Http 요청 전송
            HttpClient post = http.create();
            post.request();

            // 응답 상태코드 가져오기
            int statusCode = post.getHttpStatusCode();

            // 응답 본문 가져오기
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
           // Log.d("로그: ",s);
            try{
                JSONArray jsonArray = new JSONArray(s);
                StringBuffer sb = new StringBuffer();
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<RidingRecord> itemList = new ArrayList<>();
                RidingRecord[] items = gson.fromJson(tempData, RidingRecord[].class);

                for(RidingRecord item: items){
                    itemList.add(item);
                }

                RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

