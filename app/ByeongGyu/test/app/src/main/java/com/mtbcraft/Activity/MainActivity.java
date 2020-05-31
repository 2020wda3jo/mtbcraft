package com.mtbcraft.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mtbcraft.network.LoginAccess;
import com.example.gpstest.R;

public class MainActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //자동로그인을 위한 앱 내부 저장
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoLogin = auto.edit();
        String LoginId = auto.getString("LoginId",null);
        if(LoginId==null){
        Button loginbt = (Button)findViewById(R.id.login_bt);
        TextView userid = (TextView)findViewById(R.id.userid);
        TextView userpw = (TextView)findViewById(R.id.userpw);


            loginbt.setOnClickListener(v -> {
                Intent intent=new Intent(MainActivity.this, LoginAccess.class);
                intent.putExtra("UserId",userid.getText().toString());
                intent.putExtra("UserPw", userpw.getText().toString());
                startActivity(intent);
            });
        }else{
            Intent intent=new Intent(MainActivity.this,SubActivity.class);
            startActivity(intent);
            finish();
        }
    }
}