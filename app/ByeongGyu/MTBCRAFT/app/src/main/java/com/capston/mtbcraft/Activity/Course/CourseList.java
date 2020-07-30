package com.capston.mtbcraft.Activity.Course;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Mission.MissionList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.Recycler.Adapter.CourseAdapter;
import com.capston.mtbcraft.Recycler.Adapter.RecyclerAdapter;
import com.capston.mtbcraft.dto.RidingRecord;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
public class CourseList extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private SharedPreferences auto;

    String LoginId, Nickname;
    ImageView userImage;

    CourseAdapter courseAdapter, likeAdapter, highAdapter, disAdapter;
    Button moreButton;
    TextView textView1, textView2, textView3, textView4;
    ArrayList<RidingRecord> itemList = new ArrayList<>();
    ArrayList<RidingRecord> likeList = new ArrayList<>();
    ArrayList<RidingRecord> highList = new ArrayList<>();
    ArrayList<RidingRecord> disList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courselist);
        recyclerView= findViewById(R.id.recyclerView);

        RidingRecord rrr = new RidingRecord();
        Intent intent = new Intent(this.getIntent());
        rrr.setLoginId(intent.getStringExtra("rider_id"));

        moreButton = findViewById(R.id.more_button);
        textView1 = findViewById(R.id.textView17);
        textView2 = findViewById(R.id.textView18);
        textView3 = findViewById(R.id.textView19);
        textView4 = findViewById(R.id.textView20);

        /* 로그인 정보 가져오기 */
        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        Nickname = auto.getString("r_nickname", "");


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

        //닉네임명에 따른 이미지변경(임시)
        switch(Nickname){
            case "배고파":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo1));
                break;

            case "2병규":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo2));
                break;
            case "괴물쥐":
                userImage.setImageDrawable(getResources().getDrawable(R.drawable.peo3));
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
                    Intent mission = new Intent(getApplicationContext(), MissionList.class);
                    startActivity(mission);
                    break;
                case R.id.friend_chodae:
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_SEND);
                    intent2.setType("text/plain");
                    intent2.putExtra(Intent.EXTRA_SUBJECT, LoginId + "님이 귀하를 초대합니다. 앱 설치하기");
                    intent2.putExtra(Intent.EXTRA_TEXT, "tmarket://details?id=com.capston.mtbcraft");

                    Intent chooser = Intent.createChooser(intent2, "초대하기");
                    startActivity(chooser);
                    break;

                //위험구역
                case R.id.nav_danger:
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
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
                        likeAdapter.count += 5;
                        highAdapter.count += 5;
                        disAdapter.count += 5;
                        recyclerView.requestLayout();
                    }
                    else{
                        courseAdapter.count += courseAdapter.itemList.size() - courseAdapter.count;
                        likeAdapter.count += courseAdapter.itemList.size() - courseAdapter.count;
                        highAdapter.count += courseAdapter.itemList.size() - courseAdapter.count;
                        disAdapter.count += courseAdapter.itemList.size() - courseAdapter.count;
                        recyclerView.requestLayout();
                    }
                });
            }
        });

        textView1.setOnClickListener( v -> {
            recyclerView.setAdapter(courseAdapter);
            textView1.setTypeface(null, Typeface.BOLD);
            textView2.setTypeface(null, Typeface.NORMAL);
            textView3.setTypeface(null, Typeface.NORMAL);
            textView4.setTypeface(null, Typeface.NORMAL);
        });

        textView2.setOnClickListener( v -> {
            recyclerView.setAdapter(likeAdapter);
            textView1.setTypeface(null, Typeface.NORMAL);
            textView2.setTypeface(null, Typeface.BOLD);
            textView3.setTypeface(null, Typeface.NORMAL);
            textView4.setTypeface(null, Typeface.NORMAL);
        });

        textView3.setOnClickListener( v -> {
            recyclerView.setAdapter(highAdapter);
            textView1.setTypeface(null, Typeface.NORMAL);
            textView2.setTypeface(null, Typeface.NORMAL);
            textView3.setTypeface(null, Typeface.BOLD);
            textView4.setTypeface(null, Typeface.NORMAL);
        });

        textView4.setOnClickListener( v -> {
            recyclerView.setAdapter(disAdapter);
            textView1.setTypeface(null, Typeface.NORMAL);
            textView2.setTypeface(null, Typeface.NORMAL);
            textView3.setTypeface(null, Typeface.NORMAL);
            textView4.setTypeface(null, Typeface.BOLD);
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
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/riding/course");
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
                ArrayList<Integer> likeCount = new ArrayList<Integer>();
                ArrayList<Integer> highCount = new ArrayList<Integer>();
                ArrayList<Integer> disCount = new ArrayList<Integer>();
                RidingRecord[] items = gson.fromJson(tempData, RidingRecord[].class);

                for(RidingRecord item: items){
                    itemList.add(item);

                    likeCount.add(item.getRr_like());
                    highCount.add(item.getRr_high());
                    disCount.add(item.getRr_distance());
                }

                Collections.sort(likeCount);
                Collections.sort(highCount);
                Collections.sort(disCount);

                Collections.reverse(likeCount);
                Collections.reverse(highCount);
                Collections.reverse(disCount);

                for ( int i = 0; i < likeCount.size(); i++){
                    for ( int j = 0; j < itemList.size(); j++) {
                        if (likeCount.get(i) == itemList.get(j).getRr_like()){
                            likeList.add(itemList.get(j));
                        }

                        if (highCount.get(i) == itemList.get(j).getRr_high()){
                            highList.add(itemList.get(j));
                        }

                        if (disCount.get(i) == itemList.get(j).getRr_distance()) {
                            disList.add(itemList.get(j));
                        }
                    }
                }

                courseAdapter = new CourseAdapter(getApplicationContext(), itemList);
                likeAdapter = new CourseAdapter(getApplicationContext(), likeList);
                highAdapter = new CourseAdapter(getApplicationContext(), highList);
                disAdapter = new CourseAdapter(getApplicationContext(), disList);

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

