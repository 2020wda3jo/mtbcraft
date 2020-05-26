package com.example.gpstest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class endActivity extends AppCompatActivity{

    //스타트 액티비티에서 가져오는 String형변수
    String MaxSpeed, AvgSpeed, Getgodo, RestTime, IngTime, Distence, endsec, restsectime;
    //스타트 액티비티에서 가져온 값들을 텍스트로 설정
    TextView avgsoeed, maxspeed, getgodo, resttime, ingtime, distence, result;

    //DB로 전송하기 위한 변수
    String open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainend);

        //넘어온 intent값 넣기
        Intent intent = new Intent(this.getIntent());
        Distence = intent.getStringExtra("distence"); //이동거리
        MaxSpeed = intent.getStringExtra("endmax"); //최대속도
        AvgSpeed = intent.getStringExtra("endavg"); //평균속도
        Getgodo = intent.getStringExtra("getgodo"); //획득고도
        RestTime = intent.getStringExtra("resttime"); //휴식시간
        IngTime = intent.getStringExtra("ingtime"); //라이딩시간(시분초)
        endsec = intent.getStringExtra("endsec"); //라이딩 시간(초)
        restsectime = intent.getStringExtra("restsectime"); //휴식시간(초)


        avgsoeed = (TextView) findViewById(R.id.endavg);
        maxspeed = (TextView) findViewById(R.id.endmax);
        getgodo = (TextView) findViewById(R.id.endget);
        resttime = (TextView) findViewById(R.id.endresttime);
        ingtime = (TextView) findViewById(R.id.ending);
        distence = (TextView) findViewById(R.id.enddis);
        result = (TextView) findViewById(R.id.result);

        Button save = (Button) findViewById(R.id.saveriding);
        RadioGroup openselect = (RadioGroup)findViewById(R.id.selectgroup);



        //앞에서 받아온값들을 텍스트로 설정
        avgsoeed.setText(AvgSpeed); //평균속도
        maxspeed.setText(MaxSpeed); //최대속도
        getgodo.setText(Getgodo); //획득고도
        resttime.setText(RestTime); //휴식시간
        ingtime.setText(IngTime); //지속시간
        distence.setText(Distence); //이동거리

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        NetworkTask2 networkTask = new NetworkTask2();
        final String select;
        openselect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup rg = (RadioGroup)findViewById(R.id.selectgroup); // 라디오그룹 객체 맵핑
                RadioButton selectedRdo = (RadioButton)findViewById(rg.getCheckedRadioButtonId()); // rg 라디오그룹의 체크된(getCheckedRadioButtonId) 라디오버튼 객체 맵핑
                String selectedValue = selectedRdo.getText().toString();
                String value="";
                switch (selectedValue){
                    case "비공개":
                        value="0";
                    case "클럽 공개":
                        value="1";
                    case "전체 공개":
                        value="2";
                }
               open = value;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    NetworkTask2 networkTask = new NetworkTask2();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("rr_rider", "345");
                    params.put("rr_distance", Distence);
                    params.put("rr_topspeed", MaxSpeed);
                    params.put("rr_avgspeed", AvgSpeed);
                    params.put("rr_high",Getgodo);
                    params.put("rr_gpx", "gpx.gpx");
                    params.put("rr_open", open);
                    params.put("rr_breaktime", restsectime);
                    params.put("rr_time", endsec);
                    params.put("rr_area", "대구 북구");
                    networkTask.execute(params);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public class NetworkTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://100.92.32.8:8080/api/upload");
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
            try{
                Log.d("JSON_RESULT", s);
                Toast.makeText(getApplicationContext(), "기록이 저장되었습니다.", Toast.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }


        }
    }
}


