package com.mtbcraft.Activity.Competition;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mtbcraft.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mtbcraft.Activity.Course.CourseList;
import com.mtbcraft.Activity.Course.CourseSearch;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Scrap.MyScrap;
import com.mtbcraft.Recycler.Adapter.CompetitionAdapter;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.network.HttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class CompetitionList extends AppCompatActivity {

    TextView memberId;

    DrawerLayout mDrawerLayout;

    RecyclerView recycleView;

    Button ingBt, finishBt, joinedBt;

    String LoginId, Nickname, Image, Save_Path;

    ArrayList<String> joinedList = new ArrayList<>();

    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId","");
        Nickname = auto.getString("r_nickname", "");
        Image = auto.getString("r_image", "");
        Save_Path = getFilesDir().getPath();
        Bitmap mem_Image = BitmapFactory.decodeFile(new File(Save_Path + "/" + Image).getAbsolutePath());

        memberId = findViewById(R.id.memberId);
        memberId.setText(Nickname + " 님");

        recycleView = findViewById(R.id.recycleView1);

        imageView = findViewById(R.id.comp_mem_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ingBt = findViewById(R.id.ing_bt);
        finishBt = findViewById(R.id.finish_bt);
        joinedBt = findViewById(R.id.joined_bt);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname+"님 환영합니다");
        imageView.setImageBitmap(mem_Image);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(CompetitionList.this, MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(CompetitionList.this, CourseList.class);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(CompetitionList.this, CourseSearch.class);
                    startActivity(coursesearch);
                    //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(CompetitionList.this, MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(CompetitionList.this, CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(CompetitionList.this, Mission.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });

        try{
            new GetTask2().execute();

            GetTask getTask = new GetTask();
            getTask.execute();

        }catch(Exception e){

        }

    }

    public class GetTask2 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/competition/" + LoginId);
            // Parameter 를 전송한다.

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
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<String> itemList = new ArrayList<>();
                String[] items = gson.fromJson(tempData, String[].class);
                String pastItem = "";

                for(String item: items){
                    if ( !pastItem.equals(item) )
                        joinedList.add(item);
                    pastItem = item;
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/competition");
            // Parameter 를 전송한다.

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
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Competition> itemList = new ArrayList<>();
                ArrayList<Competition> nowItemList = new ArrayList<>();
                ArrayList<Competition> pastItemList = new ArrayList<>();
                Competition[] items = gson.fromJson(tempData, Competition[].class);
                String File_Name="";

                for(Competition item: items){
                    for ( int i = 0; i<joinedList.size(); i++){
                        if ( item.getComp_num() == Integer.parseInt(joinedList.get(i))) {
                            itemList.add(item);
                        }
                    }

                    File_Name = item.getComp_image();

                    String fileURL = "http://13.209.229.237:8080/app/getGPX/comp/" + File_Name; // URL
                    DownloadThread dThread;
                    File dir = new File(Save_Path);
                    // 폴더가 존재하지 않을 경우 폴더를 만듦
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    // 다운로드 폴더에 동일한 파일명이 존재하는지 확인
                    if (new File(Save_Path + "/" + File_Name).exists() == false) {
                        dThread = new DownloadThread(fileURL, Save_Path + "/" + File_Name);
                        dThread.start();
                    }

                    String fullDay = item.getComp_period();
                    long howDay = 0;
                    try {
                        howDay = getDate(fullDay);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if ( howDay < 0)
                        nowItemList.add(item);
                    else
                        pastItemList.add(item);
                }



                CompetitionAdapter adapter = new CompetitionAdapter(getApplicationContext(), nowItemList, Save_Path);
                recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(adapter);

                ingBt.setOnClickListener(v -> {
                    CompetitionAdapter nowAdapter = new CompetitionAdapter(getApplicationContext(), nowItemList, Save_Path);
                    recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recycleView.setAdapter(nowAdapter);
                });

                finishBt.setOnClickListener(v -> {
                    CompetitionAdapter pastAdapter = new CompetitionAdapter(getApplicationContext(), pastItemList, Save_Path);
                    recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recycleView.setAdapter(pastAdapter);
                });

                joinedBt.setOnClickListener(v -> {
                    CompetitionAdapter pastAdapter = new CompetitionAdapter(getApplicationContext(), itemList, Save_Path);
                    recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recycleView.setAdapter(pastAdapter);
                });

            }catch(Exception e){
                e.printStackTrace();
            }
        }

        public long getDate(String getPeriod) throws ParseException {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
            String temp = sdfNow.format(date);
            Date nowTime = sdfNow.parse(temp);
            Date periodTime = sdfNow.parse(String.valueOf(getPeriod.substring(8,16)));

/*            SimpleDateFormat sdfNow2 = new SimpleDateFormat("yyyyMMdd");
            int day1 = Integer.parseInt(sdfNow2.format(date));
            int day2 =  Integer.parseInt(getPeriod.substring(8,16));*/

            return (nowTime.getTime() - periodTime.getTime()) / (24*60*60*1000);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 쓰레드로 다운로드 돌림
    static class DownloadThread extends Thread {
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
