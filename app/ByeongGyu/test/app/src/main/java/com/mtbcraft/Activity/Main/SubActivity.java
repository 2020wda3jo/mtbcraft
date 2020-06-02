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
import com.mtbcraft.Activity.Mission.Mission;
import com.mtbcraft.Activity.Riding.MyReport;
import com.mtbcraft.Activity.Riding.RidingRecordAll;
import com.mtbcraft.Activity.Scrap.MyScrap;

public class SubActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain);

        // 다이얼로그 바디
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        // 메세지
        alert_confirm.setMessage("문제 및 수정예정 : \n - 라이딩 시작시 현재속도 불일치\n - 리사이클러뷰 간헐적 안보임 \n  - 단위계산 \n- 웹서버 통신 클래스\n - 레이아웃 정리(폰크기마다 차이 \n - 로그인 방식? \n - 상세보기 및 개인기록볼때 지도부분 수정중 \n - IP서버로 변환중");
        // 확인 버튼 리스너
        alert_confirm.setPositiveButton("확인", null);
        // 다이얼로그 생성
        AlertDialog alert = alert_confirm.create();
        // 다이얼로그 타이틀
        alert.setTitle("업데이트 로그");
        // 다이얼로그 보기
        alert.show();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String LoginId = auto.getString("LoginId","");



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
                case R.id.nav_home:
                    break;
                case R.id.nav_mylist:
                    Intent intent2=new Intent(SubActivity.this, MyReport.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_alllist:
                    Intent intent3=new Intent(SubActivity.this, RidingRecordAll.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_courselist:
                    Intent intent4=new Intent(SubActivity.this, CourseList.class);
                    startActivity(intent4);
                    break;

                case R.id.nav_course:
                    Intent intent5=new Intent(SubActivity.this, MyScrap.class);
                    startActivity(intent5);
                    break;

                case R.id.nav_comp:
                    Intent intent6=new Intent(SubActivity.this, Competition.class);
                    startActivity(intent6);
                    break;
                case R.id.nav_mission:
                    Intent intent7=new Intent(SubActivity.this, Mission.class);
                    startActivity(intent7);
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
}
