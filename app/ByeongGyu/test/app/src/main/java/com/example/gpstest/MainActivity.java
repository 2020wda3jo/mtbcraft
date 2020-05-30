package com.example.gpstest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gpstest.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //자동로그인을 위한 앱 내부 저장
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        String LoginId = auto.getString("LoginId",null);

        if(LoginId==null){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
            binding.loginBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,LoginAccess.class);
                    intent.putExtra("UserId", binding.userid.getText().toString());
                    intent.putExtra("UserPw", binding.userpw.getText().toString());
                    startActivity(intent);
                }
            });
        }else{
            Intent intent=new Intent(MainActivity.this,SubActivity.class);
            startActivity(intent);
        }


    }
}