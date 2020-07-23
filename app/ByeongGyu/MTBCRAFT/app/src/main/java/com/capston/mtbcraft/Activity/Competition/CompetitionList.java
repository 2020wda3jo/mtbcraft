package com.capston.mtbcraft.Activity.Competition;

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
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.CompetitionAdapter;
import com.capston.mtbcraft.dto.Competition;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    int nowSize = 0, nowCount = 0;

    ArrayList<String> joinedList = new ArrayList<>();
    ArrayList<Competition> nowItemList;

    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
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
        InFoUserId.setText(Nickname + "님 환영합니다");
        imageView.setImageBitmap(mem_Image);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist = new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist = new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch = new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    break;
                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget = new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp = new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    break;

            }
            return true;
        });

        try {
            new GetTask2().execute();

            GetTask getTask = new GetTask();
            getTask.execute();

        } catch (Exception e) {

        }

    }

    public class GetTask2 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/competition/" + LoginId);
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
            try {
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<String> itemList = new ArrayList<>();
                String[] items = gson.fromJson(tempData, String[].class);
                String pastItem = "";

                for (String item : items) {
                    if (!pastItem.equals(item))
                        joinedList.add(item);
                    pastItem = item;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/competition");
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
            try {
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Competition> itemList = new ArrayList<>();
                nowItemList = new ArrayList<>();
                ArrayList<Competition> pastItemList = new ArrayList<>();
                Competition[] items = gson.fromJson(tempData, Competition[].class);
                String File_Name = "";

                for (Competition item : items) {
                    for (int i = 0; i < joinedList.size(); i++) {
                        if (item.getComp_num() == Integer.parseInt(joinedList.get(i))) {
                            itemList.add(item);
                        }
                    }


                    String fullDay = item.getComp_period();
                    long howDay = 0;
                    try {
                        howDay = getDate(fullDay);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (howDay < 0) {
                        nowItemList.add(item);
                        DownloadTask2 downloadTask = new DownloadTask2("comp", item.getComp_image(), nowItemList.size());
                        downloadTask.execute();
                    } else {
                        pastItemList.add(item);
                        DownloadTask downloadTask = new DownloadTask("comp", item.getComp_image());
                        downloadTask.execute();
                    }
                }

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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public long getDate(String getPeriod) throws ParseException {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
            String temp = sdfNow.format(date);
            Date nowTime = sdfNow.parse(temp);
            Date periodTime = sdfNow.parse(String.valueOf(getPeriod.substring(8, 16)));

/*            SimpleDateFormat sdfNow2 = new SimpleDateFormat("yyyyMMdd");
            int day1 = Integer.parseInt(sdfNow2.format(date));
            int day2 =  Integer.parseInt(getPeriod.substring(8,16));*/

            return (nowTime.getTime() - periodTime.getTime()) / (24 * 60 * 60 * 1000);

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

    public class DownloadTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";

        public DownloadTask(String directory, String url) {
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
            try {

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class DownloadTask2 extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";
        int size;

        public DownloadTask2(String directory, String url, int size) {
            this.directory = directory;
            this.url = url;
            this.size = size;
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
            try {
                nowSize++;
                if (nowSize == size) {
                    CompetitionAdapter adapter = new CompetitionAdapter(getApplicationContext(), nowItemList, Save_Path);
                    recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recycleView.setAdapter(adapter);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패했습니다. 관리자에게 문의하세요", Toast.LENGTH_LONG).show();
            }
        }
    }
}