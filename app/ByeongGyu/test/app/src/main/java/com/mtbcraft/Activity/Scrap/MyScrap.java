package com.mtbcraft.Activity.Scrap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gpstest.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mtbcraft.Activity.Competition.Competition;
import com.mtbcraft.Activity.Course.CourseList;
import com.mtbcraft.Activity.Course.CourseSearch;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Recycler.Adapter.MyScrapAdapter;
import com.mtbcraft.dto.ScrapStatus;
import com.mtbcraft.network.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class MyScrap extends AppCompatActivity  {
    private DrawerLayout mDrawerLayout;
    RecyclerView recyclerView;
    SharedPreferences auto;
    String LoginId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreport);
        recyclerView= findViewById(R.id.recyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home=new Intent(MyScrap.this, MyReport.class);
                    startActivity(home);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(MyScrap.this, MyReport.class);
                    startActivity(mylist);
                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(MyScrap.this, CourseList.class);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(MyScrap.this, CourseSearch.class);
                    startActivity(coursesearch);
                    //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(MyScrap.this, MyScrap.class);
                    startActivity(courseget);
                    finish();
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(MyScrap.this, Competition.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(MyScrap.this, Mission.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });


        try{
            GetTask getTask = new GetTask();
            Map<String, String> params = new HashMap<String, String>();
            auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
            LoginId  = auto.getString("LoginId","");
            params.put("rr_rider", LoginId);
            getTask.execute(params);
        }catch(Exception e){

        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String>{

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/app/riding/scrap/"+LoginId);
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
            // Log.d("로그: ",s);
            try{
                String tempData = s;
                Gson gson = new Gson();
                ArrayList<ScrapStatus> itemList = new ArrayList<>();
                ScrapStatus[] items = gson.fromJson(tempData, ScrapStatus[].class);

                for(ScrapStatus item: items){
                    itemList.add(item);
                }

                MyScrapAdapter adapter = new MyScrapAdapter(getApplicationContext(), itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);
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
}

