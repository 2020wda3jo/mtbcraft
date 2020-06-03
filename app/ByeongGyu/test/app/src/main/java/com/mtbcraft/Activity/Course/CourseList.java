package com.mtbcraft.Activity.Course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Riding.RidingRecordAll;
import com.mtbcraft.Activity.Scrap.MyScrap;
import com.mtbcraft.Activity.Main.SubActivity;
import com.mtbcraft.Recycler.Adapter.CourseAdapter;
import com.mtbcraft.Recycler.Adapter.RecyclerAdapter;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.network.HttpClient;

import java.util.ArrayList;
import java.util.Map;
public class CourseList extends AppCompatActivity  {
    private RecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    ArrayList<RidingRecord> arrlist = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences auto;
    String LoginId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courselist);
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
                    Intent intent=new Intent(CourseList.this, SubActivity.class);
                    startActivity(intent);
                    //라이딩 기록
                case R.id.nav_mylist:
                    Intent intent2=new Intent(CourseList.this, MyReport.class);
                    startActivity(intent2);
                    break;
                //공유된 라이딩 기록
                case R.id.nav_alllist:
                    Intent intent3=new Intent(CourseList.this, RidingRecordAll.class);
                    startActivity(intent3);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent intent4=new Intent(CourseList.this, CourseSearch.class);
                    startActivity(intent4);
                    //코스보기
                case R.id.nav_courselist:
                    Intent intent5=new Intent(CourseList.this, CourseList.class);
                    startActivity(intent5);
                    finish();
                    break;
                //스크랩 보관함
                case R.id.nav_course:
                    Intent intent6=new Intent(CourseList.this, MyScrap.class);
                    startActivity(intent6);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent intent7=new Intent(CourseList.this, Competition.class);
                    startActivity(intent7);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent intent8=new Intent(CourseList.this, Mission.class);
                    startActivity(intent8);
                    break;
            }
            return true;
        });





        try{
            GetTask getTask = new GetTask();
            getTask.execute();
        }catch(Exception e){

        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String>{

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://13.209.229.237:8080/app/riding/course");
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
             Log.d("로그: ",s);
            try{
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<Course> itemList = new ArrayList<>();
                Course[] items = gson.fromJson(tempData, Course[].class);

                for(Course item: items){
                    itemList.add(item);
                }

                CourseAdapter adapter = new CourseAdapter(getApplicationContext(), itemList);
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

