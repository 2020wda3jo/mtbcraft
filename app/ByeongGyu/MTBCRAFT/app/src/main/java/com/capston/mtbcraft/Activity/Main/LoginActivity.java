package com.capston.mtbcraft.Activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.capston.mtbcraft.R;
import com.capston.mtbcraft.network.LoginAccess;

public class LoginActivity extends AppCompatActivity{
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
                Intent intent=new Intent(getApplicationContext(), LoginAccess.class);
                intent.putExtra("UserId",userid.getText().toString());
                intent.putExtra("UserPw", userpw.getText().toString());
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), LoginId+"님 로그인되었습니다", Toast.LENGTH_SHORT); toast.show();
                finish();
            });
        }else{
            Intent intent=new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
            finish();
        }
    }
}