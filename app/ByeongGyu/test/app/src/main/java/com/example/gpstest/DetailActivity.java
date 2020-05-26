package com.example.gpstest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    String rr_num;
    String rr_rider;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydetail);

        textView1 = (TextView)findViewById(R.id.Riding_time);
        textView2 = (TextView)findViewById(R.id.Riding_distance );
        textView3 = (TextView)findViewById(R.id.Riding_rest );
        textView4 = (TextView)findViewById(R.id.Riding_avg );
        textView5 = (TextView)findViewById(R.id.Riding_godo );
        textView6 = (TextView)findViewById(R.id.Riding_max );

        Intent intent = new Intent(this.getIntent());
        rr_num = intent.getStringExtra("rr_num"); //이동거리
        rr_rider = intent.getStringExtra("rr_rider");
        try {
            GetTask getTask = new GetTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("rr_num", rr_num);
            params.put("rr_rider", rr_rider);
            getTask.execute(params);
        } catch (Exception e) {

        }
    }


    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/api/get/"+rr_rider+"/"+rr_num);
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
            Log.d("디테일 ", s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                StringBuffer sb = new StringBuffer();
                String tempData = s;

                //json값을 받기위한 변수들

                String secS = "";
                String test = "";
                String disS = "";
                String avgS = "";
                String highS="";
                String maxS = "";
                String breakS="";
                JSONArray jarray = new JSONArray(tempData);
                for(int i=0; i<jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);
                    test = jObject.getString("rr_rider");
                    secS = jObject.getString("rr_time");
                    disS = jObject.getString("rr_distance");
                    breakS = jObject.getString("rr_breaktime");
                    avgS = jObject.getString("rr_avgspeed");
                    highS = jObject.getString("rr_high");
                    maxS = jObject.getString("rr_topspeed");
                    Log.d("test ", test);
                }

                //주행시간 계산
                int hour, min, sec = Integer.parseInt(secS);
                min = sec/60; hour = min/60; sec = sec % 60; min = min % 60;

                //휴식시간 계산
                int b_hour, b_min, b_sec = Integer.parseInt(breakS);
                b_min = b_sec/60; b_hour = b_min/60; b_sec = b_sec % 60; b_min = b_min % 60;

                int des = Integer.parseInt(disS);
                float km = (float) (des/1000.0);
                String total = String.valueOf(km)+"Km";

                textView1.setText(hour+"시간 "+min+"분 "+sec+"초");
                textView2.setText(total);
                textView3.setText(b_hour+"시간 "+b_min+"분 "+b_sec+"초");
                textView4.setText(avgS+"km/h");
                textView5.setText(highS+"m");
                textView6.setText(maxS+"km/h");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
