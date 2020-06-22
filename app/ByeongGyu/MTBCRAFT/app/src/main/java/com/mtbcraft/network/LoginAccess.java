package com.mtbcraft.network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mtbcraft.Activity.Competition.CompetitionDetail;
import com.mtbcraft.Activity.Main.SubActivity;
import com.mtbcraft.dto.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginAccess extends AppCompatActivity {

    String userid="", userpw="";

    String Save_Path;

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

                    new getLoginInfo().execute();
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

    public class getLoginInfo extends AsyncTask<Map<String, String>, Integer, String> {
        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/android/getLoginInfo/" + userid);
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
            Gson gson = new Gson();
            LoginInfo item = gson.fromJson(tempData, LoginInfo.class);
            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            SharedPreferences.Editor autoLogin = auto.edit();
            autoLogin.putInt("r_club", item.getCj_club());
            if ( item.getR_image() == null)
                item.setR_image("noImage.jpg");
            autoLogin.putString("r_image", item.getR_image());
            autoLogin.putString("r_nickname", item.getR_nickname());

            FileDownload("member", item.getR_image());
            autoLogin.commit();
        }
    }

    public void FileDownload(String directory, String url)
    {
        String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url; // URL
        DownloadThread dThread;
        File dir = new File(Save_Path);
        // 폴더가 존재하지 않을 경우 폴더를 만듦
        if (!dir.exists()) {
            dir.mkdir();
        }
        // 다운로드 폴더에 동일한 파일명이 존재하는지 확인
        if (new File(Save_Path + "/" + url).exists() == false) {
            dThread = new DownloadThread(fileURL, Save_Path + "/" + url);
            dThread.start();
        }
    }


    // 쓰레드로 다운로드 돌림
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL gpxUrl;
            int Read;
            try {
                gpxUrl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) gpxUrl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);
                for (;;) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
