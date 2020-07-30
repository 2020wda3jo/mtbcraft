package com.capston.mtbcraft.Activity.Mission;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.capston.mtbcraft.Activity.Competition.CompetitionList;
import com.capston.mtbcraft.Activity.Control.NoMtb;
import com.capston.mtbcraft.Activity.Course.CourseList;
import com.capston.mtbcraft.Activity.Course.CourseSearch;
import com.capston.mtbcraft.Activity.Danger.DangerList;
import com.capston.mtbcraft.Activity.Riding.MyReport;
import com.capston.mtbcraft.Activity.Scrap.MyScrap;
import com.capston.mtbcraft.Activity.Setting.SettingActivity;
import com.capston.mtbcraft.R;
import com.capston.mtbcraft.network.HttpClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MissionDetail extends AppCompatActivity {
    String bg_image, m_content, m_name, Save_Path, LoginId, bg_name;
    int m_badge, m_type, ms_score, m_num;
    TextView name_textView, textView3, textView4, textView5, textView6;
    ImageView imageView6;
    String[] a = new String[4];

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missiondetail);


        /* 로그인관련 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        LoginId = auto.getString("LoginId", "");
        String Nickname = auto.getString("r_nickname","");

        /*네비게이션 바 */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        name_textView = findViewById(R.id.comp_name2);
        imageView6 = findViewById(R.id.imageView6);
        textView3 = findViewById(R.id.mission_name);
        textView4 = findViewById(R.id.mission_now);
        textView5 = findViewById(R.id.mission_when);
        textView6 = findViewById(R.id.mission_content);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView InFoUserId = (TextView) header.findViewById(R.id.infouserid);
        InFoUserId.setText(LoginId+"님 환영합니다");
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
                    Intent danger = new Intent(getApplicationContext(), DangerList.class);
                    startActivity(danger);
                    break;

                //입산통제
                case R.id.no_mtb:
                    Intent nomtb = new Intent(getApplicationContext(), NoMtb.class);
                    startActivity(nomtb);
                    break;

                //설정
                case R.id.settings:
                    Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(setting);
                    break;
            }
            return true;
        });


        Intent intent = new Intent(this.getIntent());
        bg_image = intent.getStringExtra("bg_image");
        m_badge = intent.getIntExtra("m_badge", 0);
        m_content = intent.getStringExtra("m_content");
        m_name = intent.getStringExtra("m_name");
        m_type = intent.getIntExtra("m_type",0);
        ms_score = intent.getIntExtra("ms_score", 0);
        m_num = intent.getIntExtra("m_num", 0);
        Save_Path = getFilesDir().getPath();
        bg_name = intent.getStringExtra("bg_name");

        try {
            new GetTask().execute();

        } catch (Exception e) {

        }

        Bitmap badge_Image = BitmapFactory.decodeFile(new File(Save_Path + "/" + bg_image).getAbsolutePath());

        name_textView.setText(m_name);
        imageView6.setImageBitmap(badge_Image);
        textView6.setText(m_content);
        if ( m_type == 1)
            textView4.setText("진행상황 " + String.valueOf(ms_score) + "km");
        else if ( m_type == 2)
            textView4.setText("진행상황 " + String.valueOf(ms_score) + "회");
        else if ( m_type == 3)
            textView4.setText("진행상황 " + String.valueOf(ms_score) + "개");
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


    // 클럽 순위 및 정보 가져오기
    public class GetTask extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            // Http 요청 준비 작업
            //URL은 현재 자기 아이피번호를 입력해야합니다.
            HttpClient.Builder http = new HttpClient.Builder("GET", "/app/getWhenCom/" + LoginId + "/" + m_num);
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
                Date item = gson.fromJson(tempData, Date.class);

                textView3.setText(bg_name);

                if ( item != null) {
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                    String temp = sdfNow.format(item);
                    textView5.setText("획득일자 " + temp);
                    if ( m_type == 4)
                        textView4.setText("진행상황 획득");
                }
                else{
                    if ( m_type == 4)
                        textView4.setText("진행상황 미획득");
                    textView5.setText("획득일자 미획득");
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
