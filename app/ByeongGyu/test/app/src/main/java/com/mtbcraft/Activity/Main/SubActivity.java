package com.mtbcraft.Activity.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gpstest.R;
import com.google.android.material.navigation.NavigationView;
import com.mtbcraft.Activity.Competition.Competition;
import com.mtbcraft.Activity.Course.CourseList;
import com.mtbcraft.Activity.Course.CourseSearch;
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Riding.RidingRecordAll;
import com.mtbcraft.Activity.Scrap.MyScrap;

public class SubActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain);

        /*이건 그냥 업데이트 기록 다이얼로그 창 */
        // 다이얼로그 바디
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        // 메세지
        alert_confirm.setMessage("문제 및 수정예정 : \n - 라이딩 시작시 현재속도 불일치 \n  - 단위계산 \n- 웹서버 통신 클래스\n - 레이아웃 정리(폰크기마다 차이 \n - 로그인 방식?  \n - IP서버로 변환중");
        // 확인 버튼 리스너
        alert_confirm.setPositiveButton("확인", null);
        // 다이얼로그 생성
        AlertDialog alert = alert_confirm.create();
        // 다이얼로그 타이틀
        alert.setTitle("업데이트 로그");
        // 다이얼로그 보기
        alert.show();

        /* 로그인 정보 가져오기 */
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId","");

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
                    Intent intent2=new Intent(SubActivity.this, MyReport.class);
                    startActivity(intent2);
                    break;
                //공유된 라이딩 기록
                case R.id.nav_alllist:
                    Intent intent3=new Intent(SubActivity.this, RidingRecordAll.class);
                    startActivity(intent3);
                    break;
                //코스검색
                case R.id.nav_course_search:
                    Intent intent4=new Intent(SubActivity.this, CourseSearch.class);
                    startActivity(intent4);
                //코스보기
                case R.id.nav_courselist:
                    Intent intent5=new Intent(SubActivity.this, CourseList.class);
                    startActivity(intent5);
                    break;
                //스크랩 보관함
                case R.id.nav_course:
                    Intent intent6=new Intent(SubActivity.this, MyScrap.class);
                    startActivity(intent6);
                    break;
                //경쟁전
                case R.id.nav_comp:
                    Intent intent7=new Intent(SubActivity.this, Competition.class);
                    startActivity(intent7);
                    break;
                //미션
                case R.id.nav_mission:
                    Intent intent8=new Intent(SubActivity.this, Mission.class);
                    startActivity(intent8);
                    break;
            }
            return true;
        });



        Button startbt = (Button)findViewById(R.id.ridingstart);
        //라이딩 시작
        startbt.setOnClickListener(v -> {

            Intent intent=new Intent(SubActivity.this, StartActivity.class);
            startActivity(intent);
        });

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

    /* 네이베이션 끝 */
}
