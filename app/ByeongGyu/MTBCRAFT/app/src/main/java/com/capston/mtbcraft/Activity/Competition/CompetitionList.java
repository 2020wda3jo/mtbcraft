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

import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Danger.Danger;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.Mission;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.CompetitionAdapter;
import com.capston.mtbcraft.databinding.ActivityCompetitionBinding;
import com.capston.mtbcraft.databinding.RidingStartBinding;
import com.capston.mtbcraft.dto.Competition;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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
private ActivityCompetitionBinding binding;
    private TextView memberId;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recycleView;
    private String LoginId, Nickname, Image, Save_Path;
    private int nowSize = 0;
    private ArrayList<String> joinedList = new ArrayList<>();
    private ArrayList<Competition> nowItemList;
    private ImageView imageView;
    private ArrayList<Competition> pastItemList;
    private ArrayList<Competition> itemList;
    private ImageView userImage;
    private String myclub="";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCompetitionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        Nickname = auto.getString("r_nickname", "");
        myclub = auto.getString("myclub","");

        Save_Path = getFilesDir().getPath();

        memberId = findViewById(R.id.memberId);
        memberId.setText(Nickname + " 님");



        recycleView = findViewById(R.id.recycleView1);

        imageView = findViewById(R.id.comp_mem_image);


        /* 로그인 정보 가져오기 */
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        Nickname = auto.getString("r_nickname", "");
        myclub = auto.getString("r_clubname","");

        /* 드로우 레이아웃 네비게이션 부분들 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        userImage = (ImageView) header.findViewById(R.id.user_image);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(Nickname + "님 환영합니다");

        //경쟁전 유저 정보
        binding.memberId.setText("@"+LoginId);
        binding.nickname.setText(Nickname);
        binding.clubName.setText(myclub);



        //닉네임명에 따른 이미지변경(임시)
        switch(Nickname){
            case "배고파":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo1));
                binding.compMemImage.setImageDrawable(getResources().getDrawable(R.drawable.peo1));
                break;

            case "2병규":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo2));
                binding.compMemImage.setImageDrawable(getResources().getDrawable(R.drawable.peo2));
                break;
            case "괴물쥐":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo3));
                binding.compMemImage.setImageDrawable(getResources().getDrawable(R.drawable.peo3));
                break;
            default:
                break;
        }

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
                //미션
                case R.id.nav_mission:
                    Intent mission = new Intent(getApplicationContext(), Mission.class);
                    startActivity(mission);
                    break;
                case R.id.friend_chodae:
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    intent.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(intent, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent danger = new Intent(getApplicationContext(), Danger.class);
                    startActivity(danger);
                    break;

                //위험구역
                case R.id.no_mtb:
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    break;
            }
            return true;
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
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

    private void changeView(int index) {
        switch (index) {
            case 0:
                CompetitionAdapter nowAdapter = new CompetitionAdapter(getApplicationContext(), nowItemList, Save_Path);
                recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(nowAdapter);
                break;
            case 1:
                CompetitionAdapter pastAdapter = new CompetitionAdapter(getApplicationContext(), pastItemList, Save_Path);
                recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(pastAdapter);
                break;

            case 2:
                CompetitionAdapter pastAdapter2 = new CompetitionAdapter(getApplicationContext(), itemList, Save_Path);
                recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recycleView.setAdapter(pastAdapter2);
                break;
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
                pastItemList = new ArrayList<>();
                 itemList = new ArrayList<>();
                nowItemList = new ArrayList<>();

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