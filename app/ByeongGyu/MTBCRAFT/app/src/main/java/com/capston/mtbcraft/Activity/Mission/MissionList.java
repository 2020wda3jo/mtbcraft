package com.capston.mtbcraft.Activity.Mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.Activity.Setting.SettingActivity;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.MissionAdapter;
import com.capston.mtbcraft.Recycler.Adapter.MissionRankingAdapter;
import com.capston.mtbcraft.dto.Mission;
import com.capston.mtbcraft.dto.MissionRanking;
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
import java.util.ArrayList;
import java.util.Map;

public class MissionList extends AppCompatActivity {

    private TextView memberId, textView5, textView6, textView7, textView8;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recycleView;
    private String LoginId, Nickname, Image, Save_Path, r_image;
    private RadioButton radioButton1, radioButton2;
    private int allCount, sucCount, nowCount=0;
    private ImageView imageView, userImage;
    private CheckBox checkBox;
    private NavigationView navigationView;
    private View header;

    public ArrayList<Mission> itemList = new ArrayList<>();
    public ArrayList<Mission> joinedList = new ArrayList<>();
    public ArrayList<MissionRanking> itemList3 = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId","");
        Nickname = auto.getString("r_nickname", "");
        r_image = auto.getString("r_image", "");
        Image = auto.getString("r_image", "");
        Save_Path = getFilesDir().getPath();
        Bitmap mem_Image = BitmapFactory.decodeFile(new File(Save_Path + "/" + Image).getAbsolutePath());

        memberId = findViewById(R.id.memberId);
        memberId.setText(Nickname + " 님");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = navigationView.getHeaderView(0);
        userImage = (ImageView) header.findViewById(R.id.user_image);
        Bitmap user_image = BitmapFactory.decodeFile(new File(getFilesDir().getPath() + "/" + r_image).getAbsolutePath());
        userImage.setImageBitmap(user_image);

        recycleView = findViewById(R.id.recycleView1);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recycleView.setLayoutManager(mLayoutManager);
        /*recycleView.addItemDecoration(new MissionItemDecoration(this));*/

        imageView = findViewById(R.id.comp_mem_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        checkBox = findViewById(R.id.checkBox2);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
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
        userImage = (ImageView) header.findViewById(R.id.user_image);


        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(home);
                    break;

                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);
                    finish();
                    break;

                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    finish();
                    break;

                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    finish();
                    break;

                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    finish();
                    break;

                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    finish();
                    break;

                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    finish();
                    break;

                case R.id.friend_chodae:
                    Intent friend = new Intent();
                    friend.setAction(Intent.ACTION_SEND);
                    friend.setType("text/plain");
                    friend.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    friend.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(friend, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    finish();
                    break;

                //위험구역
                case R.id.no_mtb:
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    finish();
                    break;
            }
            return true;
        });

        try{
            new GetTask().execute();
            new GetTask2().execute();
            new GetTask3().execute();

        }catch(Exception e){

        }

        checkBox.setOnClickListener( v -> {
            if ( !radioButton2.isChecked() ) {
                if (checkBox.isChecked()) {
                    MissionAdapter adapter = new MissionAdapter(getApplicationContext(), joinedList, Save_Path);
                    recycleView.setAdapter(adapter);
                } else {
                    MissionAdapter adapter = new MissionAdapter(getApplicationContext(), itemList, Save_Path);
                    recycleView.setAdapter(adapter);
                }
            }
    });

        radioButton2.setOnClickListener( v -> {
            MissionRankingAdapter adapter3 = new MissionRankingAdapter(getApplicationContext(), itemList3, Save_Path, allCount);
            recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recycleView.setAdapter(adapter3);
        });

        radioButton1.setOnClickListener( v -> {
            MissionAdapter adapter1 = new MissionAdapter(getApplicationContext(), joinedList, Save_Path);
            MissionAdapter adapter2 = new MissionAdapter(getApplicationContext(), itemList, Save_Path);
            recycleView.setLayoutManager(mLayoutManager);
            if ( checkBox.isChecked())
                recycleView.setAdapter(adapter1);
            else
                recycleView.setAdapter(adapter2);
        });
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getAllMission/" + LoginId);
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

                Mission[] items = gson.fromJson(tempData, Mission[].class);

                for(Mission item: items){
                    itemList.add(item);
                }

                int[] typeScore = new int[3];

                for ( int i = 0; i < itemList.size(); i++ ){

                    if ( itemList.get(i).getM_type() == 1)
                        typeScore[0] = itemList.get(i).getMs_score();
                    else if ( itemList.get(i).getM_type() == 2)
                        typeScore[1] = itemList.get(i).getMs_score();
                    else if ( itemList.get(i).getM_type() == 3)
                        typeScore[2] = itemList.get(i).getMs_score();

                    String File_Name = itemList.get(i).getBg_image();

                    DownloadTask downloadTask = new DownloadTask( "badge", File_Name, itemList.size());
                    downloadTask.execute();
                }

                allCount = itemList.size();

                textView5.setText("주행한 총 거리       " + typeScore[0] + "km");
                textView6.setText("경쟁전 참여 횟수   " + typeScore[1] + "회");
                textView7.setText("미션 완료 갯수       " + typeScore[2] + "개");

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class DownloadTask extends AsyncTask<Map<String, String>, Integer, String> {
        String directory = "";
        String url = "";
        String LocalPath;
        int Size;

        public DownloadTask(String directory, String url, int Size) {
            this.directory = directory;
            this.url = url;
            this.Size = Size;
        }

        @Override
        protected synchronized String doInBackground(Map<String, String>... maps) {

            File dir = new File(Save_Path);
            //상위 디렉토리가 존재하지 않을 경우 생성

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileURL = "http://13.209.229.237:8080/app/getGPX/" + directory + "/" + url;
            LocalPath = Save_Path + "/" + url;

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
        protected synchronized void onPostExecute(String s) {
            try {
                nowCount++;
                Log.e("나우", String.valueOf(nowCount));
                Log.e("사이즈", String.valueOf(Size));
                if ( nowCount == Size){
                    MissionAdapter adapter = new MissionAdapter(getApplicationContext(), itemList, Save_Path);
                    recycleView.setAdapter(adapter);
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "다운로드 실패", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetTask2 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getComMission/" + LoginId);
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
                ArrayList<String> itemList2 = new ArrayList<>();
                String[] items = gson.fromJson(tempData, String[].class);

                for(String item: items){
                    itemList2.add(item);
                    for ( int i = 0; i < itemList.size(); i++) {
                        if (item.equals(String.valueOf(itemList.get(i).getM_num()))) {
                            joinedList.add(itemList.get(i));
                        }
                    }
                }

                sucCount = itemList2.size();

                textView8.setText("현재 진행 상황       " + sucCount + " / " + allCount);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public class GetTask3 extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getMisRanking");
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
                MissionRanking[] items = gson.fromJson(tempData, MissionRanking[].class);

                for(MissionRanking item: items){
                    itemList3.add(item);
                    if ( item.getR_image() != null)
                        FileDownload("rider", item.getR_image());
                    else
                        FileDownload("rider", "noImage.jpg");
                }

            }catch(Exception e){
                e.printStackTrace();
            }
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
