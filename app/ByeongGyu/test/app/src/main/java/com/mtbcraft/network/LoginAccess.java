package com.mtbcraft.network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mtbcraft.Activity.SubActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginAccess extends AppCompatActivity {

    String userid="", userpw="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Intent intent = new Intent(this.getIntent());
            userid = intent.getStringExtra("UserId");
            userpw = intent.getStringExtra("UserPw");

            LoginAccess.GetTask getTask = new LoginAccess.GetTask();
            Map<String, String> params = new HashMap<String, String>();
            params.put("r_id", userid);
            params.put("r_pw", userpw);
            getTask.execute(params);
        }catch(Exception e){

        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("POST", "http://13.209.229.237:8080/android/login");
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
             Log.d("로그: ",s);

            try {
                JSONObject jsonObject;
                jsonObject = new JSONObject(s);
                String status = jsonObject.getString("Status");
                String rider = jsonObject.getString("r_id");

                if(status.equals("Ok") ){
                    //자동로그인을 위한 앱 내부 저장
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("LoginId", rider);

                    autoLogin.commit();

                    Toast toast = Toast.makeText(getApplicationContext(), rider+"님 로그인했습니다.", Toast.LENGTH_SHORT); toast.show();
                    Intent intent = new Intent(LoginAccess.this, SubActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                   Toast toast = Toast.makeText(getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT); toast.show();
                }
                Log.d("json가져옴",status);
                Log.d("json가져옴",rider);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
