package com.example.gpstest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain);

        Button startbt = (Button)findViewById(R.id.ridingstart);
        Button viewreport = (Button)findViewById(R.id.aaa);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId","");
        Toast toast = Toast.makeText(getApplicationContext(), LoginId+"님 로그인되었습니다", Toast.LENGTH_SHORT); toast.show();

        //라이딩 시작
        startbt.setOnClickListener(v -> {

            Intent intent=new Intent(SubActivity.this,StartActivity.class);
            startActivity(intent);
        });

        //라이딩 기록보기
        viewreport.setOnClickListener(v -> {
            Intent intent=new Intent(SubActivity.this,MyReport.class);
            startActivity(intent);
        });
    }
}
