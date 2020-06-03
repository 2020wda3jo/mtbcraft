package com.mtbcraft.Activity.Riding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.mtbcraft.Activity.Scrap.MyScrap;
import com.mtbcraft.Recycler.Adapter.RecyclerAdapter;
import com.mtbcraft.Recycler.Adapter.RidingRecordListAdapter;
import com.mtbcraft.dto.RidingRecord;
import com.mtbcraft.network.HttpClient;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

public class RidingRecordAll extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    ArrayList<RidingRecord> arrlist = new ArrayList<>();
    RecyclerView recyclerView;
    SharedPreferences auto;
    String LoginId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myreport);
        recyclerView= findViewById(R.id.recyclerView);

        /* 드로우 레이아웃 네비게이션 부분들 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(LoginId+"환영합니다");
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
                    Intent intent2=new Intent(RidingRecordAll.this, MyReport.class);
                    startActivity(intent2);
                    break;
                //공유된 라이딩 기록
                case R.id.nav_alllist:
                    Intent intent3=new Intent(RidingRecordAll.this, RidingRecordAll.class);
                    startActivity(intent3);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent intent4=new Intent(RidingRecordAll.this, CourseSearch.class);
                    startActivity(intent4);
                    //코스보기
                case R.id.nav_courselist:
                    Intent intent5=new Intent(RidingRecordAll.this, CourseList.class);
                    startActivity(intent5);
                    break;
                //스크랩 보관함
                case R.id.nav_course:
                    Intent intent6=new Intent(RidingRecordAll.this, MyScrap.class);
                    startActivity(intent6);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent intent7=new Intent(RidingRecordAll.this, Competition.class);
                    startActivity(intent7);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent intent8=new Intent(RidingRecordAll.this, Mission.class);
                    startActivity(intent8);
                    break;
            }
            return true;
        });


        try{
            GetTask getTask = new GetTask();
            getTask.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "http://100.92.32.8:8080/api/get/ridingrecord");

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
                JSONArray jsonArray = new JSONArray(s);
                StringBuffer sb = new StringBuffer();
                String tempData = s;

                Gson gson = new Gson();
                ArrayList<RidingRecord> itemList = new ArrayList<>();
                RidingRecord[] items = gson.fromJson(tempData, RidingRecord[].class);

                for(RidingRecord item: items){
                    itemList.add(item);
                }

                RidingRecordListAdapter adapter = new RidingRecordListAdapter(getApplicationContext(), itemList);
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
