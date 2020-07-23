package com.capston.mtbcraft.Activity.Course;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Main.SubActivity;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.CourseAdapter;
import com.capston.mtbcraft.Recycler.Adapter.RecyclerAdapter;
import com.capston.mtbcraft.dto.RidingRecord;
import com.capston.mtbcraft.gpxparser.GPXParser;
import com.capston.mtbcraft.gpxparser.Gpx;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;
public class CourseList extends AppCompatActivity {
    GPXParser mParser = new GPXParser();
    Gpx parsedGpx = null;

    private RecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    ArrayList<RidingRecord> arrlist = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView likeimg;
    CourseAdapter courseAdapter;
    Button moreButton;

    String LoginId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courselist);
        recyclerView= findViewById(R.id.recyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        moreButton = findViewById(R.id.more_button);

        RidingRecord rrr = new RidingRecord();
        Intent intent = new Intent(this.getIntent());
        rrr.setLoginId(intent.getStringExtra("rider_id")); ;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        likeimg = (ImageView) findViewById(R.id.likeimg);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            switch (id) {
                //홈
                case R.id.nav_home:
                    Intent home = new Intent(getApplicationContext(), SubActivity.class);
                    startActivity(home);
                    break;
                //라이딩 기록
                case R.id.nav_mylist:
                    Intent mylist=new Intent(getApplicationContext(), MyReport.class);
                    startActivity(mylist);

                    break;
                //코스보기
                case R.id.nav_courselist:
                    Intent courselist=new Intent(getApplicationContext(), CourseList.class);
                    courselist.putExtra("rider_id", LoginId);
                    startActivity(courselist);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent coursesearch=new Intent(getApplicationContext(), CourseSearch.class);
                    startActivity(coursesearch);
                    break;
                //스크랩 보관함
                case R.id.nav_course_get:
                    Intent courseget=new Intent(getApplicationContext(), MyScrap.class);
                    startActivity(courseget);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent comp=new Intent(getApplicationContext(), CompetitionList.class);
                    startActivity(comp);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent mission=new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    break;
            }
            return true;
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                int totalCount = recyclerView.getAdapter().getItemCount();

                Log.e("끝포지션", String.valueOf(totalCount));
                Log.e("라스트포지션", String.valueOf(lastPosition));


                if(lastPosition == totalCount){
                    if ( courseAdapter.itemList.size() == courseAdapter.count){
                        moreButton.setVisibility(View.GONE);
                    }
                }

                moreButton.setOnClickListener( v -> {
                    if ( courseAdapter.itemList.size() - (courseAdapter.count+5) > 0) {
                        courseAdapter.count += 5;
                        recyclerView.requestLayout();
                    }
                    else{
                        courseAdapter.count += courseAdapter.itemList.size() - courseAdapter.count;
                        recyclerView.requestLayout();
                    }
                });
            }
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
                ArrayList<RidingRecord> itemList = new ArrayList<>();
                RidingRecord[] items = gson.fromJson(tempData, RidingRecord[].class);

                for(RidingRecord item: items){
                    itemList.add(item);
                }

                courseAdapter = new CourseAdapter(getApplicationContext(), itemList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(courseAdapter);
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

