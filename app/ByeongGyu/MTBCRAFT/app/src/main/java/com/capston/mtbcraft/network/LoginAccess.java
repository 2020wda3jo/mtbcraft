package com.capston.mtbcraft.network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Recycler.Adapter.CompClubAdapter;
import com.capston.mtbcraft.dto.LoginInfo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginAccess extends AppCompatActivity {

    String userid="", userpw="";

    String Save_Path, rider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            Intent intent = new Intent(this.getIntent());
            userid = intent.getStringExtra("UserId");
            userpw = intent.getStringExtra("UserPw");

            Save_Path = getFilesDir().getPath();
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

            HttpClient.Builder http = new HttpClient.Builder("POST", "/android/login");
            Log.d("urlrlrl", String.valueOf(http));
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
                rider = jsonObject.getString("r_id");

                if(status.equals("Ok") ){
                    //자동로그인을 위한 앱 내부 저장
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("LoginId", rider);
                    autoLogin.commit();

                    new getLoginInfo().execute();
                    new getUserClub().execute();

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

    public class getLoginInfo extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/android/getClubUser/" + userid);
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
            String tempData = s;
            Log.d("클럽정보",s);
            Gson gson = new Gson();
            LoginInfo item = gson.fromJson(tempData, LoginInfo.class);
            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("r_clubname", item.getCb_name());
            autoLogin.commit();
        }
    }



    public class getUserClub extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/android/getLoginInfo/" + userid);
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
            String tempData = s;
            Log.d("회원정보",s);
            Gson gson = new Gson();
            LoginInfo item = gson.fromJson(tempData, LoginInfo.class);
            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putInt("r_club", item.getCj_club());
            if ( item.getR_image() == null)
                item.setR_image("noImage.jpg");
            autoLogin.putString("r_image", item.getR_image());
            autoLogin.putString("r_nickname", item.getR_nickname());

            autoLogin.commit();
            DownloadClubTask downloadClubTask = new DownloadClubTask("rider", item.getR_image());
            downloadClubTask.execute();
        }
    }

    public class DownloadClubTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";

        public DownloadClubTask(String directory, String url) {
            this.directory = directory;
            this.url = url;
        }

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            File dir = new File(Save_Path);
            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url;
            String LocalPath = Save_Path + "/" + url;

            if (new File(Save_Path + "/" + url).exists() == false) {
                URL imgUrl = null;
                try {
                    imgUrl = new URL(fileURL);

                    //서버와 접속하는 클라이언트 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                    int response = conn.getResponseCode();

                    File file = new File(LocalPath);

                    InputStream is = conn.getInputStream();
                    OutputStream outStream = new FileOutputStream(file);

                    byte[] buf = new byte[1024];
                    int len = 0;

                    while ((len = is.read(buf)) > 0) {
                        outStream.write(buf, 0, len);
                    }

                    outStream.close();
                    is.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putString("r_image", url);
            autoLogin.commit();

            Toast toast = Toast.makeText(getApplicationContext(), rider+"님 로그인했습니다.", Toast.LENGTH_SHORT); toast.show();
            Intent intent = new Intent(LoginAccess.this, SubActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
